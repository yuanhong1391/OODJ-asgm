package services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import models.*;

/** Adapter for the group's existing CSV text files. Never uses the old hms user schema. */
public final class AdminStore {
    private final Path folder;
    private final Map<String, String[]> schemas = new LinkedHashMap<>();

    public AdminStore(Path folder) throws IOException {
        this.folder = folder.toAbsolutePath().normalize();
        Files.createDirectories(this.folder);
        schemas.put("assignments", cols("doctorId,managerId"));
        schemas.put("assets", cols("id,type,name,parentId,status"));
        schemas.put("allocations", cols("id,assetId,patientId,doctorId,requestId,start,end,status"));
        schemas.put("rates", cols("id,consultationType,amount,active"));
        schemas.put("insurance", cols("id,name,active"));
    }
    public Path getFolder() { return folder; }
    private static String[] cols(String value) { return value.split(",", -1); }
    private List<String> lines(Path path) throws IOException {
        return Files.exists(path) ? Files.readAllLines(path, StandardCharsets.UTF_8) : new ArrayList<>();
    }
    private Path tablePath(String table) {
        if (!schemas.containsKey(table)) throw new IllegalArgumentException("Unknown table: " + table);
        return folder.resolve("admin_" + table + ".txt");
    }
    public synchronized List<Map<String, String>> read(String table) throws IOException {
        if (table.equals("users")) return readUsers();
        if (table.equals("requests")) return readRequests();
        String[] keys = schemas.get(table);
        List<String> text = lines(tablePath(table));
        List<Map<String, String>> result = new ArrayList<>();
        if (text.isEmpty()) return result;
        if (!text.get(0).equals(String.join(",", keys))) throw new IOException("Invalid header: " + tablePath(table));
        Set<String> ids = new HashSet<>();
        for (int i = 1; i < text.size(); i++) {
            if (text.get(i).isBlank()) continue;
            String[] values = cols(text.get(i));
            if (values.length != keys.length || values[0].isBlank() || !ids.add(values[0]))
                throw new IOException("Invalid or duplicate row " + (i + 1) + " in " + tablePath(table));
            Map<String, String> row = new LinkedHashMap<>();
            for (int j = 0; j < keys.length; j++) row.put(keys[j], values[j]);
            result.add(row);
        }
        return result;
    }
    private List<Map<String, String>> readUsers() throws IOException {
        List<Map<String, String>> result = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (String file : List.of("users.txt")) {
            int number = 0;
            for (String line : lines(folder.resolve(file))) {
                number++;
                if (line.isBlank()) continue;
                String[] d = cols(line);
                int length = d.length >= 7 ? switch (d[6].toUpperCase(Locale.ROOT)) {
                    case "DOCTOR" -> 9; case "PATIENT" -> 10; case "MANAGER", "ADMIN" -> 7; default -> -1;
                } : -1;
                if (d.length != length || d[0].isBlank() || !ids.add(d[0].toUpperCase(Locale.ROOT)))
                    throw new IOException("Invalid user record at " + file + ":" + number + ". No data has been discarded.");
                Map<String, String> row = new LinkedHashMap<>();
                String[] common = cols("id,username,passwordHash,name,phone,email,role");
                for (int i = 0; i < common.length; i++) row.put(common[i], d[i]);
                row.put("role", d[6].toUpperCase(Locale.ROOT));
                row.put("active", "true"); // Shared User has no activation field.
                if (length == 9) { row.put("specialization", d[7]); row.put("roomNumber", d[8]); }
                if (length == 10) { row.put("bloodType", d[7]); row.put("emergencyContact", d[8]); row.put("medicalHistory", d[9]); }
                result.add(row);
            }
        }
        return result;
    }
    public static User toUser(Map<String, String> r) {
        String id=r.get("id"), username=r.get("username"), password=r.get("passwordHash"), name=r.get("name"), phone=r.get("phone"), email=r.get("email");
        return switch (r.get("role")) {
            case "ADMIN" -> new AdminStaff(id, username, password, name, phone, email);
            case "MANAGER" -> new Manager(id, username, password, name, phone, email);
            case "DOCTOR" -> new Doctor(id, username, password, name, phone, email, r.get("specialization"), r.get("roomNumber"));
            case "PATIENT" -> new Patient(id, username, password, name, phone, email, r.get("bloodType"), r.get("emergencyContact"), r.get("medicalHistory"));
            default -> throw new IllegalArgumentException("Unsupported user role.");
        };
    }
    private List<Map<String, String>> readRequests() throws IOException {
        List<Map<String, String>> result = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (String line : lines(folder.resolve("lab_tests.txt"))) {
            if (line.isBlank()) continue;
            String[] d = cols(line);
            if (d.length != 7 || d[0].isBlank() || !ids.add(d[0])) throw new IOException("Invalid or duplicate record in lab_tests.txt.");
            Map<String, String> row = new LinkedHashMap<>();
            String[] keys = cols("id,patientId,doctorId,date,details,result,status");
            for (int i=0; i<keys.length; i++) row.put(keys[i],d[i]);
            row.put("type", facilityType(d[4]));
            row.put("status", switch (d[6].toUpperCase(Locale.ROOT)) {
                case "REQUESTED" -> "OPEN";
                case "SCHEDULED" -> read("allocations").stream().anyMatch(a -> a.get("requestId").equals(d[0])) ? "OPEN" : "EXTERNAL_SCHEDULED";
                case "ALLOCATED" -> "ALLOCATED";
                case "COMPLETED" -> "COMPLETED"; case "CANCELLED" -> "CANCELLED";
                default -> throw new IOException("Unknown lab request status: " + d[6]);
            });
            result.add(row);
        }
        return result;
    }
    public static String facilityType(String test) {
        return switch (test.toLowerCase(Locale.ROOT)) {
            case "chest x-ray" -> "XRAY";
            case "blood test (full blood count)", "urine analysis" -> "LAB";
            case "ct scan (abdomen)", "mri scan", "mri scan (brain / spine)", "ultrasound" -> "IMAGING";
            default -> "UNKNOWN";
        };
    }
    public synchronized void save(String table, List<Map<String, String>> rows) throws IOException {
        Map<Path,List<String>> writes = new LinkedHashMap<>();
        if (table.equals("users")) {
            List<String> shared = new ArrayList<>();
            for (Map<String,String> row : rows) {
                User user = toUser(row);
                for (String value : row.values()) clean(value);
                shared.add(user.toTxtRecord());
            }
            writes.put(folder.resolve("users.txt"), shared);
        } else if (table.equals("requests")) {
            writes.put(folder.resolve("lab_tests.txt"), requestLines(rows, read("allocations")));
        } else {
            String[] keys = schemas.get(table);
            Path path = tablePath(table);
            List<String> out = new ArrayList<>(); out.add(String.join(",", keys));
            for (Map<String,String> row : rows) {
                List<String> values = new ArrayList<>();
                for (String key : keys) values.add(clean(row.getOrDefault(key,"")));
                out.add(String.join(",", values));
            }
            writes.put(path, out);
            if (table.equals("allocations")) writes.put(folder.resolve("lab_tests.txt"), requestLines(read("requests"), rows));
        }
        // Validate all output first; restore earlier writes if a later write fails.
        Map<Path,List<String>> originals = new LinkedHashMap<>();
        Set<Path> missing = new HashSet<>();
        try {
            for (Map.Entry<Path,List<String>> entry : writes.entrySet()) {
                Path path=entry.getKey(); List<String> previous=lines(path);
                if (previous.equals(entry.getValue())) continue;
                if (!Files.exists(path)) missing.add(path);
                originals.put(path, previous);
                writeAtomic(path,entry.getValue());
            }
        } catch (IOException ex) {
            for (Map.Entry<Path,List<String>> old : originals.entrySet()) try {
                if (missing.contains(old.getKey())) Files.deleteIfExists(old.getKey()); else writeAtomic(old.getKey(),old.getValue());
            } catch (IOException rollback) { ex.addSuppressed(rollback); }
            throw ex;
        }
    }
    private List<String> requestLines(List<Map<String,String>> rows, List<Map<String,String>> allocations) {
        List<String> out=new ArrayList<>();
        for (Map<String,String> row : rows) {
            String status = switch(row.get("status")) {
                case "COMPLETED" -> "Completed"; case "CANCELLED" -> "Cancelled";
                case "ALLOCATED" -> "Allocated"; case "EXTERNAL_SCHEDULED" -> "Scheduled"; default -> "Requested";
            };
            if (status.equals("Requested")) for (Map<String,String> a : allocations)
                if (a.get("requestId").equals(row.get("id")) && !a.get("status").equals("CANCELLED")) status="Scheduled";
            List<String> values=new ArrayList<>();
            for (String key : cols("id,patientId,doctorId,date,details,result")) values.add(clean(row.get(key)));
            values.add(status); out.add(String.join(",",values));
        }
        return out;
    }
    private static String clean(String value) {
        if (value == null || value.contains(",") || value.contains("\n") || value.contains("\r") || value.contains("\t"))
            throw new IllegalArgumentException("Text cannot contain commas, tabs or line breaks because the group uses plain CSV.");
        return value;
    }
    private void writeAtomic(Path path,List<String> text) throws IOException {
        Path tmp=Files.createTempFile(folder,"admin-save-",".tmp");
        try {
            Files.write(tmp,text,StandardCharsets.UTF_8);
            try { Files.move(tmp,path,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.ATOMIC_MOVE); }
            catch(AtomicMoveNotSupportedException ex) { Files.move(tmp,path,StandardCopyOption.REPLACE_EXISTING); }
        } finally { Files.deleteIfExists(tmp); }
    }
    /** Protect clinical history and manager rosters when deleting a user. */
    public boolean referencedByGroup(String id) throws IOException {
        for (String file : List.of("appointments.txt","feedbacks.txt","lab_tests.txt","vitals.txt","prescriptions.txt","roster.txt","department.txt","payment.txt"))
            for (String line : lines(folder.resolve(file)))
                for (String part : line.split("[,\\s:]+")) if (part.equalsIgnoreCase(id)) return true;
        return false;
    }
    public String nextUserId(String role) throws IOException {
        String prefix=switch(role) { case "ADMIN" -> "A"; case "MANAGER" -> "M"; case "DOCTOR" -> "D"; default -> "P"; };
        Set<String> taken=new HashSet<>(); for(Map<String,String> user: readUsers()) taken.add(user.get("id").toUpperCase(Locale.ROOT));
        for(int i=1; i<=999; i++) {
            String id=prefix+String.format(Locale.ROOT,"%03d",i);
            if(!taken.contains(id) && !referencedByGroup(id)) return id;
        }
        throw new IllegalArgumentException("No unused three-digit IDs available for this role.");
    }
}

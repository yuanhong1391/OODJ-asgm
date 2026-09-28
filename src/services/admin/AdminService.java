package services.admin;

import models.User;
import models.AdminStaff;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/** Shared business rules. UI code must use this service instead of editing files. */
public final class AdminService {
    private final AdminStore store;
    private User current;
    public AdminService(AdminStore store) { this.store = store; }
    public User currentUser() { return current; }
    public String dataLocation() { return store.getFolder().toString(); }
    public boolean needsSetup() throws IOException {
        return store.read("users").stream().noneMatch(u -> u.get("role").equals("ADMIN"));
    }
    public synchronized String setup(String name, String username, String password) throws IOException {
        require(needsSetup(), "Initial administrator already exists.");
        Map<String,String> data=row("role","ADMIN","name",name,"username",username,"password",password);
        saveUser(data,true);
        return data.get("id");
    }
    public User login(String id, String password) throws IOException {
        current = null;
        for (Map<String,String> record : store.read("users")) {
            if (record.get("role").equals("ADMIN") && record.get("id").equalsIgnoreCase(id.trim())
                    && Passwords.matches(password,record.get("passwordHash"))) {
                current=AdminStore.toUser(record);
                return current;
            }
        }
        throw new IllegalArgumentException("Admin ID or password is incorrect. Other roles use the group Login.");
    }
    public void logout() { current = null; }
    private void admin() throws IOException {
        require(current != null && current instanceof AdminStaff, "Administrator access is required.");
        Map<String, String> account = find("users", current.getID());
        require(account.get("role").equals("ADMIN") && account.get("active").equals("true"), "Administrator account is unavailable.");
    }
    public List<Map<String, String>> list(String table) throws IOException {
        admin();
        List<Map<String, String>> rows = store.read(table);
        for (Map<String, String> row : rows) {
            row.remove("passwordHash");
            if (table.equals("requests")) row.put("status", requestStatus(row));
        }
        return rows;
    }
    public synchronized void save(String table, Map<String, String> values) throws IOException {
        admin();
        Map<String, String> data = new LinkedHashMap<>(values);
        data.replaceAll((key, value) -> key.equals("password") ? value : value.trim());
        for (String value : data.values()) require(!value.contains(",") && !value.contains("\n") && !value.contains("\r") && !value.contains("\t"), "Do not enter commas, tabs or line breaks.");
        switch (table) {
            case "users" -> saveUser(data, false);
            case "assignments" -> saveAssignment(data);
            case "assets" -> saveAsset(data);
            case "allocations" -> saveAllocation(data);
            case "requests" -> saveRequest(data);
            case "rates" -> saveRate(data);
            case "insurance" -> saveInsurance(data);
            default -> throw new IllegalArgumentException("Unsupported record type.");
        }
    }
    private void saveUser(Map<String, String> data, boolean setup) throws IOException {
        String id=data.getOrDefault("id","");
        Map<String,String> old=id.isBlank()?null:find("users",id);
        required(data,"name","username","role");
        oneOf(data,"role","ADMIN","MANAGER","DOCTOR","PATIENT");
        require(data.get("username").matches("[A-Za-z0-9._-]{3,40}"), "Username must be 3-40 letters, digits, dots, underscores or hyphens.");
        unique("users","username",data.get("username"),id);
        String email=data.getOrDefault("email","").trim(), phone=data.getOrDefault("phone","").trim();
        require(email.isEmpty() || email.equals("-") || email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"), "Enter a valid email, or leave it blank.");
        require(phone.isEmpty() || phone.equals("-") || (phone.matches("[+0-9() .-]{5,25}") && phone.replaceAll("[^0-9]","").length()>=5), "Enter a valid phone number.");
        data.put("email",email.isEmpty()?"-":email);
        data.put("phone",phone.isEmpty()?"-":phone);
        data.put("active","true");
        if(old!=null) require(old.get("role").equals(data.get("role")), "Existing roles cannot change. Create the correct role as a new account.");
        String password=data.getOrDefault("password","");
        if(old==null || !password.isEmpty()) {
            require(password.length()>=8, "Password must contain at least 8 characters.");
            require(password.equals(password.trim()), "Password cannot start or end with spaces.");
            require(!password.contains(",") && !password.contains("\n") && !password.contains("\r") && !password.contains("\t"), "Password cannot contain commas, tabs or line breaks.");
            data.put("passwordHash", data.get("role").equals("ADMIN")?Passwords.hash(password):password);
        } else data.put("passwordHash",old.get("passwordHash"));
        // Keep clinical fields from the shared Doctor and Patient records.
        for(String key:List.of("specialization","roomNumber","bloodType","emergencyContact","medicalHistory")) {
            String value=data.getOrDefault(key,old==null?"":old.getOrDefault(key,"")).trim();
            data.put(key,value.isEmpty()?"Not specified":value);
        }
        if(old==null) {
            data.put("id",store.nextUserId(data.get("role")));
            List<Map<String,String>> users=store.read("users"); users.add(new LinkedHashMap<>(data)); store.save("users",users);
        } else upsert("users",data,"USR");
        if(!setup && current!=null && current.getID().equals(data.get("id"))) current=AdminStore.toUser(data);
    }
    private long activeAdmins() throws IOException {
        return store.read("users").stream().filter(u -> u.get("role").equals("ADMIN") && u.get("active").equals("true")).count();
    }
    private boolean hasLiveWork(String id) throws IOException {
        for (Map<String, String> a : store.read("assignments")) if (a.containsValue(id)) return true;
        for (Map<String, String> r : store.read("requests"))
            if ((r.get("doctorId").equals(id) || r.get("patientId").equals(id)) && r.get("status").equals("OPEN")) return true;
        for (Map<String, String> a : store.read("allocations"))
            if ((a.get("doctorId").equals(id) || a.get("patientId").equals(id)) && liveAllocation(a)) return true;
        return false;
    }
    private boolean userReferenced(String id) throws IOException {
        for (String table : List.of("assignments", "requests", "allocations"))
            for (Map<String, String> row : store.read(table))
                if (id.equals(row.get("doctorId")) || id.equals(row.get("managerId")) || id.equals(row.get("patientId"))) return true;
        return store.referencedByGroup(id);
    }
    private void saveAssignment(Map<String, String> data) throws IOException {
        required(data, "doctorId", "managerId");
        role(data.get("doctorId"), "DOCTOR"); role(data.get("managerId"), "MANAGER");
        List<Map<String, String>> rows = store.read("assignments");
        rows.removeIf(r -> r.get("doctorId").equals(data.get("doctorId")));
        rows.add(data); store.save("assignments", rows);
    }
    private void saveAsset(Map<String, String> data) throws IOException {
        required(data, "type", "name", "status");
        oneOf(data, "type", "CONSULTATION_ROOM", "WARD", "BED", "LAB", "XRAY", "IMAGING");
        oneOf(data, "status", "AVAILABLE", "MAINTENANCE", "INACTIVE");
        String id = data.getOrDefault("id", "");
        unique("assets", "name", data.get("name"), id);
        String parent = data.getOrDefault("parentId", "");
        if (data.get("type").equals("BED")) {
            require(!parent.isEmpty() && !parent.equals(id), "A bed must belong to a separate ward.");
            Map<String, String> ward = find("assets", parent);
            require(ward.get("type").equals("WARD"), "The parent asset must be a ward.");
        } else require(parent.isEmpty(), "Only beds can have a parent ward.");
        if (!id.isEmpty()) {
            Map<String, String> old = find("assets", id);
            boolean hasChildren = store.read("assets").stream().anyMatch(a -> a.get("parentId").equals(id));
            boolean hasHistory = store.read("allocations").stream().anyMatch(a -> a.get("assetId").equals(id));
            require((!hasChildren && !hasHistory) || old.get("type").equals(data.get("type")), "Asset type cannot change while beds or allocation history reference it.");
            if (!data.get("status").equals("AVAILABLE") || !old.get("parentId").equals(parent)) {
                Set<String> affected = new HashSet<>(); affected.add(id);
                for (Map<String, String> a : store.read("assets")) if (a.get("parentId").equals(id)) affected.add(a.get("id"));
                for (Map<String, String> a : store.read("allocations"))
                    require(!affected.contains(a.get("assetId")) || !liveAllocation(a), "Finish or cancel active allocations before changing asset availability or its ward.");
            }
        }
        upsert("assets", data, "AST");
    }
    private void saveAllocation(Map<String, String> data) throws IOException {
        required(data, "assetId", "patientId", "start", "end", "status");
        oneOf(data, "status", "BOOKED", "COMPLETED", "CANCELLED");
        String id = data.getOrDefault("id", "");
        Map<String, String> old = id.isEmpty() ? null : find("allocations", id);
        if (old != null && !old.get("status").equals("BOOKED")) throw new IllegalArgumentException("Completed/cancelled allocations are historical records. Create a new allocation.");
        if (old == null) require(data.get("status").equals("BOOKED"), "New allocations must have BOOKED status.");
        LocalDateTime start = time(data.get("start")); LocalDateTime end = time(data.get("end"));
        require(end.isAfter(start), "End time must be after start time.");
        if (old != null && !data.get("status").equals("BOOKED")) {
            for (String key : List.of("assetId", "patientId", "doctorId", "requestId", "start", "end"))
                require(old.get(key).equals(data.getOrDefault(key, "")), "Change allocation status separately from other details.");
            upsert("allocations", data, "ALC"); return;
        }
        role(data.get("patientId"), "PATIENT");
        String doctor = data.getOrDefault("doctorId", "");
        if (!doctor.isEmpty()) role(doctor, "DOCTOR");
        Map<String, String> asset = find("assets", data.get("assetId"));
        require(!asset.get("type").equals("WARD"), "Allocate a bed within the ward, not the whole ward.");
        require(asset.get("status").equals("AVAILABLE"), "The selected asset is not available.");
        if (asset.get("type").equals("BED")) require(find("assets", asset.get("parentId")).get("status").equals("AVAILABLE"), "The bed's ward is not available.");
        if (asset.get("type").equals("CONSULTATION_ROOM")) require(!doctor.isEmpty(), "Consultation rooms require a doctor.");
        String requestId = data.getOrDefault("requestId", "");
        if (!requestId.isEmpty()) {
            Map<String, String> request = find("requests", requestId);
            require(request.get("status").equals("OPEN"), "Only open requests can be scheduled.");
            require(request.get("patientId").equals(data.get("patientId")) && request.get("doctorId").equals(doctor), "Patient and doctor must match the request.");
            require(request.get("type").equals(asset.get("type")), "The facility type must match the requested test.");
            for (Map<String, String> a : store.read("allocations"))
                require(a.get("id").equals(id) || !a.get("requestId").equals(requestId) || a.get("status").equals("CANCELLED"), "This request already has an allocation. Edit it, or cancel it before rescheduling.");
        } else require(!List.of("LAB", "XRAY", "IMAGING").contains(asset.get("type")), "Lab/imaging allocations must link to a doctor's request.");
        for (Map<String, String> a : store.read("allocations")) {
            if (a.get("id").equals(id) || !liveAllocation(a)) continue;
            boolean overlap = start.isBefore(time(a.get("end"))) && end.isAfter(time(a.get("start")));
            if (overlap) {
                require(!a.get("assetId").equals(data.get("assetId")), "This asset already has an overlapping allocation.");
                Map<String, String> otherAsset = find("assets", a.get("assetId"));
                if (!asset.get("type").equals("BED") && !otherAsset.get("type").equals("BED")) {
                    require(!a.get("patientId").equals(data.get("patientId")), "The patient has an overlapping appointment/test allocation.");
                    if (!doctor.isEmpty() && asset.get("type").equals("CONSULTATION_ROOM") && otherAsset.get("type").equals("CONSULTATION_ROOM"))
                        require(!a.get("doctorId").equals(doctor), "The doctor already has an overlapping consultation allocation.");
                }
                if (asset.get("type").equals("BED") && otherAsset.get("type").equals("BED"))
                    require(!a.get("patientId").equals(data.get("patientId")), "The patient already has an overlapping bed allocation.");
            }
        }
        upsert("allocations", data, "ALC");
    }
    private boolean liveAllocation(Map<String, String> allocation) throws IOException {
        if (!allocation.get("status").equals("BOOKED")) return false;
        String requestId = allocation.get("requestId");
        return requestId.isEmpty() || find("requests", requestId).get("status").equals("OPEN");
    }
    private String requestStatus(Map<String, String> request) throws IOException {
        if (!request.get("status").equals("OPEN")) return request.get("status");
        for (Map<String, String> a : store.read("allocations"))
            if (a.get("requestId").equals(request.get("id")) && !a.get("status").equals("CANCELLED")) return "SCHEDULED";
        return "OPEN";
    }
    private void saveRequest(Map<String, String> data) throws IOException {
        required(data, "doctorId", "patientId", "type", "details");
        oneOf(data, "type", "LAB", "XRAY", "IMAGING");
        String id = data.getOrDefault("id", "");
        String status = data.getOrDefault("status", "OPEN");
        if (status.equals("SCHEDULED")) status = "OPEN";
        data.put("status", status);
        oneOf(data, "status", "OPEN", "COMPLETED", "CANCELLED");
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Doctors create test requests through the group Doctor dashboard.");
        } else {
            Map<String, String> old = find("requests", id);
            data.put("date",old.get("date"));
            data.put("result",old.get("result"));
            if (status.equals("CANCELLED")) require(store.read("allocations").stream().noneMatch(a -> a.get("requestId").equals(id) && a.get("status").equals("COMPLETED")), "A performed test cannot be cancelled. Complete the request instead.");
            require(old.get("status").equals("OPEN"), "Closed requests are historical records and cannot be edited.");
            for (String key : List.of("doctorId", "patientId", "type", "details"))
                require(old.get(key).equals(data.get(key)), "Clinical request details cannot be edited by admin. Cancel and record a replacement request.");
            if (status.equals("COMPLETED")) {
                boolean done = store.read("allocations").stream().anyMatch(a -> a.get("requestId").equals(id) && a.get("status").equals("COMPLETED"));
                require(done, "Mark the request's allocation COMPLETED before completing the request.");
            }
            if (!status.equals("OPEN")) for (Map<String, String> a : store.read("allocations"))
                require(!a.get("requestId").equals(id) || !a.get("status").equals("BOOKED"), "Complete or cancel the booked allocation before closing the request.");
        }
        upsert("requests", data, "REQ");
    }
    private void saveRate(Map<String, String> data) throws IOException {
        required(data, "consultationType", "amount", "active"); bool(data, "active");
        unique("rates", "consultationType", data.get("consultationType"), data.getOrDefault("id", ""));
        try {
            BigDecimal amount = new BigDecimal(data.get("amount"));
            require(amount.signum() >= 0 && amount.compareTo(new BigDecimal("1000000")) <= 0 && amount.scale() <= 2,
                    "Rate must be between 0 and 1,000,000 with at most two decimal places.");
            data.put("amount", amount.setScale(2).toPlainString());
        } catch (NumberFormatException ex) { throw new IllegalArgumentException("Enter a numeric consultation rate."); }
        upsert("rates", data, "RAT");
    }
    private void saveInsurance(Map<String, String> data) throws IOException {
        required(data, "name", "active"); bool(data, "active");
        unique("insurance", "name", data.get("name"), data.getOrDefault("id", ""));
        upsert("insurance", data, "INS");
    }
    public synchronized void delete(String table, String id) throws IOException {
        admin();
        if (table.equals("users")) {
            Map<String, String> user = find(table, id);
            require(!current.getID().equals(id), "You cannot delete your own account.");
            require(!userReferenced(id), "This user is referenced by other records. Keep this account to preserve clinical history.");
            if (user.get("role").equals("ADMIN") && user.get("active").equals("true")) require(activeAdmins() > 1, "Keep at least one active administrator.");
        } else if (table.equals("assets")) {
            require(store.read("assets").stream().noneMatch(a -> a.get("parentId").equals(id)), "Remove this ward's beds first.");
            require(store.read("allocations").stream().noneMatch(a -> a.get("assetId").equals(id)), "This asset has allocation history; mark it INACTIVE instead.");
        } else require(List.of("assignments", "rates", "insurance").contains(table), "Keep allocation/request history. Change its status instead of deleting it.");
        String key = table.equals("assignments") ? "doctorId" : "id";
        List<Map<String, String>> rows = store.read(table);
        require(rows.removeIf(r -> r.get(key).equals(id)), "Record no longer exists.");
        store.save(table, rows);
    }
    private void upsert(String table, Map<String, String> data, String prefix) throws IOException {
        List<Map<String, String>> rows = store.read(table);
        String id = data.getOrDefault("id", "");
        if (id.isEmpty()) {
            data.put("id", prefix + "-" + UUID.randomUUID());
            rows.add(new LinkedHashMap<>(data));
        } else {
            find(table, id);
            for (int i=0; i<rows.size(); i++) if (rows.get(i).get("id").equals(id)) {
                rows.set(i,new LinkedHashMap<>(data)); break;
            }
        }
        store.save(table, rows);
    }
    private Map<String, String> find(String table, String id) throws IOException {
        return store.read(table).stream().filter(r -> id.equals(r.get("id"))).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing " + table + " record: " + id));
    }
    private void role(String id, String expected) throws IOException {
        Map<String, String> user = find("users", id);
        require(user.get("role").equals(expected) && user.get("active").equals("true"), "Select an active " + expected.toLowerCase() + ".");
    }
    private void unique(String table, String field, String value, String exceptId) throws IOException {
        for (Map<String, String> row : store.read(table)) require(row.get("id").equals(exceptId) || !row.get(field).equalsIgnoreCase(value), "A record already uses this " + field + ".");
    }
    private static void required(Map<String, String> data, String... fields) {
        for (String field : fields) {
            require(!data.getOrDefault(field, "").isBlank(), "Please enter " + field + ".");
            require(data.get(field).length() <= 2000, field + " is too long (maximum 2000 characters).");
        }
    }
    private static void bool(Map<String, String> data, String field) { oneOf(data, field, "true", "false"); }
    private static void oneOf(Map<String, String> data, String field, String... allowed) {
        require(Arrays.asList(allowed).contains(data.get(field)), "Invalid " + field + ".");
    }
    private static void require(boolean condition, String message) { if (!condition) throw new IllegalArgumentException(message); }
    private static LocalDateTime time(String value) {
        try { return LocalDateTime.parse(value); }
        catch (DateTimeParseException ex) { throw new IllegalArgumentException("Use date/time format YYYY-MM-DDTHH:MM, for example 2026-09-27T09:00."); }
    }
    public static Map<String, String> row(String... values) {
        Map<String, String> result = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i += 2) result.put(values[i], values[i + 1]);
        return result;
    }
}

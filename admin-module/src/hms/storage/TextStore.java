package hms.storage;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Owns file I/O. One running application owns this data folder at a time. */
public final class TextStore implements AutoCloseable {
    private final Path folder;
    private final FileChannel channel;
    private final FileLock lock;
    private final Map<String, String[]> schemas = new LinkedHashMap<>();

    public TextStore(Path folder) throws IOException {
        this.folder = folder.toAbsolutePath();
        Files.createDirectories(this.folder);
        channel = FileChannel.open(this.folder.resolve("application.lock"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        FileLock acquired;
        try { acquired = channel.tryLock(); }
        catch (OverlappingFileLockException ex) { acquired = null; }
        if (acquired == null) { channel.close(); throw new IOException("This data folder is already open in another application."); }
        lock = acquired;
        schemas.put("users", cols("id|role|name|username|passwordHash|email|phone|active"));
        schemas.put("assignments", cols("doctorId|managerId"));
        schemas.put("assets", cols("id|type|name|parentId|status"));
        schemas.put("allocations", cols("id|assetId|patientId|doctorId|requestId|start|end|status"));
        schemas.put("requests", cols("id|doctorId|patientId|type|details|status"));
        schemas.put("rates", cols("id|consultationType|amount|active"));
        schemas.put("insurance", cols("id|name|active"));
        try {
            for (String table : schemas.keySet()) {
                if (!Files.exists(path(table))) save(table, new ArrayList<>());
                read(table); // Fail explicitly on corrupt files instead of silently discarding data.
            }
        } catch (IOException | RuntimeException ex) { close(); throw ex; }
    }
    private static String[] cols(String text) { return text.split("\\|"); }
    public Path getFolder() { return folder; }
    public String[] columns(String table) {
        if (!schemas.containsKey(table)) throw new IllegalArgumentException("Unknown table: " + table);
        return schemas.get(table).clone();
    }
    private Path path(String table) { columns(table); return folder.resolve(table + ".txt"); }
    public synchronized List<Map<String, String>> read(String table) throws IOException {
        String[] keys = columns(table);
        List<String> lines = Files.readAllLines(path(table), StandardCharsets.UTF_8);
        if (lines.isEmpty() || !lines.get(0).equals(String.join("\t", keys)))
            throw new IOException("Invalid header in " + table + ".txt. Restore a valid file before continuing.");
        List<Map<String, String>> rows = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (int i = 1; i < lines.size(); i++) {
            String[] values = lines.get(i).split("\t", -1);
            if (values.length != keys.length) throw new IOException("Invalid row " + (i + 1) + " in " + table + ".txt");
            Map<String, String> row = new LinkedHashMap<>();
            for (int j = 0; j < keys.length; j++) row.put(keys[j], decode(values[j]));
            if (row.get(keys[0]).isBlank() || !ids.add(row.get(keys[0]))) throw new IOException("Missing or duplicate ID in " + table + ".txt");
            rows.add(row);
        }
        return rows;
    }
    public synchronized void save(String table, List<Map<String, String>> rows) throws IOException {
        String[] keys = columns(table);
        List<String> lines = new ArrayList<>();
        lines.add(String.join("\t", keys));
        for (Map<String, String> row : rows) {
            List<String> values = new ArrayList<>();
            for (String key : keys) values.add(encode(row.getOrDefault(key, "")));
            lines.add(String.join("\t", values));
        }
        Path temp = Files.createTempFile(folder, table, ".tmp");
        try {
            Files.write(temp, lines, StandardCharsets.UTF_8);
            try { Files.move(temp, path(table), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
            catch (AtomicMoveNotSupportedException ex) { Files.move(temp, path(table), StandardCopyOption.REPLACE_EXISTING); }
        } finally { Files.deleteIfExists(temp); }
    }
    private static String encode(String value) {
        return value.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n").replace("\r", "\\r");
    }
    private static String decode(String value) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\') {
                if (++i == value.length()) throw new IOException("Invalid trailing escape in text file.");
                c = value.charAt(i);
                switch (c) {
                    case 't' -> result.append('\t'); case 'n' -> result.append('\n');
                    case 'r' -> result.append('\r'); case '\\' -> result.append('\\');
                    default -> throw new IOException("Invalid escape in text file.");
                }
            } else result.append(c);
        }
        return result.toString();
    }
    @Override public void close() throws IOException { if (lock.isValid()) lock.release(); channel.close(); }
}

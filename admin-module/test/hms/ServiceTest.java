package hms;

import hms.service.HospitalService;
import hms.storage.TextStore;
import hms.ui.EditorPanel;
import hms.ui.forms.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import static hms.service.HospitalService.row;

/** Dependency-free integration tests; uses isolated temporary files, never live data. */
public final class ServiceTest {
    private static int checks;
    @FunctionalInterface interface Checked { void run() throws Exception; }
    private static void check(boolean ok, String description) {
        if (!ok) throw new AssertionError(description);
        checks++; System.out.println("PASS " + description);
    }
    private static void rejected(Checked action, String description) throws Exception {
        try { action.run(); } catch (IllegalArgumentException | java.io.IOException ex) { check(true, description); return; }
        throw new AssertionError("Expected rejection: " + description);
    }
    private static String id(HospitalService service, String table, String field, String value) throws Exception {
        return service.list(table).stream().filter(r -> value.equals(r.get(field))).findFirst().orElseThrow().get("id");
    }
    private static void user(HospitalService service, String role, String name) throws Exception {
        service.save("users", row("role", role, "name", name, "username", name, "password", "Practice123!", "email", "", "phone", "", "active", "true"));
    }
    public static void main(String[] args) throws Exception {
        Path folder = Files.createTempDirectory("hms-service-test-");
        String doctor;
        String patient;
        try (TextStore store = new TextStore(folder)) {
            HospitalService s = new HospitalService(store);
            check(s.needsSetup(), "Empty data folder requires initial setup");
            rejected(() -> s.list("users"), "Unauthenticated access rejected");
            rejected(() -> s.setup("Admin", "admin", "short"), "Short password rejected");
            s.setup("Admin", "admin", "Practice123!");
            rejected(() -> s.setup("Other", "other", "Practice123!"), "Second bootstrap rejected");
            rejected(() -> s.login("admin", "incorrect"), "Incorrect password rejected");
            check(s.login("ADMIN", "Practice123!").canAdminister(), "Case-insensitive username login");
            String adminId = s.currentUser().getId();
            rejected(() -> s.delete("users", adminId), "Self deletion blocked");
            Map<String, String> admin = new LinkedHashMap<>(s.list("users").get(0)); admin.put("active", "false");
            rejected(() -> s.save("users", admin), "Self deactivation blocked");
            check(!s.list("users").get(0).containsKey("passwordHash"), "Service does not expose password hashes");
            check(!Files.readString(folder.resolve("users.txt")).contains("Practice123!"), "Password is not persisted as plaintext");
            user(s, "DOCTOR", "doctor"); user(s, "DOCTOR", "doctor2"); user(s, "MANAGER", "manager"); user(s, "PATIENT", "patient"); user(s, "PATIENT", "patient2");
            rejected(() -> user(s, "PATIENT", "DOCTOR"), "Duplicate username is case-insensitively rejected");
            doctor = id(s, "users", "username", "doctor"); patient = id(s, "users", "username", "patient");
            String d = doctor, p = patient;
            String manager = id(s, "users", "username", "manager");
            String patient2 = id(s, "users", "username", "patient2");
            String doctor2 = id(s, "users", "username", "doctor2");
            rejected(() -> s.save("assignments", row("doctorId", p, "managerId", manager)), "Wrong-role assignment blocked");
            s.save("assignments", row("doctorId", doctor, "managerId", manager));
            s.save("assignments", row("doctorId", doctor, "managerId", manager));
            check(s.list("assignments").size() == 1, "Reassignment replaces the doctor's existing assignment");
            rejected(() -> s.delete("users", manager), "Referenced manager deletion blocked");
            Map<String, String> changed = new LinkedHashMap<>(s.list("users").stream().filter(u -> u.get("id").equals(d)).findFirst().orElseThrow());
            changed.put("role", "PATIENT"); rejected(() -> s.save("users", changed), "Referenced user role cannot change");
            s.save("assets", row("name", "Ward A", "type", "WARD", "parentId", "", "status", "AVAILABLE"));
            String ward = id(s, "assets", "name", "Ward A");
            rejected(() -> s.save("assets", row("name", "Bad Bed", "type", "BED", "parentId", "", "status", "AVAILABLE")), "Bed requires a ward");
            s.save("assets", row("name", "Bed A1", "type", "BED", "parentId", ward, "status", "AVAILABLE"));
            String bed = id(s, "assets", "name", "Bed A1");
            s.save("assets", row("name", "Room 1", "type", "CONSULTATION_ROOM", "parentId", "", "status", "AVAILABLE"));
            s.save("assets", row("name", "Room 2", "type", "CONSULTATION_ROOM", "parentId", "", "status", "AVAILABLE"));
            s.save("assets", row("name", "Lab 1", "type", "LAB", "parentId", "", "status", "AVAILABLE"));
            String room = id(s, "assets", "name", "Room 1"), room2 = id(s, "assets", "name", "Room 2"), lab = id(s, "assets", "name", "Lab 1");
            Map<String, String> allocation = row("assetId", room, "patientId", patient, "doctorId", doctor, "requestId", "", "start", "2026-09-27T09:00", "end", "2026-09-27T10:00", "status", "BOOKED");
            s.save("allocations", allocation);
            rejected(() -> s.save("allocations", allocation), "Same-room overlap rejected");
            Map<String, String> clash = new LinkedHashMap<>(allocation); clash.put("assetId", room2); clash.put("patientId", patient2);
            rejected(() -> s.save("allocations", clash), "Doctor double booking rejected across consultation rooms");
            clash.put("doctorId", doctor2); clash.put("patientId", patient);
            rejected(() -> s.save("allocations", clash), "Patient overlap rejected across consultation rooms");
            Map<String, String> adjacent = new LinkedHashMap<>(allocation); adjacent.put("start", "2026-09-27T10:00"); adjacent.put("end", "2026-09-27T11:00"); s.save("allocations", adjacent);
            check(s.list("allocations").size() == 2, "Adjacent bookings are allowed");
            Map<String, String> invalidTime = new LinkedHashMap<>(allocation); invalidTime.put("end", "2026-09-27T08:00");
            rejected(() -> s.save("allocations", invalidTime), "Reversed time range rejected");
            invalidTime.put("end", "not a date"); rejected(() -> s.save("allocations", invalidTime), "Malformed date rejected");
            s.save("allocations", row("assetId", bed, "patientId", patient, "doctorId", "", "requestId", "", "start", "2026-09-27T08:00", "end", "2026-09-28T08:00", "status", "BOOKED"));
            check(s.list("allocations").size() == 3, "Inpatient stay can overlap a consultation");
            rejected(() -> s.save("assets", row("id", ward, "name", "Ward A", "type", "WARD", "parentId", "", "status", "MAINTENANCE")), "Ward maintenance blocked while a bed is allocated");
            rejected(() -> s.delete("assets", room), "Asset deletion preserves allocation history");
            s.save("requests", row("doctorId", doctor, "patientId", patient, "type", "LAB", "details", "Blood test\twith note\nFollow up \\ result", "status", "OPEN"));
            String request = s.list("requests").get(0).get("id");
            check(s.list("requests").get(0).get("details").contains("\twith note\nFollow up \\"), "Text escaping round-trips tabs, newlines and backslashes");
            Map<String, String> testAllocation = row("assetId", lab, "patientId", patient, "doctorId", doctor, "requestId", request, "start", "2026-09-27T11:00", "end", "2026-09-27T12:00", "status", "BOOKED");
            s.save("allocations", testAllocation);
            check(s.list("requests").get(0).get("status").equals("SCHEDULED"), "Scheduling derives SCHEDULED request status");
            Map<String, String> duplicateTest = new LinkedHashMap<>(testAllocation); duplicateTest.put("start", "2026-09-27T13:00"); duplicateTest.put("end", "2026-09-27T14:00");
            rejected(() -> s.save("allocations", duplicateTest), "Duplicate scheduling rejected even at a different time");
            Map<String, String> req = new LinkedHashMap<>(s.list("requests").get(0)); req.put("status", "COMPLETED");
            rejected(() -> s.save("requests", req), "Request cannot complete before its allocation");
            Map<String, String> booked = s.list("allocations").stream().filter(a -> request.equals(a.get("requestId"))).findFirst().orElseThrow();
            booked.put("status", "COMPLETED"); s.save("allocations", booked); s.save("requests", req);
            check(s.list("requests").get(0).get("status").equals("COMPLETED"), "Request completion follows allocation completion");
            rejected(() -> s.save("requests", req), "Closed request cannot be edited");
            rejected(() -> s.delete("requests", request), "Request history cannot be deleted");
            s.save("rates", row("consultationType", "General", "amount", "80", "active", "true"));
            check(s.list("rates").get(0).get("amount").equals("80.00"), "Currency stored with two decimals");
            rejected(() -> s.save("rates", row("consultationType", "Bad", "amount", "-1", "active", "true")), "Negative rate rejected");
            rejected(() -> s.save("rates", row("consultationType", "Bad", "amount", "10.001", "active", "true")), "Excess currency precision rejected");
            s.save("insurance", row("name", "Sample Network", "active", "true"));
            rejected(() -> s.save("insurance", row("name", "sample network", "active", "true")), "Duplicate insurance network rejected");
            user(s, "PATIENT", "temporary"); String temporary = id(s, "users", "username", "temporary"); s.delete("users", temporary);
            check(s.list("users").stream().noneMatch(u -> u.get("id").equals(temporary)), "Unreferenced user can be deleted");
            s.logout(); rejected(() -> s.save("insurance", row("name", "Unauthorised", "active", "true")), "Writes blocked after logout");
            s.login("doctor", "Practice123!"); rejected(() -> s.list("users"), "Doctor cannot access admin listing");
            s.submitRequest(patient, "XRAY", "Chest X-ray requested by doctor");
            s.logout(); s.login("admin", "Practice123!"); check(s.list("requests").size() == 2, "Doctor request API feeds admin queue");
            Map<String, String> xrayRequest = s.list("requests").stream().filter(r -> r.get("type").equals("XRAY")).findFirst().orElseThrow();
            String xrayRequestId = xrayRequest.get("id");
            Map<String, String> xrayAllocation = row("assetId", lab, "patientId", patient, "doctorId", doctor, "requestId", xrayRequestId, "start", "2026-09-28T09:00", "end", "2026-09-28T10:00", "status", "BOOKED");
            rejected(() -> s.save("allocations", xrayAllocation), "Request rejects a facility of the wrong type");
            s.save("assets", row("name", "Xray 1", "type", "XRAY", "parentId", "", "status", "AVAILABLE"));
            xrayAllocation.put("assetId", id(s, "assets", "name", "Xray 1"));
            xrayAllocation.put("patientId", patient2);
            rejected(() -> s.save("allocations", xrayAllocation), "Request rejects a mismatched patient");
            xrayAllocation.put("patientId", patient); s.save("allocations", xrayAllocation);
            xrayRequest.put("status", "CANCELLED");
            rejected(() -> s.save("requests", xrayRequest), "Request cancellation requires booked allocation cancellation first");
            Map<String, String> cancelled = s.list("allocations").stream().filter(a -> xrayRequestId.equals(a.get("requestId"))).findFirst().orElseThrow();
            cancelled.put("status", "CANCELLED"); s.save("allocations", cancelled);
            check(s.list("requests").stream().filter(r -> r.get("id").equals(xrayRequestId)).findFirst().orElseThrow().get("status").equals("OPEN"), "Cancelling allocation returns request to OPEN");
            s.save("allocations", xrayAllocation);
            check(s.list("allocations").stream().filter(a -> xrayRequestId.equals(a.get("requestId"))).count() == 2, "Cancelled allocation permits rescheduling and preserves history");
            Map<String, String> rescheduled = s.list("allocations").stream().filter(a -> xrayRequestId.equals(a.get("requestId")) && a.get("status").equals("BOOKED")).findFirst().orElseThrow();
            rescheduled.put("status", "CANCELLED"); s.save("allocations", rescheduled); s.save("requests", xrayRequest);
            check(s.list("requests").stream().filter(r -> r.get("id").equals(xrayRequestId)).findFirst().orElseThrow().get("status").equals("CANCELLED"), "Request cancels after allocations are cancelled");
            rejected(() -> { try (TextStore second = new TextStore(folder)) { } }, "Concurrent application cannot own the same data folder");
        }
        try (TextStore reopened = new TextStore(folder)) {
            HospitalService s = new HospitalService(reopened); s.login("admin", "Practice123!");
            check(s.list("requests").size() == 2 && s.list("allocations").size() == 6, "Records survive close and reopen");
        }
        Path corrupt = Files.createTempDirectory("hms-corrupt-test-");
        Files.writeString(corrupt.resolve("users.txt"), "wrong header\n");
        rejected(() -> { try (TextStore ignored = new TextStore(corrupt)) { } }, "Corrupt file rejected without data reset");
        check(Files.readString(corrupt.resolve("users.txt")).equals("wrong header\n"), "Corrupt source is preserved for recovery");
        renderForms();
        System.out.println("ALL " + checks + " CHECKS PASSED");
        System.out.println("Isolated test records: " + folder);
    }
    private static void renderForms() throws Exception {
        Path out = Path.of("build", "form-previews"); Files.createDirectories(out);
        SwingUtilities.invokeAndWait(() -> {
            try {
                EditorPanel[] forms = {new LoginForm(), new SetupForm(), new UserForm(), new AssignmentForm(), new AssetForm(), new AllocationForm(), new RequestForm(), new RateForm(), new InsuranceForm()};
                for (EditorPanel panel : forms) {
                    panel.setValues(row("name", "Sample record", "username", "sample.user", "email", "sample@example.test", "phone", "+60 12 345 6789", "start", "2026-09-27T09:00", "end", "2026-09-27T10:00", "details", "Sample request for a blood test", "amount", "80.00", "consultationType", "General consultation"));
                    Dimension preferred = panel.getPreferredSize();
                    panel.setSize(Math.max(800, preferred.width), Math.max(160, preferred.height)); layoutAll(panel);
                    BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = image.createGraphics(); panel.printAll(graphics); graphics.dispose();
                    ImageIO.write(image, "png", out.resolve(panel.getClass().getSimpleName() + ".png").toFile());
                    for (Component component : panel.getComponents()) {
                        if (component instanceof JLabel label) check(label.getFontMetrics(label.getFont()).stringWidth(label.getText()) <= label.getWidth(), panel.getClass().getSimpleName() + " label fits: " + label.getText());
                    }
                    check(!panel.values().isEmpty(), panel.getClass().getSimpleName() + " fields initialise");
                }
            } catch (Exception ex) { throw new RuntimeException(ex); }
        });
    }
    private static void layoutAll(Container container) {
        container.doLayout();
        for (Component child : container.getComponents()) if (child instanceof Container nested) layoutAll(nested);
    }
}

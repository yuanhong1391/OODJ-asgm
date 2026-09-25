package hms;

import hms.service.HospitalService;
import hms.storage.TextStore;
import java.nio.file.Path;
import static hms.service.HospitalService.row;

/** Optional fictional demonstration dataset, deliberately separate from live data. */
public final class DemoData {
    private DemoData() { }
    public static void main(String[] args) throws Exception {
        try (TextStore store = new TextStore(Path.of("demo-data"))) {
            HospitalService s = new HospitalService(store);
            if (!s.needsSetup()) throw new IllegalStateException("demo-data already has users; nothing has been overwritten.");
            s.setup("Demo Administrator", "admin.demo", "Practice123!"); s.login("admin.demo", "Practice123!");
            for (String[] u : new String[][]{{"MANAGER", "Dr Maya Tan", "manager.demo"}, {"DOCTOR", "Dr Amir Lee", "doctor.demo"}, {"PATIENT", "Alex Sample", "patient.demo"}})
                s.save("users", row("role", u[0], "name", u[1], "username", u[2], "password", "Practice123!", "email", "", "phone", "", "active", "true"));
            String manager = find(s, "users", "username", "manager.demo"), doctor = find(s, "users", "username", "doctor.demo"), patient = find(s, "users", "username", "patient.demo");
            s.save("assignments", row("doctorId", doctor, "managerId", manager));
            s.save("assets", row("name", "Ward A", "type", "WARD", "parentId", "", "status", "AVAILABLE"));
            String ward = find(s, "assets", "name", "Ward A");
            s.save("assets", row("name", "Bed A1", "type", "BED", "parentId", ward, "status", "AVAILABLE"));
            for (String[] a : new String[][]{{"CONSULTATION_ROOM", "Consultation 101"}, {"LAB", "Pathology Lab"}, {"XRAY", "X-ray Room"}, {"IMAGING", "Imaging Room"}})
                s.save("assets", row("name", a[1], "type", a[0], "parentId", "", "status", "AVAILABLE"));
            s.save("requests", row("doctorId", doctor, "patientId", patient, "type", "LAB", "details", "Fictional demo: full blood count", "status", "OPEN"));
            s.save("requests", row("doctorId", doctor, "patientId", patient, "type", "XRAY", "details", "Fictional demo: chest X-ray", "status", "OPEN"));
            s.save("rates", row("consultationType", "General consultation", "amount", "80.00", "active", "true"));
            s.save("rates", row("consultationType", "Specialist consultation", "amount", "150.00", "active", "true"));
            s.save("insurance", row("name", "Example Insurance Network", "active", "true"));
            System.out.println("Fictional demo data created in demo-data. Admin: admin.demo / Practice123!");
            System.out.println("Run with -Dhms.data=demo-data before -jar dist/HMSAdmin.jar to use it.");
        }
    }
    private static String find(HospitalService s, String table, String key, String value) throws Exception {
        return s.list(table).stream().filter(r -> value.equals(r.get(key))).findFirst().orElseThrow().get("id");
    }
}

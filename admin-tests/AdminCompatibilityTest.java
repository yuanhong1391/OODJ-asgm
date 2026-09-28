import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import models.*;
import services.UserService;
import services.admin.*;
import views.admin.*;
import views.admin.forms.*;
import static services.admin.AdminService.row;

/** Uses a disposable working directory, never the real group data directory. */
public class AdminCompatibilityTest {
    private static int checks;
    private static AdminService service;
    private static final String PASS="DemoPass123";
    interface Action { void run() throws Exception; }
    static void check(boolean ok,String label) { if(!ok) throw new AssertionError(label); checks++; System.out.println("PASS " + label); }
    static void reject(Action action,String label) throws Exception {
        try { action.run(); } catch(IllegalArgumentException | java.io.IOException expected) { checks++; System.out.println("PASS " + label); return; }
        throw new AssertionError("Should reject: " + label);
    }
    static Map<String,String> find(String table,String key,String value) throws Exception {
        return service.list(table).stream().filter(r->value.equals(r.get(key))).findFirst().orElseThrow();
    }
    static Map<String,String> user(String role,String username) {
        return row("role",role,"name","Test " + username,"username",username,"password",PASS,"email",username+"@example.com","phone","0123456789");
    }
    static String asset(String type,String name,String parent) throws Exception {
        service.save("assets",row("type",type,"name",name,"parentId",parent,"status","AVAILABLE"));
        return find("assets","name",name).get("id");
    }
    static Map<String,String> allocation(String asset,String patient,String doctor,String request,String start,String end) {
        return row("assetId",asset,"patientId",patient,"doctorId",doctor,"requestId",request,"start",start,"end",end,"status","BOOKED");
    }
    static void render(JComponent component,String name,int width,int height) throws Exception {
        SwingUtilities.invokeAndWait(()->{
            component.setSize(width,height); layout(component);
            BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            Graphics2D g=image.createGraphics(); component.printAll(g); g.dispose();
            try { ImageIO.write(image,"png",Path.of(name).toFile()); } catch(Exception ex) { throw new RuntimeException(ex); }
        });
    }
    static void layout(Container c) { c.doLayout(); for(Component child:c.getComponents()) if(child instanceof Container nested) layout(nested); }
    public static void main(String[] args) throws Exception {
        if (args.length>0 && args[0].equals("--inspect")) {
            AdminStore store=new AdminStore(Path.of("data"));
            for(String table:List.of("users","requests")) System.out.println("Readable group " + table + ": " + store.read(table).size() + " records");
            return;
        }
        if (args.length>0 && args[0].equals("--restart")) {
            service=new AdminService(new AdminStore(Path.of("data")));
            check(service.login("ADM001",PASS)!=null,"Admin login survives a new JVM process");
            check(service.list("users").size()==7,"shared user changes survive a new JVM process");
            check(find("requests","id","T0003").get("status").equals("SCHEDULED"),"allocation and shared request status survive a new JVM process");
            System.out.println("PASSED " + checks + " RESTART CHECKS");
            return;
        }
        check(!Files.exists(Path.of("data")),"isolated fixture has no existing data");
        Files.createDirectory(Path.of("data"));
        Doctor existingDoctor=new Doctor("D007","existingdoc",PASS,"Existing Doctor","0123456789","doctor@example.com","Cardiology","C12");
        Patient existingPatient=new Patient("P007","existingpat",PASS,"Existing Patient","0123456789","patient@example.com","O+","0122222222","Asthma");
        Manager existingManager=new Manager("M007","existingmgr",PASS,"Existing Manager","0123456789","manager@example.com");
        Files.write(Path.of("data/users.txt"),List.of(existingDoctor.toTxtRecord(),existingPatient.toTxtRecord(),existingManager.toTxtRecord()));
        byte[] originalUsers=Files.readAllBytes(Path.of("data/users.txt"));
        service=new AdminService(new AdminStore(Path.of("data")));
        check(service.needsSetup(),"setup works when clinical users already exist");
        reject(()->service.list("users"),"unauthenticated access");
        reject(()->service.setup("Admin","admin.test","short"),"short setup password");
        String admin=service.setup("Demo Admin","admin.test",PASS);
        check(Arrays.equals(originalUsers,Files.readAllBytes(Path.of("data/users.txt"))),"setup leaves clinical accounts unchanged");
        reject(()->service.setup("Other","otheradmin",PASS),"repeated initial setup");
        reject(()->service.login(admin,"wrong"),"wrong admin password");
        check(service.login(admin,PASS) instanceof AdminStaff,"AdminStaff authenticates through shared User parent");
        check(!Files.readString(Path.of("data/admin_accounts.txt")).contains(PASS),"admin password is hashed");
        check(service.list("users").size()==4,"Admin sees all four user roles");
        check(service.list("users").stream().noneMatch(u->u.containsKey("passwordHash")),"passwords hidden from UI rows");
        service.save("users",user("DOCTOR","newdoc"));
        service.save("users",user("PATIENT","newpat"));
        service.save("users",user("MANAGER","newmgr"));
        String doctor=find("users","username","newdoc").get("id"),patient=find("users","username","newpat").get("id"),manager=find("users","username","newmgr").get("id");
        check(doctor.matches("D\\d{3}"),"new Doctor ID meets Manager roster validation");
        UserService peer=new UserService();
        List<User> users=peer.loadAllUsers();
        check(users.size()==6,"unmodified group UserService reads all clinical users");
        check(users.stream().anyMatch(u->u.getID().equals(doctor)&&u instanceof Doctor&&u.getPassword().equals(PASS)),"Doctor credentials match original Login comparison");
        check(users.stream().anyMatch(u->u.getID().equals(patient)&&u instanceof Patient&&u.getPassword().equals(PASS)),"Patient credentials match original Login comparison");
        check(users.stream().anyMatch(u->u.getID().equals(manager)&&u instanceof Manager&&u.getPassword().equals(PASS)),"Manager credentials match original Login comparison");
        Map<String,String> updated=find("users","id","D007"); updated.put("name","Updated Doctor"); service.save("users",updated);
        Doctor loaded=(Doctor)peer.loadAllUsers().stream().filter(u->u.getID().equals("D007")).findFirst().orElseThrow();
        check(loaded.getName().equals("Updated Doctor")&&loaded.getSpecialization().equals("Cardiology")&&loaded.getRoomNumber().equals("C12"),"Admin edits preserve Doctor clinical fields");
        updated=find("users","id","P007"); updated.put("phone","0199999999"); service.save("users",updated);
        Patient loadedPatient=(Patient)peer.loadAllUsers().stream().filter(u->u.getID().equals("P007")).findFirst().orElseThrow();
        check(loadedPatient.getMedicalHistory().equals("Asthma")&&loadedPatient.getEmergencyContact().equals("0122222222"),"Admin edits preserve Patient clinical fields");
        check(peer.updateUserProfile("D007","Peer Updated","0123456789","updated@example.com"),"original profile service can update Admin-managed users");
        check(find("users","id","D007").get("name").equals("Peer Updated"),"Admin sees peer profile updates");
        check(peer.updatePassword(doctor,"Changed123"),"original password service works");
        check(service.login(admin,PASS)!=null,"group user saves preserve separate Admin login");
        reject(()->service.save("users",user("PATIENT","newdoc")),"duplicate username");
        Map<String,String> invalid=user("PATIENT","invalid"); invalid.put("email","bad"); reject(()->service.save("users",invalid),"invalid email");
        invalid.put("email","valid@example.com"); invalid.put("phone","-----"); reject(()->service.save("users",invalid),"phone without digits");
        invalid.put("phone","0123456789"); invalid.put("name","Bad, Name"); reject(()->service.save("users",invalid),"CSV delimiter in input");
        service.save("users",user("PATIENT","disposable")); String disposable=find("users","username","disposable").get("id"); service.delete("users",disposable);
        check(peer.loadAllUsers().stream().noneMatch(u->u.getID().equals(disposable)),"unreferenced user deletion persists to shared file");
        reject(()->service.delete("users",admin),"self-deletion");
        service.save("assignments",row("doctorId",doctor,"managerId",manager));
        service.save("assignments",row("doctorId",doctor,"managerId","M007"));
        check(service.list("assignments").size()==1&&find("assignments","doctorId",doctor).get("managerId").equals("M007"),"doctor reassignment remains unique");
        reject(()->service.delete("users",doctor),"assigned doctor cannot be deleted");
        service.delete("assignments",doctor);
        check(service.list("assignments").isEmpty(),"unassign doctor");
        String ward=asset("WARD","Ward A",""),bed=asset("BED","Bed A1",ward),bed2=asset("BED","Bed A2",ward),room=asset("CONSULTATION_ROOM","Consult A",""),lab=asset("LAB","Lab A",""),xray=asset("XRAY","X-ray A",""),imaging=asset("IMAGING","MRI A","");
        reject(()->asset("BED","Orphan",""),"bed requires parent ward");
        reject(()->asset("WARD","Ward A",""),"duplicate asset name");
        reject(()->service.delete("assets",ward),"ward with beds cannot be deleted");
        service.save("allocations",allocation(bed,patient,"","","2026-10-01T08:00","2026-10-02T08:00"));
        reject(()->service.save("allocations",allocation(bed,patient,"","","2026-10-01T10:00","2026-10-01T11:00")),"same bed overlap");
        reject(()->service.save("allocations",allocation(bed2,patient,"","","2026-10-01T10:00","2026-10-01T11:00")),"patient cannot occupy overlapping beds");
        service.save("allocations",allocation(room,patient,doctor,"","2026-10-01T10:00","2026-10-01T11:00"));
        check(service.list("allocations").size()==2,"inpatient can attend consultation during bed stay");
        reject(()->service.save("allocations",allocation(room,"P007",doctor,"","2026-10-01T10:30","2026-10-01T11:30")),"consultation room overlap");
        reject(()->service.save("allocations",allocation(ward,patient,"","","2026-10-03T10:00","2026-10-03T11:00")),"cannot allocate whole ward");
        reject(()->service.save("allocations",allocation(room,patient,"","","2026-10-03T10:00","2026-10-03T11:00")),"consultation requires doctor");
        reject(()->service.save("allocations",allocation(room,patient,doctor,"","2026-10-03T11:00","2026-10-03T10:00")),"reversed times");
        Map<String,String> blocked=find("assets","id",ward); blocked.put("status","MAINTENANCE"); reject(()->service.save("assets",blocked),"occupied ward cannot go into maintenance");
        String unused=asset("LAB","Disposable Lab",""); service.delete("assets",unused);
        check(service.list("assets").size()==7,"unallocated asset deletion");
        existingDoctor.requestsTest(patient,"Blood Test (Full Blood Count)");
        existingDoctor.requestsTest(patient,"Chest X-Ray");
        existingDoctor.requestsTest(patient,"MRI Scan");
        check(service.list("requests").size()==3,"Admin reads real Doctor.requestsTest output");
        Files.writeString(Path.of("data/lab_tests.txt"),"T0090,"+patient+",D007,2026-09-28,Blood Test (Full Blood Count),none,Allocated\nT0091,"+patient+",D007,2026-09-28,MRI Scan (Brain / Spine),none,Scheduled\n",StandardOpenOption.APPEND);
        check(find("requests","id","T0090").get("status").equals("ALLOCATED"),"legacy Allocated status stays distinct from open requests");
        check(find("requests","id","T0091").get("type").equals("IMAGING"),"existing expanded MRI name maps to imaging");
        check(find("requests","id","T0001").get("type").equals("LAB")&&find("requests","id","T0002").get("type").equals("XRAY")&&find("requests","id","T0003").get("type").equals("IMAGING"),"doctor test categories map to facilities");
        reject(()->service.save("allocations",allocation(xray,patient,"D007","T0001","2026-10-03T10:00","2026-10-03T11:00")),"wrong facility for lab test");
        service.save("allocations",allocation(lab,patient,"D007","T0001","2026-10-03T10:00","2026-10-03T11:00"));
        check(Files.readAllLines(Path.of("data/lab_tests.txt")).get(0).endsWith(",Scheduled"),"scheduling writes status to Doctor's actual file");
        reject(()->service.save("allocations",allocation(lab,patient,"D007","T0001","2026-10-04T10:00","2026-10-04T11:00")),"duplicate request allocation");
        Map<String,String> request=find("requests","id","T0001"); request.put("status","COMPLETED"); reject(()->service.save("requests",request),"cannot complete test before allocation");
        Map<String,String> done=find("allocations","requestId","T0001"); done.put("status","COMPLETED"); service.save("allocations",done);
        Map<String,String> cancelPerformed=find("requests","id","T0001"); cancelPerformed.put("status","CANCELLED"); reject(()->service.save("requests",cancelPerformed),"performed test cannot be cancelled");
        service.save("requests",request);
        check(Files.readAllLines(Path.of("data/lab_tests.txt")).get(0).endsWith(",none,Completed"),"completion preserves clinical result and updates status");
        check(Files.readAllLines(Path.of("data/lab_tests.txt")).stream().anyMatch(line->line.startsWith("T0090,")&&line.endsWith(",Allocated")),"unrelated scheduling preserves legacy Allocated status");
        check(Files.readAllLines(Path.of("data/lab_tests.txt")).stream().anyMatch(line->line.startsWith("T0091,")&&line.endsWith(",Scheduled")),"unrelated scheduling preserves external schedule status");
        service.save("allocations",allocation(xray,patient,"D007","T0002","2026-10-04T10:00","2026-10-04T11:00"));
        Map<String,String> cancelled=find("allocations","requestId","T0002"); cancelled.put("status","CANCELLED"); service.save("allocations",cancelled);
        check(Files.readAllLines(Path.of("data/lab_tests.txt")).get(1).endsWith(",Requested"),"cancelled schedule returns request to queue");
        Map<String,String> cancelRequest=find("requests","id","T0002"); cancelRequest.put("status","CANCELLED"); service.save("requests",cancelRequest);
        check(Files.readAllLines(Path.of("data/lab_tests.txt")).get(1).endsWith(",Cancelled"),"request cancellation shared with Doctor");
        service.save("allocations",allocation(imaging,patient,"D007","T0003","2026-10-05T10:00","2026-10-05T11:00"));
        reject(()->service.delete("users","D007"),"clinical request history protects doctor");
        existingPatient.bookAppointment(doctor,"2026-10-06","Follow up");
        reject(()->service.delete("users","P007"),"group appointment history protects patient");
        Files.writeString(Path.of("data/roster.txt"),"RosterID:R001\nDoctorID:"+doctor+"\nShiftDate:06/10/2026\nShiftType:Morning\n");
        reject(()->service.delete("users",doctor),"group roster protects doctor");
        service.save("rates",row("consultationType","General","amount","80","active","true"));
        check(find("rates","consultationType","General").get("amount").equals("80.00"),"consultation rate formatted to two decimals");
        reject(()->service.save("rates",row("consultationType","Negative","amount","-1","active","true")),"negative consultation rate");
        reject(()->service.save("rates",row("consultationType","Precise","amount","1.001","active","true")),"overprecise consultation rate");
        service.save("insurance",row("name","Demo Insurance","active","true"));
        reject(()->service.save("insurance",row("name","demo insurance","active","true")),"duplicate insurance network");
        Map<String,String> rate=find("rates","consultationType","General"); rate.put("amount","90"); service.save("rates",rate);
        Map<String,String> insurer=find("insurance","name","Demo Insurance"); insurer.put("active","false"); service.save("insurance",insurer);
        service=new AdminService(new AdminStore(Path.of("data"))); service.login(admin,PASS);
        check(find("rates","consultationType","General").get("amount").equals("90.00")&&find("insurance","name","Demo Insurance").get("active").equals("false"),"settings survive fresh service instance");
        check(find("requests","id","T0003").get("status").equals("SCHEDULED"),"request scheduling survives reload");
        service.delete("rates",rate.get("id")); service.delete("insurance",insurer.get("id"));
        check(service.list("rates").isEmpty()&&service.list("insurance").isEmpty(),"rate and insurance deletion");
        service.logout(); reject(()->service.list("assets"),"logout removes access"); service.login(admin,PASS);
        // Verify malformed shared files fail safely, instead of being silently overwritten.
        byte[] valid=Files.readAllBytes(Path.of("data/users.txt"));
        Files.writeString(Path.of("data/users.txt"),"invalid,row\n",StandardOpenOption.APPEND);
        byte[] corrupt=Files.readAllBytes(Path.of("data/users.txt"));
        reject(()->service.save("users",user("DOCTOR","safetycheck")),"malformed shared users file blocks mutation");
        check(Arrays.equals(corrupt,Files.readAllBytes(Path.of("data/users.txt"))),"invalid data is not silently discarded");
        Files.write(Path.of("data/users.txt"),valid);
        render(new DashboardPanel(service,key->{}),"admin-dashboard.png",1100,700);
        UserForm form=new UserForm(); form.applyTheme(); render(form,"admin-user-form.png",700,670);
        render(new SignInPanel(new LoginForm(),new JButton("Sign in"),false),"admin-login.png",1100,650);
        check(true,"headless dashboard and NetBeans form components render");
        Files.writeString(Path.of("test-result.txt"),"Passed " + checks + " compatibility checks.\n");
        System.out.println("PASSED " + checks + " CHECKS");
    }
}

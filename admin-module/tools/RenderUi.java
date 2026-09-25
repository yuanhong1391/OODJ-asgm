import hms.service.HospitalService;
import hms.storage.TextStore;
import hms.ui.*;
import hms.ui.forms.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import static hms.service.HospitalService.row;

/** Offscreen renders of the production Swing panels using isolated fictional data. */
public class RenderUi {
    public static void main(String[] args) throws Exception {
        HospitalTheme.install();
        Path folder = Files.createTempDirectory("hms-visual-");
        try (TextStore store = new TextStore(folder)) {
            HospitalService s = new HospitalService(store); s.setup("Admin Staff", "admin.preview", "PreviewOnly123!"); s.login("admin.preview", "PreviewOnly123!");
            s.save("users", row("role", "DOCTOR", "name", "Dr. Maya Tan", "username", "doctor.preview", "password", "PreviewOnly123!", "active", "true"));
            s.save("users", row("role", "PATIENT", "name", "Alex Sample", "username", "patient.preview", "password", "PreviewOnly123!", "active", "true"));
            String doctor = s.list("users").stream().filter(u -> u.get("role").equals("DOCTOR")).findFirst().get().get("id");
            String patient = s.list("users").stream().filter(u -> u.get("role").equals("PATIENT")).findFirst().get().get("id");
            for (String type : new String[]{"LAB", "XRAY", "IMAGING"}) {
                s.save("assets", row("name", HospitalTheme.human(type) + " room", "type", type, "parentId", "", "status", "AVAILABLE"));
                s.save("requests", row("doctorId", doctor, "patientId", patient, "type", type, "details", "Fictional preview request", "status", "OPEN"));
            }
            SwingUtilities.invokeAndWait(() -> {
                try {
                    JPanel shell = new JPanel(new BorderLayout()); JLabel header = new JLabel(); HospitalTheme.header(header); shell.add(header, BorderLayout.NORTH);
                    JPanel nav = new JPanel(); String[] labels = {"Dashboard", "End users", "Doctor assignments", "Hospital assets", "Asset allocations", "Lab / imaging requests", "Consultation rates", "Insurance networks", "Log out"};
                    JButton[] buttons = java.util.Arrays.stream(labels).map(JButton::new).toArray(JButton[]::new);
                    HospitalTheme.navigation(nav, buttons, "Dashboard"); shell.add(nav, BorderLayout.WEST);
                    shell.add(new DashboardPanel(s, key -> {}), BorderLayout.CENTER);
                    JLabel footer = HospitalTheme.label("  Signed in as Admin Staff    /    Administrative staff", 12, false, HospitalTheme.MUTED); footer.setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12)); shell.add(footer, BorderLayout.SOUTH);
                    render(shell, "dashboard", 1280, 800);
                    render(shell, "dashboard-compact", 1040, 680);
                    render(new SignInPanel(new LoginForm(), new JButton("Sign in  →"), false), "sign-in", 1120, 650);
                    render(new SignInPanel(new SetupForm(), new JButton("Create administrator  →"), true), "first-run", 1120, 650);
                    UserForm user = new UserForm(); user.applyTheme(); user.setValues(row("name", "Alex Sample", "username", "alex.sample", "email", "alex@example.test"));
                    render(user, "user-form", 840, 430);
                } catch (Exception e) { throw new RuntimeException(e); }
            });
        }
    }
    private static void render(JComponent panel, String name, int width, int height) throws Exception {
        panel.setSize(width, height); layout(panel);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics(); g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); panel.printAll(g); g.dispose();
        Path output = Path.of("docs", "previews", name + ".png"); Files.createDirectories(output.getParent());
        ImageIO.write(image, "png", output.toFile()); System.out.println("Rendered " + output);
    }
    private static void layout(Container c) { c.doLayout(); for (Component child : c.getComponents()) if (child instanceof Container nested) layout(nested); }
}

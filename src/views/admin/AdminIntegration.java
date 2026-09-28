package views.admin;

import java.io.IOException;
import java.nio.file.Path;
import services.admin.AdminService;
import services.admin.AdminStore;

/** Optional single hook for the group's existing Login; see ADMIN-INTEGRATION.md. */
public final class AdminIntegration {
    private AdminIntegration() { }

    public static boolean tryOpen(String userId, String password) throws IOException {
        AdminService service = new AdminService(new AdminStore(Path.of("data")));
        try { service.login(userId, password); }
        catch(IllegalArgumentException invalidLogin) { return false; }
        AdminDashboard dashboard = new AdminDashboard(service, () -> new views.Login().setVisible(true));
        dashboard.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dashboard.setVisible(true);
        return true;
    }
}

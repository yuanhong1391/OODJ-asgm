import java.nio.file.Path;
import javax.swing.*;
import services.admin.AdminService;
import services.admin.AdminStore;
import views.admin.AdminDashboard;

/** NetBeans: right-click this file and choose Run File (Shift+F6). */
public final class AdminLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                AdminService service=new AdminService(new AdminStore(Path.of("data")));
                new AdminDashboard(service).setVisible(true);
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null,"Cannot start Admin: " + ex.getMessage(),"Admin",JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

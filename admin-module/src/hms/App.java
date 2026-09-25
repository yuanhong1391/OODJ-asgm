package hms;

import hms.service.HospitalService;
import hms.storage.TextStore;
import hms.ui.MainFrame;
import hms.ui.HospitalTheme;
import java.nio.file.Path;
import javax.swing.*;

public final class App {
    private App() { }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                HospitalTheme.install();
                TextStore store = new TextStore(Path.of(System.getProperty("hms.data", "data")));
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try { store.close(); } catch (Exception ignored) { /* Process is terminating. */ }
                }));
                MainFrame frame = new MainFrame(new HospitalService(store));
                frame.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Cannot start HMS: " + ex.getMessage(), "Startup error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

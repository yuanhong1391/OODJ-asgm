package hms.ui.forms;

import hms.ui.EditorPanel;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;

/** Edit component layout in NetBeans Design view. */
public class LoginForm extends EditorPanel {
    public LoginForm() {
        initComponents();
        setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    @Override protected Map<String, JComponent> fields() {
        Map<String, JComponent> result = new LinkedHashMap<>();
        result.put("username", usernameField);
        result.put("password", passwordField);
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new java.awt.GridLayout(0, 2, 14, 12));
        usernameLabel = new javax.swing.JLabel("Username");
        usernameField = new javax.swing.JTextField();
        usernameLabel.setLabelFor(usernameField);
        add(usernameLabel);
        add(usernameField);
        passwordLabel = new javax.swing.JLabel("Password");
        passwordField = new javax.swing.JPasswordField();
        passwordLabel.setLabelFor(passwordField);
        add(passwordLabel);
        add(passwordField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField passwordField;
    // End of variables declaration//GEN-END:variables
}
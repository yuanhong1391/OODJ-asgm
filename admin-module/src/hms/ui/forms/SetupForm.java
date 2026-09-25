package hms.ui.forms;

import hms.ui.EditorPanel;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;

/** Edit component layout in NetBeans Design view. */
public class SetupForm extends EditorPanel {
    public SetupForm() {
        initComponents();
        setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    @Override protected Map<String, JComponent> fields() {
        Map<String, JComponent> result = new LinkedHashMap<>();
        result.put("name", nameField);
        result.put("username", usernameField);
        result.put("password", passwordField);
        result.put("confirmPassword", confirmPasswordField);
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new java.awt.GridLayout(0, 2, 14, 12));
        nameLabel = new javax.swing.JLabel("Your full name");
        nameField = new javax.swing.JTextField();
        nameLabel.setLabelFor(nameField);
        add(nameLabel);
        add(nameField);
        usernameLabel = new javax.swing.JLabel("Admin username");
        usernameField = new javax.swing.JTextField();
        usernameLabel.setLabelFor(usernameField);
        add(usernameLabel);
        add(usernameField);
        passwordLabel = new javax.swing.JLabel("Password (at least 8 characters)");
        passwordField = new javax.swing.JPasswordField();
        passwordLabel.setLabelFor(passwordField);
        add(passwordLabel);
        add(passwordField);
        confirmPasswordLabel = new javax.swing.JLabel("Confirm password");
        confirmPasswordField = new javax.swing.JPasswordField();
        confirmPasswordLabel.setLabelFor(confirmPasswordField);
        add(confirmPasswordLabel);
        add(confirmPasswordField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel confirmPasswordLabel;
    private javax.swing.JPasswordField confirmPasswordField;
    // End of variables declaration//GEN-END:variables
}
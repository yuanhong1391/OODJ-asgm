package hms.ui.forms;

import hms.ui.EditorPanel;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;

/** Edit component layout in NetBeans Design view. */
public class UserForm extends EditorPanel {
    public UserForm() {
        initComponents();
        setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    @Override protected Map<String, JComponent> fields() {
        Map<String, JComponent> result = new LinkedHashMap<>();
        result.put("name", nameField);
        result.put("username", usernameField);
        result.put("role", roleField);
        result.put("password", passwordField);
        result.put("email", emailField);
        result.put("phone", phoneField);
        result.put("active", activeField);
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new java.awt.GridLayout(0, 2, 14, 12));
        nameLabel = new javax.swing.JLabel("Full name *");
        nameField = new javax.swing.JTextField();
        nameLabel.setLabelFor(nameField);
        add(nameLabel);
        add(nameField);
        usernameLabel = new javax.swing.JLabel("Username *");
        usernameField = new javax.swing.JTextField();
        usernameLabel.setLabelFor(usernameField);
        add(usernameLabel);
        add(usernameField);
        roleLabel = new javax.swing.JLabel("Role *");
        roleField = new javax.swing.JComboBox<String>();
        roleField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "MANAGER", "DOCTOR", "PATIENT" }));
        roleLabel.setLabelFor(roleField);
        add(roleLabel);
        add(roleField);
        passwordLabel = new javax.swing.JLabel("Password (blank keeps existing)");
        passwordField = new javax.swing.JPasswordField();
        passwordLabel.setLabelFor(passwordField);
        add(passwordLabel);
        add(passwordField);
        emailLabel = new javax.swing.JLabel("Email");
        emailField = new javax.swing.JTextField();
        emailLabel.setLabelFor(emailField);
        add(emailLabel);
        add(emailField);
        phoneLabel = new javax.swing.JLabel("Phone");
        phoneField = new javax.swing.JTextField();
        phoneLabel.setLabelFor(phoneField);
        add(phoneLabel);
        add(phoneField);
        activeLabel = new javax.swing.JLabel("Active *");
        activeField = new javax.swing.JComboBox<String>();
        activeField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "true", "false" }));
        activeLabel.setLabelFor(activeField);
        add(activeLabel);
        add(activeField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel roleLabel;
    private javax.swing.JComboBox<String> roleField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JTextField phoneField;
    private javax.swing.JLabel activeLabel;
    private javax.swing.JComboBox<String> activeField;
    // End of variables declaration//GEN-END:variables
}
package views;


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
        result.put("specialization", specializationField);
        result.put("roomNumber", roomNumberField);
        result.put("bloodType", bloodTypeField);
        result.put("emergencyContact", emergencyContactField);
        result.put("medicalHistory", medicalHistoryField);
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        usernameLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        roleLabel = new javax.swing.JLabel();
        roleField = new javax.swing.JComboBox<>();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        emailLabel = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        phoneLabel = new javax.swing.JLabel();
        phoneField = new javax.swing.JTextField();
        specializationLabel = new javax.swing.JLabel();
        specializationField = new javax.swing.JTextField();
        roomNumberLabel = new javax.swing.JLabel();
        roomNumberField = new javax.swing.JTextField();
        bloodTypeLabel = new javax.swing.JLabel();
        bloodTypeField = new javax.swing.JTextField();
        emergencyContactLabel = new javax.swing.JLabel();
        emergencyContactField = new javax.swing.JTextField();
        medicalHistoryLabel = new javax.swing.JLabel();
        medicalHistoryField = new javax.swing.JTextField();

        setLayout(new java.awt.GridLayout(0, 2, 14, 12));

        nameLabel.setText("Full name *");
        nameLabel.setLabelFor(nameField);
        add(nameLabel);
        add(nameField);

        usernameLabel.setText("Username *");
        usernameLabel.setLabelFor(usernameField);
        add(usernameLabel);
        add(usernameField);

        roleLabel.setText("Role *");
        roleLabel.setLabelFor(roleField);
        add(roleLabel);

        roleField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "MANAGER", "DOCTOR", "PATIENT" }));
        add(roleField);

        passwordLabel.setText("Password (blank keeps existing)");
        passwordLabel.setLabelFor(passwordField);
        add(passwordLabel);
        add(passwordField);

        emailLabel.setText("Email");
        emailLabel.setLabelFor(emailField);
        add(emailLabel);
        add(emailField);

        phoneLabel.setText("Phone");
        phoneLabel.setLabelFor(phoneField);
        add(phoneLabel);
        add(phoneField);

        specializationLabel.setText("Doctor: specialization");
        specializationLabel.setLabelFor(specializationField);
        add(specializationLabel);
        add(specializationField);

        roomNumberLabel.setText("Doctor: room number");
        roomNumberLabel.setLabelFor(roomNumberField);
        add(roomNumberLabel);
        add(roomNumberField);

        bloodTypeLabel.setText("Patient: blood type");
        bloodTypeLabel.setLabelFor(bloodTypeField);
        add(bloodTypeLabel);
        add(bloodTypeField);

        emergencyContactLabel.setText("Patient: emergency contact");
        emergencyContactLabel.setLabelFor(emergencyContactField);
        add(emergencyContactLabel);
        add(emergencyContactField);

        medicalHistoryLabel.setText("Patient: medical history");
        medicalHistoryLabel.setLabelFor(medicalHistoryField);
        add(medicalHistoryLabel);
        add(medicalHistoryField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bloodTypeField;
    private javax.swing.JLabel bloodTypeLabel;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emergencyContactField;
    private javax.swing.JLabel emergencyContactLabel;
    private javax.swing.JTextField medicalHistoryField;
    private javax.swing.JLabel medicalHistoryLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField phoneField;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JComboBox<String> roleField;
    private javax.swing.JLabel roleLabel;
    private javax.swing.JTextField roomNumberField;
    private javax.swing.JLabel roomNumberLabel;
    private javax.swing.JTextField specializationField;
    private javax.swing.JLabel specializationLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}

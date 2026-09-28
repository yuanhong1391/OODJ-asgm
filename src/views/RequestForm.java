package views;


import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;

/** Edit component layout in NetBeans Design view. */
public class RequestForm extends EditorPanel {
    public RequestForm() {
        initComponents();
        setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    @Override protected Map<String, JComponent> fields() {
        Map<String, JComponent> result = new LinkedHashMap<>();
        result.put("doctorId", doctorIdField);
        result.put("patientId", patientIdField);
        result.put("type", typeField);
        result.put("details", detailsField);
        result.put("status", statusField);
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        doctorIdLabel = new javax.swing.JLabel();
        doctorIdField = new javax.swing.JComboBox<>();
        patientIdLabel = new javax.swing.JLabel();
        patientIdField = new javax.swing.JComboBox<>();
        typeLabel = new javax.swing.JLabel();
        typeField = new javax.swing.JComboBox<>();
        detailsLabel = new javax.swing.JLabel();
        detailsField = new javax.swing.JTextField();
        statusLabel = new javax.swing.JLabel();
        statusField = new javax.swing.JComboBox<>();

        setLayout(new java.awt.GridLayout(0, 2, 14, 12));

        doctorIdLabel.setText("Requesting doctor *");
        doctorIdLabel.setLabelFor(doctorIdField);
        add(doctorIdLabel);

        doctorIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        add(doctorIdField);

        patientIdLabel.setText("Patient *");
        patientIdLabel.setLabelFor(patientIdField);
        add(patientIdLabel);

        patientIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        add(patientIdField);

        typeLabel.setText("Test type *");
        typeLabel.setLabelFor(typeField);
        add(typeLabel);

        typeField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LAB", "XRAY", "IMAGING" }));
        add(typeField);

        detailsLabel.setText("Doctor request details *");
        detailsLabel.setLabelFor(detailsField);
        add(detailsLabel);
        add(detailsField);

        statusLabel.setText("Request status *");
        statusLabel.setLabelFor(statusField);
        add(statusLabel);

        statusField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "OPEN", "COMPLETED", "CANCELLED" }));
        add(statusField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField detailsField;
    private javax.swing.JLabel detailsLabel;
    private javax.swing.JComboBox<String> doctorIdField;
    private javax.swing.JLabel doctorIdLabel;
    private javax.swing.JComboBox<String> patientIdField;
    private javax.swing.JLabel patientIdLabel;
    private javax.swing.JComboBox<String> statusField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JComboBox<String> typeField;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables
}

package views;


import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;

/** Edit component layout in NetBeans Design view. */
public class AllocationForm extends EditorPanel {
    public AllocationForm() {
        initComponents();
        setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    @Override protected Map<String, JComponent> fields() {
        Map<String, JComponent> result = new LinkedHashMap<>();
        result.put("assetId", assetIdField);
        result.put("patientId", patientIdField);
        result.put("doctorId", doctorIdField);
        result.put("requestId", requestIdField);
        result.put("start", startField);
        result.put("end", endField);
        result.put("status", statusField);
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        assetIdLabel = new javax.swing.JLabel();
        assetIdField = new javax.swing.JComboBox<>();
        patientIdLabel = new javax.swing.JLabel();
        patientIdField = new javax.swing.JComboBox<>();
        doctorIdLabel = new javax.swing.JLabel();
        doctorIdField = new javax.swing.JComboBox<>();
        requestIdLabel = new javax.swing.JLabel();
        requestIdField = new javax.swing.JComboBox<>();
        startLabel = new javax.swing.JLabel();
        startField = new javax.swing.JTextField();
        endLabel = new javax.swing.JLabel();
        endField = new javax.swing.JTextField();
        statusLabel = new javax.swing.JLabel();
        statusField = new javax.swing.JComboBox<>();

        setLayout(new java.awt.GridLayout(0, 2, 14, 12));

        assetIdLabel.setText("Facility or bed *");
        assetIdLabel.setLabelFor(assetIdField);
        add(assetIdLabel);

        assetIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        add(assetIdField);

        patientIdLabel.setText("Patient *");
        patientIdLabel.setLabelFor(patientIdField);
        add(patientIdLabel);

        patientIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        add(patientIdField);

        doctorIdLabel.setText("Doctor (required for consultations/tests)");
        doctorIdLabel.setLabelFor(doctorIdField);
        add(doctorIdLabel);

        doctorIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        add(doctorIdField);

        requestIdLabel.setText("Lab or imaging request");
        requestIdLabel.setLabelFor(requestIdField);
        add(requestIdLabel);

        requestIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        add(requestIdField);

        startLabel.setText("Start (YYYY-MM-DDTHH:MM) *");
        startLabel.setLabelFor(startField);
        add(startLabel);
        add(startField);

        endLabel.setText("End (YYYY-MM-DDTHH:MM) *");
        endLabel.setLabelFor(endField);
        add(endLabel);
        add(endField);

        statusLabel.setText("Allocation status *");
        statusLabel.setLabelFor(statusField);
        add(statusLabel);

        statusField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BOOKED", "COMPLETED", "CANCELLED" }));
        add(statusField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> assetIdField;
    private javax.swing.JLabel assetIdLabel;
    private javax.swing.JComboBox<String> doctorIdField;
    private javax.swing.JLabel doctorIdLabel;
    private javax.swing.JTextField endField;
    private javax.swing.JLabel endLabel;
    private javax.swing.JComboBox<String> patientIdField;
    private javax.swing.JLabel patientIdLabel;
    private javax.swing.JComboBox<String> requestIdField;
    private javax.swing.JLabel requestIdLabel;
    private javax.swing.JTextField startField;
    private javax.swing.JLabel startLabel;
    private javax.swing.JComboBox<String> statusField;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}

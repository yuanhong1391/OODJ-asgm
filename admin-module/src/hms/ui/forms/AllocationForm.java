package hms.ui.forms;

import hms.ui.EditorPanel;
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
        setLayout(new java.awt.GridLayout(0, 2, 14, 12));
        assetIdLabel = new javax.swing.JLabel("Facility or bed *");
        assetIdField = new javax.swing.JComboBox<String>();
        assetIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        assetIdLabel.setLabelFor(assetIdField);
        add(assetIdLabel);
        add(assetIdField);
        patientIdLabel = new javax.swing.JLabel("Patient *");
        patientIdField = new javax.swing.JComboBox<String>();
        patientIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        patientIdLabel.setLabelFor(patientIdField);
        add(patientIdLabel);
        add(patientIdField);
        doctorIdLabel = new javax.swing.JLabel("Doctor (required for consultations/tests)");
        doctorIdField = new javax.swing.JComboBox<String>();
        doctorIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        doctorIdLabel.setLabelFor(doctorIdField);
        add(doctorIdLabel);
        add(doctorIdField);
        requestIdLabel = new javax.swing.JLabel("Lab or imaging request");
        requestIdField = new javax.swing.JComboBox<String>();
        requestIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        requestIdLabel.setLabelFor(requestIdField);
        add(requestIdLabel);
        add(requestIdField);
        startLabel = new javax.swing.JLabel("Start (YYYY-MM-DDTHH:MM) *");
        startField = new javax.swing.JTextField();
        startLabel.setLabelFor(startField);
        add(startLabel);
        add(startField);
        endLabel = new javax.swing.JLabel("End (YYYY-MM-DDTHH:MM) *");
        endField = new javax.swing.JTextField();
        endLabel.setLabelFor(endField);
        add(endLabel);
        add(endField);
        statusLabel = new javax.swing.JLabel("Allocation status *");
        statusField = new javax.swing.JComboBox<String>();
        statusField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BOOKED", "COMPLETED", "CANCELLED" }));
        statusLabel.setLabelFor(statusField);
        add(statusLabel);
        add(statusField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel assetIdLabel;
    private javax.swing.JComboBox<String> assetIdField;
    private javax.swing.JLabel patientIdLabel;
    private javax.swing.JComboBox<String> patientIdField;
    private javax.swing.JLabel doctorIdLabel;
    private javax.swing.JComboBox<String> doctorIdField;
    private javax.swing.JLabel requestIdLabel;
    private javax.swing.JComboBox<String> requestIdField;
    private javax.swing.JLabel startLabel;
    private javax.swing.JTextField startField;
    private javax.swing.JLabel endLabel;
    private javax.swing.JTextField endField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JComboBox<String> statusField;
    // End of variables declaration//GEN-END:variables
}
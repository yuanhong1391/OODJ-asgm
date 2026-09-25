package hms.ui.forms;

import hms.ui.EditorPanel;
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
        setLayout(new java.awt.GridLayout(0, 2, 14, 12));
        doctorIdLabel = new javax.swing.JLabel("Requesting doctor *");
        doctorIdField = new javax.swing.JComboBox<String>();
        doctorIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        doctorIdLabel.setLabelFor(doctorIdField);
        add(doctorIdLabel);
        add(doctorIdField);
        patientIdLabel = new javax.swing.JLabel("Patient *");
        patientIdField = new javax.swing.JComboBox<String>();
        patientIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        patientIdLabel.setLabelFor(patientIdField);
        add(patientIdLabel);
        add(patientIdField);
        typeLabel = new javax.swing.JLabel("Test type *");
        typeField = new javax.swing.JComboBox<String>();
        typeField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LAB", "XRAY", "IMAGING" }));
        typeLabel.setLabelFor(typeField);
        add(typeLabel);
        add(typeField);
        detailsLabel = new javax.swing.JLabel("Doctor request details *");
        detailsField = new javax.swing.JTextField();
        detailsLabel.setLabelFor(detailsField);
        add(detailsLabel);
        add(detailsField);
        statusLabel = new javax.swing.JLabel("Request status *");
        statusField = new javax.swing.JComboBox<String>();
        statusField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "OPEN", "COMPLETED", "CANCELLED" }));
        statusLabel.setLabelFor(statusField);
        add(statusLabel);
        add(statusField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel doctorIdLabel;
    private javax.swing.JComboBox<String> doctorIdField;
    private javax.swing.JLabel patientIdLabel;
    private javax.swing.JComboBox<String> patientIdField;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JComboBox<String> typeField;
    private javax.swing.JLabel detailsLabel;
    private javax.swing.JTextField detailsField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JComboBox<String> statusField;
    // End of variables declaration//GEN-END:variables
}
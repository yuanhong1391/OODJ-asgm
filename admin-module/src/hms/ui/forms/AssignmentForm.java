package hms.ui.forms;

import hms.ui.EditorPanel;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;

/** Edit component layout in NetBeans Design view. */
public class AssignmentForm extends EditorPanel {
    public AssignmentForm() {
        initComponents();
        setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    @Override protected Map<String, JComponent> fields() {
        Map<String, JComponent> result = new LinkedHashMap<>();
        result.put("doctorId", doctorIdField);
        result.put("managerId", managerIdField);
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new java.awt.GridLayout(0, 2, 14, 12));
        doctorIdLabel = new javax.swing.JLabel("Doctor *");
        doctorIdField = new javax.swing.JComboBox<String>();
        doctorIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        doctorIdLabel.setLabelFor(doctorIdField);
        add(doctorIdLabel);
        add(doctorIdField);
        managerIdLabel = new javax.swing.JLabel("Medical manager *");
        managerIdField = new javax.swing.JComboBox<String>();
        managerIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        managerIdLabel.setLabelFor(managerIdField);
        add(managerIdLabel);
        add(managerIdField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel doctorIdLabel;
    private javax.swing.JComboBox<String> doctorIdField;
    private javax.swing.JLabel managerIdLabel;
    private javax.swing.JComboBox<String> managerIdField;
    // End of variables declaration//GEN-END:variables
}
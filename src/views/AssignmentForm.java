package views;


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

        doctorIdLabel = new javax.swing.JLabel();
        doctorIdField = new javax.swing.JComboBox<>();
        managerIdLabel = new javax.swing.JLabel();
        managerIdField = new javax.swing.JComboBox<>();

        setLayout(new java.awt.GridLayout(0, 2, 14, 12));

        doctorIdLabel.setText("Doctor *");
        doctorIdLabel.setLabelFor(doctorIdField);
        add(doctorIdLabel);

        doctorIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        add(doctorIdField);

        managerIdLabel.setText("Medical manager *");
        managerIdLabel.setLabelFor(managerIdField);
        add(managerIdLabel);

        managerIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        add(managerIdField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> doctorIdField;
    private javax.swing.JLabel doctorIdLabel;
    private javax.swing.JComboBox<String> managerIdField;
    private javax.swing.JLabel managerIdLabel;
    // End of variables declaration//GEN-END:variables
}

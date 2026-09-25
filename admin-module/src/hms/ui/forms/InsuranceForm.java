package hms.ui.forms;

import hms.ui.EditorPanel;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;

/** Edit component layout in NetBeans Design view. */
public class InsuranceForm extends EditorPanel {
    public InsuranceForm() {
        initComponents();
        setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    @Override protected Map<String, JComponent> fields() {
        Map<String, JComponent> result = new LinkedHashMap<>();
        result.put("name", nameField);
        result.put("active", activeField);
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new java.awt.GridLayout(0, 2, 14, 12));
        nameLabel = new javax.swing.JLabel("Insurance network name *");
        nameField = new javax.swing.JTextField();
        nameLabel.setLabelFor(nameField);
        add(nameLabel);
        add(nameField);
        activeLabel = new javax.swing.JLabel("Accepted *");
        activeField = new javax.swing.JComboBox<String>();
        activeField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "true", "false" }));
        activeLabel.setLabelFor(activeField);
        add(activeLabel);
        add(activeField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel activeLabel;
    private javax.swing.JComboBox<String> activeField;
    // End of variables declaration//GEN-END:variables
}
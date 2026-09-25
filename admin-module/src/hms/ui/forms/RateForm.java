package hms.ui.forms;

import hms.ui.EditorPanel;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;

/** Edit component layout in NetBeans Design view. */
public class RateForm extends EditorPanel {
    public RateForm() {
        initComponents();
        setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    @Override protected Map<String, JComponent> fields() {
        Map<String, JComponent> result = new LinkedHashMap<>();
        result.put("consultationType", consultationTypeField);
        result.put("amount", amountField);
        result.put("active", activeField);
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new java.awt.GridLayout(0, 2, 14, 12));
        consultationTypeLabel = new javax.swing.JLabel("Consultation type *");
        consultationTypeField = new javax.swing.JTextField();
        consultationTypeLabel.setLabelFor(consultationTypeField);
        add(consultationTypeLabel);
        add(consultationTypeField);
        amountLabel = new javax.swing.JLabel("Base rate (MYR) *");
        amountField = new javax.swing.JTextField();
        amountLabel.setLabelFor(amountField);
        add(amountLabel);
        add(amountField);
        activeLabel = new javax.swing.JLabel("Active *");
        activeField = new javax.swing.JComboBox<String>();
        activeField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "true", "false" }));
        activeLabel.setLabelFor(activeField);
        add(activeLabel);
        add(activeField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel consultationTypeLabel;
    private javax.swing.JTextField consultationTypeField;
    private javax.swing.JLabel amountLabel;
    private javax.swing.JTextField amountField;
    private javax.swing.JLabel activeLabel;
    private javax.swing.JComboBox<String> activeField;
    // End of variables declaration//GEN-END:variables
}
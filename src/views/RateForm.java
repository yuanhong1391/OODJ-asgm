package views;


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

        consultationTypeLabel = new javax.swing.JLabel();
        consultationTypeField = new javax.swing.JTextField();
        amountLabel = new javax.swing.JLabel();
        amountField = new javax.swing.JTextField();
        activeLabel = new javax.swing.JLabel();
        activeField = new javax.swing.JComboBox<>();

        setLayout(new java.awt.GridLayout(0, 2, 14, 12));

        consultationTypeLabel.setText("Consultation type *");
        consultationTypeLabel.setLabelFor(consultationTypeField);
        add(consultationTypeLabel);
        add(consultationTypeField);

        amountLabel.setText("Base rate (MYR) *");
        amountLabel.setLabelFor(amountField);
        add(amountLabel);
        add(amountField);

        activeLabel.setText("Active *");
        activeLabel.setLabelFor(activeField);
        add(activeLabel);

        activeField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "true", "false" }));
        add(activeField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> activeField;
    private javax.swing.JLabel activeLabel;
    private javax.swing.JTextField amountField;
    private javax.swing.JLabel amountLabel;
    private javax.swing.JTextField consultationTypeField;
    private javax.swing.JLabel consultationTypeLabel;
    // End of variables declaration//GEN-END:variables
}

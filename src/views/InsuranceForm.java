package views;


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

        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        activeLabel = new javax.swing.JLabel();
        activeField = new javax.swing.JComboBox<>();

        setLayout(new java.awt.GridLayout(0, 2, 14, 12));

        nameLabel.setText("Insurance network name *");
        nameLabel.setLabelFor(nameField);
        add(nameLabel);
        add(nameField);

        activeLabel.setText("Accepted *");
        activeLabel.setLabelFor(activeField);
        add(activeLabel);

        activeField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "true", "false" }));
        add(activeField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> activeField;
    private javax.swing.JLabel activeLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    // End of variables declaration//GEN-END:variables
}

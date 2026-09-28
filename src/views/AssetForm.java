package views;


import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;

/** Edit component layout in NetBeans Design view. */
public class AssetForm extends EditorPanel {
    public AssetForm() {
        initComponents();
        setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    @Override protected Map<String, JComponent> fields() {
        Map<String, JComponent> result = new LinkedHashMap<>();
        result.put("name", nameField);
        result.put("type", typeField);
        result.put("parentId", parentIdField);
        result.put("status", statusField);
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        typeLabel = new javax.swing.JLabel();
        typeField = new javax.swing.JComboBox<>();
        parentIdLabel = new javax.swing.JLabel();
        parentIdField = new javax.swing.JComboBox<>();
        statusLabel = new javax.swing.JLabel();
        statusField = new javax.swing.JComboBox<>();

        setLayout(new java.awt.GridLayout(0, 2, 14, 12));

        nameLabel.setText("Asset name *");
        nameLabel.setLabelFor(nameField);
        add(nameLabel);
        add(nameField);

        typeLabel.setText("Asset type *");
        typeLabel.setLabelFor(typeField);
        add(typeLabel);

        typeField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CONSULTATION_ROOM", "WARD", "BED", "LAB", "XRAY", "IMAGING" }));
        add(typeField);

        parentIdLabel.setText("Parent ward (beds only)");
        parentIdLabel.setLabelFor(parentIdField);
        add(parentIdLabel);

        parentIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        add(parentIdField);

        statusLabel.setText("Availability *");
        statusLabel.setLabelFor(statusField);
        add(statusLabel);

        statusField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AVAILABLE", "MAINTENANCE", "INACTIVE" }));
        add(statusField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JComboBox<String> parentIdField;
    private javax.swing.JLabel parentIdLabel;
    private javax.swing.JComboBox<String> statusField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JComboBox<String> typeField;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables
}

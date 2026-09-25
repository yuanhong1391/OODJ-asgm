package hms.ui.forms;

import hms.ui.EditorPanel;
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
        setLayout(new java.awt.GridLayout(0, 2, 14, 12));
        nameLabel = new javax.swing.JLabel("Asset name *");
        nameField = new javax.swing.JTextField();
        nameLabel.setLabelFor(nameField);
        add(nameLabel);
        add(nameField);
        typeLabel = new javax.swing.JLabel("Asset type *");
        typeField = new javax.swing.JComboBox<String>();
        typeField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CONSULTATION_ROOM", "WARD", "BED", "LAB", "XRAY", "IMAGING" }));
        typeLabel.setLabelFor(typeField);
        add(typeLabel);
        add(typeField);
        parentIdLabel = new javax.swing.JLabel("Parent ward (beds only)");
        parentIdField = new javax.swing.JComboBox<String>();
        parentIdField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        parentIdLabel.setLabelFor(parentIdField);
        add(parentIdLabel);
        add(parentIdField);
        statusLabel = new javax.swing.JLabel("Availability *");
        statusField = new javax.swing.JComboBox<String>();
        statusField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AVAILABLE", "MAINTENANCE", "INACTIVE" }));
        statusLabel.setLabelFor(statusField);
        add(statusLabel);
        add(statusField);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JComboBox<String> typeField;
    private javax.swing.JLabel parentIdLabel;
    private javax.swing.JComboBox<String> parentIdField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JComboBox<String> statusField;
    // End of variables declaration//GEN-END:variables
}
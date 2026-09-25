package hms.ui;

import java.awt.Component;
import java.util.*;
import javax.swing.*;

/** Common behaviour for the individually editable NetBeans forms. */
public class EditorPanel extends JPanel {
    /** Concrete no-argument base allows the NetBeans designer to instantiate it. */
    public EditorPanel() { super(); }
    protected Map<String, JComponent> fields() { return Map.of(); }
    public void applyTheme() {
        setBackground(java.awt.Color.WHITE);
        for (java.awt.Component child : getComponents()) {
            if (child instanceof JLabel label) {
                label.setForeground(HospitalTheme.MUTED);
                label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
            } else if (child instanceof JComponent field) HospitalTheme.field(field);
        }
        fields().forEach((key, control) -> {
            if (control instanceof JComboBox<?> combo && java.util.List.of("role", "type", "status", "active").contains(key)) {
                combo.setRenderer(new DefaultListCellRenderer() {
                    @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean focus) {
                        return super.getListCellRendererComponent(list, HospitalTheme.human(Objects.toString(value, "")), index, selected, focus);
                    }
                });
            }
        });
    }
    public Map<String, String> values() {
        Map<String, String> result = new LinkedHashMap<>();
        fields().forEach((key, control) -> {
            if (control instanceof JComboBox<?> combo) result.put(key, Objects.toString(combo.getSelectedItem(), ""));
            else if (control instanceof JPasswordField password) {
                char[] chars = password.getPassword(); result.put(key, new String(chars)); Arrays.fill(chars, '\0');
            } else result.put(key, ((JTextField) control).getText());
        });
        return result;
    }
    public void setValues(Map<String, String> values) {
        values.forEach((key, value) -> {
            JComponent control = fields().get(key);
            if (control instanceof JComboBox<?> raw) {
                @SuppressWarnings("unchecked") JComboBox<String> combo = (JComboBox<String>) raw;
                boolean found = false;
                for (int i = 0; i < combo.getItemCount(); i++) if (combo.getItemAt(i).equals(value)) found = true;
                if (!found) combo.addItem(value);
                combo.setSelectedItem(value);
            } else if (control instanceof JTextField text) text.setText(value);
        });
    }
    public void options(String key, LinkedHashMap<String, String> labels) {
        @SuppressWarnings("unchecked") JComboBox<String> combo = (JComboBox<String>) fields().get(key);
        combo.setModel(new DefaultComboBoxModel<>(labels.keySet().toArray(String[]::new)));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean focus) {
                return super.getListCellRendererComponent(list, labels.getOrDefault(value, Objects.toString(value, "")), index, selected, focus);
            }
        });
    }
    public void lock(String... keys) { for (String key : keys) if (fields().containsKey(key)) fields().get(key).setEnabled(false); }
}

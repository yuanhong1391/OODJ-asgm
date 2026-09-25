package hms.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicButtonUI;

/** Shared visual styling, kept outside GUI Builder's guarded component definitions. */
public final class HospitalTheme {
    public static final Color NAVY = new Color(19, 43, 64);
    public static final Color TEAL = new Color(0, 112, 117);
    public static final Color CANVAS = new Color(243, 247, 249);
    public static final Color LINE = new Color(219, 228, 233);
    public static final Color MUTED = new Color(91, 111, 128);
    public static final Color TINT = new Color(227, 243, 242);
    private HospitalTheme() { }

    public static void install() throws Exception {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        for (Object key : java.util.Collections.list(UIManager.getDefaults().keys()))
            if (UIManager.get(key) instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, new FontUIResource("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", NAVY);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", TINT);
        UIManager.put("ComboBox.selectionForeground", NAVY);
        UIManager.put("TextField.selectionBackground", TINT);
        UIManager.put("TextField.selectionForeground", NAVY);
        UIManager.put("Table.selectionBackground", TINT);
        UIManager.put("Table.selectionForeground", NAVY);
        UIManager.put("ScrollBar.width", 12);
    }
    public static JLabel label(String text, int size, boolean bold, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size)); label.setForeground(color);
        return label;
    }
    public static JPanel surface(LayoutManager layout) {
        JPanel panel = new JPanel(layout); panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(LINE), BorderFactory.createEmptyBorder(22, 22, 22, 22)));
        return panel;
    }
    public static void button(JButton button, boolean primary) {
        button.setUI(new BasicButtonUI()); button.setOpaque(true); button.setContentAreaFilled(true);
        button.setBackground(primary ? TEAL : Color.WHITE); button.setForeground(primary ? Color.WHITE : NAVY);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createCompoundBorder(new LineBorder(primary ? TEAL : LINE), BorderFactory.createEmptyBorder(11, 17, 11, 17)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Preserve keyboard focus painting; this is an interactive desktop interface.
    }
    public static void field(JComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13)); field.setForeground(NAVY); field.setBackground(Color.WHITE);
        Border normal = BorderFactory.createCompoundBorder(new LineBorder(LINE), BorderFactory.createEmptyBorder(9, 10, 9, 10));
        Border focused = BorderFactory.createCompoundBorder(new LineBorder(TEAL), BorderFactory.createEmptyBorder(9, 10, 9, 10));
        field.setBorder(normal);
        if (field.getClientProperty("hospital.focus") == null) {
            field.putClientProperty("hospital.focus", true);
            field.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { field.setBorder(focused); }
                @Override public void focusLost(FocusEvent e) { field.setBorder(normal); }
            });
        }
    }
    public static void header(JLabel header) {
        header.setText("APU  /  MEDICAL CENTRE"); header.setOpaque(true); header.setBackground(NAVY); header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 19));
        header.setBorder(BorderFactory.createEmptyBorder(22, 25, 22, 25));
    }
    public static void navigation(JPanel panel, JButton[] buttons, String selected) {
        panel.removeAll(); panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE); panel.setPreferredSize(new Dimension(230, 500));
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, LINE), BorderFactory.createEmptyBorder(26, 14, 18, 14)));
        JLabel caption = label("STAFF WORKSPACE", 10, true, MUTED); caption.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(caption); panel.add(Box.createVerticalStrut(22));
        for (int i = 0; i < buttons.length; i++) {
            JButton b = buttons[i]; button(b, false);
            boolean active = b.getText().equals(selected);
            b.setBackground(active ? TINT : Color.WHITE); b.setForeground(active ? TEAL : MUTED);
            b.setHorizontalAlignment(SwingConstants.LEFT); b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, active ? TEAL : Color.WHITE), BorderFactory.createEmptyBorder(12, 13, 12, 10)));
            if (i == buttons.length - 1) panel.add(Box.createVerticalGlue());
            panel.add(b); panel.add(Box.createVerticalStrut(5));
        }
    }
    public static String human(String text) {
        if (text.equals("true")) return "Active";
        if (text.equals("false")) return "Inactive";
        String cleaned = text.replaceAll("([a-z])([A-Z])", "$1 $2").replace('_', ' ').toLowerCase(java.util.Locale.ROOT);
        if (cleaned.isBlank()) return cleaned;
        return Character.toUpperCase(cleaned.charAt(0)) + cleaned.substring(1);
    }
    public static Color statusColor(String status) {
        return switch (status) {
            case "OPEN", "MAINTENANCE" -> new Color(143, 89, 16);
            case "CANCELLED", "INACTIVE", "false" -> new Color(144, 57, 69);
            case "SCHEDULED", "BOOKED" -> new Color(42, 94, 153);
            default -> TEAL;
        };
    }
}

package hms.ui;

import hms.service.HospitalService;
import hms.model.User;
import hms.ui.forms.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

public class MainFrame extends JFrame {
    private final HospitalService service;
    private String currentTable;
    private JTable table;
    private List<Map<String, String>> records = new ArrayList<>();
    private static final Map<String, String> TITLES = new LinkedHashMap<>();
    static {
        TITLES.put("users", "End users"); TITLES.put("assignments", "Doctor assignments");
        TITLES.put("assets", "Hospital assets"); TITLES.put("allocations", "Asset allocations");
        TITLES.put("requests", "Lab & imaging requests"); TITLES.put("rates", "Consultation rates (MYR)");
        TITLES.put("insurance", "Insurance networks");
    }
    public MainFrame(HospitalService service) {
        this.service = service;
        initComponents();
        setMinimumSize(new Dimension(1040, 680));
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1280, screen.width - 50), Math.min(820, screen.height - 60));
        setLocationRelativeTo(null);
        HospitalTheme.header(headerLabel);
        updateNavigation("Dashboard");
        contentPanel.setBackground(HospitalTheme.CANVAS);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        footerLabel.setForeground(HospitalTheme.MUTED); footerLabel.setOpaque(true); footerLabel.setBackground(Color.WHITE);
        dashboardButton.addActionListener(e -> safely(this::dashboard));
        usersButton.addActionListener(e -> safely(() -> showTable("users")));
        assignmentsButton.addActionListener(e -> safely(() -> showTable("assignments")));
        assetsButton.addActionListener(e -> safely(() -> showTable("assets")));
        allocationsButton.addActionListener(e -> safely(() -> showTable("allocations")));
        requestsButton.addActionListener(e -> safely(() -> showTable("requests")));
        ratesButton.addActionListener(e -> safely(() -> showTable("rates")));
        insuranceButton.addActionListener(e -> safely(() -> showTable("insurance")));
        logoutButton.addActionListener(e -> { service.logout(); safely(this::showLogin); });
        safely(this::showLogin);
    }
    @FunctionalInterface private interface Action { void run() throws Exception; }
    private void safely(Action action) {
        try { action.run(); }
        catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Please check", JOptionPane.ERROR_MESSAGE); }
    }
    private void display(JComponent panel) {
        contentPanel.removeAll(); contentPanel.add(panel, BorderLayout.CENTER); contentPanel.revalidate(); contentPanel.repaint();
    }
    private void updateNavigation(String selected) {
        HospitalTheme.navigation(navigationPanel, new JButton[]{dashboardButton, usersButton, assignmentsButton, assetsButton, allocationsButton, requestsButton, ratesButton, insuranceButton, logoutButton}, selected);
    }
    private void showLogin() throws IOException {
        navigationPanel.setVisible(false); HospitalTheme.header(headerLabel);
        footerLabel.setText("APU Medical Centre    /    Staff portal");
        footerLabel.setToolTipText(service.dataLocation());
        boolean setup = service.needsSetup();
        EditorPanel form = setup ? new SetupForm() : new LoginForm();
        JButton login = new JButton(setup ? "Create administrator  →" : "Sign in  →");
        JPanel outer = new SignInPanel(form, login, setup);
        login.addActionListener(e -> safely(() -> {
            Map<String, String> values = form.values();
            if (setup) {
                if (!values.get("password").equals(values.get("confirmPassword"))) throw new IllegalArgumentException("Passwords do not match.");
                service.setup(values.get("name").trim(), values.get("username").trim(), values.get("password"));
                JOptionPane.showMessageDialog(this, "Administrator created. Sign in to continue."); showLogin();
            } else {
                User user = service.login(values.get("username"), values.get("password"));
                getRootPane().setDefaultButton(null);
                if (user.canAdminister()) { navigationPanel.setVisible(true); dashboard(); }
                else {
                    JOptionPane.showMessageDialog(this, user.welcomeMessage(), "Team integration", JOptionPane.INFORMATION_MESSAGE);
                    service.logout(); showLogin();
                }
            }
        }));
        display(outer); getRootPane().setDefaultButton(login);
    }
    private void dashboard() throws IOException {
        currentTable = null;
        HospitalTheme.header(headerLabel); updateNavigation("Dashboard");
        footerLabel.setText("Signed in as " + service.currentUser().getName() + "    /    Administrative staff");
        display(new DashboardPanel(service, key -> safely(() -> showTable(key))));
    }
    private void showTable(String key) throws IOException {
        currentTable = key; records = service.list(key);
        updateNavigation(switch (key) { case "requests" -> "Lab / imaging requests"; case "rates" -> "Consultation rates"; default -> TITLES.get(key); });
        JPanel panel = new JPanel(new BorderLayout(12, 16)); panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        panel.setBackground(HospitalTheme.CANVAS);
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setOpaque(false);
        JLabel title = HospitalTheme.label(TITLES.get(key), 27, true, HospitalTheme.NAVY); top.add(title, BorderLayout.NORTH);
        JTextField search = new JTextField(); search.setToolTipText("Filter visible records by any column");
        HospitalTheme.field(search);
        JPanel searchRow = new JPanel(new BorderLayout(10, 0)); searchRow.add(new JLabel("Search"), BorderLayout.WEST); searchRow.add(search, BorderLayout.CENTER); top.add(searchRow, BorderLayout.SOUTH);
        searchRow.setOpaque(false); searchRow.add(HospitalTheme.label(records.size() + " records", 12, false, HospitalTheme.MUTED), BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);
        String[] columns = columns(key);
        DefaultTableModel model = new DefaultTableModel(columns, 0) { @Override public boolean isCellEditable(int row, int col) { return false; } };
        for (Map<String, String> record : records) model.addRow(Arrays.stream(columns).map(c -> record.getOrDefault(c, "")).toArray());
        table = new JTable(model); table.setRowHeight(44); table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setShowVerticalLines(false); table.setGridColor(HospitalTheme.LINE); table.setIntercellSpacing(new Dimension(0, 1));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13)); table.setFillsViewportHeight(true);
        table.getTableHeader().setPreferredSize(new Dimension(100, 42));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object value, boolean selected, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, HospitalTheme.human(value.toString()), false, false, row, col);
                if (t.getRowSorter() != null && !t.getRowSorter().getSortKeys().isEmpty()) {
                    RowSorter.SortKey sort = t.getRowSorter().getSortKeys().get(0);
                    if (sort.getColumn() == t.convertColumnIndexToModel(col)) setText(getText() + (sort.getSortOrder() == SortOrder.ASCENDING ? "  ↑" : "  ↓"));
                }
                setBackground(new Color(234, 240, 244)); setForeground(HospitalTheme.MUTED); setFont(new Font("Segoe UI", Font.BOLD, 12));
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12)); return this;
            }
        });
        for (int i = 0; i < columns.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(columns[i].equals("details") ? 300 : columns[i].endsWith("Id") || columns[i].equals("id") ? 160 : 140);
        Map<String, String> displayNames = new HashMap<>();
        for (Map<String, String> user : service.list("users")) displayNames.put(user.get("id"), user.get("name") + " (" + user.get("username") + ")");
        for (Map<String, String> asset : service.list("assets")) displayNames.put(asset.get("id"), asset.get("name"));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private String badge;
            private Color badgeColor;
            @Override protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                if (badge != null) {
                    Graphics2D g = (Graphics2D) graphics.create(); g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setFont(getFont()); int width = g.getFontMetrics().stringWidth(badge) + 20;
                    g.setColor(new Color(badgeColor.getRed(), badgeColor.getGreen(), badgeColor.getBlue(), 22)); g.fillRoundRect(10, 10, width, getHeight() - 20, 10, 10);
                    g.setColor(badgeColor); g.drawString(badge, 20, (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent()); g.dispose();
                }
            }
            @Override public Component getTableCellRendererComponent(JTable source, Object value, boolean selected, boolean focused, int row, int column) {
                String raw = Objects.toString(value, "");
                String heading = columns[source.convertColumnIndexToModel(column)];
                String text = heading.endsWith("Id") ? displayNames.getOrDefault(raw, raw) : raw;
                boolean status = heading.equals("status") || heading.equals("active");
                if (status || heading.equals("role") || heading.equals("type")) text = HospitalTheme.human(raw);
                super.getTableCellRendererComponent(source, text, selected, focused, row, column);
                setBackground(selected ? HospitalTheme.TINT : row % 2 == 0 ? Color.WHITE : new Color(249, 251, 252));
                setForeground(status ? HospitalTheme.statusColor(raw) : HospitalTheme.NAVY);
                setFont(new Font("Segoe UI", status ? Font.BOLD : Font.PLAIN, 13));
                badge = status ? text : null; badgeColor = HospitalTheme.statusColor(raw); if (status) setText("");
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                setToolTipText(raw.isEmpty() ? null : raw); return this;
            }
        });
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model); table.setRowSorter(sorter);
        search.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String query = search.getText().toLowerCase(Locale.ROOT);
                sorter.setRowFilter(query.isEmpty() ? null : new RowFilter<DefaultTableModel, Integer>() {
                    @Override public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                        for (int i = 0; i < entry.getValueCount(); i++) {
                            String value = entry.getStringValue(i);
                            if (value.toLowerCase(Locale.ROOT).contains(query) || displayNames.getOrDefault(value, "").toLowerCase(Locale.ROOT).contains(query)) return true;
                        }
                        return false;
                    }
                });
            }
            public void insertUpdate(DocumentEvent e) { filter(); } public void removeUpdate(DocumentEvent e) { filter(); } public void changedUpdate(DocumentEvent e) { filter(); }
        });
        JScrollPane tableScroll = new JScrollPane(table); tableScroll.setBorder(BorderFactory.createLineBorder(HospitalTheme.LINE)); tableScroll.getViewport().setBackground(Color.WHITE);
        panel.add(tableScroll, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout(8, 12)); JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton add = new JButton(key.equals("requests") ? "Record incoming request" : "Add");
        JButton edit = new JButton(key.equals("requests") ? "Update status" : "Edit selected");
        JButton delete = new JButton(key.equals("assignments") ? "Unassign selected" : "Delete selected");
        JButton refresh = new JButton("Refresh");
        HospitalTheme.button(add, true); HospitalTheme.button(edit, false); HospitalTheme.button(delete, false); HospitalTheme.button(refresh, false);
        delete.setForeground(new Color(157, 52, 66)); bottom.setOpaque(false); buttons.setOpaque(false);
        add.addActionListener(e -> safely(() -> editRecord(null)));
        edit.addActionListener(e -> safely(() -> editRecord(selected())));
        delete.addActionListener(e -> safely(() -> {
            Map<String, String> row = selected();
            String id = row.get(key.equals("assignments") ? "doctorId" : "id");
            if (JOptionPane.showConfirmDialog(this, "Delete selected record?\n" + id, "Confirm deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                service.delete(key, id); showTable(key);
            }
        }));
        refresh.addActionListener(e -> safely(() -> showTable(key)));
        buttons.add(add); buttons.add(edit);
        if (!List.of("requests", "allocations").contains(key)) buttons.add(delete);
        if (key.equals("requests")) {
            JButton schedule = new JButton("Schedule selected request");
            HospitalTheme.button(schedule, false);
            schedule.addActionListener(e -> safely(() -> scheduleRequest(selected()))); buttons.add(schedule);
        }
        buttons.add(refresh); bottom.add(buttons, BorderLayout.NORTH);
        String tip = switch (key) {
            case "users" -> "Passwords are hidden. Leave password blank when editing to keep the existing password.";
            case "assignments" -> "Each doctor has one manager. Edit an assignment to reassign the doctor.";
            case "assets" -> "Create a ward before adding its beds. Allocate individual beds for inpatient stays.";
            case "allocations" -> "Times use YYYY-MM-DDTHH:MM. Complete/cancel allocations using Edit selected.";
            case "requests" -> "Record incoming requests on behalf of doctors; schedule them, finish the allocation, then close the request.";
            case "rates" -> "Base rates are in MYR. Teammates' billing module will consume these settings.";
            default -> "Accepted insurance networks only; policy coverage and claims are outside this module.";
        };
        bottom.add(HospitalTheme.label("<html>" + tip + "</html>", 12, false, HospitalTheme.MUTED), BorderLayout.SOUTH); panel.add(bottom, BorderLayout.SOUTH); display(panel);
    }
    private Map<String, String> selected() {
        if (table.getSelectedRow() < 0) throw new IllegalArgumentException("Select a record first.");
        return new LinkedHashMap<>(records.get(table.convertRowIndexToModel(table.getSelectedRow())));
    }
    private void editRecord(Map<String, String> existing) throws IOException {
        editDialog(currentTable, existing, Map.of());
    }
    private void scheduleRequest(Map<String, String> request) throws IOException {
        if (!request.get("status").equals("OPEN")) throw new IllegalArgumentException("Select an OPEN request. Existing schedules can be edited under Asset allocations.");
        editDialog("allocations", null, HospitalService.row("requestId", request.get("id"), "doctorId", request.get("doctorId"), "patientId", request.get("patientId")));
    }
    private void editDialog(String key, Map<String, String> existing, Map<String, String> preset) throws IOException {
        EditorPanel editor = switch (key) {
            case "users" -> new UserForm(); case "assignments" -> new AssignmentForm(); case "assets" -> new AssetForm();
            case "allocations" -> new AllocationForm(); case "requests" -> new RequestForm(); case "rates" -> new RateForm();
            default -> new InsuranceForm();
        };
        if (key.equals("assignments")) {
            editor.options("doctorId", userOptions("DOCTOR", false)); editor.options("managerId", userOptions("MANAGER", false));
            if (existing != null) editor.lock("doctorId");
        }
        if (key.equals("assets")) editor.options("parentId", assetOptions(true));
        if (List.of("allocations", "requests").contains(key)) {
            editor.options("doctorId", userOptions("DOCTOR", key.equals("allocations")));
            editor.options("patientId", userOptions("PATIENT", false));
        }
        if (key.equals("allocations")) {
            editor.options("assetId", assetOptions(false));
            LinkedHashMap<String, String> requestOptions = new LinkedHashMap<>(); requestOptions.put("", "None — consultation or inpatient stay");
            for (Map<String, String> r : service.list("requests")) if (List.of("OPEN", "SCHEDULED").contains(r.get("status")))
                requestOptions.put(r.get("id"), r.get("type") + " — " + r.get("id"));
            editor.options("requestId", requestOptions);
            LocalDateTime start = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS);
            editor.setValues(HospitalService.row("start", start.toString(), "end", start.plusHours(1).toString()));
        }
        if (existing != null) {
            Map<String, String> values = new LinkedHashMap<>(existing);
            if (key.equals("requests") && values.get("status").equals("SCHEDULED")) values.put("status", "OPEN");
            editor.setValues(values);
            if (key.equals("requests")) editor.lock("doctorId", "patientId", "type", "details");
        }
        editor.setValues(preset);
        editor.applyTheme();
        if (!preset.isEmpty()) editor.lock("requestId", "doctorId", "patientId");
        while (JOptionPane.showConfirmDialog(this, editor, (existing == null ? "Add — " : "Edit — ") + TITLES.get(key), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            try {
                Map<String, String> values = editor.values();
                if (existing != null && existing.containsKey("id")) values.put("id", existing.get("id"));
                service.save(key, values); showTable(currentTable); return;
            } catch (IllegalArgumentException | IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Please check", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private LinkedHashMap<String, String> userOptions(String role, boolean optional) throws IOException {
        LinkedHashMap<String, String> result = new LinkedHashMap<>(); result.put("", optional ? "None" : "Select " + role.toLowerCase());
        for (Map<String, String> u : service.list("users")) if (u.get("role").equals(role) && u.get("active").equals("true"))
            result.put(u.get("id"), u.get("name") + " (" + u.get("username") + ")");
        return result;
    }
    private LinkedHashMap<String, String> assetOptions(boolean wardsOnly) throws IOException {
        LinkedHashMap<String, String> result = new LinkedHashMap<>(); result.put("", wardsOnly ? "None — not a bed" : "Select facility or bed");
        for (Map<String, String> a : service.list("assets")) if (wardsOnly == a.get("type").equals("WARD"))
            result.put(a.get("id"), a.get("name") + " (" + a.get("type") + ", " + a.get("status") + ")");
        return result;
    }
    private String[] columns(String key) {
        return switch (key) {
            case "users" -> new String[]{"id", "role", "name", "username", "email", "phone", "active"};
            case "assignments" -> new String[]{"doctorId", "managerId"};
            case "assets" -> new String[]{"id", "type", "name", "parentId", "status"};
            case "allocations" -> new String[]{"id", "assetId", "patientId", "doctorId", "requestId", "start", "end", "status"};
            case "requests" -> new String[]{"id", "doctorId", "patientId", "type", "details", "status"};
            case "rates" -> new String[]{"id", "consultationType", "amount", "active"};
            default -> new String[]{"id", "name", "active"};
        };
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        headerLabel = new javax.swing.JLabel();
        navigationPanel = new javax.swing.JPanel();
        dashboardButton = new javax.swing.JButton();
        usersButton = new javax.swing.JButton();
        assignmentsButton = new javax.swing.JButton();
        assetsButton = new javax.swing.JButton();
        allocationsButton = new javax.swing.JButton();
        requestsButton = new javax.swing.JButton();
        ratesButton = new javax.swing.JButton();
        insuranceButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();
        footerLabel = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("APU Medical Centre — HMS Administration");
        headerLabel.setText("APU Medical Centre");
        getContentPane().add(headerLabel, java.awt.BorderLayout.NORTH);
        navigationPanel.setLayout(new java.awt.GridLayout(9, 1, 0, 10));
        dashboardButton.setText("Dashboard"); navigationPanel.add(dashboardButton);
        usersButton.setText("End users"); navigationPanel.add(usersButton);
        assignmentsButton.setText("Doctor assignments"); navigationPanel.add(assignmentsButton);
        assetsButton.setText("Hospital assets"); navigationPanel.add(assetsButton);
        allocationsButton.setText("Asset allocations"); navigationPanel.add(allocationsButton);
        requestsButton.setText("Lab / imaging requests"); navigationPanel.add(requestsButton);
        ratesButton.setText("Consultation rates"); navigationPanel.add(ratesButton);
        insuranceButton.setText("Insurance networks"); navigationPanel.add(insuranceButton);
        logoutButton.setText("Log out"); navigationPanel.add(logoutButton);
        getContentPane().add(navigationPanel, java.awt.BorderLayout.WEST);
        contentPanel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);
        footerLabel.setText("Ready");
        getContentPane().add(footerLabel, java.awt.BorderLayout.SOUTH);
        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel headerLabel;
    private javax.swing.JPanel navigationPanel;
    private javax.swing.JButton dashboardButton;
    private javax.swing.JButton usersButton;
    private javax.swing.JButton assignmentsButton;
    private javax.swing.JButton assetsButton;
    private javax.swing.JButton allocationsButton;
    private javax.swing.JButton requestsButton;
    private javax.swing.JButton ratesButton;
    private javax.swing.JButton insuranceButton;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel footerLabel;
    // End of variables declaration//GEN-END:variables
}

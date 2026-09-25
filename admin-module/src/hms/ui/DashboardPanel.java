package hms.ui;

import hms.service.HospitalService;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.*;

/** Actual dashboard view; also renderable offscreen for visual review. */
public final class DashboardPanel extends JPanel {
    public DashboardPanel(HospitalService service, Consumer<String> navigate) throws IOException {
        super(new BorderLayout(22, 24)); setBackground(HospitalTheme.CANVAS);
        setBorder(BorderFactory.createEmptyBorder(28, 30, 28, 30));
        JPanel intro = new JPanel(new BorderLayout()); intro.setOpaque(false);
        JPanel heading = new JPanel(); heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS)); heading.setOpaque(false);
        heading.add(HospitalTheme.label("ADMINISTRATION  /  OVERVIEW", 11, true, HospitalTheme.TEAL));
        heading.add(Box.createVerticalStrut(9));
        heading.add(HospitalTheme.label("Hospital operations", 29, true, HospitalTheme.NAVY));
        heading.add(Box.createVerticalStrut(9));
        heading.add(HospitalTheme.label("Welcome back, " + service.currentUser().getName() + ". Here is your workspace at a glance.", 13, false, HospitalTheme.MUTED));
        intro.add(heading, BorderLayout.CENTER);
        JLabel date = HospitalTheme.label(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy", java.util.Locale.ENGLISH)), 12, false, HospitalTheme.MUTED);
        date.setVerticalAlignment(SwingConstants.TOP); intro.add(date, BorderLayout.EAST); add(intro, BorderLayout.NORTH);

        List<Map<String, String>> users = service.list("users"), assets = service.list("assets"), requests = service.list("requests");
        JPanel body = new JPanel(new BorderLayout(20, 24)); body.setOpaque(false);
        JPanel metrics = new JPanel(new GridLayout(1, 4, 14, 0)); metrics.setOpaque(false);
        metrics.add(metric("Registered users", users.size(), "All hospital accounts", "users", navigate));
        metrics.add(metric("Active doctors", users.stream().filter(u -> u.get("role").equals("DOCTOR") && u.get("active").equals("true")).count(), "Clinical team", "assignments", navigate));
        metrics.add(metric("Available assets", assets.stream().filter(a -> a.get("status").equals("AVAILABLE")).count(), "Availability setting", "assets", navigate));
        metrics.add(metric("Awaiting scheduling", requests.stream().filter(r -> r.get("status").equals("OPEN")).count(), "Lab & imaging requests", "requests", navigate));
        body.add(metrics, BorderLayout.NORTH);
        JPanel lower = new JPanel(new GridLayout(1, 2, 20, 0)); lower.setOpaque(false);
        JPanel queue = HospitalTheme.surface(new BorderLayout(0, 18));
        queue.add(HospitalTheme.label("Request queue", 19, true, HospitalTheme.NAVY), BorderLayout.NORTH);
        JPanel rows = new JPanel(); rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS)); rows.setOpaque(false);
        List<Map<String, String>> pending = requests.stream().filter(r -> List.of("OPEN", "SCHEDULED").contains(r.get("status"))).toList();
        if (pending.isEmpty()) {
            rows.add(HospitalTheme.label("No pending requests", 16, true, HospitalTheme.TEAL)); rows.add(Box.createVerticalStrut(12));
            rows.add(HospitalTheme.label("New lab and imaging requests will appear here.", 12, false, HospitalTheme.MUTED));
        }
        for (Map<String, String> request : pending.stream().limit(5).toList()) {
            JPanel row = new JPanel(new BorderLayout(10, 5)); row.setOpaque(false);
            row.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, HospitalTheme.LINE), BorderFactory.createEmptyBorder(12, 0, 12, 0)));
            String patient = users.stream().filter(u -> u.get("id").equals(request.get("patientId"))).map(u -> u.get("name")).findFirst().orElse("Patient");
            JPanel names = new JPanel(new GridLayout(2, 1, 0, 5)); names.setOpaque(false);
            names.add(HospitalTheme.label(HospitalTheme.human(request.get("type")) + " request", 14, true, HospitalTheme.NAVY));
            JLabel person = HospitalTheme.label(patient, 12, false, HospitalTheme.MUTED); person.setToolTipText(patient); names.add(person);
            row.add(names, BorderLayout.CENTER);
            row.add(HospitalTheme.label(HospitalTheme.human(request.get("status")), 11, true, HospitalTheme.statusColor(request.get("status"))), BorderLayout.EAST);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72)); rows.add(row);
        }
        JScrollPane queueScroll = new JScrollPane(rows); queueScroll.setBorder(null); queueScroll.getViewport().setBackground(Color.WHITE);
        queue.add(queueScroll, BorderLayout.CENTER);
        JButton open = new JButton("View all requests  →"); HospitalTheme.button(open, false); open.addActionListener(e -> navigate.accept("requests")); queue.add(open, BorderLayout.SOUTH);
        lower.add(queue);
        JPanel actions = HospitalTheme.surface(new BorderLayout(0, 20));
        actions.add(HospitalTheme.label("Daily administration", 19, true, HospitalTheme.NAVY), BorderLayout.NORTH);
        JPanel links = new JPanel(new GridLayout(0, 1, 0, 12)); links.setOpaque(false);
        for (String[] action : new String[][]{{"users", "Manage hospital users"}, {"allocations", "Room & bed allocations"}, {"assignments", "Doctor assignments"}, {"rates", "Consultation rates"}, {"insurance", "Insurance networks"}}) {
            JButton link = new JButton(action[1] + "  →"); HospitalTheme.button(link, false); link.setHorizontalAlignment(SwingConstants.LEFT);
            link.addActionListener(e -> navigate.accept(action[0])); links.add(link);
        }
        JScrollPane actionScroll = new JScrollPane(links); actionScroll.setBorder(null);
        actionScroll.getVerticalScrollBar().setUnitIncrement(20); actionScroll.getViewport().setBackground(Color.WHITE);
        actions.add(actionScroll, BorderLayout.CENTER);
        actions.add(HospitalTheme.label("APU Medical Centre · Staff services", 11, false, HospitalTheme.MUTED), BorderLayout.SOUTH);
        lower.add(actions); body.add(lower, BorderLayout.CENTER); add(body, BorderLayout.CENTER);
    }
    private JPanel metric(String title, long value, String note, String destination, Consumer<String> navigate) {
        JPanel panel = HospitalTheme.surface(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(HospitalTheme.LINE), BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        panel.add(HospitalTheme.label(title, 12, true, HospitalTheme.MUTED), BorderLayout.NORTH);
        JButton number = new JButton(Long.toString(value)); HospitalTheme.button(number, false); number.setBorder(null);
        number.setHorizontalAlignment(SwingConstants.LEFT); number.setFont(new Font("Segoe UI", Font.BOLD, 35)); number.setForeground(HospitalTheme.NAVY);
        number.setToolTipText("Open " + title.toLowerCase()); number.addActionListener(e -> navigate.accept(destination)); panel.add(number, BorderLayout.CENTER);
        panel.add(HospitalTheme.label(note, 10, false, HospitalTheme.MUTED), BorderLayout.SOUTH); return panel;
    }
}

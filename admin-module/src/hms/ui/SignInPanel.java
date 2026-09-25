package hms.ui;

import java.awt.*;
import javax.swing.*;

/** Shared sign-in composition using the existing editable NetBeans form fields. */
public final class SignInPanel extends JPanel {
    public SignInPanel(EditorPanel form, JButton submit, boolean setup) {
        super(new GridBagLayout()); setBackground(HospitalTheme.CANVAS);
        JPanel card = new JPanel(new GridLayout(1, 2));
        card.setBorder(BorderFactory.createLineBorder(HospitalTheme.LINE));
        JPanel identity = new JPanel(new BorderLayout(0, 30)); identity.setBackground(HospitalTheme.NAVY);
        identity.setBorder(BorderFactory.createEmptyBorder(40, 34, 38, 34));
        identity.add(HospitalTheme.label("APU  /  MEDICAL CENTRE", 14, true, Color.WHITE), BorderLayout.NORTH);
        JPanel message = new JPanel(); message.setOpaque(false); message.setLayout(new BoxLayout(message, BoxLayout.Y_AXIS));
        message.add(HospitalTheme.label("Care starts with", 28, true, Color.WHITE));
        message.add(HospitalTheme.label("better coordination.", 28, true, Color.WHITE));
        message.add(Box.createVerticalStrut(24));
        message.add(HospitalTheme.label("One workspace for people, facilities", 13, false, new Color(187, 207, 217)));
        message.add(HospitalTheme.label("and the everyday work of your hospital.", 13, false, new Color(187, 207, 217)));
        message.add(Box.createVerticalStrut(40));
        message.add(HospitalTheme.label("PEOPLE   /   FACILITIES   /   CARE", 10, true, new Color(123, 209, 203)));
        identity.add(message, BorderLayout.CENTER);
        identity.add(HospitalTheme.label("Hospital Management System", 11, false, new Color(187, 207, 217)), BorderLayout.SOUTH);
        card.add(identity);
        JPanel entry = new JPanel(new BorderLayout(0, 26)); entry.setBackground(Color.WHITE);
        entry.setBorder(BorderFactory.createEmptyBorder(38, 30, 36, 30));
        JPanel heading = new JPanel(new GridLayout(0, 1, 0, 10)); heading.setOpaque(false);
        heading.add(HospitalTheme.label("STAFF ACCESS", 10, true, HospitalTheme.TEAL));
        heading.add(HospitalTheme.label(setup ? "Set up your workspace" : "Welcome back", 24, true, HospitalTheme.NAVY));
        heading.add(HospitalTheme.label(setup ? "Create your first administrator account." : "Sign in with your hospital account.", 12, false, HospitalTheme.MUTED));
        entry.add(heading, BorderLayout.NORTH);
        // Stack labels over fields for a compact, conventional sign-in layout.
        Component[] fields = form.getComponents(); form.removeAll(); form.setLayout(new GridBagLayout());
        for (int i = 0; i < fields.length; i++) {
            GridBagConstraints c = new GridBagConstraints(); c.gridx = 0; c.gridy = i; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, 0, i % 2 == 0 ? 4 : 13, 0); form.add(fields[i], c);
        }
        form.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); form.applyTheme();
        entry.add(form, BorderLayout.CENTER); HospitalTheme.button(submit, true); entry.add(submit, BorderLayout.SOUTH);
        card.add(entry); card.setPreferredSize(new Dimension(980, setup ? 570 : 450)); add(card);
    }
}

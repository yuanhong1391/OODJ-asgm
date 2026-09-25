package hms.model;

/** Shared integration identity for roles whose dashboards are owned by teammates. */
public final class ClinicalUser extends User {
    public ClinicalUser(String id, String name, String username, Role role) {
        super(id, name, username, role);
        if (role == Role.ADMIN) throw new IllegalArgumentException("Use AdminStaff for administrators.");
    }
    @Override public boolean canAdminister() { return false; }
    @Override public String welcomeMessage() {
        return "Your " + getRole().name().toLowerCase() + " account is ready. This dashboard is awaiting team integration.";
    }
}

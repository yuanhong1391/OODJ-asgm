package hms.model;

public final class AdminStaff extends User {
    public AdminStaff(String id, String name, String username) { super(id, name, username, Role.ADMIN); }
    @Override public boolean canAdminister() { return true; }
    @Override public String welcomeMessage() { return "Manage people, facilities and hospital settings."; }
}

package hms.model;

/** Shared role abstraction. Password hashes are never displayed in tables. */
public abstract class User {
    public enum Role { ADMIN, MANAGER, DOCTOR, PATIENT }
    private final String id;
    private final String name;
    private final String username;
    private final Role role;

    protected User(String id, String name, String username, Role role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.role = role;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }
    public abstract boolean canAdminister();
    public abstract String welcomeMessage();

    public static User create(String id, String name, String username, Role role) {
        return role == Role.ADMIN ? new AdminStaff(id, name, username)
                : new ClinicalUser(id, name, username, role);
    }
}

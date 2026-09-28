package models;

/** Uses the same parent class as Doctor, Patient and Manager. */
public class AdminStaff extends User {
    public AdminStaff(String id, String username, String password, String name, String phone, String email) {
        super(id, username, password, name, phone, email, "Admin");
    }

    @Override public String toTxtRecord() {
        return String.join(",", getID(), getUsername(), getPassword(), getName(), getPhone(), getEmail(), getRole());
    }
}

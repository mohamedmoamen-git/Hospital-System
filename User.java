package hospital.model;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected String password;
    protected String phone;

    public User(String id, String name, String email, String password, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public abstract String getRole();

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", getRole(), name, email);
    }
}

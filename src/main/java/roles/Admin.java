package roles;

/**
 * Datahållare för admin
 */
public class Admin implements Role{

    private String email;
    private String name;
    private String hashedPassword;

    public Admin(String email, String name, String hashedPassword) {
        this.email = email;
        this.name = name;
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

}

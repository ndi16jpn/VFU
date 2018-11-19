package roles;

/**
 * Datahållare för handledare
 */
public class Handledare implements Role{

    private String name;
    private String email;
    private String phoneNumber;
    private String hashedPassword;


    public Handledare(String name, String email, String phoneNumber, String hashedPassword) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hashedPassword = hashedPassword;
    }
    public Handledare(String email, String hashedPassword){
        this.email = email;
        this.hashedPassword = hashedPassword;
    }
    public Handledare(String email,String name, String hashedPassword){
        this.name = name;
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}

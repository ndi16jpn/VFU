package roles;

import organisations.Region;

/**
 * Datahållare för vfu samordnare
 */
public class VFUSamordnare implements Role{

    private String name;
    private String email;
    private String phoneNumber;
    private String hashedPassword;
    private Region region;

    public VFUSamordnare(String name, String email, String phoneNumber,Region region, String hashedPassword) {
        this.region = region;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hashedPassword = hashedPassword;
    }

    public VFUSamordnare(String name, String email, String phoneNumber,Region region) {
        this.region = region;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
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

    public Region getRegion() {
        return region;
    }
}

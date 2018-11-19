package data;

/**
 * Datahållare för reglänk till handledare
 */
public class LinkMailHandledare {

    private String regLink;
    private String email;

    public LinkMailHandledare(String regLink, String email) {
        this.regLink = regLink;
        this.email = email;
    }

    public String getRegLink() {
        return regLink;
    }

    public String getEmail() {
        return email;
    }
}

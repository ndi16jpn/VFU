package data;
/**
 * Datahållare för reglänk till handledare
 */
public class LinkMailVFUSamordnare {

    private String email;
    private String regLink;

    public LinkMailVFUSamordnare(String email, String link) {
        this.email = email;
        this.regLink = link;
    }

    public String getEmail() {
        return email;
    }

    public String getRegLink() {
        return regLink;
    }
}

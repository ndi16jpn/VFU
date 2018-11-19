package roles;

/**
 * Enum för olika roller
 */
public enum LoggedInRole {

    ADMIN("Admin"),
    STUDENT("Student"),
    HANDLEDARE("Handledare"),
    VFU_SAMORDNARE("VFU Samordnare");

    private String roleName;

    LoggedInRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

}

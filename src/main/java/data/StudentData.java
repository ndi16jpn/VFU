package data;

/**
 * Datahållare för studentdata
 */
public class StudentData {


    private String email;
    private String name;
    private String dob;
    private String phoneNumber;
    private String hashedPassword;

    public StudentData(String email, String name, String dob, String phoneNumber) {

        this.phoneNumber = phoneNumber;
        this.email = email;
        this.name = name;
        this.dob = dob;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    public String getHashedPassword() {
        return hashedPassword;
    }
}

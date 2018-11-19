package data;

/**
 * Datahållare för studentdata
 */
public class StudentData {


    private String email;
    private String name;
    private String dob;
    private String phoneNumber;

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
}

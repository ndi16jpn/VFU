package roles;

import data.StudentData;
import organisations.Region;
import organisations.Unit;

/**
 * Datahållare för student
 */
public class Student {

    private String email;
    private StudentData studentData;
    private Region region;
    private String personalLetter;
    private Unit choice_1;
    private Unit choice_2;
    private Unit choice_3;
    private Unit choice_4;
    private Unit choice_5;

    public Student(String email, StudentData studentData, Region region, String personalLetter,
                   Unit choice_1, Unit choice_2, Unit choice_3, Unit choice_4, Unit choice_5) {
        this.email = email;
        this.studentData = studentData;
        this.region = region;
        this.personalLetter = personalLetter;
        this.choice_1 = choice_1;
        this.choice_2 = choice_2;
        this.choice_3 = choice_3;
        this.choice_4 = choice_4;
        this.choice_5 = choice_5;
    }

    public String getEmail() {
        return email;
    }

    public StudentData getStudentData() {
        return studentData;
    }

    public Region getRegion() {
        return region;
    }

    public String getPersonalLetter() {
        return personalLetter;
    }

    public Unit getChoice_1() {
        return choice_1;
    }

    public Unit getChoice_2() {
        return choice_2;
    }

    public Unit getChoice_3() {
        return choice_3;
    }

    public Unit getChoice_4() {
        return choice_4;
    }

    public Unit getChoice_5() {
        return choice_5;
    }

}

package matching;

import organisations.Place;
import roles.Student;

/**
 * Datahållare för en match mellan en student och en plats
 */
public class MatchStudentPlace {
    private Student student;
    private Place place;

    MatchStudentPlace(Student student, Place place){
        this.place=place;
        this.student=student;
    }
    MatchStudentPlace(){

    }

    public Place getPlace(){
        return place;
    }
    public Student getStudent(){
        return student;
    }
    public void setStudent(Student student){
        this.student = student;
    }
    public void setPlace(Place place){
        this.place=place;
    }
}

package organisations;

import roles.Handledare;
import roles.Student;

/**
 * Datahållare för plats
 */
public class Place {

    private int id;
    private Handledare handledare;
    private Student student;
    private Unit unit;

    public Place(int id, Handledare handledare, Unit unit) {
        this.id = id;
        this.handledare = handledare;
        this.unit = unit;
    }
    public Place(int id, Student student, Unit unit) {
        this.id = id;
        this.student = student;
        this.unit = unit;
    }
    public Place(int id,  Handledare handledare, Student student, Unit unit) {
        this.id = id;
        this.handledare = handledare;
        this.student = student;
        this.unit = unit;
    }

    public Place( int id, Unit unit) {
        this.id = id;
        this.unit = unit;
    }
    public Place(Unit unit) {
        this.unit = unit;
    }
    public Place(Unit unit, Handledare handledare) {
        this.handledare = handledare;
        this.unit = unit;
    }
    public Place(int id, Unit unit, Student student, Handledare handledare){
        this.handledare = handledare;
        this.id = id;
        this.unit = unit;
        this.student = student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setHandledare(Handledare handledare) {
        this.handledare = handledare;
    }

    public int getId() {
        return id;
    }

    public Handledare getHandledare() {
        return handledare;
    }

    public Student getStudent() {
        return student;
    }

    public Unit getUnit() {
        return unit;
    }
}

package organisations;

import roles.Handledare;
import roles.Student;

import java.util.List;

/**
 * Datahållare för plats
 */
public class Place {

    private int id;
    private List<Handledare> handledare;
    private Student student;
    private Unit unit;
    private boolean reserved;

    public Place(int id, List<Handledare> handledare, Unit unit, boolean reserved) {
        this.id = id;
        this.handledare = handledare;
        this.unit = unit;
        this.reserved = reserved;
    }
    public Place(int id, Student student, Unit unit, boolean reserved) {
        this.id = id;
        this.student = student;
        this.unit = unit;
        this.reserved = reserved;
    }
    public Place(int id,  List<Handledare> handledare, Student student, Unit unit, boolean reserved) {
        this.id = id;
        this.handledare = handledare;
        this.student = student;
        this.unit = unit;
        this.reserved = reserved;
    }

    public Place( int id, Unit unit, boolean reserved) {
        this.id = id;
        this.unit = unit;
        this.reserved = reserved;
    }
    public Place(Unit unit, boolean reserved) {
        this.unit = unit;
        this.reserved = reserved;
    }
    public Place(Unit unit, List<Handledare> handledare, boolean reserved) {
        this.handledare = handledare;
        this.unit = unit;
        this.reserved = reserved;
    }
    public Place(int id, Unit unit, Student student, List<Handledare> handledare, boolean reserved){
        this.handledare = handledare;
        this.id = id;
        this.unit = unit;
        this.student = student;
        this.reserved = reserved;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setHandledare(List<Handledare> handledare) {
        this.handledare = handledare;
    }

    public int getId() {
        return id;
    }

    public List<Handledare> getHandledare() {
        return handledare;
    }

    public Student getStudent() {
        return student;
    }

    public Unit getUnit() {
        return unit;
    }
    public void setReserved (boolean state) {
        this.reserved = state;
    }
    public boolean getReserved () {
        return reserved;
    }
}

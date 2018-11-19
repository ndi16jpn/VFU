package organisations;

/**
 * Datahållare för enhet
 */
public class Unit {


    private int id;
    private Municipality municipality;
    private String name;
    private String info;
    private int numOfSlots;

    public Unit(int id, Municipality municipality, String name, int numOfSlots, String info) {
        this.id = id;
        this.info = info;
        this.municipality = municipality;
        this.name = name;
        this.numOfSlots = numOfSlots;
    }
    public Unit(Municipality municipality, String name, int numOfSlots, String info) {
        this.municipality = municipality;
        this.name = name;
        this.numOfSlots = numOfSlots;
        this.info = info;
    }

    public Unit(int id, String info, Municipality municipality, String name) {
        this.id = id;
        this.info = info;
        this.municipality = municipality;
        this.name = name;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public String getName() {
        return name;
    }
    public String getInfo() {
        return info;
    }


    public int getNumOfSlots() {
        return numOfSlots;
    }

    public int getId() {
        return id;
    }

}

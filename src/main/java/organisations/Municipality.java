package organisations;

/**
 * Datahållare för kommun
 */
public class Municipality {

    private String name;
    private Region region;

    public Municipality(String name, Region region) {
        this.name = name;
        this.region = region;
    }
    public Municipality(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Region getRegion() {
        return region;
    }
}

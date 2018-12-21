package organisations;

import java.util.List;

/**
 * Datahållare för kommun
 */
public class Municipality {

    private String name;
    private List<Region> regions;

    public Municipality(String name, List<Region> regions) {
        this.name = name;
        this.regions = regions;
    }
    public Municipality(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Region> getRegions() {
        return regions;
    }
}

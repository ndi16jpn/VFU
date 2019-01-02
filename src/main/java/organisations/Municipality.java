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
    public String getRegionNamesString() {
        if (regions == null) {
            return "";
        }
        String regionNames = "";
        for (Region region: regions) {
            if (regions.indexOf(region) != regions.size() - 1) {
                regionNames += region.getName() + ", ";
            } else {
                regionNames += region.getName();
            }

        }
        return regionNames;
    }
}

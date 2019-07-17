package com.d2z.d2zservice.model.etower;

import java.util.HashMap;
import java.util.Map;

public enum Facility {

	ADL("33G7N"),
    SYD("33G7K"),
    BNE2("33G7M"),
    MEL("33G7L"),
    PER("33G7P");
	
 
    private String mlid;
 
    Facility(String mlid) {
        this.mlid = mlid;
    }
 
    public String getFacility() {
        return mlid;
    }
    
    private static final Map<String, Facility> lookup = new HashMap<>();
    
    //Populate the lookup table on loading time
    static
    {
        for(Facility facility : Facility.values())
        {
            lookup.put(facility.getFacility(), facility);
        }
    }
  
    //This method can be used for reverse lookup purpose
    public static Facility get(String mlid)
    {
        return lookup.get(mlid);
    }
}

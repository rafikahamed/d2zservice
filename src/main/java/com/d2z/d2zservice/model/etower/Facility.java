package com.d2z.d2zservice.model.etower;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Facility {

	//ADL("33G7N"),
    SYD("33G7K","33Y9G"),
    BNE2("33G7M"),
    MEL("33G7L","33G7N"),
    PER("33G7P"),
	MEL2("33QU7");
	
 
    private List<String> mlid;
 
    Facility(String ...mlid) {
        this.mlid = Arrays.asList(mlid);
    }
 
    
    
    public List<String> getFacility() {
        return mlid;
    }
    
    private static final Map<String, Facility> lookup = new HashMap<>();
    
    //Populate the lookup table on loading time
    static
    {
        for(Facility facility : Facility.values())
        {
        	for(String name: facility.mlid)
            lookup.put(name, facility);
        }
    }
  
    //This method can be used for reverse lookup purpose
    public static Facility get(String mlid)
    {
        return lookup.get(mlid);
    }
    public static boolean contains(String s)
	  {
	      
	           if (lookup.containsKey(s)){ 
	              return true;
	           }else {
	      return false;
	           }
	  } 
}

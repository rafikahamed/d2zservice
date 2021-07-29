package com.d2z.d2zservice.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PostcodeZoneEnum {

	VC1Zone("VC1"),
    MCSZone("MCS");
	
 
    private List<String> serviceType;
 
    PostcodeZoneEnum(String ...serviceType) {
        this.serviceType = Arrays.asList(serviceType);
    }
 
    
    
    public List<String> getPostcodeZone() {
        return serviceType;
    }
    
    private static final Map<String, PostcodeZoneEnum> lookup = new HashMap<>();
    
    //Populate the lookup table on loading time
    static
    {
        for(PostcodeZoneEnum zoneName : PostcodeZoneEnum.values())
        {
        	for(String name: zoneName.serviceType)
            lookup.put(name, zoneName);
        }
    }
  
    //This method can be used for reverse lookup purpose
    public static PostcodeZoneEnum get(String serviceType)
    {
        return lookup.get(serviceType);
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

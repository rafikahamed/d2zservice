package com.d2z.d2zservice.model.veloce;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Courier {


	CJ_LOGISTICS("MY4"),
    NINJA_VAN("SG4");
   
	
 
    private List<String> serviceType;
 
    Courier(String ...serviceType) {
        this.serviceType = Arrays.asList(serviceType);
    }
 
    
    
    public List<String> getCourier() {
        return serviceType;
    }
    
    private static final Map<String, Courier> lookup = new HashMap<>();
    
    //Populate the lookup table on loading time
    static
    {
        for(Courier courier : Courier.values())
        {
        	for(String name: courier.serviceType)
            lookup.put(name, courier);
        }
    }
  
    //This method can be used for reverse lookup purpose
    public static Courier get(String serviceType)
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

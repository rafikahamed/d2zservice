package com.d2z.singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.APIRates;
import com.d2z.d2zservice.entity.PostcodeZone;
import com.d2z.d2zservice.util.BeanUtil;


public class D2ZSingleton {
	private static D2ZSingleton instance;
	private static List<String> postCodeZoneList;
	private static Map<String,String> postCodeStateMap;
	private static Map<String,Double> postCodeWeightMap = new HashMap<String,Double>(); ;

	
    public static List<String> getPostCodeZoneList() {
		return postCodeZoneList;
	}
    
    
	private ID2ZDao d2zDao = BeanUtil.getBean(ID2ZDao.class);
	
	private D2ZSingleton() {
		getPostCodeZone();
		getRates_PostCodeWeight();
	}
	
	private void getRates_PostCodeWeight() {
	List<APIRates> apiRates = d2zDao.fetchAllAPIRates();
		apiRates.forEach(obj -> {
			String key = obj.getPostCode()+obj.getMaxWeight()+obj.getUserId();
			postCodeWeightMap.put(key, obj.getRate());
		});
	}

	public static D2ZSingleton getInstance() {
		if(instance == null){
	        synchronized (D2ZSingleton.class) {
	            if(instance == null){
	                instance = new D2ZSingleton();
	            }
	        }
	    }
	    return instance;
	}
	private void getPostCodeZone() {
			List<PostcodeZone> postCodeZoneDaoObj = d2zDao.fetchAllPostCodeZone();
			//System.out.println("Fetched PostCodeZone details");
			//System.out.println(postCodeZoneDaoObj.toString());
			postCodeZoneList = postCodeZoneDaoObj.stream().map(daoObj -> {
				return daoObj.getPostcodeId().getSuburb().concat(daoObj.getPostcodeId().getPostcode());
			}).collect(Collectors.toList());
			System.out.println(postCodeZoneList.size());
			postCodeStateMap = new HashMap<String,String>();
			postCodeZoneDaoObj.forEach(obj -> {
				postCodeStateMap.put(obj.getPostcodeId().getPostcode(), obj.getState());
			});		
	}

	public static Map<String, String> getPostCodeStateMap(){
		return postCodeStateMap;
	}
	public static Map<String, Double> getPostCodeWeightMap() {
		return postCodeWeightMap;
	}

}

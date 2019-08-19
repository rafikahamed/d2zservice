package com.d2z.singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.APIRates;
import com.d2z.d2zservice.entity.FastwayPostcode;
import com.d2z.d2zservice.entity.PostcodeZone;
import com.d2z.d2zservice.util.BeanUtil;

public class D2ZSingleton {
	
	private static D2ZSingleton instance;
	private static List<String> postCodeZoneList;
	private static List<String> postCodeStateNameList;
	private static List<String> FWPostCodeZoneList;
	private static List<String> FWPostCodeStateNameList;
	private static Map<String,String> postCodeStateMap;
	private static Map<String,Double> postCodeWeightMap = new HashMap<String,Double>(); ;
	private static Map<String,String> postCodeZoneMap;
	
	
	public static List<String> getFWPostCodeStateNameList() {
		return FWPostCodeStateNameList;
	}

	public static List<String> getPostCodeStateNameList() {
		return postCodeStateNameList;
	}

    public static List<String> getPostCodeZoneList() {
		return postCodeZoneList;
	}
    
    public List<String> getFWPostCodeZoneList() {
		return FWPostCodeZoneList;
	}
    
	private ID2ZDao d2zDao = BeanUtil.getBean(ID2ZDao.class);
	
	private D2ZSingleton() {
		getPostCodeZone();
		getRates_PostCodeWeight();
		getFWPostCodeZone();
	}
	
	private void getRates_PostCodeWeight() {
	List<APIRates> apiRates = d2zDao.fetchAllAPIRates();
		apiRates.forEach(obj -> {
			//String key = obj.getPostCode()+obj.getMaxWeight()+obj.getUserId()+obj.getServiceType();
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
			postCodeZoneList = postCodeZoneDaoObj.stream().map(daoObj -> {
				return daoObj.getState().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
			}).collect(Collectors.toList());
			postCodeStateNameList = postCodeZoneDaoObj.stream().map(daoObj -> {
				return daoObj.getStateName().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
			}).collect(Collectors.toList());
			System.out.println(postCodeZoneList.size());
			postCodeStateMap = new HashMap<String,String>();
			postCodeZoneDaoObj.forEach(obj -> {
				postCodeStateMap.put(obj.getPostcodeId().getPostcode(), obj.getState());
			});	
			postCodeZoneMap = new HashMap<String,String>();
			postCodeZoneDaoObj.forEach(obj -> {
				postCodeZoneMap.put(obj.getPostcodeId().getSuburb()+obj.getPostcodeId().getPostcode(), obj.getZone());
			});	
	}
	
	private void getFWPostCodeZone(){
		List<FastwayPostcode> postCodeFWZoneDaoObj = d2zDao.fetchFWPostCodeZone();
		FWPostCodeZoneList = postCodeFWZoneDaoObj.stream().map(daoObj -> {
			return daoObj.getState().concat(daoObj.getFwPostCodeId().getSuburb().concat(daoObj.getFwPostCodeId().getPostcode()));
		}).collect(Collectors.toList());
		
		FWPostCodeStateNameList = postCodeFWZoneDaoObj.stream().map(daoObj -> {
			return daoObj.getStateName().concat(daoObj.getFwPostCodeId().getSuburb().concat(daoObj.getFwPostCodeId().getPostcode()));
		}).collect(Collectors.toList());
		System.out.println(FWPostCodeZoneList.size());
	}

	public static Map<String, String> getPostCodeStateMap(){
		return postCodeStateMap;
	}
	public static Map<String, Double> getPostCodeWeightMap() {
		return postCodeWeightMap;
	}
	public static Map<String, String> getPostCodeZoneMap() {
		return postCodeZoneMap;
	}

}
	

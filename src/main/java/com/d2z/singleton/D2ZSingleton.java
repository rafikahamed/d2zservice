package com.d2z.singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.APIRates;
import com.d2z.d2zservice.entity.FastwayPostcode;
import com.d2z.d2zservice.entity.MasterPostCode;
import com.d2z.d2zservice.entity.PostcodeZone;
import com.d2z.d2zservice.entity.StarTrackPostcode;
import com.d2z.d2zservice.util.BeanUtil;
import com.d2z.d2zservice.entity.NZPostcodes;
import com.d2z.d2zservice.entity.PFLPostcode;

public class D2ZSingleton {
	
	private static D2ZSingleton instance;
	private static List<String> postCodeZoneList;
	private static List<String> postCodeStateNameList;
	private static List<String> FWPostCodeZoneList;
	private static List<String> STPostCodeZoneList;
	private static List<String> FWPostCodeStateNameList;
	private static Map<String,String> postCodeStateMap;
	private static Map<String,Double> postCodeWeightMap = new HashMap<String,Double>(); ;
	private static Map<String,String> postCodeZoneMap;
	private static Map<String,String> fwPostCodeZoneNoMap;
	private static List<String> nzPostCodeZoneList;
	private static List<String> pflPostCodeZoneList;
	
	private static List<String> masterPflPostCodeZoneList;
	private static List<String> masterFWPostCodeZoneList;
	private static List<String> masterPostCodeZone3List;
	private static List<String> masterPostCodeZone4List;
	private static List<String> masterTollPostCodeList;
	private static List<String> masterRC2PostCodeList;

	private static Map<String,String> postCodeFDMRouteMap = new HashMap<String,String>();

	public static Map<String, String> getPostCodeFDMRouteMap() {
		return postCodeFDMRouteMap;
	}


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
    
    public List<String> getSTPostCodeZoneList() {
		return STPostCodeZoneList;
	}
    public List<String> getPFLPostCodeZoneList() {
		return pflPostCodeZoneList;
	}
   
    public static List<String> getMasterPflPostCodeZoneList() {
		return masterPflPostCodeZoneList;
	}

    public static List<String> getMasterFWPostCodeZoneList() {
		return masterFWPostCodeZoneList;
	}
    
    public static List<String> getMasterPostCodeZone3List() {
		return masterPostCodeZone3List;
	}

	public static List<String> getMasterPostCodeZone4List() {
		return masterPostCodeZone4List;
	}

	public static List<String> getMasterTollPostCodeList() {
		return masterTollPostCodeList;
	}
	public static List<String> getMasterRC2PostCodeList() {
		return masterRC2PostCodeList;
	}
	private ID2ZDao d2zDao = BeanUtil.getBean(ID2ZDao.class);
	
	private D2ZSingleton() {
		getPostCodeZone();
		getRates_PostCodeWeight();
		getFWPostCodeZone();
		getSTPostCodeZone();
		getNZPostCodeZone();
		getPFLPostCodeZone();
		getMasterPostcode();
		}
	
	private void getMasterPostcode() {
		List<MasterPostCode> postCodeZoneDaoObj = d2zDao.fetchAllMasterPostCodeZone();
		masterPflPostCodeZoneList = postCodeZoneDaoObj.stream().filter(obj -> obj.getPflZone().equalsIgnoreCase("1"))
				.map(daoObj -> {
					return daoObj.getPostcodeId().getState().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
				}).collect(Collectors.toList());
		masterPflPostCodeZoneList.addAll( postCodeZoneDaoObj.stream().filter(obj -> obj.getPflZone().equalsIgnoreCase("1") && obj.getStateName()!=null)
				.map(daoObj -> {
					return daoObj.getStateName().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
				}).collect(Collectors.toList()));
		masterFWPostCodeZoneList = postCodeZoneDaoObj.stream().filter(obj -> obj.getFastwayZone().equalsIgnoreCase("Fast"))
				.map(daoObj -> {
					return daoObj.getPostcodeId().getState().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
				}).collect(Collectors.toList());
		masterFWPostCodeZoneList.addAll(postCodeZoneDaoObj.stream().filter(obj -> obj.getFastwayZone().equalsIgnoreCase("Fast") && obj.getStateName()!=null)
				.map(daoObj -> {
					return daoObj.getStateName().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
				}).collect(Collectors.toList()));
		masterPostCodeZone3List = postCodeZoneDaoObj.stream().filter(obj -> obj.getMcLogic().equalsIgnoreCase("3"))
				.map(daoObj -> {
					return daoObj.getPostcodeId().getState().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
				}).collect(Collectors.toList());
		masterPostCodeZone3List.addAll(postCodeZoneDaoObj.stream().filter(obj -> obj.getMcLogic().equalsIgnoreCase("3") && obj.getStateName()!=null)
				.map(daoObj -> {
					return daoObj.getStateName().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
				}).collect(Collectors.toList()));
		masterPostCodeZone4List = postCodeZoneDaoObj.stream().filter(obj -> obj.getMcLogic().equalsIgnoreCase("4"))
				.map(daoObj -> {
					return daoObj.getPostcodeId().getState().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
				}).collect(Collectors.toList());
		masterPostCodeZone4List.addAll(postCodeZoneDaoObj.stream().filter(obj -> obj.getMcLogic().equalsIgnoreCase("4") && obj.getStateName()!=null)
				.map(daoObj -> {
					return daoObj.getStateName().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
				}).collect(Collectors.toList()));
		masterTollPostCodeList = postCodeZoneDaoObj.stream().filter(obj -> !obj.getTollZone().equals("0"))
				.map(daoObj -> {
					return daoObj.getPostcodeId().getState().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
				}).collect(Collectors.toList());
		masterTollPostCodeList.addAll(postCodeZoneDaoObj.stream().filter(obj -> !obj.getTollZone().equals("0") && obj.getStateName()!=null)
				.map(daoObj -> {
					return daoObj.getStateName().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
				}).collect(Collectors.toList()));
		masterRC2PostCodeList = postCodeZoneDaoObj.stream().filter(obj -> !obj.getRc2Zone().equals("0"))
				.map(daoObj -> {
					return daoObj.getPostcodeId().getState().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
				}).collect(Collectors.toList());
		masterRC2PostCodeList.addAll(postCodeZoneDaoObj.stream().filter(obj -> !obj.getRc2Zone().equals("0") && obj.getStateName()!=null)
				.map(daoObj -> {
					return daoObj.getStateName().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
				}).collect(Collectors.toList()));
		
		postCodeZoneDaoObj.forEach(daoObj -> {
			postCodeFDMRouteMap.put(daoObj.getPostcodeId().getState().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode()),daoObj.getFdmRoute());
		});
		
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
				return daoObj.getPostcodeId().getState().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
			}).collect(Collectors.toList());
			postCodeStateNameList = postCodeZoneDaoObj.stream().map(daoObj -> {
				return daoObj.getStateName().concat(daoObj.getPostcodeId().getSuburb()).concat(daoObj.getPostcodeId().getPostcode());
			}).collect(Collectors.toList());
			System.out.println(postCodeZoneList.size());
			postCodeStateMap = new HashMap<String,String>();
			postCodeZoneDaoObj.forEach(obj -> {
				postCodeStateMap.put(obj.getPostcodeId().getPostcode(), obj.getPostcodeId().getState());
			});	
			postCodeZoneMap = new HashMap<String,String>();
			postCodeZoneDaoObj.forEach(obj -> {
				postCodeZoneMap.put(obj.getPostcodeId().getSuburb()+obj.getPostcodeId().getPostcode(), obj.getZone());
			});
			
	}
	
	private void getNZPostCodeZone() {
		List<NZPostcodes> postCodeZoneDaoObj = d2zDao.fetchAllNZPostCodeZone();
		nzPostCodeZoneList = postCodeZoneDaoObj.stream().map(daoObj -> {
			return daoObj.getPostcodeId().getState().toUpperCase().concat(daoObj.getPostcodeId().getSuburb().toUpperCase().concat
					(daoObj.getPostcodeId().getPostcode()));
		}).collect(Collectors.toList());		
}
	private void getPFLPostCodeZone() {
		List<PFLPostcode> postCodeZoneDaoObj = d2zDao.fetchAllPFLPostCodeZone();
		pflPostCodeZoneList = postCodeZoneDaoObj.stream().map(obj -> {
			return obj.getFwPostCodeId().getState().concat(obj.getFwPostCodeId().getSuburb().concat(obj.getFwPostCodeId().getPostcode()));
		}).collect(Collectors.toList());
}
	
	private void getFWPostCodeZone(){
		List<FastwayPostcode> postCodeFWZoneDaoObj = d2zDao.fetchFWPostCodeZone();
		FWPostCodeZoneList = postCodeFWZoneDaoObj.stream().map(daoObj -> {
			return daoObj.getFwPostCodeId().getState().concat(daoObj.getFwPostCodeId().getSuburb().concat(daoObj.getFwPostCodeId().getPostcode()));
		}).collect(Collectors.toList());
		
		FWPostCodeStateNameList = postCodeFWZoneDaoObj.stream().map(daoObj -> {
			return daoObj.getStateName().concat(daoObj.getFwPostCodeId().getSuburb().concat(daoObj.getFwPostCodeId().getPostcode()));
		}).collect(Collectors.toList());
		System.out.println(FWPostCodeZoneList.size());
		
		fwPostCodeZoneNoMap = new HashMap<String,String>();
		postCodeFWZoneDaoObj.forEach(obj -> {
			if(null != obj.getZoneNo()) {
			fwPostCodeZoneNoMap.put(obj.getFwPostCodeId().getPostcode(), obj.getZoneNo());
			}
		});
		System.out.println(fwPostCodeZoneNoMap.size());

	}

	private void getSTPostCodeZone(){
		List<StarTrackPostcode> postCodeSTZoneDaoObj = d2zDao.fetchSTPostCodeZone();
		STPostCodeZoneList = postCodeSTZoneDaoObj.stream().map(daoObj -> {
			return daoObj.getStPostCodeId().getState().concat(daoObj.getStPostCodeId().getSuburb().concat(daoObj.getStPostCodeId().getPostcode()));
		}).collect(Collectors.toList());
		System.out.println(STPostCodeZoneList.size());
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
	public static Map<String, String> getFwPostCodeZoneNoMap() {
		return fwPostCodeZoneNoMap;
	}

	public List<String> getNZPostCodeZoneList() {
		// TODO Auto-generated method stub
		return nzPostCodeZoneList;
	}

}
	

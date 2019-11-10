package com.d2z.singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.SystemRefCount;
import com.d2z.d2zservice.util.BeanUtil;

public class SingletonCounter {

	private static SingletonCounter instance;
	private static Map<String,Integer> initSystemRefCount = new HashMap<String,Integer>();;
	private static Map<String,Integer> increSystemRefCount = new HashMap<String,Integer>();
	
	static AtomicInteger eTowerCounter;
	static AtomicInteger auPostCounter;
	static AtomicInteger pflCounter;
	static AtomicInteger pcaCounter;
	 
	private static int eTowerCount;
	private static int auPostCount;
	private static int pflCount;
	private static int pcaCount;
	
	private ID2ZDao d2zDao = BeanUtil.getBean(ID2ZDao.class);
	
	private SingletonCounter() {
		init();
	}
	
	public static SingletonCounter getInstance() {
		if(instance == null){
	        synchronized (SingletonCounter.class) {
	            if(instance == null){
	                instance = new SingletonCounter();
	            }
	        }
	    }
	    return instance;
	}
	
	public void init() {
		 
		 List<SystemRefCount> systemRefCount = d2zDao.fetchAllSystemRefCount();
		 for(SystemRefCount obj : systemRefCount) {
			 initSystemRefCount.put(obj.getSupplier(), obj.getSystemRefNo());
			}
		  eTowerCount = initSystemRefCount.get("ETOWER");
		  auPostCount = initSystemRefCount.get("AUPOST");
		  pflCount = initSystemRefCount.get("PFL");
		  pcaCount = initSystemRefCount.get("PCA");
		  eTowerCounter = new AtomicInteger(eTowerCount);
	      auPostCounter = new AtomicInteger(auPostCount);
		  pflCounter = new AtomicInteger(pflCount);
		  pcaCounter = new AtomicInteger(pcaCount);
	}
	
	public static int getEtowerCount () {
		eTowerCount = eTowerCounter.incrementAndGet();
		return eTowerCount;
	}
	public static int getAuPostCount () {
		auPostCount = auPostCounter.incrementAndGet();
		return auPostCount;
	}
	public static int getPFLCount () {
		pflCount = pflCounter.incrementAndGet();
		return pflCount;
	}
	public static int getPCACount () {
		pcaCount = pcaCounter.incrementAndGet();
		return pcaCount;
	}
	
	public static Map<String,Integer> getIncrementedSystemRefCount(){
		
		increSystemRefCount.put("ETOWER", eTowerCount);
		increSystemRefCount.put("AUPOST", auPostCount);
		increSystemRefCount.put("PFL", pflCount);
		increSystemRefCount.put("PCA", pcaCount);
		return increSystemRefCount;
	}
}
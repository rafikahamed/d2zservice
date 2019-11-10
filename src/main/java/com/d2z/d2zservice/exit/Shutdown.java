package com.d2z.d2zservice.exit;

import java.util.Map;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.singleton.SingletonCounter;

@Component
public class Shutdown {

	@Autowired
	private ID2ZDao d2zDao;
	
	@PreDestroy
    public void destroy() {
        System.out.println(
          "Callback triggered - @PreDestroy.");

		Map<String,Integer> currentSysRefCount = SingletonCounter.getIncrementedSystemRefCount();
		
		currentSysRefCount.entrySet().forEach(entry -> {
		    System.out.println(entry.getKey() + " - " + entry.getValue());
		});
		
		d2zDao.updateSystemRefCount(currentSysRefCount);
    }
	
}

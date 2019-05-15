package com.d2z.d2zservice.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.d2z.d2zservice.service.ID2ZService;
import com.d2z.d2zservice.service.ISuperUserD2ZService;

@Controller
public class Scheduler {
	
	@Autowired
    private  ID2ZService d2zService;
	
	@Autowired
    private  ISuperUserD2ZService superUserD2zService;
	
	@Scheduled(cron = "0 0 0/2 * * ?")
	//@Scheduled(cron = "0 0/10 * * * ?")
	public void scheduledEvents() {
	try {
		System.out.println("Calling Etower Proxy");
		superUserD2zService.scheduledTrackingEvent();
	}
	catch(Exception e)
	{
		System.out.println(e.getLocalizedMessage());
	}
	
	try {
		System.out.println("Calling AUPost Proxy");
		d2zService.makeCalltoAusPost();
	}
	catch(Exception e)
	{
		System.out.println(e.getLocalizedMessage());
	}
	
/*	try {
		System.out.println("Calling PCA Proxy");
		superUserD2zService.scheduledPCATrackingEvent();
	}
	catch(Exception e)
	{
		System.out.println(e.getLocalizedMessage());
	}*/
	
/*	try {
		System.out.println("Calling AUPost Tracking Proxy");
		d2zService.auTrackingEvent();
	}
	catch(Exception e)
	{
		System.out.println(e.getLocalizedMessage());
	}*/
	}
	@Scheduled(cron = "0 0 12 * * ?",zone = "GMT")
	public void triggerFDMCall() {
		try {
			System.out.println("Calling - FDM");
			d2zService.triggerFDM();
		}
		catch(Exception e)
		{
			System.out.println(e.getLocalizedMessage());
		}
	}
}

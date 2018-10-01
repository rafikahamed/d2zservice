package com.d2z.d2zservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	
	@RequestMapping("/")
    public String index()
    {
        return "index";
    }
	
//	@RequestMapping(value="/",method=RequestMethod.GET)
//	public String welcomeTheSlash() {
//
//    	System.err.println("\n\n\nWelcome: The / (slash) ; Index page\n\n\n");
//
//    	return "index";
//	}
//
//
//	@RequestMapping(value="/index",method=RequestMethod.GET)
//	public ModelAndView welcomeTheIndex() {
//
//    	System.err.println("\n\n\nWelcome2: The /index Index page\n\n\n");
//
//		return new ModelAndView("/index","message","Index Index Page: dynamic message from Controller");
//	}
//
//
//	@RequestMapping(value="/pending",method=RequestMethod.GET)
//	public ModelAndView pending() {
//
//    	System.err.println("\n\n\nPending\n\n\n");
//
//		return new ModelAndView("/pending","message","Pending Requests Page: message from Controller");
//
//	}

}

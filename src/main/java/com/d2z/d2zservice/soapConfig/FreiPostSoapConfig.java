package com.d2z.d2zservice.soapConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.d2z.d2zservice.soapConnector.FreiPostConnector;

@Configuration
public class FreiPostSoapConfig {

	   @Bean
	    public Jaxb2Marshaller marshaller() {
	        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
	        // this is the package name specified in the <generatePackage> specified in
	        // pom.xml
	        marshaller.setContextPath("org.tempuri");
	        return marshaller;
	    }
	 
	    @Bean
	    public FreiPostConnector connectFreipost(Jaxb2Marshaller marshaller) {
	    	FreiPostConnector client = new FreiPostConnector();
	        client.setDefaultUri("http://www.logicons.com/LCIAPITest/Service.svc");
	        client.setMarshaller(marshaller);
	        client.setUnmarshaller(marshaller);
	        return client;
	    }
}

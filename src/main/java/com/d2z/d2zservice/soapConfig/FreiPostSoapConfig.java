package com.d2z.d2zservice.soapConfig;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import com.d2z.d2zservice.interceptor.FreiPostInterceptor;
import com.d2z.d2zservice.soapConnector.FreiPostConnector;

@Configuration
public class FreiPostSoapConfig {

	@Value("${freiPost.url}")
	private String url;
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
	    	ClientInterceptor[] interceptors =
	    	        new ClientInterceptor[] {new FreiPostInterceptor()};
	    	// client.setDefaultUri("http://www.logicons.com/LCIAPITest/Service.svc");
	    	//client.setDefaultUri("http://www.logicons.com/LCIAPI/Service.svc");
	    	client.setDefaultUri(url);
	        client.setMarshaller(marshaller);
	        client.setUnmarshaller(marshaller);
	        client.getWebServiceTemplate().setInterceptors(interceptors);
	        return client;
	    }
}

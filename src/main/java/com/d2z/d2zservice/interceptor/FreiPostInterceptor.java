package com.d2z.d2zservice.interceptor;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

@Component
public class FreiPostInterceptor implements ClientInterceptor{
    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        return true;
    }
 
    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        return true;
    }
 
    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        return true;
    }
 
    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {
        try {
            System.out.println("Request :");
            messageContext.getRequest().writeTo(System.out);
            System.out.println("\nResponse : ");
            messageContext.getResponse().writeTo(System.out);
            System.out.println();
        } catch (IOException ignored) {
        }
    }

}

package com.d2z.d2zservice.util;

import java.util.List;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EmailUtil {
	
	
	
	
	/**
	 * Utility method to send simple HTML email
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendEmail(Session session, String fromEmail, String toEmail, String name, String messageData, String subject){
		try{
        	MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("cs@d2z.com.au"));
        
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Contact Us -->"+subject);
            String msgBody = "HI d2z Support Team,<br><br>";
            msgBody += "Name : "+name+"<br><br>";
            msgBody += "From Email: "+fromEmail+"<br><br>";
            msgBody += "Message: "+messageData+"<br><br>";
     	    message.setContent(msgBody, "text/html");
     	    Transport.send(message);
	    }catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	public void senderEmail(Session session, String email, String toEmail, String name, String subject) {
		try{
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress("cs@d2z.com.au"));
				
			        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			        message.setSubject("Acknowledgement -->"+subject);
			        String msgBody = "Dear "+name+",<br><br>";
			        msgBody += " Thank you for your enquiry, this has been received and we will endeavour to respond to you within 24 hours.  <br><br>";
			        msgBody += " Thank you, <br>";
			        msgBody += "D2Z Support Team <br><br>";
			        message.setContent(msgBody, "text/html");
			 	    Transport.send(message);
		    }catch (Exception e) {
			      e.printStackTrace();
		    }
	}
	
	
	
}

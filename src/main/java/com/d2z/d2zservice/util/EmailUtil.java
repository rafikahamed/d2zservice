package com.d2z.d2zservice.util;

import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class EmailUtil {
	
	@Autowired
	private JavaMailSender mailSender;


	public void sendReport(String subject,String name, String toMail, String body, byte[] report,String fileName) {

		 String mailBody = "<body><h4> Dear "+name+",</br></br>" +
						   body + "</h4>" +
						   "<h4>Regards, </br>" + "D2Z Support Team</h4>" + "</body> "; 
		 MimeMessage message = mailSender.createMimeMessage(); 
		 try 
		 { 
		  MimeMessageHelper helper = new MimeMessageHelper(message, true); 
		  helper.setFrom("report@d2z.com.au");
		  helper.setTo(toMail);
		  helper.setSubject(subject); 
		  helper.setText(mailBody, true);
		  if(report != null) {
			  System.out.println("attaching report");
			  helper.addAttachment(fileName, new ByteArrayResource(report)); 
		  }
		  mailSender.send(message); 
		  }catch (MessagingException e) { 
			 e.printStackTrace(); 
		  }
	}
	
	public void sendEmail(String subject, String toMail, String body) {

		 String mailBody = "<p>" +
						   body + "</p>" +
						   "<p>Regards, </br>" + "D2Z Support Team</p>" + "</body> "; 
		 MimeMessage message = mailSender.createMimeMessage(); 
		 try 
		 { 
		  MimeMessageHelper helper = new MimeMessageHelper(message, true); 
		  helper.setFrom("report@d2z.com.au");
		  helper.setTo(toMail);
		  helper.setSubject(subject); 
		  helper.setText(mailBody, true);
		  mailSender.send(message); 
		  }catch (MessagingException e) { 
			 e.printStackTrace(); 
		  }
	}
	
	
	
}

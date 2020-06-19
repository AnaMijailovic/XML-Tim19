package com.ftn.scientific_papers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
	private Environment environment;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	private static final String EMAIL_SENDER = "spring.mail.username";
	
	public void registrationEmail(String recipientEmail) throws MailException, InterruptedException{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(recipientEmail);
		mail.setFrom(environment.getProperty(EMAIL_SENDER));
		mail.setSubject("Registration mail for users");
		mail.setText("You have successfully registered. To start using app visit http://localhost:4200/login");
		javaMailSender.send(mail);
		System.out.println("Registration email for users sent.");
	}
	// ROLE_EDITOR assigns review of scientific paper to ROLE_REVIEWER
	public void assignReviewerEmail(String reviewerEmail,String scientificPaperName,String scientificPaperId, String coverLetterId) throws MailException, InterruptedException{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(reviewerEmail);
		mail.setFrom(environment.getProperty(EMAIL_SENDER));
		mail.setSubject("Review assignment for scientific Paper: " + scientificPaperName);
		mail.setText("You have been assigned for review of scientific paper: " + 
		scientificPaperName + ". You can accept or decline review request."+
				" Links:\n" + "http://localhost:8088/api/scientificPapers/html/" + scientificPaperId
        + "\nhttp://localhost:8088/api/scientificPapers/pdf/" + scientificPaperId
        + "\n\nhttp://localhost:8088/api/coverLetters/html/" + coverLetterId
        + "\nhttp://localhost:8088/api/coverLetters/pdf/" + coverLetterId);
		javaMailSender.send(mail);
		System.out.println("Assign reviewer email sent.");
	}

}

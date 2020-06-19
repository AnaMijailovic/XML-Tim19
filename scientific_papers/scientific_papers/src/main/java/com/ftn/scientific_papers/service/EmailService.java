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
	// Registration mail for users
	public void registrationEmail(String recipientEmail) throws MailException, InterruptedException{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(recipientEmail);
		mail.setFrom(environment.getProperty(EMAIL_SENDER));
		mail.setSubject("Registration mail for users");
		mail.setText("You have successfully registered. To start using app visit http://localhost:4200/login");
		javaMailSender.send(mail);
		System.out.println("Registration email for users sent.");
	}
	// ROLE_EDITOR assigns review of scientific paper and notifies ROLE_REVIEWER
	public void assignReviewerEmail(String reviewerEmail,String scientificPaperName,String scientificPaperId, String coverLetterId) throws MailException, InterruptedException{
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(reviewerEmail);
		mail.setFrom(environment.getProperty(EMAIL_SENDER));
		mail.setSubject("Review assignment for scientific paper: " + scientificPaperName);
		mail.setText("You have been assigned for review of scientific paper: " + 
		scientificPaperName + ". You can accept or decline review request."+
				" Links:\n" + "http://localhost:8088/api/scientificPapers/html/" + scientificPaperId
        + "\nhttp://localhost:8088/api/scientificPapers/pdf/" + scientificPaperId
        + "\n\nhttp://localhost:8088/api/coverLetters/html/" + coverLetterId
        + "\nhttp://localhost:8088/api/coverLetters/pdf/" + coverLetterId);
		javaMailSender.send(mail);
		System.out.println("Assign reviewer email sent.");
	}
	// ROLE_REVIEWER accept review of scientific paper and notifies ROLE_EDITOR
	public void acceptReviewEmail(String editorEmail,String reviewerName,String reviewerSurname, String scientificPaperName) throws MailException, InterruptedException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(editorEmail);
		mail.setFrom(environment.getProperty(EMAIL_SENDER));
		mail.setSubject("Accepted review for scientific paper: " + scientificPaperName);
		mail.setText("Reviewer: " + reviewerName + " " + reviewerSurname+" accepted your review assignment for scientific paper: " + scientificPaperName);
		javaMailSender.send(mail);
		System.out.println("Accept review email sent.");
	}
	// ROLE_REVIEWER reject review of scientific paper and notifies ROLE_EDITOR
	public void rejectReviewEmail(String editorEmail,String reviewerName,String reviewerSurname, String scientificPaperName) throws MailException, InterruptedException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(editorEmail);
		mail.setFrom(environment.getProperty(EMAIL_SENDER));
		mail.setSubject("Rejected review for scientific paper: " + scientificPaperName);
		mail.setText("Reviewer: " + reviewerName + " " + reviewerSurname+" rejected your review assignment for scientific paper: " + scientificPaperName);
		javaMailSender.send(mail);
		System.out.println("Reject review email sent.");
	}
	// ROLE_EDITOR accept scientific paper and notifies ROLE_AUTHOR
	public void acceptPaperEmail(String authorEmail,String scientificPaperName) throws MailException, InterruptedException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(authorEmail);
		mail.setFrom(environment.getProperty(EMAIL_SENDER));
		mail.setSubject("Accepted scientific paper: " + scientificPaperName);
		mail.setText("Your paper: " + scientificPaperName + " has been accepted.");
		javaMailSender.send(mail);
		System.out.println("Accept paper email sent");
	}
	
	// ROLE_EDITOR reject scientific paper and notifies ROLE_AUTHOR
	public void rejectPaperEmail(String authorEmail,String scientificPaperName) throws MailException, InterruptedException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(authorEmail);
		mail.setFrom(environment.getProperty(EMAIL_SENDER));
		mail.setSubject("Rejected scientific paper: " + scientificPaperName);
		mail.setText("Your paper: " + scientificPaperName + " has been rejected.");
		javaMailSender.send(mail);
		System.out.println("Reject paper email sent");
	}
	
	// ROLE_EDITOR orders new revision for scientific paper and notifies ROLE_AUTHOR 
	public void newRevisionPaperEmail(String authorEmail,String scientificPaperName) throws MailException, InterruptedException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(authorEmail);
		mail.setFrom(environment.getProperty(EMAIL_SENDER));
		mail.setSubject("New revision for scientific paper: " + scientificPaperName);
		mail.setText("Your paper: " + scientificPaperName + " needs new revision.");
		javaMailSender.send(mail);
		System.out.println("New revision for scientific paper email sent");
	}
	
	
	
	

}

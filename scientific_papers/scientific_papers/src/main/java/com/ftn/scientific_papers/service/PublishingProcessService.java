package com.ftn.scientific_papers.service;

import com.ftn.scientific_papers.exceptions.CustomUnexpectedException;
import com.ftn.scientific_papers.exceptions.ResourceNotFoundException;
import com.ftn.scientific_papers.model.publishing_process.PublishingProcess;
import com.ftn.scientific_papers.model.publishing_process.VersionReview;
import com.ftn.scientific_papers.model.scientific_paper.ScientificPaper;
import com.ftn.scientific_papers.model.user.TUser;
import com.ftn.scientific_papers.repository.ScientificPaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.dom.DOMParser;
import com.ftn.scientific_papers.repository.PublishingProcessRepository;
import com.ftn.scientific_papers.util.DBManager;
import com.ftn.scientific_papers.util.FileUtil;
import com.ftn.scientific_papers.util.XUpdateTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublishingProcessService {
	
	@Value("${publishing-process-template-path}")
	private String templatePath;
	
	@Value("${publishing-process-schema-path}")
	private String schemaPath;
	
	@Value("${publishing-process-collection-id}")
	private String collectionId;

	@Autowired
	private PublishingProcessRepository publishingProcessRepository;

	@Autowired
	private ScientificPaperRepository scientificPaperRepository;
	
	@Autowired
	private DBManager dbManager;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	public List<PublishingProcess> getAll() {
		return  publishingProcessRepository.getAll();
	}
	
	public XMLResource findOne(String id) throws Exception {
		return publishingProcessRepository.findOne(id);
	}

	public PublishingProcess findOneUnmarshalled(String id)  {
		return publishingProcessRepository.findOneUnmarshalled(id);
	}
	
	public String findOneByPaperId(String paperId) throws Exception {
		return publishingProcessRepository.findOneByPaperId(paperId);
	}

	public String getAuthorByProcessId(String processId) throws Exception {
		return publishingProcessRepository.getAuthorFromProcess(processId);
	}
	
	public String getLetterByPaperId(String paperId) throws Exception {
		return publishingProcessRepository.getLetterByPaperId(paperId);
	}
	
	public String createProcess(String paperId, String authorId) throws Exception {

		// Read xml template
		String template = FileUtil.readFile(templatePath);
		System.out.println("Template: " + template);
		
		Document document = DOMParser.buildDocument(template, schemaPath);
		
		// Set publishing process id
		String id = publishingProcessRepository.getNextId();

		NodeList nodeList = document.getElementsByTagName("publishing-process");
		Element paperElement = (Element) nodeList.item(0);
		paperElement.getAttributes().getNamedItem("id").setNodeValue(id);

		// Set scientific paper id
		NodeList paperIdNode = document.getElementsByTagName("scientific-paper-id");
		Element paperIdElement = (Element) paperIdNode.item(0);
		paperIdElement.setTextContent(paperId);

		// Set author id
		NodeList authorIdNode = document.getElementsByTagName("author-id");
		Element authorIdElement = (Element) authorIdNode.item(0);
		authorIdElement.setTextContent(authorId);

		// Convert Document to string and save to database
		String documentString = DOMParser.getStringFromDocument(document);
		System.out.println("Process to save: \n" + documentString);
		publishingProcessRepository.save(documentString, id);

		return id;
	}
	
	public void addCoverLetter(String processId, String coverLetterId) throws Exception {
		
		String updatePath = "/publishing-process/paper-version[last()]/cover-letter-id";
		String xUpdateExpression =  String.format(XUpdateTemplate.UPDATE, updatePath, coverLetterId);
		
		dbManager.executeXUpdate(collectionId, xUpdateExpression, processId);
		System.out.println("Process after adding cover letter id: \n" + findOne(processId).getContent().toString());
		
	}
	
	public void addNewPaperVersion(String processId, String paperVersionId) throws Exception{
		String updatePath = "/publishing-process/paper-version[last()]";
		String insertString = " <paper-version>\r\n" + 
				           "        <scientific-paper-id>" + paperVersionId +"</scientific-paper-id>\r\n" + 
				           "        <cover-letter-id></cover-letter-id>\r\n" + 
				           "    </paper-version>";
		
		String xUpdateExpression =  String.format(XUpdateTemplate.INSERT_AFTER, updatePath, insertString);
		
		dbManager.executeXUpdate(collectionId, xUpdateExpression, processId);
		System.out.println("Process after adding new paper version: \n" + findOne(processId).getContent().toString());
	}

	public void assignEditor(String processId, String userId) throws Exception {
		publishingProcessRepository.assignEditor(processId, userId);
	}

    public void updateStatus(String processId, String status) {
		try {
			PublishingProcess publishingProcess = publishingProcessRepository.findOneUnmarshalled(processId);

			String paperId = publishingProcess.getPaperVersion().get(publishingProcess.getLatestVersion().intValue()-1).getScientificPaperId();
			scientificPaperRepository.updateStatus(paperId, status);

			publishingProcessRepository.updateStatus(processId, status);
		} catch (Exception e) {
			throw new CustomUnexpectedException("Unexpected exception while updating paper status");
		}
    }

    public void assignReviewer(PublishingProcess process, TUser user) {
		try {
			PublishingProcess.PaperVersion latestVersion = process.getPaperVersion().get(process.getLatestVersion().intValue()-1);
			VersionReview versionReview = new VersionReview();
			versionReview.setReviewerId(user.getUserId());
			versionReview.setReviewId(null);
			versionReview.setStatus("PENDING");

			if (latestVersion.getVersionReviews() == null) {
				PublishingProcess.PaperVersion.VersionReviews reviews = new PublishingProcess.PaperVersion.VersionReviews();
				reviews.getVersionReview().add(versionReview);
				latestVersion.setVersionReviews(reviews);
			} else {
				latestVersion.getVersionReviews().getVersionReview().add(versionReview);
			}

			if (checkIfEnoughReviewers(latestVersion.getVersionReviews())) {
				process.setStatus("WAITING_FOR_REVIEWERS_ACCEPTANCE");
			}

			publishingProcessRepository.update(process);

			
			try {
				String reviewerEmail = user.getEmail();
				String scientificPaperId = latestVersion.getScientificPaperId();
				String coverLetterId = latestVersion.getCoverLetterId();
				ScientificPaper scientificPaper = scientificPaperRepository.findOneUnmarshalled(scientificPaperId);
				String scientificPaperName = scientificPaper.getHead().getTitle().get(0).getValue();
				emailService.assignReviewerEmail(reviewerEmail, scientificPaperName, scientificPaperId, coverLetterId);
			} catch (MailException | InterruptedException e) {
				System.out.println("There was an error while sending an e-mail");
				e.printStackTrace();
			}
			

		} catch (Exception e) {
			throw new CustomUnexpectedException("Unexpected exception while assigning reviewer");
		}

    }

	public List<PublishingProcess> getReviewRequestForUser(String userId) {
		List<PublishingProcess> allProcess = publishingProcessRepository.getAll();
		List<PublishingProcess> result = new ArrayList<>();

		for (PublishingProcess process: allProcess) {
			PublishingProcess.PaperVersion latestVersion = process.getPaperVersion().get(process.getLatestVersion().intValue()-1);
			PublishingProcess.PaperVersion.VersionReviews reviews = latestVersion.getVersionReviews();

			if (reviews == null)
				continue;

			for (VersionReview review: reviews.getVersionReview()) {
				if (review.getReviewerId().equals(userId) && review.getStatus().equals("PENDING")) {
					result.add(process);
				}
			}
		}

		return result;
	}

	public void acceptReview(PublishingProcess process, String userId) {
		try {
			PublishingProcess.PaperVersion latestVersion = process.getPaperVersion().get(process.getLatestVersion().intValue()-1);
			List<VersionReview> versionReviews = latestVersion.getVersionReviews().getVersionReview();

			for (VersionReview review : versionReviews) {
				if (review.getReviewerId().equals(userId) && review.getStatus().equals("PENDING")) {
					review.setStatus("ACCEPTED");
				}
			}

			if (checkIfAllAccepted(latestVersion.getVersionReviews())) {
				process.setStatus("REVIEWS_ACCEPTED");
				String paperId = process.getPaperVersion().get(process.getLatestVersion().intValue()-1).getScientificPaperId();
				scientificPaperRepository.getById(paperId, "REVIEWING");
			}

			publishingProcessRepository.update(process);
			try {
				TUser reviewer = customUserDetailsService.findById(userId);
				String reviewerName = reviewer.getName();
				String reviewerSurname = reviewer.getSurname();
				String scientificPaperId = latestVersion.getScientificPaperId();
				
				ScientificPaper scientificPaper = scientificPaperRepository.findOneUnmarshalled(scientificPaperId);
				String scientificPaperName = scientificPaper.getHead().getTitle().get(0).getValue();
				String editorId = process.getEditorId();
				TUser editor = customUserDetailsService.findById(editorId);
				String editorEmail = editor.getEmail();
				emailService.acceptReviewEmail(editorEmail, reviewerName, reviewerSurname, scientificPaperName);

			} catch (MailException | InterruptedException e) {
				System.out.println("There was an error while sending an e-mail");
				e.printStackTrace();
			}
		} catch (Exception e) {
			throw new CustomUnexpectedException("Unexpected exception while accepting review");
		}
	}

	public void rejectReview(PublishingProcess process, String userId) {
		try {
			PublishingProcess.PaperVersion latestVersion = process.getPaperVersion().get(process.getLatestVersion().intValue()-1);
			List<VersionReview> versionReviews = latestVersion.getVersionReviews().getVersionReview();

			for (VersionReview review : versionReviews) {
				if (review.getReviewerId().equals(userId) && review.getStatus().equals("PENDING")) {
					review.setStatus("REJECTED");
				}
			}
			process.setStatus("NEW_REVIEWER_NEEDED");
			publishingProcessRepository.update(process);
			try {
				TUser reviewer = customUserDetailsService.findById(userId);
				String reviewerName = reviewer.getName();
				String reviewerSurname = reviewer.getSurname();
				String scientificPaperId = latestVersion.getScientificPaperId();
				
				ScientificPaper scientificPaper = scientificPaperRepository.findOneUnmarshalled(scientificPaperId);
				String scientificPaperName = scientificPaper.getHead().getTitle().get(0).getValue();
				String editorId = process.getEditorId();
				TUser editor = customUserDetailsService.findById(editorId);
				String editorEmail = editor.getEmail();
				emailService.rejectReviewEmail(editorEmail, reviewerName, reviewerSurname, scientificPaperName);

			} catch (MailException | InterruptedException e) {
				System.out.println("There was an error while sending an e-mail");
				e.printStackTrace();
			}

		} catch (Exception e) {
			throw new CustomUnexpectedException("Unexpected exception while rejecting review");
		}
	}


	public List<PublishingProcess> getAssignedReviewsForUser(String userId) {
		List<PublishingProcess> allProcess = publishingProcessRepository.getAll();
		List<PublishingProcess> result = new ArrayList<>();

		for (PublishingProcess process: allProcess) {
			PublishingProcess.PaperVersion latestVersion = process.getPaperVersion().get(process.getLatestVersion().intValue()-1);
			PublishingProcess.PaperVersion.VersionReviews reviews = latestVersion.getVersionReviews();

			if (reviews == null)
				continue;

			for (VersionReview review: reviews.getVersionReview()) {
				if (review.getReviewerId().equals(userId) && review.getStatus().equals("ACCEPTED")) {
					result.add(process);
				}
			}
		}

		return result;
	}


	public void submitReview(PublishingProcess process, String userId, String reviewId) {
		try {
			PublishingProcess.PaperVersion latestVersion = process.getPaperVersion().get(process.getLatestVersion().intValue()-1);
			List<VersionReview> versionReviews = latestVersion.getVersionReviews().getVersionReview();

			for (VersionReview review : versionReviews) {
				if (review.getReviewerId().equals(userId) && review.getStatus().equals("ACCEPTED")) {
					review.setStatus("FINISHED");
					review.setReviewId(reviewId);
				}
			}

			if (checkIfAllReviewsFinished(latestVersion.getVersionReviews())) {
				process.setStatus("REVIEWS_DONE");
			}

			publishingProcessRepository.update(process);
		} catch (Exception e) {
			throw new CustomUnexpectedException("Unexpected exception while updating review in process");
		}
	}

	private boolean checkIfAllReviewsFinished(PublishingProcess.PaperVersion.VersionReviews versionReviews) {
		int cnt = 0;

		for (VersionReview review: versionReviews.getVersionReview()) {
			if (review.getStatus().equals("FINISHED")) {
				cnt++;
			}
		}

		return cnt == 2;
	}

	private boolean checkIfEnoughReviewers(PublishingProcess.PaperVersion.VersionReviews versionReviews) {
		int cnt = 0;

		for (VersionReview review: versionReviews.getVersionReview()) {
			if (review.getStatus().equals("PENDING") ||
					review.getStatus().equals("ACCEPTED") ||
					review.getStatus().equals("FINISHED")) {
				cnt++;
			}
		}

		return cnt == 2;
	}

	private boolean checkIfAllAccepted(PublishingProcess.PaperVersion.VersionReviews versionReviews) {
		int cnt = 0;

		for (VersionReview review: versionReviews.getVersionReview()) {
			if (review.getStatus().equals("ACCEPTED")) {
				cnt++;
			}
		}
		return cnt == 2;
	}

	public void update(PublishingProcess process) {
		publishingProcessRepository.save(process);
	}
}

package com.ftn.scientific_papers.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmldb.api.modules.XMLResource;

import com.ftn.scientific_papers.dom.DOMParser;
import com.ftn.scientific_papers.dto.SearchData;
import com.ftn.scientific_papers.exceptions.MaxChapterLevelsExceededException;
import com.ftn.scientific_papers.exceptions.ProcessStatusException;
import com.ftn.scientific_papers.exceptions.RevisionForbiddenException;
import com.ftn.scientific_papers.fuseki.FusekiManager;
import com.ftn.scientific_papers.fuseki.FusekiReader;
import com.ftn.scientific_papers.fuseki.MetadataExtractor;
import com.ftn.scientific_papers.model.scientific_paper.ScientificPaper;
import com.ftn.scientific_papers.repository.PublishingProcessRepository;
import com.ftn.scientific_papers.repository.ScientificPaperRepository;
import com.ftn.scientific_papers.util.FileUtil;
import com.ftn.scientific_papers.util.XSLFOTransformer;

@Service
public class ScientificPaperService {

	private static final String QUERY_FILE_PATH = "src/main/resources/sparql/metadataSearch.rq";
	private static final String QUOTES_FILE_PATH = "src/main/resources/sparql/findQuotes.rq";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Value("${max-chapter-levels}")
	private int maxChapterLevels;

	@Value("${scientific-paper-schema-path}")
	private String spSchemaPath;

	@Value("${scientific-paper-template-path}")
	private String spTemplatePath;

	@Autowired
	private ScientificPaperRepository spRepository;

	@Autowired
	private PublishingProcessRepository publishingProcessRepository;

	@Autowired
	private PublishingProcessService publishingProcessService;

	@Autowired
	private MetadataExtractor metadataExtractor;

	@Autowired
	private FusekiManager fusekiManager;
	
	@Autowired
	private XSLFOTransformer xslfoTransformer;

	public XMLResource findOne(String id) throws Exception {

		return spRepository.findOne(id);
	}

	public ScientificPaper findOneUnmarshalled(String id) {

		return spRepository.findOneUnmarshalled(id);
	}

	public String getAll(String searchText, String loggedAuthor) {
		return spRepository.getAll(searchText, loggedAuthor);
	}

	public String getTemplate() throws IOException {

		return FileUtil.readFile(spTemplatePath);
	}

	public String getByIds(Set<String> ids, String loggedAuthor) {

		StringBuilder sb = new StringBuilder();
		sb.append("<search>");

		ids.forEach(id -> sb.append(spRepository.getById(id, loggedAuthor)));

		sb.append("</search>");

		System.out.println("Full result:  \n" + sb.toString());
		return sb.toString();
	}
	
	public String getQuotedBy(String paperId) throws IOException {
		HashMap<String, String> values = new HashMap<>();

		values.put("quotedPaper", "https://github.com/AnaMijailovic/XML-Tim19/scientific_papers/" + paperId);
		
		// execute sparql query
		Set<String> paperURLs = FusekiReader.executeQuery(QUOTES_FILE_PATH, values);
		Set<String> paperIds = getIdsFromUrls(paperURLs);

		return getByIds(paperIds, "");
	}
	
	
	// sparql query returns a set of urls of matching papers
	// xQuerys are then executed to find papers with that ids
	public String metadataSearch(SearchData searchData, String loggedAuthor) throws IOException {

		HashMap<String, String> values = new HashMap<>();

		values.put("keyword", searchData.getKeyword());
		values.put("title", searchData.getTitle());
		values.put("author", searchData.getAuthor());
		values.put("affiliation", searchData.getAffiliation());

		String acceptedFromDate = searchData.getAcceptedFromDate() == null ? ""
				: sdf.format(searchData.getAcceptedFromDate());
		String acceptedToDate = searchData.getAcceptedToDate() == null ? ""
				: sdf.format(searchData.getAcceptedToDate());
		String recievedFromDate = searchData.getRecievedFromDate() == null ? ""
				: sdf.format(searchData.getRecievedFromDate());
		String recievedToDate = searchData.getRecievedToDate() == null ? ""
				: sdf.format(searchData.getRecievedToDate());
		values.put("acceptedFromDate", acceptedFromDate);
		values.put("acceptedToDate", acceptedToDate);
		values.put("recievedFromDate", recievedFromDate);
		values.put("recievedToDate", recievedToDate);

		// execute sparql query
		Set<String> paperURLs = FusekiReader.executeQuery(QUERY_FILE_PATH, values);
		Set<String> paperIds = getIdsFromUrls(paperURLs);

		return getByIds(paperIds, loggedAuthor);

	}

	// get paper ids from full urls
	// url example:
	// https://github.com/AnaMijailovic/XML-Tim19/scientific_papers/paper0
	// id: paper0
	public Set<String> getIdsFromUrls(Set<String> paperURLs) {
		Set<String> paperIds = new HashSet<>();

		for (String url : paperURLs) {
			String[] idArray = url.split("/");
			String id = idArray[idArray.length - 1];
			paperIds.add(id);
			System.out.println("Paper Id: " + id);
		}

		return paperIds;
	}
	
	public String getMetadataXml(String id) throws Exception {
		return spRepository.getMetadataXml(id);
	}

	public String getMetadataJson(String id) throws Exception {
		return spRepository.getMetadataJson(id);
	}
	
	
	
	public String save(String scientificPaperXml, String paperVersion) throws Exception {

		// SAXParseExcetion is thrown when xml is not valid
		Document document = DOMParser.buildDocument(scientificPaperXml, spSchemaPath);

		// Get id
		String id = spRepository.getNextId();

		// set ids and check chapter levels
		generateIds(document, id);

		// change id in xml document -> needed for metadata search
		// get scientific_paper element from dom and set id attribute value
		NodeList paperNodeList = document.getElementsByTagName("scientific_paper");
		Element paperElement = (Element) paperNodeList.item(0);
		paperElement.getAttributes().getNamedItem("id").setNodeValue(id);

		// set metadata attributes
		generateMetadataAttributes(paperElement, id);

		// set paper version
		paperElement.setAttribute("version", paperVersion.toString());

		// set paper status
		paperElement.setAttribute("status", "PENDING");

		// Convert Document to string and save
		String newXml = DOMParser.getStringFromDocument(document);
		System.out.println("New: \n" + newXml);
		spRepository.save(newXml, id);

		// Save metadata
		String rdfFilePath = "src/main/resources/rdf/newMetadata.rdf";
		metadataExtractor.extractMetadata(newXml, rdfFilePath);
		fusekiManager.saveMetadata(rdfFilePath, "/scientificPapers");

		return id;

	}

	public void addPaperRevision(String processId, String scientificPaperXml, String authorUsername) throws Exception {

		// check author
		String authorFromProcess = publishingProcessRepository.getAuthorFromProcess(processId);
		if (!authorFromProcess.equals(authorUsername)) {
			throw new RevisionForbiddenException("You are not the author of this paper");
		}

		// check process status
		String processStatus = publishingProcessRepository.getProcessStatus(processId);
		if (!processStatus.equals("NEW_REVISION")) {
			throw new RevisionForbiddenException("Revision is forbidden in this phase of publishing process");
		}

		// get paper latest version from process
		Integer latestVersion = Integer.valueOf(publishingProcessRepository.getProcessLatestVersion(processId));
		String newVersion = Integer.toString(latestVersion + 1);

		String paperVersionId = save(scientificPaperXml, newVersion);

		publishingProcessService.addNewPaperVersion(processId, paperVersionId);
		publishingProcessRepository.updateLatestVersion(processId, newVersion);
		publishingProcessRepository.updateStatus(processId, "NEW_SUBMISSION");

	}

	public void withdrawScientificPaper(String paperId, String authorUsername) throws Exception {

		String processId = publishingProcessService.findOneByPaperId(paperId);
		String status = publishingProcessRepository.getProcessStatus(processId);

		// check author
		String authorFromProcess = publishingProcessRepository.getAuthorFromProcess(processId);
		if (!authorFromProcess.equals(authorUsername)) {
			throw new RevisionForbiddenException("You are not the author of this paper");
		}

		// check publishing process status
		if (status.equalsIgnoreCase("WITHDRAWN"))
			throw new ProcessStatusException("Papers has already been withrawn");

		if (status.equalsIgnoreCase("ACCEPTED") || status.equalsIgnoreCase("REJECTED")
				|| status.equalsIgnoreCase("WITHDRAWN"))
			throw new ProcessStatusException("You cannot withraw published papers");

		publishingProcessRepository.updateStatus(processId, "WITHDRAWN");
		spRepository.updateStatus(paperId, "WITHDRAWN");
	}
	
	public void generateIds(Document document, String paperId) throws MaxChapterLevelsExceededException {

		// Set abstract id
		Element abstractEl = (Element) document.getElementsByTagName("abstract").item(0);
		abstractEl.setAttribute("id", paperId + "/abstract");

		// Set chapters ids

		NodeList chapters = document.getElementsByTagName("chapter");

		for (int i = 0; i < chapters.getLength(); i++) {

			Element chapter = (Element) chapters.item(i);

			// only if parent is body element! -> 1. level chapter
			if (chapter.getParentNode().getNodeName().equals("body")) {
				String id = paperId + "/chapter" + i;
				chapter.setAttribute("id", id);

				// Set paragraphs ids
				NodeList paragraphs = chapter.getElementsByTagName("paragraph");
				for (int j = 0; j < paragraphs.getLength(); j++) {
					Element paragraph = (Element) paragraphs.item(j);
					paragraph.setAttribute("id", id + "/paragraph" + j);
				}

				setSubchapterIds(chapter, id, 1);

			}

		}

		// Set images ids
		NodeList images = document.getElementsByTagName("image");
		for (int i = 0; i < images.getLength(); i++) {
			Element image = (Element) images.item(i);
			image.setAttribute("id", paperId + "/image" + i);
		}

		// Set tables ids
		NodeList tables = document.getElementsByTagName("table");
		for (int i = 0; i < tables.getLength(); i++) {
			Element table = (Element) tables.item(i);
			table.setAttribute("id", paperId + "/table" + i);
		}

	}

	// recursively set subchapter ids
	// check max chapter level
	public void setSubchapterIds(Element chapter, String parentId, int levelCount)
			throws MaxChapterLevelsExceededException {

		if (levelCount > maxChapterLevels)
			throw new MaxChapterLevelsExceededException(
					"Max Chapter Levels Excedeed - maximum is " + maxChapterLevels + " levels");

		NodeList subchapters = chapter.getElementsByTagName("chapter");

		for (int i = 0; i < subchapters.getLength(); i++) {

			Element subchapter = (Element) subchapters.item(i);

			// only if parent is chapter with the right id -> only next level chapters
			if (((Element) subchapter.getParentNode()).getAttribute("id").equals(parentId)) {
				String id = parentId + "/subchapter" + i;
				subchapter.setAttribute("id", id);

				// Set paragraph ids
				NodeList paragraphs = subchapter.getElementsByTagName("paragraph");
				for (int j = 0; j < paragraphs.getLength(); j++) {
					Element paragraph = (Element) paragraphs.item(j);
					paragraph.setAttribute("id", id + "/paragraph" + j);
				}

				setSubchapterIds(subchapter, id, levelCount + 1);

			}

		}

	}

	public void generateMetadataAttributes(Element paperElement, String id) throws Exception {

		String aboutAttributeValue = "https://github.com/AnaMijailovic/XML-Tim19/scientific_papers/" + id;
	    paperElement.setAttribute("rdfa:about", aboutAttributeValue);
		paperElement.setAttribute("rdfa:vocab", "https://github.com/AnaMijailovic/XML-Tim19/predicate/");

		// titles
		NodeList titlesNodeList = paperElement.getElementsByTagName("title");

		for (int i = 0; i < titlesNodeList.getLength(); i++) {
			Element titleElement = (Element) titlesNodeList.item(i);

			titleElement.setAttribute("rdfa:property", "pred:titled");
			titleElement.setAttribute("rdfa:datatype", "xs:string");

		}

		// keywords
		NodeList keywordsNodeList = paperElement.getElementsByTagName("keyword");

		for (int i = 0; i < keywordsNodeList.getLength(); i++) {
			Element keywordElement = (Element) keywordsNodeList.item(i);

			keywordElement.setAttribute("rdfa:property", "pred:keyword");
			keywordElement.setAttribute("rdfa:datatype", "xs:string");

		}

		// get author elements and change rdfa:href attribute value
		NodeList authorsNodeList = paperElement.getElementsByTagName("author");

		for (int i = 0; i < authorsNodeList.getLength(); i++) {
			Element authorElement = (Element) authorsNodeList.item(i);

			authorElement.setAttribute("rdfa:href", aboutAttributeValue);
			authorElement.setAttribute("rdfa:rel", "pred:isTheAuthorOf");
			authorElement.setAttribute("rdfa:rev", "pred:isWrittenBy");
			authorElement.setAttribute("rdfa:about", "author" + i);

			// first and last name
			Element firstName = (Element) authorElement.getElementsByTagName("first_name").item(0);
			Element lastName = (Element) authorElement.getElementsByTagName("last_name").item(0);

			firstName.setAttribute("rdfa:property", "pred:firstName");
			firstName.setAttribute("rdfa:datatype", "xs:string");
			lastName.setAttribute("rdfa:property", "pred:lastName");
			lastName.setAttribute("rdfa:datatype", "xs:string");

			// affiliation
			Element affiliationElement = (Element) authorElement.getElementsByTagName("affiliation").item(0);
			affiliationElement.setAttribute("rdfa:href", "author" + i);
			affiliationElement.setAttribute("rdfa:rel", "pred:hasAMember");
			affiliationElement.setAttribute("rdfa:rev", "pred:isAMemberOf");
			affiliationElement.setAttribute("rdfa:about", "affiliation");

			// affiliation name
			Element affiliationNameElement = (Element) affiliationElement.getElementsByTagName("name").item(0);
			affiliationNameElement.setAttribute("rdfa:property", "pred:affiliationNamed");
			affiliationNameElement.setAttribute("rdfa:datatype", "xs:string");

		}

		// set recieved date
		Element recievedDateElement = (Element) paperElement.getElementsByTagName("recieved_date").item(0);
		recievedDateElement.setTextContent(sdf.format(new Date()));
		recievedDateElement.setAttribute("rdfa:property", "pred:recieved");
		recievedDateElement.setAttribute("rdfa:datatype", "xs:date");

		// revised date
		Element revisedDateElement = (Element) paperElement.getElementsByTagName("revised_date").item(0);
		revisedDateElement.setTextContent("");
		revisedDateElement.setAttribute("rdfa:property", "pred:revised");
		revisedDateElement.setAttribute("rdfa:datatype", "xs:date");

		// accepted date
		Element acceptedDateElement = (Element) paperElement.getElementsByTagName("accepted_date").item(0);
		acceptedDateElement.setTextContent("");
		acceptedDateElement.setAttribute("rdfa:property", "pred:accepted");
		acceptedDateElement.setAttribute("rdfa:datatype", "xs:date");
		
		
		// quotes
		NodeList quoteList = paperElement.getElementsByTagName("quote");

		for (int i = 0; i < quoteList.getLength(); i++) {
			Element quoteElement = (Element) quoteList.item(i);
			String quoteSourcePaperId = quoteElement.getElementsByTagName("source").item(0).getTextContent();
			findOne(quoteSourcePaperId); // check if id is valid
			quoteElement.setAttribute("rdfa:href", aboutAttributeValue);
			quoteElement.setAttribute("rdfa:rel", "pred:isQuotedBy");
			quoteElement.setAttribute("rdfa:rev", "pred:quotes");
			quoteElement.setAttribute("rdfa:about", "https://github.com/AnaMijailovic/XML-Tim19/scientific_papers/"
			                         + quoteSourcePaperId);

		}
		
    // https://github.com/AnaMijailovic/XML-Tim19/scientific_papers/paper3
	}
	
	public XMLResource findOneXml(String id) throws Exception {
		XMLResource scientificPaperXml = spRepository.findOne(id);
		return scientificPaperXml;
	}
  
    public byte[] findOnePdf(String id) throws Exception {
		String xmlString = spRepository.findOne(id).getContent().toString();
		String xslString = ScientificPaperRepository.SCIENTIFIC_PAPER_XSL_FO_PATH;
		ByteArrayOutputStream scientificPaperPdf = xslfoTransformer.generatePDF(xmlString, xslString); 
		return scientificPaperPdf.toByteArray();
    }
    
    public byte[] findOneHtml(String id) throws Exception {
    	String xmlString = spRepository.findOne(id).getContent().toString();
    	String xslString = ScientificPaperRepository.SCIENTIFIC_PAPER_XSL_PATH;
    	ByteArrayOutputStream scientificPaperHtml = xslfoTransformer.generateHTML(xmlString, xslString); 
		return scientificPaperHtml.toByteArray();
	}

	public void update(ScientificPaper scientificPaper) {
		spRepository.update(scientificPaper);
	}
}

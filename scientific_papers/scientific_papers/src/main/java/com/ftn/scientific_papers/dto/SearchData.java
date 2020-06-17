package com.ftn.scientific_papers.dto;

import java.util.Date;

public class SearchData {
	
	private String title;
	private String author;
	private String affiliation;
	private String keyword;
	private Date acceptedFromDate;
	private Date acceptedToDate;
	private Date recievedFromDate;
	private Date recievedToDate;
	
	public SearchData() {
		super();
	}

	public SearchData(String title, String author, String affiliation, String keyword, Long acceptedFromDate,
			Long acceptedToDate, Long recievedFromDate, Long recievedToDate) {
		super();
		this.title = title;
		this.author = author;
		this.affiliation = affiliation;
		this.keyword = keyword;
		this.acceptedFromDate = acceptedFromDate == null ? null : new Date(acceptedFromDate);
		this.acceptedToDate = acceptedToDate == null ? null : new Date(acceptedToDate);
		this.recievedFromDate = recievedFromDate == null ? null : new Date(recievedFromDate);
		this.recievedToDate = recievedToDate == null ? null : new Date(recievedToDate);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Date getAcceptedFromDate() {
		return acceptedFromDate;
	}

	public void setAcceptedFromDate(Date acceptedFromDate) {
		this.acceptedFromDate = acceptedFromDate;
	}

	public Date getAcceptedToDate() {
		return acceptedToDate;
	}

	public void setAcceptedToDate(Date acceptedToDate) {
		this.acceptedToDate = acceptedToDate;
	}

	public Date getRecievedFromDate() {
		return recievedFromDate;
	}

	public void setRecievedFromDate(Date recievedFromDate) {
		this.recievedFromDate = recievedFromDate;
	}

	public Date getRecievedToDate() {
		return recievedToDate;
	}

	public void setRecievedToDate(Date recievedToDate) {
		this.recievedToDate = recievedToDate;
	}
	
	
}
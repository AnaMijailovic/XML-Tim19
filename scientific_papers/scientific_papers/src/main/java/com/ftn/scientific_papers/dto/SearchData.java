package com.ftn.scientific_papers.dto;

import java.util.Date;

public class SearchData {
	
	private String title;
	private String author;
	private String affiliation;
	private String keyword;
	private Date fromDate;
	private Date toDate;
	
	public SearchData() {
		super();
	}
	
	
	public SearchData(String title, String author, String affiliation, String keyword, Long fromDate, Long toDate) {
		super();
		this.title = title;
		this.author = author;
		this.affiliation = affiliation;
		this.keyword = keyword;
		this.fromDate = fromDate == null ? null : new Date(fromDate);
		this.toDate = toDate == null ? null : new Date(toDate);;
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
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	

}

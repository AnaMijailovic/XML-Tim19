package com.ftn.scientific_papers.dto;

import java.util.List;

public class PublishingProcessDTO {
    String processId;
    String paperTitle;
    String author;
    String editor;
    List<String> reviewers;
    String status;
    String version;

    public PublishingProcessDTO() {}

    public PublishingProcessDTO(String processId, String paperTitle, String author, String editor, List<String> reviewers, String status, String version) {
        this.processId = processId;
        this.paperTitle = paperTitle;
        this.author = author;
        this.editor = editor;
        this.reviewers = reviewers;
        this.status = status;
        this.version = version;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public List<String> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<String> reviewers) {
        this.reviewers = reviewers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

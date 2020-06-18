package com.ftn.scientific_papers.dto;

import java.util.List;

public class PublishingProcessDTO {
    String processId;
    List<String> paperTitles;
    List<String> authors;
    String editorUsername;
    String editorName;
    List<String> reviewers;
    String status;
    String version;

    public PublishingProcessDTO() {}

    public PublishingProcessDTO(String processId, List<String> paperTitles, List<String> authors, String editorId, String editorName, List<String> reviewers, String status, String version) {
        this.processId = processId;
        this.paperTitles = paperTitles;
        this.authors = authors;
        this.editorUsername = editorId;
        this.editorName = editorName;
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

    public List<String> getPaperTitles() {
        return paperTitles;
    }

    public void setPaperTitles(List<String> paperTitles) {
        this.paperTitles = paperTitles;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getEditorUsername() {
        return editorUsername;
    }

    public void setEditorUsername(String editorUsername) {
        this.editorUsername = editorUsername;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
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
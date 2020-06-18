package com.ftn.scientific_papers.dto;

import java.util.List;

public class ReviewRequestDTO {
    private String processId;
    private String paperId;
    private String version;
    List<String> paperTitles;

    public ReviewRequestDTO() {}

    public ReviewRequestDTO(String processId, String paperId, String version, List<String> paperTitles) {
        this.processId = processId;
        this.paperId = paperId;
        this.version = version;
        this.paperTitles = paperTitles;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getPaperTitles() {
        return paperTitles;
    }

    public void setPaperTitles(List<String> paperTitles) {
        this.paperTitles = paperTitles;
    }
}

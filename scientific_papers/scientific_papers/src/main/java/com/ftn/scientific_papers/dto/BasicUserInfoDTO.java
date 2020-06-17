package com.ftn.scientific_papers.dto;

public class BasicUserInfoDTO {
    private String userId;
    private String fullName;

    public BasicUserInfoDTO(String userId, String fullName) {
        this.userId = userId;
        this.fullName = fullName;
    }

    public BasicUserInfoDTO() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}

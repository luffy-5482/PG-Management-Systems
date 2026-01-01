package com.parent.tenant.dto;

public class TenantDocumentsRequest {

    private String aadhaarNumber;
    private String aadhaarFrontUrl;
    private String aadhaarBackUrl;

    private String panNumber;
    private String panCardUrl;

    private String photoUrl;
    private String educationCertificateUrl; // optional
	public String getAadhaarNumber() {
		return aadhaarNumber;
	}
	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}
	public String getAadhaarFrontUrl() {
		return aadhaarFrontUrl;
	}
	public void setAadhaarFrontUrl(String aadhaarFrontUrl) {
		this.aadhaarFrontUrl = aadhaarFrontUrl;
	}
	public String getAadhaarBackUrl() {
		return aadhaarBackUrl;
	}
	public void setAadhaarBackUrl(String aadhaarBackUrl) {
		this.aadhaarBackUrl = aadhaarBackUrl;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	public String getPanCardUrl() {
		return panCardUrl;
	}
	public void setPanCardUrl(String panCardUrl) {
		this.panCardUrl = panCardUrl;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getEducationCertificateUrl() {
		return educationCertificateUrl;
	}
	public void setEducationCertificateUrl(String educationCertificateUrl) {
		this.educationCertificateUrl = educationCertificateUrl;
	}

    
}

   

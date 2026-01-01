package com.parent.tenant.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tenant_documents")
public class TenantDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long applicationId;

    private String aadhaarNumber;
    private String aadhaarFrontUrl;
    private String aadhaarBackUrl;

    private String panNumber;
    private String panUrl;
    private String photoUrl;

    private String educationCertificateUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

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

	public String getPanUrl() {
		return panUrl;
	}

	public void setPanUrl(String panUrl) {
		this.panUrl = panUrl;
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

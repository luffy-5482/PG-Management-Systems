package com.parent.tenant.dto;

public class DocumentDto {
    private Long id;
    private String type;
    private String url;
    private Boolean verified;

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }
}

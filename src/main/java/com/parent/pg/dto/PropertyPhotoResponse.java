package com.parent.pg.dto;

public class PropertyPhotoResponse {
    private Long id;
    private String imageUrl;
    private Boolean isMain;
    private Long pgId;

    public PropertyPhotoResponse() {}

    public PropertyPhotoResponse(Long id, String imageUrl, Boolean isMain, Long pgId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
        this.pgId = pgId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsMain() { return isMain; }
    public void setIsMain(Boolean isMain) { this.isMain = isMain; }
    public Long getPgId() { return pgId; }
    public void setPgId(Long pgId) { this.pgId = pgId; }
}

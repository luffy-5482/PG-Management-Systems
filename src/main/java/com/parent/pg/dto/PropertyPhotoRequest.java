package com.parent.pg.dto;

public class PropertyPhotoRequest {
    private String imageUrl;
    private Boolean isMain;
    private Long pgId;

    public PropertyPhotoRequest() {}

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsMain() { return isMain; }
    public void setIsMain(Boolean isMain) { this.isMain = isMain; }
    public Long getPgId() { return pgId; }
    public void setPgId(Long pgId) { this.pgId = pgId; }
}

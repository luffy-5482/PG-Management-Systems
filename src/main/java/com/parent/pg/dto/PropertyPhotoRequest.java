package com.parent.pg.dto;

public class PropertyPhotoRequest {
	private Long id; // optional
	private String imageUrl;
	private Boolean isMain;
	private Long pgId;
	private Boolean delete; // optional

	public PropertyPhotoRequest() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Boolean getIsMain() {
		return isMain;
	}

	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}

	public Long getPgId() {
		return pgId;
	}

	public void setPgId(Long pgId) {
		this.pgId = pgId;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}
}

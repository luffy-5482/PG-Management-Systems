package com.parent.tenant.dto;

public class PrivacySettingsDto {
    private Boolean showPhone;
    private Boolean showEmail;
    private Boolean showProfile;

    public Boolean getShowPhone() { return showPhone; }
    public void setShowPhone(Boolean showPhone) { this.showPhone = showPhone; }

    public Boolean getShowEmail() { return showEmail; }
    public void setShowEmail(Boolean showEmail) { this.showEmail = showEmail; }

    public Boolean getShowProfile() { return showProfile; }
    public void setShowProfile(Boolean showProfile) { this.showProfile = showProfile; }
}

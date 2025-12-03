package com.parent.tenant.dto;

import java.time.LocalDate;
import java.util.List;

public class TenantProfileResponse {
    private Long tenantId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String occupation;
    private String roomNumber;
    private String floor;
    private String sharingType;
    private Integer monthlyRent;
    private String checkinDate;
    private String leaseDuration;
    private List<EmergencyContactDto> emergencyContacts;
    private List<DocumentDto> documents;

    // getters / setters
    // (you can use Lombok @Data if project has lombok)
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }
    public String getSharingType() { return sharingType; }
    public void setSharingType(String sharingType) { this.sharingType = sharingType; }
    public Integer getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(Integer monthlyRent) { this.monthlyRent = monthlyRent; }
    public String getCheckinDate() { return checkinDate; }
    public void setCheckinDate(String checkinDate) { this.checkinDate = checkinDate; }
    public String getLeaseDuration() { return leaseDuration; }
    public void setLeaseDuration(String leaseDuration) { this.leaseDuration = leaseDuration; }
    public List<EmergencyContactDto> getEmergencyContacts() { return emergencyContacts; }
    public void setEmergencyContacts(List<EmergencyContactDto> emergencyContacts) { this.emergencyContacts = emergencyContacts; }
    public List<DocumentDto> getDocuments() { return documents; }
    public void setDocuments(List<DocumentDto> documents) { this.documents = documents; }
}

package com.parent.tenant.dto;

import com.parent.tenant.enums.TenantStatus;

public class TenantDashboardResponse {
    private String email;
    private TenantStatus accountStatus;
    private TenantStatus applicationStatus;
    private int unreadNotifications;
    
    
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public TenantStatus getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(TenantStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
	public TenantStatus getApplicationStatus() {
		return applicationStatus;
	}
	public void setApplicationStatus(TenantStatus applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	public int getUnreadNotifications() {
		return unreadNotifications;
	}
	public void setUnreadNotifications(int unreadNotifications) {
		this.unreadNotifications = unreadNotifications;
	}
}

package com.parent.tenant.dto;

import java.time.LocalDate;

public class DashboardResponse {

    private int totalBookings;
    private int activeBookings;
    private int pendingPayments;
    private double totalPayments;
    private LocalDate upcomingRentDate;

    public DashboardResponse() {}

    public int getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }

    public int getActiveBookings() {
        return activeBookings;
    }

    public void setActiveBookings(int activeBookings) {
        this.activeBookings = activeBookings;
    }

    public int getPendingPayments() {
        return pendingPayments;
    }

    public void setPendingPayments(int pendingPayments) {
        this.pendingPayments = pendingPayments;
    }

    public double getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(double totalPayments) {
        this.totalPayments = totalPayments;
    }

    public LocalDate getUpcomingRentDate() {
        return upcomingRentDate;
    }

    public void setUpcomingRentDate(LocalDate upcomingRentDate) {
        this.upcomingRentDate = upcomingRentDate;
    }
}

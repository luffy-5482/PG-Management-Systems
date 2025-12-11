package com.parent.mess.dto;

import java.time.LocalDate;

public class MessMenuDto {

    private LocalDate menuDate;
    private String breakfast;
    private String lunch;
    private String dinner;
    private String notes;

    public LocalDate getMenuDate() { return menuDate; }
    public void setMenuDate(LocalDate menuDate) { this.menuDate = menuDate; }

    public String getBreakfast() { return breakfast; }
    public void setBreakfast(String breakfast) { this.breakfast = breakfast; }

    public String getLunch() { return lunch; }
    public void setLunch(String lunch) { this.lunch = lunch; }

    public String getDinner() { return dinner; }
    public void setDinner(String dinner) { this.dinner = dinner; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

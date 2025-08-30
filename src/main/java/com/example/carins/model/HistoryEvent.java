package com.example.carins.model;

import java.time.LocalDate;

public class HistoryEvent {

    private String eventType;
    private LocalDate eventDate;
    private String description;

    public HistoryEvent(String eventType, LocalDate eventDate, String description) {
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.description = description;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
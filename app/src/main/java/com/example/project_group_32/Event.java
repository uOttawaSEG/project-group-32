package com.example.project_group_32;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Event implements Serializable {

    private long id;
    private final String title, description, date, startTime, endTime, address;
    public enum Mode {
        manual,
        auto
    };

    private final Mode mode;

    public Event (String title, String description, String date, String startTime, String endTime, String address, Mode mode) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        this.mode = mode;
    }

    public Event (long id, String title, String description, String date, String startTime, String endTime, String address, Mode mode) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        this.mode = mode;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getAddress() {
        return address;
    }

    public Mode getMode() {
        return mode;
    }

    @NonNull
    public String toString() {
        return title
                + "|\t" + description
                + "|\t" + date
                + "|\t" + startTime
                + "|\t" + endTime
                + "|\t" + address;
    }
}

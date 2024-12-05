package com.example.project_group_32;

import java.io.PushbackReader;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private long id;

    private final String username;
    private final String password;
    private Role role;
    private ArrayList<Long> events;

    public enum Role {
        Administrator,
        Attendee,
        Organizer
    }

    User (String username, String password) {
        this.username = username;
        this.password = password;
    }

    User (long id, String username, String password, Role role, ArrayList<Long> events) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.events = events;
    }

    public long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public ArrayList<Long> getEvents() {
        return events;
    }

    public String getUsername() {
        return username;
    }

    public boolean verify(String password) {
        return this.password.equals(password);
    }

}

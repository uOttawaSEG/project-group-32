package com.example.project_group_32;

import java.io.Serializable;

public class User implements Serializable {

    private final String username;
    private final String password;

    public enum Type {
        Administrator,
        Attendee,
        Organizer
    }

    User (String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean verify(String password) {
        return this.password.equals(password);
    }

}

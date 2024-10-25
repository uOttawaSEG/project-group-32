package com.example.project_group_32;

public class Organizer extends User {

    private final Userinfo userinfo;

    Organizer (String email, String password, Userinfo userinfo) {
        super(email, password);
        this.userinfo = userinfo;
    }

    public Userinfo getInfo() {
        return userinfo;
    }
}

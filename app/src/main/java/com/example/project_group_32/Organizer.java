package com.example.project_group_32;

public class Organizer extends User {

    private final Userinfo userinfo;

    Organizer (String username, String password, Userinfo userinfo) {
        super(username, password);
        this.userinfo = userinfo;
    }

    public Userinfo getInfo() {
        return userinfo;
    }
}

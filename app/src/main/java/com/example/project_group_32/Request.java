package com.example.project_group_32;

import androidx.annotation.NonNull;

public class Request {

    private final long id;

    public enum Status {
        Processing,
        Rejected,
        Approved
    }

    private final Status status;

    private final User.Role role;
    private final Userinfo userinfo;

    public Request (long id, Status status, User.Role role, Userinfo userinfo) {
        this.id = id;
        this.status = status;
        this.role = role;
        this.userinfo = userinfo;
    }

    public long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    @NonNull
    public String toString() {
        return status.toString()
                + "|\t" + role.toString()
                + "|\t" + userinfo.toString();
    }
}

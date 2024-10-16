package com.example.project_group_32;

public class Organizer extends User {

    private String organization;

    Organizer (String firstName, String lastName, String email, String password, String phoneNumber, String address, String organization) {
        super(firstName, lastName, email, password, phoneNumber, address);
        this.organization = organization;
    }

    public String getOrganization() {
        return organization;
    }
}

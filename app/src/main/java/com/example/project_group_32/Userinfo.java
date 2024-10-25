package com.example.project_group_32;

import java.io.Serializable;

public class Userinfo implements Serializable {

    private final String firstName, lastName, email, phoneNumber, address, organization;

    Userinfo (String firstName, String lastName, String email, String phoneNumber, String address, String organization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.organization = organization;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getOrganization() {
        return organization;
    }
}

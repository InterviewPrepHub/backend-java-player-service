package com.app.playerservicejava.dto;

import com.app.playerservicejava.model.Player;

public class GuestPlayerDto {

    private String firstName;
    private String lastName;

    public GuestPlayerDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}

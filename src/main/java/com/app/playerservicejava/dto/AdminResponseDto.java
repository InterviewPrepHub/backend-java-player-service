package com.app.playerservicejava.dto;


public class AdminResponseDto {
    private String firstName;
    private String lastName;

    public AdminResponseDto(String firstName, String lastName) {
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

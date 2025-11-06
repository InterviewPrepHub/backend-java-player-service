package com.app.playerservicejava.dto;

public class AdminDto {

    private String firstName;
    private String lastName;
    private String birthYear;

    public AdminDto() {}

    public AdminDto(String firstName, String lastName, String birthYear) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getBirthYear() { return birthYear; }
    public void setBirthYear(String birthYear) { this.birthYear = birthYear; }
}


package com.app.playerservicejava.dto;

import com.app.playerservicejava.service.ScoutMapper;

public class ScoutDto {

    private String lastName;
    private String birthCountry;
    private String debutYear;

    public ScoutDto() {

    }

    public ScoutDto(String lastName, String birthCountry, String debutYear) {
        this.lastName = lastName;
        this.birthCountry = birthCountry;
        this.debutYear = debutYear;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public String getDebutYear() {
        return debutYear;
    }
}

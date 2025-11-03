package com.app.playerservicejava.dto;

public class PlayerSummary {

    private String playerId;
    private String firstName;
    private String lastName;
    private String birthYear;

    public PlayerSummary(String playerId, String firstName, String lastName, String birthYear) {
        this.playerId = playerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthYear() {
        return birthYear;
    }
}

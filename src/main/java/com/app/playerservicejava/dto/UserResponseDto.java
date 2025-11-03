package com.app.playerservicejava.dto;

public class UserResponseDto {
    private String firstName;

    public UserResponseDto(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }
}

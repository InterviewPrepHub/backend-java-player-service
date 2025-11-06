package com.app.playerservicejava.dto;

public class APIError {

    private String error;
    private String message;
    private int status;
    private String path;

    public APIError(String error, String message, int status, String path) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getPath() {
        return path;
    }
}

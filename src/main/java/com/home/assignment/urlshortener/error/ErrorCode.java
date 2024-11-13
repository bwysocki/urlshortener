package com.home.assignment.urlshortener.error;

public enum ErrorCode {
    NO_SUCH_ENTITY(1, 404, "Missing entity", "The operation could not be completed because one of the core entities was not recognised"),
    INVALID_REQUEST(2, 400, "Bad request", "Invalid request"),
    CONFLICT(3, 409, "Entity already exists", "Specified entity already exists"),
    ;

    private int number;
    private int httpStatus;
    private String title;
    private String description;

    ErrorCode(int number, int httpStatus, String title, String description) {
        this.number = number;
        this.httpStatus = httpStatus;
        this.title = title;
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}

package com.home.assignment.urlshortener.error;

import java.util.Map;

public class ConflictException extends UrlShortenerException {

    private final String type;
    private final Object id;

    public ConflictException(String type, Object id) {
        super(ErrorCode.CONFLICT, Map.of("entityType", type, "id", id));
        this.type = type;
        this.id = id;
    }
}

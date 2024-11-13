package com.home.assignment.urlshortener.error;

import java.util.Map;

public class NoSuchEntityException extends UrlShortenerException {

    private final String type;
    private final Object id;

    public NoSuchEntityException(String type, Object id) {
        super(ErrorCode.NO_SUCH_ENTITY, Map.of("entityType", type, "id", id));
        this.type = type;
        this.id = id;
    }
}

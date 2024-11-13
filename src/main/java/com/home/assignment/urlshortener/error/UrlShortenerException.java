package com.home.assignment.urlshortener.error;

import java.util.Map;

public class UrlShortenerException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, ?> arguments;

    public UrlShortenerException(ErrorCode errorCode, Map<String, ?> arguments) {
        this(errorCode, arguments, null);
    }

    public UrlShortenerException(ErrorCode errorCode, Map<String, ?> arguments, Throwable cause) {
        super(toString(errorCode, arguments), cause);
        this.errorCode = errorCode;
        this.arguments = arguments;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, ?> getArguments() {
        return arguments;
    }

    public static String toString(ErrorCode errorCode, Map<String, ?> arguments) {
        if (arguments == null || arguments.isEmpty()) {
            return errorCode.toString();
        }
        return "%s - %s".formatted(errorCode.toString(), arguments);
    }
}

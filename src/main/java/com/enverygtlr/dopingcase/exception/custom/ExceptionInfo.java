package com.enverygtlr.dopingcase.exception.custom;

import org.springframework.http.HttpStatus;

public enum ExceptionInfo {

    ENTITY_NOT_FOUND("Entity not found", HttpStatus.NOT_FOUND),
    ENTITY_ALREADY_EXISTS("Entity already exists", HttpStatus.CONFLICT),
    VALIDATION_ERROR("Validation error", HttpStatus.BAD_REQUEST),;

    private final String message;
    private final HttpStatus status;

    ExceptionInfo(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

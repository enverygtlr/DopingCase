package com.enverygtlr.dopingcase.exception.custom;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    private final String title;
    private final String message;
    private final HttpStatus status;

    protected CustomException(ExceptionInfo exceptionInfo, Object... args) {
        super(String.format(exceptionInfo.getMessage(), args));
        this.title = exceptionInfo.getTitle();
        this.message = String.format(exceptionInfo.getMessage(), args);
        this.status = exceptionInfo.getStatus();
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
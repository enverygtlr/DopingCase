package com.enverygtlr.dopingcase.exception.custom;

public abstract class CustomException extends RuntimeException {
    private final ExceptionInfo info;

    protected CustomException(ExceptionInfo info) {
        super(info.getMessage());
        this.info = info;
    }

    public ExceptionInfo getInfo() {
        return info;
    }
}
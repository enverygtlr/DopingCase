package com.enverygtlr.dopingcase.exception;


import com.enverygtlr.dopingcase.exception.custom.ExceptionInfo;

public class ValidationException extends RuntimeException {
    public ValidationException() {
        super(ExceptionInfo.VALIDATION_ERROR.getMessage());
    }
}

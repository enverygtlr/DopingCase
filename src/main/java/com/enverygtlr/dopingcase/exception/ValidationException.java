package com.enverygtlr.dopingcase.exception;


import com.enverygtlr.dopingcase.exception.custom.CustomException;
import com.enverygtlr.dopingcase.exception.custom.ExceptionInfo;

public class ValidationException extends CustomException {
    public ValidationException() {
        super(ExceptionInfo.VALIDATION_ERROR);
    }
}

package com.enverygtlr.dopingcase.exception;

import com.enverygtlr.dopingcase.exception.custom.CustomException;
import com.enverygtlr.dopingcase.exception.custom.ExceptionInfo;

public class NotFoundException extends CustomException {
    public NotFoundException() {
        super(ExceptionInfo.ENTITY_NOT_FOUND);
    }
}

package com.enverygtlr.dopingcase.exception;

import com.enverygtlr.dopingcase.exception.custom.CustomException;
import com.enverygtlr.dopingcase.exception.custom.ExceptionInfo;

public class NotFoundException extends CustomException {
    private NotFoundException(ExceptionInfo info, Object... args) {
        super(info, args);
    }

    public static NotFoundException forStudent(String studentId) {
        return new NotFoundException(ExceptionInfo.STUDENT_NOT_FOUND, studentId);
    }

    public static NotFoundException forTest(String testId) {
        return new NotFoundException(ExceptionInfo.TEST_NOT_FOUND, testId);
    }

    public NotFoundException(String entityId) {
        super(ExceptionInfo.ENTITY_NOT_FOUND, entityId);
    }
}

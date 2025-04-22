package com.enverygtlr.dopingcase.exception.custom;

import org.springframework.http.HttpStatus;

public enum ExceptionInfo {
    ENTITY_NOT_FOUND("Entity Not Found", "Entity with ID %s not found", HttpStatus.NOT_FOUND),
    ENTITY_ALREADY_EXISTS("Conflict", "Entity with field %s already exists", HttpStatus.CONFLICT),
    VALIDATION_ERROR("Validation Failed", "Validation error: %s", HttpStatus.BAD_REQUEST),
    CHOICE_DOES_NOT_BELONG("Invalid Choice", "Choice %s does not belong to question %s", HttpStatus.BAD_REQUEST),
    QUESTION_DOES_NOT_BELONG("Invalid Question", "Question %s does not belong to test %s", HttpStatus.BAD_REQUEST),
    QUESTION_IS_NOT_VALID("Invalid Question", "Only one choice can be true", HttpStatus.BAD_REQUEST),
    STUDENT_NOT_FOUND("Student Not Found", "Student with ID %s not found", HttpStatus.NOT_FOUND),
    TEST_NOT_FOUND("Test Not Found", "Test with ID %s not found", HttpStatus.NOT_FOUND),
    DUPLICATE_ANSWER("Duplicate Answer", "Student %s has already answered question %s", HttpStatus.CONFLICT),
    STUDENT_NOT_ATTENDED("Test Not Attended", "Student %s has not attended test %s", HttpStatus.BAD_REQUEST),
    STUDENT_ALREADY_ATTENDED("Conflict", "Student %s has already attended test %s", HttpStatus.CONFLICT),;

    private final String title;
    private final String message;
    private final HttpStatus status;

    ExceptionInfo(String title, String message, HttpStatus status) {
        this.title = title;
        this.message = message;
        this.status = status;
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
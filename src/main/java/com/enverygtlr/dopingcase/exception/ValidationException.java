package com.enverygtlr.dopingcase.exception;


import com.enverygtlr.dopingcase.exception.custom.CustomException;
import com.enverygtlr.dopingcase.exception.custom.ExceptionInfo;

public class ValidationException extends CustomException {

    private ValidationException(ExceptionInfo info, Object... args) {
        super(info, args);
    }

    public static ValidationException choiceDoesNotBelong(String choiceId, String questionId) {
        return new ValidationException(ExceptionInfo.CHOICE_DOES_NOT_BELONG, choiceId, questionId);
    }

    public static ValidationException questionDoesNotBelong(String questionId, String testId) {
        return new ValidationException(ExceptionInfo.QUESTION_DOES_NOT_BELONG, questionId, testId);
    }

    public static ValidationException duplicateAnswer(String studentId, String questionId) {
        return new ValidationException(ExceptionInfo.DUPLICATE_ANSWER, studentId, questionId);
    }

    public static ValidationException testNotAttended(String studentId, String testId) {
        return new ValidationException(ExceptionInfo.STUDENT_NOT_ATTENDED, studentId, testId);
    }

    public static ValidationException studentAlreadyAttended(String studentId, String testId) {
        return new ValidationException(ExceptionInfo.STUDENT_ALREADY_ATTENDED, studentId, testId);
    }

    public static ValidationException questionIsNotValid() {
        return new ValidationException(ExceptionInfo.QUESTION_IS_NOT_VALID);
    }

    public ValidationException(String detailMessage) {
        super(ExceptionInfo.VALIDATION_ERROR, detailMessage);
    }
}
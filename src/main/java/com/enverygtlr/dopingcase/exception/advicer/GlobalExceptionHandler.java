package com.enverygtlr.dopingcase.exception.advicer;

import com.enverygtlr.dopingcase.exception.custom.CustomException;
import com.enverygtlr.dopingcase.exception.custom.ExceptionInfo;
import com.enverygtlr.dopingcase.exception.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, WebRequest request) {
        ExceptionInfo info = ex.getInfo();

        logger.error("CustomException: {} - {}", ex.getClass().getSimpleName(), info, ex);

        return buildErrorResponse(info.getMessage(), info.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        logger.warn("Database constraint violation: {}", ex.getMessage(), ex);
        return buildErrorResponse(ExceptionInfo.ENTITY_ALREADY_EXISTS.getMessage(), ExceptionInfo.ENTITY_ALREADY_EXISTS.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        String combinedErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> formatFieldError(fieldError))
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("Validation error");

        logger.warn("Validation failed: {}", combinedErrors);

        return buildErrorResponse(combinedErrors, HttpStatus.BAD_REQUEST);
    }

    private String formatFieldError(FieldError fieldError) {
        return String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unhandled exception occurred", ex);

        return buildErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .httpCode(status.value())
                .build();

        return ResponseEntity.status(status).body(response);
    }
}

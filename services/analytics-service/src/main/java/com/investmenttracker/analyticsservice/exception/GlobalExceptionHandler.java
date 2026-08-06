package com.investmenttracker.analyticsservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(
            MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Invalid request");

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                AnalyticsErrorCode.INVALID_REQUEST,
                message
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception exception) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                AnalyticsErrorCode.INTERNAL_SERVER_ERROR,
                "An unexpected internal error occurred"
        );
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            AnalyticsErrorCode code,
            String message) {

        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                code,
                message
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }
}
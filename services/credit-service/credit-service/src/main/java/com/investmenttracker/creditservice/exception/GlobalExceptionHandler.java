package com.investmenttracker.creditservice.exception;

import com.investmenttracker.creditservice.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CreditNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(CreditNotFoundException e, HttpServletRequest request) {
        return new ResponseEntity<> (new ErrorResponse(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()),
                HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CreditAlreadyClosedException.class)
    public ResponseEntity<ErrorResponse> handleCreditAlreadyClosedException(CreditAlreadyClosedException e, HttpServletRequest request){
        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()),
                HttpStatus.CONFLICT
        );
    }
    @ExceptionHandler(CreditAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCreditAlreadyExistsException(CreditAlreadyExistsException e, HttpServletRequest request){
        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()),
                HttpStatus.CONFLICT
        );
    }
    @ExceptionHandler(CreditFinancialDataModificationNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleCreditFinancialDataModificationNotAllowedException(CreditFinancialDataModificationNotAllowedException e, HttpServletRequest request){
        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()),
                HttpStatus.CONFLICT
        );
    }
    @ExceptionHandler(PaymentExceedsRemainingDebtException.class)
    public ResponseEntity<ErrorResponse> handlePaymentExceedsRemainingDebtException(PaymentExceedsRemainingDebtException e, HttpServletRequest request){
        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getBindingResult().getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                        .collect(Collectors.joining("; ")),
                request.getRequestURI()),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(HttpServerErrorException.InternalServerError e, HttpServletRequest request){
        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred",
                request.getRequestURI()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}

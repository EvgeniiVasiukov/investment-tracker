package com.investmenttracker.userservice.exception;

import com.investmenttracker.userservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UserIsBlockedException.class)
    public ResponseEntity<ErrorResponse> handleUserIsBlockedException(UserIsBlockedException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(SelfModificationNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleSelfModificationNotAlloedException(SelfModificationNotAllowedException exception) {
       ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), LocalDateTime.now());
       return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() +": " + error.getDefaultMessage())
                .collect(Collectors.joining(";"));
        ErrorResponse errorResponse = new ErrorResponse(message, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        String parameterName = exception.getName();
        Object invalidName = exception.getValue();
        Class<?> requiredType = exception.getRequiredType();
        String message;
        if (requiredType != null && requiredType.isEnum()) {
            String allowedValues = Arrays.stream(
                    requiredType
                            .getEnumConstants())
                            .map(Object::toString)
                            .collect(Collectors.joining(","));
            message = "Invalid value '" + invalidName +
                    "' for parameter '" + parameterName +
                    "'. \n Allowed values: " + allowedValues;
            ErrorResponse errorResponse = new ErrorResponse(message, LocalDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            message = "Invalid value '" + invalidName + "' for parameter '" + parameterName + "'" ;
        }
        ErrorResponse errorResponse = new ErrorResponse(message, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }
}

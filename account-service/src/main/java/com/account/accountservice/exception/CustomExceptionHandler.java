package com.account.accountservice.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{

  @ExceptionHandler(UserDataNotValidException.class)
  ResponseEntity<ErrorRequest> handleUserDataNotValidException(UserDataNotValidException ex) {
    ErrorRequest error = ErrorRequest.builder()
        .status(HttpStatus.BAD_REQUEST)
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .timestamp(LocalDateTime.now())
        .errorMessage(ex.getMessage())
        .rejectedValue(ex.getRejectedValue())
        .build();

    return new ResponseEntity<ErrorRequest>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  ResponseEntity<ErrorRequest> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
    ErrorRequest error = ErrorRequest.builder()
        .status(HttpStatus.BAD_REQUEST)
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .timestamp(LocalDateTime.now())
        .errorMessage(ex.getMessage())
        .rejectedValue(ex.getRejectedValue())
        .build();

    return new ResponseEntity<ErrorRequest>(error, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
    ErrorRequest error = ErrorRequest.builder()
        .status(status)
        .statusCode(status.value())
        .timestamp(LocalDateTime.now())
        .errorMessages(ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()))
        .rejectedValues(ex.getFieldErrors().stream().map(FieldError::getField).collect(Collectors.toList()))
        .build();

    return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
  }
}

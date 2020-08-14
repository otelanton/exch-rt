package com.exchangerates.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CurrencyNotFoundException.class)
  public ResponseEntity<ApiRequestError> handleCurrencyNotFoundException(CurrencyNotFoundException exception, WebRequest request){
    return buildResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage(), request.getDescription(false), exception.getRejectedValue());
  }

  @ExceptionHandler(DateOutOfRangeException.class)
  public ResponseEntity<ApiRequestError> handleDateOutOfRangeException(DateOutOfRangeException exception, WebRequest request){
    return buildResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getDescription(false), exception.getRejectedValue());
  }

  @ExceptionHandler(RateNotFoundException.class)
  public ResponseEntity<ApiRequestError> handleMissingPathVariableException(RateNotFoundException exception, WebRequest request){
    return buildResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage(), request.getDescription(false), exception.getRejectValue());
  }

  private ResponseEntity<ApiRequestError> buildResponseEntity(HttpStatus status, String exceptionMessage, String description, Object rejectedValue){
    ApiRequestError error = new ApiRequestError(status, LocalDateTime.now(), exceptionMessage, description, status.value(), rejectedValue);
    return new ResponseEntity<>(error, status);
  }
}
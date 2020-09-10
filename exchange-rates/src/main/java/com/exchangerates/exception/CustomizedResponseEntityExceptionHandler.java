package com.exchangerates.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CurrencyNotFoundException.class)
  public ResponseEntity<Object> handleCurrencyNotFoundException(CurrencyNotFoundException exception, WebRequest request){
    return buildResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage(), request.getDescription(false), exception.getRejectedValue());
  }

  @ExceptionHandler(DateOutOfRangeException.class)
  public ResponseEntity<Object> handleDateOutOfRangeException(DateOutOfRangeException exception, WebRequest request){
    return buildResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getDescription(false), exception.getRejectedValue());
  }

  @ExceptionHandler(RateNotFoundException.class)
  public ResponseEntity<Object> handleRateNotFoundException(RateNotFoundException exception, WebRequest request){
    return buildResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage(), request.getDescription(false), exception.getRejectValue());
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
    return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false), ex.getParameterName());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationException(Exception ex, WebRequest request){
    return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), request.getDescription(false), ex.getCause().getCause());
  }

  @ExceptionHandler(DateTimeParseException.class)
  ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex, WebRequest request){
    return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false), ex.getCause());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request){
    return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false), ex.getValue());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request){
    return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false), ex.getCause());
  }

  private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String exceptionMessage, String description, Object rejectedValue){
    ApiRequestError error = new ApiRequestError(status, LocalDateTime.now(), exceptionMessage, description, status.value(), rejectedValue);
    return new ResponseEntity<>(error, status);
  }
}
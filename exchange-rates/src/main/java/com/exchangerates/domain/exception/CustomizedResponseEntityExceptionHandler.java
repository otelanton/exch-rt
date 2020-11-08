package com.exchangerates.domain.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

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
  protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatus status, WebRequest request){
    return buildResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getDescription(false), exception.getParameterName());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request){
    String message = null;
    Object rejectedParam = null;
    for(ConstraintViolation<?> violation : exception.getConstraintViolations()) {
      rejectedParam = violation.getInvalidValue();
      if(rejectedParam.equals("")){
        rejectedParam = "blank";
      }
      message = String.format("Parameter %s must not be %s", violation.getPropertyPath().toString().split("\\.")[1], rejectedParam);
    }
    return buildResponseEntity(HttpStatus.BAD_REQUEST, message, request.getDescription(false), rejectedParam);
  }

  @ExceptionHandler(DateTimeParseException.class)
  ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException exception, WebRequest request){
    return buildResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getDescription(false), exception.getParsedString());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, WebRequest request){
    String providedType = Objects.requireNonNull(request.getParameter(exception.getName())).getClass().getSimpleName();
    String requiredType = Objects.requireNonNull(exception.getRequiredType()).getSimpleName();
    String message = String.format("Failed to convert value of type '%s' to required type '%s", providedType, requiredType);
    return buildResponseEntity(HttpStatus.BAD_REQUEST, message, request.getDescription(false), exception.getValue());
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers, HttpStatus status, WebRequest request){
    return buildResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage(), request.getDescription(false), exception.getRequestURL());
  }

  private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String exceptionMessage, String description, Object rejectedValue){
    ApiRequestError error = new ApiRequestError(status, LocalDateTime.now(), exceptionMessage, description, status.value(), rejectedValue);
    return new ResponseEntity<>(error, status);
  }
}
package com.exchangerates.domain.exception;

public class DateOutOfRangeException extends ApiException {

  public DateOutOfRangeException(String message, Object rejectedValue){
    super(message, rejectedValue);
  }

  public Object getRejectedValue() {
    return super.getRejectedValue();
  }
}

package com.exchangerates.exception;

public class RateNotFoundException extends ApiException {

  public RateNotFoundException(String message, Object rejectValue){
    super(message, rejectValue);
  }

  public Object getRejectValue() {
    return super.getRejectedValue();
  }
}

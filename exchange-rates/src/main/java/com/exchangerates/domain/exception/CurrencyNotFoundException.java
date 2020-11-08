package com.exchangerates.domain.exception;

public class CurrencyNotFoundException extends ApiException {

  public CurrencyNotFoundException(String message, Object rejectedValue){
    super(message, rejectedValue);
  }

  public Object getRejectedValue() {
    return super.getRejectedValue();
  }
}

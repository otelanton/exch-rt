package com.exchangerates.exception;

public class ApiException extends RuntimeException {

  private Object rejectedValue;

  ApiException(String message, Object rejectedValue){
    super(message);
    this.rejectedValue = rejectedValue;
  }

  Object getRejectedValue(){
   return this.rejectedValue;
  }
}

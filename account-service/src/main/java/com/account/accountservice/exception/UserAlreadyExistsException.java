package com.account.accountservice.exception;

public class UserAlreadyExistsException extends RuntimeException{
  private final String rejectedValue;

  public UserAlreadyExistsException(String message, String rejectedValue){
    super(message);
    this.rejectedValue = rejectedValue;
  }

  public String getRejectedValue() {
    return rejectedValue;
  }
}

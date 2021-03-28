package com.account.accountservice.exception;

public class UserDataNotValidException extends RuntimeException{
  private String rejectedValue;

  public UserDataNotValidException(String message, String rejectedValue) {
    super(message);
    this.rejectedValue = rejectedValue;
  }

  public String getRejectedValue() {
    return rejectedValue;
  }
}

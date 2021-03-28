package com.account.accountservice.domain;

import com.account.accountservice.exception.UserAlreadyExistsException;
import com.account.accountservice.exception.UserDataNotValidException;
import com.account.accountservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserValidator {

  private final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
  private final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z]+)(?=.*[A-Z]+)([a-zA-Z0-9]+){8,16}$";
  private final String USERNAME_PATTERN = "^([a-zA-Z0-9]+){4,16}$";
  private UserRepository userRepository;

  @Autowired
  UserValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void validateUser(User user) {
    validateEmail(user.getEmail());
    validatePassword(user.getPassword());
    validateUsername(user.getUsername());
    if(userRepository.findByEmail(user.getEmail()) != null) {
      throw new UserAlreadyExistsException("User with specified email address already exists.", user.getEmail());
    }
    else if(userRepository.findByUsername(user.getUsername()) != null) {
      throw new UserAlreadyExistsException("User with specified username already exists.", user.getUsername());
    }
  }

  public void validatePassword(String password) {
    if (!Pattern.compile(PASSWORD_PATTERN).matcher(password).find()) {
      throw new UserDataNotValidException("Invalid password. Password should consist with at least 1 number and letters of latin alphabet in both cases.", password);
    }
  }

  public void validateUsername(String username) {
    if (!Pattern.compile(USERNAME_PATTERN).matcher(username).find()) {
      throw new UserDataNotValidException("Invalid username. Username should consist with at least 1 number and letters of latin alphabet in both cases.", username);
    }
  }

  public void validateEmail(String email) {
    if (!Pattern.compile(EMAIL_PATTERN).matcher(email).find()) {
      throw new UserDataNotValidException("Invalid email address.", email);
    }
  }

  public void validateField(String value, String fieldName) {
    value = value.replaceAll("\"", "");
    switch (fieldName) {
      case "username" -> this.validateUsername(value);
      case "password" -> this.validatePassword(value);
      case "email" -> this.validateEmail(value);
    }
  }
}

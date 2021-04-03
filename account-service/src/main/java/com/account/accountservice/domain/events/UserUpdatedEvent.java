package com.account.accountservice.domain.events;

import com.account.accountservice.domain.User;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@ToString
public class UserUpdatedEvent implements Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long userUpdatedEventID;
  private long userID;
  private String username;
  private String email;

  public UserUpdatedEvent(User user) {
    this.userID = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
  }
}

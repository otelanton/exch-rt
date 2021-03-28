package com.account.accountservice.domain.events;

import com.account.accountservice.domain.Alert;
import com.account.accountservice.domain.SubscriptionType;
import com.account.accountservice.domain.User;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@ToString
public class AlertCreatedEvent implements Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long subscriptionCreatedEventID;
  private long alertID;
  private long userID;
  private String username;
  private String email;
  private BigDecimal rate;
  private String currency;
  private SubscriptionType type;

  public AlertCreatedEvent(Alert alert, User user) {
    alertID = alert.getId();
    userID = user.getId();
    username = user.getUsername();
    email = user.getEmail();
    rate = alert.getRateValue();
    currency = alert.getCurrency();
    type = alert.getType();
  }
}

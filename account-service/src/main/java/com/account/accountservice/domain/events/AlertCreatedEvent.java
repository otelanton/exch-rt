package com.account.accountservice.domain.events;

import com.account.accountservice.domain.Alert;
import com.account.accountservice.domain.User;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
  @Column(nullable = false, precision = 9, scale = 4)
  private BigDecimal low;
  @Column(nullable = false, precision = 9, scale = 4)
  private BigDecimal high;
  private String currency;

  public AlertCreatedEvent(Alert alert, User user) {
    alertID = alert.getId();
    userID = user.getId();
    username = user.getUsername();
    email = user.getEmail();
    currency = alert.getCurrency();
    low = alert.getLow();
    high = alert.getHigh();
  }
}

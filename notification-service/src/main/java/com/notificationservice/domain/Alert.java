package com.notificationservice.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
public class Alert {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private long alertID;
  private long userID;
  private String username;
  private String email;
  @Column(nullable = false, precision = 9, scale = 4)
  private BigDecimal low;
  @Column(nullable = false, precision = 9, scale = 4)
  private BigDecimal high;
  private String currency;
  private String type;

  public Alert(long alertID, long userID, String username, String email, BigDecimal low, BigDecimal high, String currency, String type) {
    this.alertID = alertID;
    this.userID = userID;
    this.username = username;
    this.email = email;
    this.low = low;
    this.high = high;
    this.currency = currency;
    this.type = type;
  }
}

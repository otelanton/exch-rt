package com.account.accountservice.domain;

import com.account.accountservice.util.Views;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class Alert {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView(Views.UserGet.class)
  private long id;
  @ManyToOne
  @JsonBackReference
  private User user;
  @JsonView(Views.UserGet.class)
  private String currency;
  @Column(nullable = false, precision = 9, scale = 4)
  @JsonView(Views.UserGet.class)
  private BigDecimal rateValue;
  @JsonView(Views.UserGet.class)
  private SubscriptionType type;

  public Alert(User user, String currency, BigDecimal rate, SubscriptionType type) {
    this.user = user;
    this.currency = currency;
    this.rateValue = rate;
    this.type = type;
  }
}

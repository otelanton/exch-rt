package com.account.accountservice.domain;

import com.account.accountservice.util.Views;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Alert {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView(Views.UserGet.class)
  private long id;
  @ManyToOne
  @JsonBackReference
  @ToString.Exclude
  private User user;
  @JsonView(Views.UserGet.class)
  private String currency;
  @Column(nullable = false, precision = 9, scale = 4)
  @JsonView(Views.UserGet.class)
  private BigDecimal low;
  @Column(nullable = false, precision = 9, scale = 4)
  @JsonView(Views.UserGet.class)
  private BigDecimal high;

  public Alert(User user, String currency, BigDecimal low, BigDecimal high) {
    this.user = user;
    this.currency = currency;
    this.low = low;
    this.high = high;
  }
}

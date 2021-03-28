package com.notificationservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@ToString
@Getter
@Setter
public class Alert {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private long alertID;
  private long userID;
  private String username;
  private String email;
  @Column(nullable = false, precision = 9, scale = 4)
  private BigDecimal rate;
  private String currency;
  private String type;
}

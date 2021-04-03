package com.notificationservice.domain.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@ToString
public class NewRateCreatedEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String currency;
  private BigDecimal rate;
}

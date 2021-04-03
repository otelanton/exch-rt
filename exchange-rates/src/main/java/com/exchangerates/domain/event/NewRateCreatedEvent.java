package com.exchangerates.domain.event;

import java.math.BigDecimal;

public class NewRateCreatedEvent {

  private String currency;
  private BigDecimal rate;

  public NewRateCreatedEvent(String currency, BigDecimal rate) {
    this.currency = currency;
    this.rate = rate;
  }

  public String getCurrency() {
    return currency;
  }

  public BigDecimal getRate() {
    return rate;
  }
}

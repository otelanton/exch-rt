package com.exchangerates.initializer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
class CurrencyInitializer {

  private Creator currencyCreator;

  @Autowired
  public CurrencyInitializer(@Qualifier("currency") Creator currencyCreator) {
    this.currencyCreator = currencyCreator;
  }

  @PostConstruct
  private void init() {
    CreationExecutor.execute(currencyCreator, LocalDate.now());
  }
}
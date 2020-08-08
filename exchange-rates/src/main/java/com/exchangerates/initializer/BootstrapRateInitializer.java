package com.exchangerates.initializer;

import java.time.LocalDate;
// import java.time.Month;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class BootstrapRateInitializer {
  
  private final Creator ratesCreator;

  @Autowired
  public BootstrapRateInitializer(@Qualifier("initial") Creator ratesCreator) {
    this.ratesCreator = ratesCreator;
  }

  @EventListener(ApplicationReadyEvent.class)
  private void init(){
    parseAndPersistInitialRates();
  }

  private void parseAndPersistInitialRates(){
    LocalDate end = LocalDate.now();
    // LocalDate start = end.minusMonths(6);
    LocalDate start = end.minusDays(8);
    for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)){
      CreationExecutor.execute(ratesCreator ,date);
    }
  }

}


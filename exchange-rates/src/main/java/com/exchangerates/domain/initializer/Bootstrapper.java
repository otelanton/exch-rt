package com.exchangerates.domain.initializer;

import com.exchangerates.domain.initializer.creators.Creator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("prod")
class Bootstrapper {

  private Creator currencyCreator;
  private Creator ratesCreator;

  @Autowired
  public Bootstrapper(@Qualifier("currency") Creator currencyCreator, @Qualifier("initial") Creator ratesCreator) {
    this.currencyCreator = currencyCreator;
    this.ratesCreator = ratesCreator;
  }

  @EventListener(ApplicationReadyEvent.class)
  private void bootstrap() {
    currencyBootstrap();
    rateBootstrap();
  }

  private void currencyBootstrap(){
    CreationExecutor.execute(currencyCreator, LocalDate.now());
  }

  private void rateBootstrap(){
    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.minusMonths(6);

    while(!startDate.isAfter(endDate)){
      CreationExecutor.execute(ratesCreator, startDate);
      startDate = startDate.plusDays(1);
    }
  }
}
package com.exchangerates.domain.initializer;

import com.exchangerates.domain.initializer.creators.Creator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@Profile("dev")
public class BootstrapperDev {

  private Creator currencyCreator;
  private Creator ratesCreator;

  @Autowired
  public BootstrapperDev(@Qualifier("currency") Creator currencyCreator, @Qualifier("initial") Creator ratesCreator) {
    this.currencyCreator = currencyCreator;
    this.ratesCreator = ratesCreator;
  }

  @EventListener(ApplicationReadyEvent.class)
  private void bootstrap() {
    currencyBootstrap();
    rateBootstrap();
  }

  @Transactional
  private void currencyBootstrap(){
    currencyCreator.create(LocalDate.now());
  }

  @Transactional
  private void rateBootstrap(){
    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.minusDays(8);

    while(!startDate.isAfter(endDate)){
      ratesCreator.create(startDate);
      startDate = startDate.plusDays(1);
    }
  }
}

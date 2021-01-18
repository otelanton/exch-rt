package com.exchangerates.domain.initializer;

import com.exchangerates.domain.initializer.creators.Creator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@EnableScheduling
@Component
class RatesCreationScheduler {
  private final Creator ratesCreator;

  @Autowired
  public RatesCreationScheduler(@Qualifier("scheduled") Creator ratesCreator) {
    this.ratesCreator = ratesCreator;
  }

  @Scheduled(cron = "${schedule.cron}")
  @Transactional
  public void run() {
    ratesCreator.create(LocalDate.now());
  }
}
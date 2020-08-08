package com.exchangerates.initializer;

import com.exchangerates.initializer.Creator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.beans.factory.annotation.Autowired;

@EnableScheduling
@Component
class ScheduledInsertNewRate {

  private final Creator ratesCreator;

  @Autowired
  public ScheduledInsertNewRate(@Qualifier("scheduled") Creator ratesCreator) {
    this.ratesCreator = ratesCreator;
  }

  // @Scheduled(cron = "0 5 0 * * *")
  @Scheduled(fixedRate = 50000, initialDelay = 50000)
  @Transactional
  public void execute() {
    CreationExecutor.execute(ratesCreator, LocalDate.now());
  }
}
package com.exchangerates.domain.initializer.creators.rate;

import com.exchangerates.domain.Rate;
import com.exchangerates.domain.dao.DataAccessObject;
import com.exchangerates.domain.event.NewRateCreatedEvent;
import com.exchangerates.domain.initializer.CreationExecutor;
import com.exchangerates.domain.initializer.creators.Creator;
import com.exchangerates.domain.initializer.factory.RateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("initial")
class InitialRatesCreator implements Creator{

  private DataAccessObject dataAccessObject;
  private RateFactory factory;
  private KafkaTemplate<Long, List<NewRateCreatedEvent>> kafkaTemplate;

  @Autowired
  public InitialRatesCreator(DataAccessObject dataAccessObject, RateFactory factory, KafkaTemplate<Long, List<NewRateCreatedEvent>> kafkaTemplate) {
    this.dataAccessObject = dataAccessObject;
    this.factory = factory;
    this.kafkaTemplate = kafkaTemplate;
  }

  public List<Rate> create(LocalDate date) {
    List<Rate> rates = CreationExecutor.execute(date, factory, Rate.class);
    dataAccessObject.addAllRates(rates);
    List<NewRateCreatedEvent> events = rates.stream()
        .map(rate -> new NewRateCreatedEvent(rate.getCurrency().getCharCode(), rate.getValue())).collect(Collectors.toList());
    kafkaTemplate.send("rates", events);
    log.info(String.format("Posted %s event%n", NewRateCreatedEvent.class.getSimpleName()));
    return rates;
  }
}

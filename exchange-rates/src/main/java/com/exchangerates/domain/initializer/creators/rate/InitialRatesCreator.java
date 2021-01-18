package com.exchangerates.domain.initializer.creators.rate;

import com.exchangerates.domain.Rate;
import com.exchangerates.domain.dao.DataAccessObject;
import com.exchangerates.domain.initializer.CreationExecutor;
import com.exchangerates.domain.initializer.creators.Creator;
import com.exchangerates.domain.initializer.factory.RateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Qualifier("initial")
class InitialRatesCreator implements Creator{

  private DataAccessObject dataAccessObject;
  private RateFactory factory;

  @Autowired
  public InitialRatesCreator(DataAccessObject dataAccessObject, RateFactory factory) {
    this.dataAccessObject = dataAccessObject;
    this.factory = factory;
  }

  public List<Rate> create(LocalDate date) {
    List<Rate> rates = CreationExecutor.execute(date, factory, Rate.class);
    dataAccessObject.addAllRates(rates);
    return rates;
  }
}

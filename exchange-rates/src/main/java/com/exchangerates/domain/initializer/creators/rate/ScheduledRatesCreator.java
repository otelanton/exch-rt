package com.exchangerates.domain.initializer.creators.rate;

import com.exchangerates.domain.Currency;
import com.exchangerates.domain.Rate;
import com.exchangerates.domain.dao.DataAccessObject;
import com.exchangerates.domain.initializer.creators.Creator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Qualifier("scheduled")
class ScheduledRatesCreator implements Creator {
  private DataAccessObject dataAccessObject;
  private Creator initialRatesCreator;

  public ScheduledRatesCreator(DataAccessObject dataAccessObject, @Qualifier("initial") Creator initialRatesCreator) {
    this.dataAccessObject = dataAccessObject;
    this.initialRatesCreator = initialRatesCreator;
  }

  @Override
  public List<Rate> create(LocalDate date) {
    List<Rate> rateList  = initialRatesCreator.create(date);
    rateList.forEach((Rate r) -> removeOldestRate(r.getCurrency()));
    return rateList;
  }

  private void removeOldestRate(Currency currency){
    dataAccessObject.deleteCurrencyFirstRate(currency.getId());
  }
}

package com.exchangerates.domain.initializer.creators.rate;

import com.exchangerates.domain.dao.DataAccessObject;
import com.exchangerates.domain.Rate;
import com.exchangerates.domain.initializer.creators.Creator;
import com.exchangerates.domain.initializer.factory.EntitiesFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.time.LocalDate;

@Component
@Qualifier("initial")
class InitialRatesCreator implements Creator {

  private DataAccessObject dataAccessObject;
  private EntitiesFactory factory;

  @Autowired
  public InitialRatesCreator(DataAccessObject dataAccessObject, @Qualifier("rate_f") EntitiesFactory factory) {
    this.dataAccessObject = dataAccessObject;
    this.factory = factory;
  }

  @Override
  public void create(LocalDate date, Element xmlElement) {
    Rate newRate = getNewRate(date, xmlElement);
    dataAccessObject.save(newRate);
  }

  private Rate getNewRate(LocalDate date, Element xmlElement){
    return (Rate) factory.getInstance(date, xmlElement);
  }
}

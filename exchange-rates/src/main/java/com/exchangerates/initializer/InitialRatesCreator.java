package com.exchangerates.initializer;

import com.exchangerates.dao.DataAccessObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.time.LocalDate;

@Component
@Qualifier("initial")
public class InitialRatesCreator implements Creator {

  private DataAccessObject dataAccessObject;
  private RateFactory factory;

  @Autowired
  public InitialRatesCreator(DataAccessObject dataAccessObject, RateFactory factory) {
    this.dataAccessObject = dataAccessObject;
    this.factory = factory;
  }

  @Override
  public void create(LocalDate date, Element xmlElement) {
    dataAccessObject.save(factory.createNewRate(date, xmlElement));
  }
}

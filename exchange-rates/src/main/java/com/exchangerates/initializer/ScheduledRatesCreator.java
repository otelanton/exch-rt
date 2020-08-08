package com.exchangerates.initializer;

import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.entities.Currency;
import com.exchangerates.parse.ParseExchangeRates;
import com.exchangerates.parse.ParseExchangeRates.Tags;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.time.LocalDate;

@Component
@Qualifier("scheduled")
public class ScheduledRatesCreator implements Creator {
  private DataAccessObject dataAccessObject;
  private ParseExchangeRates parser;
  private InitialRatesCreator initialRatesCreator;

  public ScheduledRatesCreator(DataAccessObject dataAccessObject, InitialRatesCreator initialRatesCreator, ParseExchangeRates parser) {
    this.dataAccessObject = dataAccessObject;
    this.initialRatesCreator = initialRatesCreator;
    this.parser = parser;
  }

  @Override
  public void create(LocalDate date, Element xmlElement) {
    removeOldestRate(xmlElement);
    initialRatesCreator.create(date, xmlElement);
  }

  private void removeOldestRate(Element xmlElement){
    getCurrency(xmlElement).removeRate(0);
  }

  private Currency getCurrency(Element xmlElement){
    return dataAccessObject.getCurrencyByCharCode(parser.getTextFromXmlElement(Tags.CHARCODE, xmlElement));
  }
}

package com.exchangerates.initializer;

import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.domain.Currency;
import com.exchangerates.parse.ExchangeRatesParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.time.LocalDate;

@Component
@Qualifier("scheduled")
class ScheduledRatesCreator implements Creator {
  private DataAccessObject dataAccessObject;
  private ExchangeRatesParser parser;
  private InitialRatesCreator initialRatesCreator;
  private CurrencyMap currencyMap;

  public ScheduledRatesCreator(DataAccessObject dataAccessObject, InitialRatesCreator initialRatesCreator, ExchangeRatesParser parser, CurrencyMap currencyMap) {
    this.dataAccessObject = dataAccessObject;
    this.initialRatesCreator = initialRatesCreator;
    this.parser = parser;
    this.currencyMap = currencyMap;
  }

  @Override
  public void create(LocalDate date, Element xmlElement) {
    removeOldestRate(xmlElement);
    initialRatesCreator.create(date, xmlElement);
  }

  private void removeOldestRate(Element xmlElement){
    int currencyId = getCurrency(xmlElement).getId();
    dataAccessObject.deleteCurrencyFirstRate(currencyId);
  }

  private Currency getCurrency(Element xmlElement){
    String charCode = getCharCode(xmlElement);
    return currencyMap.getCurrency(charCode);
  }

  private String getCharCode(Element xmlElement){
    return parser.parseCharCode(xmlElement);
  }
}

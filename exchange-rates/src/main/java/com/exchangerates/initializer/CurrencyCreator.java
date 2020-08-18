package com.exchangerates.initializer;

import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.entities.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.time.LocalDate;

@Component
@Qualifier("currency")
class CurrencyCreator implements Creator {

  private DataAccessObject dataAccessObject;
  private CurrencyFactory currencyFactory;

  @Autowired
  public CurrencyCreator(DataAccessObject dataAccessObject, CurrencyFactory currencyFactory) {
    this.dataAccessObject = dataAccessObject;
    this.currencyFactory = currencyFactory;
  }

  public void create(LocalDate date, Element xmlElement){
    Currency newCurrency = getNewCurrency(xmlElement);
    dataAccessObject.save(newCurrency);
  }

  private Currency getNewCurrency(Element xmlElement) {
    return currencyFactory.createNewCurrency(xmlElement);
  }
}

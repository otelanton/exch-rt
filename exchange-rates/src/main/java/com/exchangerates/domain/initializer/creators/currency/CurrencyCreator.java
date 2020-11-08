package com.exchangerates.domain.initializer.creators.currency;

import com.exchangerates.domain.dao.DataAccessObject;
import com.exchangerates.domain.Currency;
import com.exchangerates.domain.initializer.creators.Creator;
import com.exchangerates.domain.initializer.factory.EntitiesFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.time.LocalDate;

@Component
@Qualifier("currency")
class CurrencyCreator implements Creator {

  private DataAccessObject dataAccessObject;
  private EntitiesFactory currencyFactory;

  @Autowired
  public CurrencyCreator(DataAccessObject dataAccessObject, @Qualifier("currency_f") EntitiesFactory currencyFactory) {
    this.dataAccessObject = dataAccessObject;
    this.currencyFactory = currencyFactory;
  }

  public void create(LocalDate date, Element xmlElement){
    Currency newCurrency = getNewCurrency(xmlElement);
    dataAccessObject.save(newCurrency);
  }

  private Currency getNewCurrency(Element xmlElement) {
    return (Currency) currencyFactory.getInstance(LocalDate.now(), xmlElement);
  }
}

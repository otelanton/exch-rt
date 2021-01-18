package com.exchangerates.domain.initializer.creators.currency;

import com.exchangerates.domain.Currency;
import com.exchangerates.domain.dao.DataAccessObject;
import com.exchangerates.domain.initializer.CreationExecutor;
import com.exchangerates.domain.initializer.creators.Creator;
import com.exchangerates.domain.initializer.factory.CurrencyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Qualifier("currency")
public class CurrencyCreator implements Creator {

  private DataAccessObject dataAccessObject;
  private CurrencyFactory currencyFactory;

  @Autowired
  public CurrencyCreator(DataAccessObject dataAccessObject, CurrencyFactory currencyFactory) {
    this.dataAccessObject = dataAccessObject;
    this.currencyFactory = currencyFactory;
  }

  public List<Currency> create(LocalDate date){
    List<Currency> currencies = CreationExecutor.execute(date, currencyFactory, Currency.class);
    dataAccessObject.addAllCurrencies(currencies);

    return currencies;
  }

}

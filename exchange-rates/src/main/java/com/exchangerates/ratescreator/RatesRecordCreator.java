package com.exchangerates.ratescreator;

import java.time.LocalDate;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;

import org.springframework.stereotype.Component;

@Component
public class RatesRecordCreator {

  public Rates createNewRatesTableRecord(float rate, LocalDate date, Currency currencyFK){
    return new Rates(rate, date, currencyFK);
  }

  public Currency createNewRatesTableRecord(int currencyCode, String charCode, int nominal, String currencyName) {
    return new Currency(currencyCode, charCode, nominal, currencyName);
  }
}
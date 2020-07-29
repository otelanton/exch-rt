package com.exchangerates.ratescreator;

import java.time.LocalDate;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;

import org.springframework.stereotype.Component;

@Component
public class TabelRecordCreator {

  public Rates createNewTableRecord(float value, LocalDate date, Currency currencyFK, float difference){
    Rates rate = new Rates(value, date, currencyFK, difference);
    currencyFK.addRate(rate);
    return rate;
  }

  public Currency createNewTableRecord(int currencyCode, String charCode, int nominal, String currencyName) {
    return new Currency(currencyCode, charCode, nominal, currencyName);
  }
}
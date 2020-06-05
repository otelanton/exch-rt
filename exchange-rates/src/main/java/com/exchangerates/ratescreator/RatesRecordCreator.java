package com.exchangerates.ratescreator;

import java.time.LocalDate;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;

import org.springframework.stereotype.Component;

@Component
public class RatesRecordCreator {

  public Rates createNewRatesTableRecord(float rate, LocalDate date, Currency currencyFK, int nominal){
    Rates ratesRecord = new Rates();
    ratesRecord.setCurrency(currencyFK);
    ratesRecord.setDate(date);
    ratesRecord.setRate(rate);
    // ratesRecord.setNominal(nominal);
    currencyFK.addRate(ratesRecord);
    return ratesRecord;
  }
}
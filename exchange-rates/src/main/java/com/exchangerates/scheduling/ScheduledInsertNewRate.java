package com.exchangerates.scheduling;

import com.exchangerates.RateValueDifference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.exchangerates.parse.ParseExchangeRatesImpl;
import com.exchangerates.parse.ParseExchangeRates;
import com.exchangerates.ratescreator.TabelRecordCreator;

import java.time.LocalDate;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.exchangerates.cache.InternalCache;
import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.dao.DataAccessObjectImpl;

@EnableScheduling
@Component
public class ScheduledInsertNewRate {

  private DataAccessObject dao;
  private TabelRecordCreator tableRecordCreator;
  private ParseExchangeRates parser;
  private InternalCache cache;

  // @Scheduled(cron = "0 5 0 * * *")
  @Scheduled(fixedRate = 50000, initialDelay = 50000)
  @Transactional
  public void execute() {
    List<Currency> listOfCurrencies = dao.getAllCurrencies();
    for (Currency currency : listOfCurrencies) {

      //remove the oldest rate
      currency.removeRate(0);

      Rate newRate = create(currency);
      dao.save(newRate);
      cache.getExchangeRates(currency.getCharCode());
    }
  }

  private Rate create(Currency currency){
    float value = parser.getRate(currency.getCharCode());
    float diff = RateValueDifference.getDifferenceBetweenRates(currency.getId(), value);
    return tableRecordCreator.createNewTableRecord(value, LocalDate.now(), currency, diff);
  }

  /*
   * Setter methods for autowiring dependencies 
  */ 

  @Autowired
  public void setCache(InternalCache cache){
    this.cache = cache;
  }

  @Autowired
  public void setDataAccessObject(DataAccessObjectImpl dao){
    this.dao = dao;
  }

  @Autowired
  public void setRatesRecordCreator(TabelRecordCreator tableRecordCreator){
    this.tableRecordCreator = tableRecordCreator;
  }

  @Autowired
  public void setParser(ParseExchangeRatesImpl parser){
    this.parser = parser;
  }

}
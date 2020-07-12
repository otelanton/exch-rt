package com.exchangerates.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.exchangerates.parse.ParseExchangeRatesImpl;
import com.exchangerates.parse.ParseExchangeRates;
import com.exchangerates.ratescreator.TabelRecordCreator;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.dao.DataAccessObjectImpl;

@EnableScheduling
@Component
public class ScheduledInsertNewRate {

  private static final Logger LOG = LoggerFactory.getLogger(ScheduledInsertNewRate.class);

  private DataAccessObject dao;
  private TabelRecordCreator tableRecordCreator;
  private ParseExchangeRates parser;

  // @Scheduled(cron = "0 5 0 * * *")
  @Scheduled(fixedRate = 50000, initialDelay = 50000)
  @Transactional
  public void execute() {
    List<Currency> listOfCurrencies = dao.getAllCurrencies();
    for (Currency currency : listOfCurrencies) {
      currency.removeRate(0);
      LOG.info("\n{}", currency.toString());
      Rates newRate = create(currency);
      save(newRate);
    }
  }

  private Rates create(Currency currency){
    float value = parser.getRate(currency.getCharCode());
    return tableRecordCreator.createNewTableRecord(value, LocalDate.now(), currency);
  }

  private void save(Rates rate){
    dao.save(rate);
  }

  /*
   * Setter methods for autowiring dependencies 
  */ 

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
package com.exchangerates.service;

import java.time.LocalDate;
import java.util.List;

import com.exchangerates.entities.Currency;
import com.exchangerates.parse.ParseExchangeRates;
import com.exchangerates.parse.ParseExchangeRatesInterface;
import com.exchangerates.ratescreator.RatesRecordCreator;
import com.exchangerates.repositories.CurrencyRepository;
import com.exchangerates.repositories.RatesRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class RatesService {

  private static final Logger LOG = LoggerFactory.getLogger(RatesService.class);
  private RatesRepository ratesRepository;
  private RatesRecordCreator ratesRecordCreator;
  private ParseExchangeRatesInterface parser;
  private InternalCache cache;

  // @Scheduled(cron = "0 5 0 * * *")
  // @Scheduled(fixedDelay = "50000")
  public void build() {
    List<Currency> listOfCurrencies = cache.getListOfAllCurrencies();
    for (Currency currency : listOfCurrencies) {
      String charCode = currency.getCharCode();
      float currencyRate = parser.getRate(charCode);
      ratesRepository.save(
        ratesRecordCreator.createNewRatesTableRecord(currencyRate, LocalDate.now(), currency)
        );
    }
  }

  public Currency get(String charCode) {
    return cache.getCurrency(charCode);
  }

  /*
   * Setter methods for autowiring dependencies 
  */ 

  @Autowired
  public void setRatesRepository(RatesRepository ratesRepository){
    this.ratesRepository = ratesRepository;
  }

  @Autowired
  public void setRatesRecordCreator(RatesRecordCreator ratesRecordCreator){
    this.ratesRecordCreator = ratesRecordCreator;
  }

  @Autowired
  public void setParser(ParseExchangeRates parser){
    this.parser = parser;
  }

  @Autowired
  public void setInternalCache(InternalCache cache){
    this.cache = cache;
  }


  /**
   * Spring caching feature works over AOP proxies, thus internal calls to cached methods don't work. That's why
   * this internal bean is created: it "proxifies" build() and get() methods
   * to real AOP proxified cacheable bean methods getListOfAllCurrencies and getCurrency.
   */

  @EnableCaching
  static class InternalCache {
    private CurrencyRepository currencyRepository;

    @Cacheable(unless = "#result == null", value = "currencies")
    public List<Currency> getListOfAllCurrencies(){
      LOG.info("\n============MISS============");
      return currencyRepository.findAll();
    }
  
    @Cacheable(unless = "#result == null", value = "rates")
    public Currency getCurrency(String charCode){
      LOG.info("\n\tGetting rate by cahrcode: {}. Cache MISS", charCode);
      Currency fk = this.currencyRepository.findByCharCode(charCode);
      if(fk == null){
        LOG.error("\nCURRENCY WASN'T FOUND: " + charCode);
        return null;
      } 
      return fk;
    }

    @Autowired
    public void setCurrencyRepository(CurrencyRepository currencyRepository){
      this.currencyRepository = currencyRepository;
    }
  }
}
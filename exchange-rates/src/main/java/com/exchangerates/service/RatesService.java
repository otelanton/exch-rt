package com.exchangerates.service;

import java.time.LocalDate;
import java.util.List;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;
import com.exchangerates.parse.ParseExchangeRates;
import com.exchangerates.parse.ParseExchangeRatesInterface;
import com.exchangerates.ratescreator.RatesRecordCreator;
import com.exchangerates.repositories.CurrencyRepository;
import com.exchangerates.repositories.RatesRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
// import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@CacheConfig(cacheNames = "rates")
public class RatesService {

  private static final Logger LOG = LoggerFactory.getLogger(RatesService.class);
  private CurrencyRepository currencyRepository;
  private RatesRepository ratesRepository;
  private RatesRecordCreator ratesRecordCreator;
  private ParseExchangeRatesInterface parser = new ParseExchangeRates();

  @Scheduled(fixedDelay = 50000)
  public void gen(){
    Names[] abbreviations = Names.values();
    for(Names code : abbreviations){
      build(code.getRateAbbr());
    }
  }
  // @CachePut(key = "#charCode", unless = "#result == null")
  public void build(String charCode) {

    // Names[] abbreviations = Names.values();
    LOG.info("\n\tInvocation: build {}", charCode);
    // for (Names abbreviation : abbreviations) {
      // String charCode = abbreviation.getRateAbbr();
    Currency fk = this.currencyRepository.findByCharCode(charCode);

    float currencyRate = parser.getRate(charCode);
    this.ratesRepository
      .save(this.ratesRecordCreator.createNewRatesTableRecord(currencyRate, LocalDate.now(), fk));
    // }
  }

  @Cacheable(unless = "#result == null")
  public List<Rates> get(String charCode){
    LOG.info("\n\tGetting rate by cahrcode: {}\t Cache MISS", charCode);
    return this.currencyRepository.findByCharCode(charCode).getRates();
  }

  /*
   *
   * Setter methods for autowiring dependencies 
   *  
  */ 

  @Autowired
  public void setCurrencyRepository(CurrencyRepository currencyRepository){
    this.currencyRepository = currencyRepository;
  }

  @Autowired
  public void setRatesRepository(RatesRepository ratesRepository){
    this.ratesRepository = ratesRepository;
  }

  @Autowired
  public void setRatesRecordCreator(RatesRecordCreator ratesRecordCreator){
    this.ratesRecordCreator = ratesRecordCreator;
  }
}
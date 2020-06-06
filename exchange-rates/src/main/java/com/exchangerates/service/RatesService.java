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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class RatesService {

  private CurrencyRepository currencyRepository;
  private RatesRepository ratesRepository;
  private RatesRecordCreator ratesRecordCreator;
  private ParseExchangeRatesInterface parser = new ParseExchangeRates();

  @Scheduled(fixedDelay = 500000)
  public void build(){

    Names[] abbreviations = Names.values();

    for(Names abbreviation : abbreviations){
      String charCode = abbreviation.getRateAbbr();
      Currency fk = this.currencyRepository.findByCharCode(charCode);

      float currencyRate = parser.getRate(charCode);
      int nominal = parser.getNominal(charCode);
      this.ratesRepository.save(
        this.ratesRecordCreator
          .createNewRatesTableRecord(currencyRate, LocalDate.now(), fk, nominal)
      );
    }
  }

  public List<Rates> get(String charCode){
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
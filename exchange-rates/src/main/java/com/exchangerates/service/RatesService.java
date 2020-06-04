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
import org.springframework.stereotype.Service;

@Service
public class RatesService {

  private CurrencyRepository currencyRepository;
  private RatesRepository ratesRepository;
  private RatesRecordCreator ratesRecordCreator;
  private ParseExchangeRatesInterface parser = new ParseExchangeRates();

  public void build(){

    Names[] abbreviations = Names.values();

    for(Names abbreviation: abbreviations){
      String currencyNameAbbreviation = abbreviation.getRateAbbr();
      Currency fk = this.currencyRepository.findByAbbreviation(currencyNameAbbreviation);


      List<Rates> r = this.currencyRepository.findByAbbreviation("EUR")
        .getRates();
      for(Rates a: r){
        System.out.println("gfkjghfjkhgjkfakgFHGJHG G HRKH G" + " " + a.toString());
      }



      float currencyRate = parser.getRate(currencyNameAbbreviation);
      this.ratesRepository.save(
        this.ratesRecordCreator
          .createNewRatesTableRecord(currencyRate, LocalDate.now(), fk)
      );
    }
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
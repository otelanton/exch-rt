package com.exchangerates.service;

import java.time.LocalDate;

import com.exchangerates.entities.Currency;
import com.exchangerates.factory.CurrencyFactory;
import com.exchangerates.parse.ParseExchangeRates;
import com.exchangerates.parse.ParseExchangeRatesInterface;
import com.exchangerates.repositories.CurrencyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/a")
public class RatesService {

  @Autowired
  private CurrencyFactory factory;
  @Autowired
  private CurrencyRepository repository;
  private ParseExchangeRatesInterface parser = new ParseExchangeRates();

  @RequestMapping(value="/b", method=RequestMethod.GET)
  public void build(){

    Rates[] abbreviations = Rates.values();
    for(Rates abbreviation: abbreviations){
      String abbr = abbreviation.getRateAbbr();
      Currency fk = this.repository.findByAbbreviation(abbr);
      factory.get(abbr, parser.getRate(abbr), LocalDate.now(), fk);
    }
  }
}
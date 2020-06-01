package com.exchangerates.factory;

import java.time.LocalDate;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.EUR;
import com.exchangerates.entities.USD;
// import com.exchangerates.factory.create.USDCreate;
import com.exchangerates.repositories.USDRepository;
import com.exchangerates.repositories.EURRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrencyFactory {
  
  @Autowired
  private USDRepository ur;
  @Autowired
  private EURRepository er;

  public void get(String abbr, float rate, LocalDate date, Currency currency){
    switch(abbr){
      case "USD":
        USD usd = new USD();
        usd.setCurrency(currency);
        usd.setDate(date);
        usd.setRate(rate);
        ur.save(usd);
        break;
      case "EUR":
        EUR eur = new EUR();
        eur.setCurrency(currency);
        eur.setDate(date);
        eur.setRate(rate);
        er.save(eur);
        
    }
  }

}
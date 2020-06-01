package com.exchangerates.factory.create;

import java.time.LocalDate;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.USD;
import com.exchangerates.repositories.USDRepository;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class USDCreate {
  
  @Autowired
  private static USDRepository repo;

  public static USD create(float rate, LocalDate date, Currency FK){
    USD usd = new USD();
    usd.setCurrency(FK);
    usd.setDate(date);
    usd.setRate(rate);
    return usd;
  }
  
  public static void save(USD usd){
    repo.save(usd);
  }

}
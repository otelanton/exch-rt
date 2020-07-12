package com.exchangerates.service;


import java.util.List;

import com.exchangerates.entities.DTO.CurrencyDTO;
import com.exchangerates.cache.InternalCache;
import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RatesService {

  private static final Logger LOG = LoggerFactory.getLogger(RatesService.class);

  private InternalCache cache;

  public List<Rates> getExchangeRates(String charCode){
    return cache.getExchangeRates(charCode);
  }

  public List<CurrencyDTO> getAllCurrencies(){
    return cache.getAllCurrenciesDTO();
  }

  public Currency getCurrency(String charCode) {
    return cache.getCurrencyByCharCode(charCode);
  }

  /*
   * Setter methods for autowiring dependencies 
  */ 

  @Autowired
  public void setInternalCache(InternalCache cache){
    this.cache = cache;
  }
}
package com.exchangerates.service;


import java.util.List;

import com.exchangerates.entities.DTO.CurrencyDTO;
import com.exchangerates.cache.InternalCache;
import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RatesService {

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

  public Page<Rates> getPagedRates(String code, Pageable page){
    return cache.getPagedRates(code, page);
  }

  public CurrencyDTO getDto(String charCode){
    return cache.getDto(charCode);
  }

  /*
   * Setter methods for autowiring dependencies 
  */ 

  @Autowired
  public void setInternalCache(InternalCache cache){
    this.cache = cache;
  }
}
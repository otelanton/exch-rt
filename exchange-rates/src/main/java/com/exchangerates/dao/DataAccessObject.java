package com.exchangerates.dao;

import java.util.List;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;
import com.exchangerates.entities.DTO.CurrencyDTO;

public interface DataAccessObject {
  public List<Rates> getAllExchangeRatesForCurrency(String charCode);
  public void save(Rates entity);
  public void save(Currency entity);
  public Currency getCurrencyByCharCode(String charCode);
  public List<Currency> getAllCurrencies();
  public List<CurrencyDTO> getAllCurrencyDto();
}
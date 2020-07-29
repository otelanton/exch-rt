package com.exchangerates.dao;

import java.util.List;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;
import com.exchangerates.entities.DTO.CurrencyDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DataAccessObject {
  public List<Rates> getAllExchangeRatesForCurrency(String charCode);
  public void save(Rates entity);
  public void save(Currency entity);
  public Currency getCurrencyByCharCode(String charCode);
  public List<Currency> getAllCurrencies();
  public List<CurrencyDTO> getAllCurrencyDto();
  public Rates getLatestForCurrencyRate(int id);
  public Page<Rates> getPagedRatesByCharCode(String charCode, Pageable page);
  public CurrencyDTO getCurrencyAsDto(String charCode);
}
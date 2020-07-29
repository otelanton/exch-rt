package com.exchangerates.dao;

import java.util.List;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rate;
import com.exchangerates.entities.DTO.CurrencyDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DataAccessObject {
  public List<Rate> getAllExchangeRatesForCurrency(String charCode);
  public void save(Rate entity);
  public void save(Currency entity);
  public Currency getCurrencyByCharCode(String charCode);
  public List<Currency> getAllCurrencies();
  public List<CurrencyDTO> getAllCurrencyDto();
  public Rate getLatestForCurrencyRate(int id);
  public Page<Rate> getPagedRatesByCharCode(String charCode, Pageable page);
  public CurrencyDTO getCurrencyAsDto(String charCode);
}
package com.exchangerates.dao;

import java.util.List;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rate;
import com.exchangerates.entities.DTO.CurrencyDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DataAccessObject {
  List<Rate> getAllExchangeRatesForCurrency(String charCode);
  void save(Rate entity);
  void save(Currency entity);
  Currency getCurrencyByCharCode(String charCode);
  List<Currency> getAllCurrencies();
  List<CurrencyDTO> getAllCurrencyDto();
  Rate getLatestForCurrencyRate(int id);
  Page<Rate> getPagedRatesByCharCode(String charCode, Pageable page);
  CurrencyDTO getCurrencyAsDto(String charCode);
}
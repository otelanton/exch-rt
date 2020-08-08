package com.exchangerates.dao;

import java.time.LocalDate;
import java.util.List;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rate;
import com.exchangerates.entities.dto.CurrencyDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DataAccessObject {
  List<Rate> getAllExchangeRatesByCurrencyId(int id);

  void save(Rate entity);

  void save(Currency entity);

  Currency getCurrencyByCharCode(String charCode);

  List<Currency> getAllCurrencies();

  List<CurrencyDTO> getAllCurrencyDto();

  Float getLatestRateByCurrencyId(int id);

  Page<Rate> getPagedRateByCharCode(String charCode, Pageable page);

  CurrencyDTO getCurrencyDtoByCharCode(String charCode);

  CurrencyDTO getCurrencyDtoById(int id);

  List<Rate> getRateInRange(LocalDate startDate, LocalDate endDate, int id);
}
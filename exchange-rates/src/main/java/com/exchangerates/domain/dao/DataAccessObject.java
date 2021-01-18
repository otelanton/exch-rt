package com.exchangerates.domain.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.exchangerates.domain.Average;
import com.exchangerates.domain.Currency;
import com.exchangerates.domain.Month;
import com.exchangerates.domain.Rate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DataAccessObject {
  List<Rate> getAllExchangeRatesByCurrencyId(int id);

  void save(Rate entity);

  void save(Currency entity);

  void addAllCurrencies(List<Currency> currencyList);

  void addAllRates(List<Rate> rateList);

  Currency getCurrencyByCharCode(String charCode);

  List<Currency> getAllCurrencies();

  BigDecimal getLatestRateByCurrencyId(int id);

  Page<Rate> getPagedRateByCharCode(String charCode, Pageable page);

  List<Rate> getRateInRange(LocalDate startDate, LocalDate endDate, int id);

  void deleteCurrencyFirstRate(int currencyId);

  BigDecimal getAverageValue(int currencyId, int month);

  Average getAverage(int currencyId, int month);

  List<Month> getAllMonths();

  void save(Average entity);
}
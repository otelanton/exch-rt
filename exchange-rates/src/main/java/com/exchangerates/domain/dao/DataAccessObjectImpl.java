package com.exchangerates.domain.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.exchangerates.domain.Average;
import com.exchangerates.domain.Currency;
import com.exchangerates.domain.Month;
import com.exchangerates.domain.Rate;
import com.exchangerates.domain.repositories.AverageRepository;
import com.exchangerates.domain.repositories.CurrencyRepository;
import com.exchangerates.domain.repositories.MonthRepository;
import com.exchangerates.domain.repositories.RateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DataAccessObjectImpl implements DataAccessObject {

  private RateRepository rateRepository;
  private CurrencyRepository currencyRepository;
  private AverageRepository averageRepository;
  private MonthRepository monthRepository;

  @Autowired
  DataAccessObjectImpl(RateRepository rateRepository, CurrencyRepository currencyRepository, AverageRepository averageRepository, MonthRepository monthRepository){
    this.rateRepository = rateRepository;
    this.currencyRepository = currencyRepository;
    this.averageRepository = averageRepository;
    this.monthRepository = monthRepository;
  }

  @Override
  public List<Rate> getAllExchangeRatesByCurrencyId(int id) {
    return rateRepository.findAllByCurrency(id);
  }

  @Override
  public void save(Rate entity) {
    rateRepository.save(entity);
  }

  @Override
  public void save(Currency entity) {
    currencyRepository.save(entity);
  }

  @Override
  public Currency getCurrencyByCharCode(String charCode) {
    return currencyRepository.findCurrencyByCharCode(charCode);
  }

  @Override
  public List<Currency> getAllCurrencies() {
    return currencyRepository.findAll();
  }

  @Override
  public BigDecimal getLatestRateByCurrencyId(int id){
    return rateRepository.findLatestByCurrencyId(id);
  }

  @Override
  public Page<Rate> getPagedRateByCharCode(String charCode, Pageable pageable){
    return rateRepository.findAllByCurrency_CharCode(charCode, pageable);
  }

  @Override
  public List<Rate> getRateInRange(LocalDate startDate, LocalDate endDate, int id) {
    return rateRepository.findInRange(startDate, endDate, id);
  }

  @Override
  public void deleteCurrencyFirstRate(int currencyId){
    rateRepository.deleteFirstRateByCurrencyId(currencyId);
  }

  @Override
  public BigDecimal getAverageValue(int currencyId, int month){
    return averageRepository.findValueByForeignKeyIdAndMonthId(currencyId, month);
  }

  @Override
  public Average getAverage(int currencyId, int month){
    return averageRepository.findAverageByForeignKeyIdAndMonthId(currencyId, month);
  }

  @Override
  public List<Month> getAllMonths(){
    return monthRepository.findAll();
  }

  @Override
  public void save(Average entity){
    averageRepository.save(entity);
  }
}
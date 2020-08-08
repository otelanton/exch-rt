package com.exchangerates.dao;

import java.time.LocalDate;
import java.util.List;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rate;
import com.exchangerates.entities.dto.CurrencyDTO;
import com.exchangerates.repositories.CurrencyRepository;
import com.exchangerates.repositories.RateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DataAccessObjectImpl implements DataAccessObject {

  private RateRepository rateRepository;
  private CurrencyRepository currencyRepository;

  @Override
  public List<Rate> getAllExchangeRatesByCurrencyId(int id) {
    return rateRepository.findAllByCurrencyId(id);
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
  public List<CurrencyDTO> getAllCurrencyDto(){
    return currencyRepository.findCurrencyDtoListBy();
  }

  @Override
  public Float getLatestRateByCurrencyId(int id){
    return rateRepository.findLatestByCurrencyId(id);
  }

  @Override
  public Page<Rate> getPagedRateByCharCode(String charCode, Pageable pageable){
    return rateRepository.findAllByCurrency_CharCode(charCode, pageable);
  }
  
  @Override
  public CurrencyDTO getCurrencyDtoByCharCode(String charCode){
    return currencyRepository.findCurrencyDtoByCharCode(charCode);
  }

  @Override
  public CurrencyDTO getCurrencyDtoById(int id){
    return currencyRepository.findCurrencyDtoById(id);
  }

  @Override
  public List<Rate> getRateInRange(LocalDate startDate, LocalDate endDate, int id) {
    return rateRepository.findInRange(startDate, endDate, id);
  }

  /*
   * Setter methods for autowiring dependencies 
  */ 

  @Autowired
  public void setRateRepository(RateRepository rateRepository) {
    this.rateRepository = rateRepository;
  }

  @Autowired
  public void setCurrencyRepository(CurrencyRepository currencyRepository) {
    this.currencyRepository = currencyRepository;
  }
  
}
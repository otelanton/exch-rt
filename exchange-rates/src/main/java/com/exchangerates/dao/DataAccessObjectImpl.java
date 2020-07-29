package com.exchangerates.dao;

import java.util.List;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rate;
import com.exchangerates.entities.DTO.CurrencyDTO;
import com.exchangerates.repositories.CurrencyRepository;
import com.exchangerates.repositories.RateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DataAccessObjectImpl implements DataAccessObject {

  private RateRepository ratesRepository;
  private CurrencyRepository currencyRepository;

  @Override
  public List<Rate> getAllExchangeRatesForCurrency(String charCode) {
    return ratesRepository.findAllByCurrency_CharCode(charCode);
  }

  @Override
  public void save(Rate entity) {
    ratesRepository.save(entity);
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
    return currencyRepository.findBy();
  }

  @Override
  public Rate getLatestForCurrencyRate(int id){
    return ratesRepository.findTopByCurrencyIdOrderByIdDesc(id);
  }

  @Override
  public Page<Rate> getPagedRatesByCharCode(String charCode, Pageable pageable){
    return ratesRepository.findAllByCurrency_CharCode(charCode, pageable);
  }
  
  @Override
  public CurrencyDTO getCurrencyAsDto(String charCode){
    return currencyRepository.findByCharCode(charCode);
  }

  /*
   * Setter methods for autowiring dependencies 
  */ 

  @Autowired
  public void setRatesRepository(RateRepository ratesRepository) {
    this.ratesRepository = ratesRepository;
  }

  @Autowired
  public void setCurrencyRepository(CurrencyRepository currencyRepository) {
    this.currencyRepository = currencyRepository;
  }
  
}
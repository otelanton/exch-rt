package com.exchangerates.dao;

import java.util.List;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;
import com.exchangerates.entities.DTO.CurrencyDTO;
import com.exchangerates.repositories.CurrencyRepository;
import com.exchangerates.repositories.RatesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataAccessObjectImpl implements DataAccessObject {

  private RatesRepository ratesRepository;
  private CurrencyRepository currencyRepository;

  @Override
  public List<Rates> getAllExchangeRatesForCurrency(String charCode) {
    return ratesRepository.findAllByCurrency_CharCode(charCode);
  }

  @Override
  public void save(Rates entity) {
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

  @Autowired
  public void setRatesRepository(RatesRepository ratesRepository) {
    this.ratesRepository = ratesRepository;
  }

  @Autowired
  public void setCurrencyRepository(CurrencyRepository currencyRepository) {
    this.currencyRepository = currencyRepository;
  }
  
}
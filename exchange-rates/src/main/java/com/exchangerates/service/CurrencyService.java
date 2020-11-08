package com.exchangerates.service;

import com.exchangerates.domain.Currency;
import com.exchangerates.domain.exception.CurrencyNotFoundException;
import com.exchangerates.domain.initializer.CurrencyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService{

  private CurrencyModelAssembler currencyModelAssembler;
  private CurrencyMap currencyMap;

  @Autowired
  CurrencyService(CurrencyModelAssembler currencyModelAssembler, CurrencyMap currencyMap){
    this.currencyModelAssembler = currencyModelAssembler;
    this.currencyMap = currencyMap;
  }

  public EntityModel<Currency> getCurrency(String charCode){
    Currency currency = Optional.ofNullable(currencyMap.getCurrency(charCode.toUpperCase())).orElseThrow(() -> new CurrencyNotFoundException("Currency not found!", charCode));
    return currencyModelAssembler.toModel(currency, charCode);
  }

  public CollectionModel<EntityModel<Currency>> getAllCurrencies(){
    List<Currency> currencyList = currencyMap.getAllCurrencies();

    return currencyModelAssembler.toCollectionModel(currencyList);
  }

  int getCurrencyId(String charCode){
    return getCurrency(charCode).getContent().getId();
  }
}

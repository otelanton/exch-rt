package com.exchangerates.service;

import com.exchangerates.cache.InternalCache;
import com.exchangerates.entities.Currency;
import com.exchangerates.exception.CurrencyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService{

  private InternalCache cache;
  private CurrencyModelAssembler currencyModelAssembler;

  @Autowired
  CurrencyService(InternalCache cache, CurrencyModelAssembler currencyModelAssembler){
    this.cache = cache;
    this.currencyModelAssembler = currencyModelAssembler;
  }

  public EntityModel<Currency> getCurrency(String charCode){
    Currency currency = cache.getCurrency(charCode);

    if(currency == null){
      throw new CurrencyNotFoundException("Currency not found!", charCode);
    }

    return currencyModelAssembler.toModel(currency, charCode);
  }

  public CollectionModel<EntityModel<Currency>> getAllCurrencies(){
    List<Currency> currencyList = cache.getAllCurrencies();

    return currencyModelAssembler.toCollectionModel(currencyList);
  }

  int getCurrencyId(String charCode){
    return getCurrency(charCode).getContent().getId();
  }
}

package com.exchangerates.domain.initializer;

import com.exchangerates.domain.Currency;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public final class CurrencyMap {
  private final Map<String, Currency> CURRENCY_MAP = new HashMap<>();

  public void add(Currency entity){
    CURRENCY_MAP.put(entity.getCharCode(), entity);
  }

  public Map<String, Currency> getCurrencyMap(){
    return new HashMap<String, Currency>(CURRENCY_MAP);
  }

  public Currency getCurrency(String charCode){
    return CURRENCY_MAP.get(charCode);
  }

  public List<Currency> getAllCurrencies(){
    return new ArrayList<>(CURRENCY_MAP.values());
  }
}

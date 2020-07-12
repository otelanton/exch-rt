package com.exchangerates.controller;

import java.util.List;

import com.exchangerates.entities.DTO.CurrencyDTO;
import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;
import com.exchangerates.service.RatesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/exchange_rates")
public class Controller {

  private RatesService service;

  @RequestMapping(value = "/currencies", method = RequestMethod.GET)
  public List<CurrencyDTO> getAllCurrencies() {
    return service.getAllCurrencies();
  }  

  @RequestMapping(value = "/currency/{charCode}", method = RequestMethod.GET)
  public Currency getCurrency(@PathVariable String charCode) {
    return service.getCurrency(charCode);
  }

  @RequestMapping(value = "/{charCode}", method = RequestMethod.GET)
  public List<Rates> getExchangeRates(@PathVariable String charCode) {
    return service.getExchangeRates(charCode);
  }

  /*
   * Setter methods for autowiring dependencies 
  */ 

  @Autowired
  public void setService(RatesService service){
    this.service = service;
  }
}
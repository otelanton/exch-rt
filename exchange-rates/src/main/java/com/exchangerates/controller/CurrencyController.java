package com.exchangerates.controller;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rate;
import com.exchangerates.exception.CurrencyNotFoundException;
import com.exchangerates.exception.DateOutOfRangeException;
import com.exchangerates.exception.RateNotFoundException;
import com.exchangerates.service.CurrencyService;
import com.exchangerates.service.RatesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping(path = "/currencies")
public class CurrencyController {

  private CurrencyService service;

  @RequestMapping(value = "/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<EntityModel<Currency>> getCurrency(@PathVariable String charCode){
    EntityModel<Currency> currency = service.getCurrency(charCode);

    return new ResponseEntity<>(currency, HttpStatus.OK);
  }

  @RequestMapping(value = "/all", method = RequestMethod.GET)
  public ResponseEntity<CollectionModel<EntityModel<Currency>>> getAllCurrencies(){
    CollectionModel<EntityModel<Currency>> allCurrencies = service.getAllCurrencies();

    return new ResponseEntity<>(allCurrencies, HttpStatus.OK);
  }

  /*
   * Setter methods for autowiring dependencies 
  */ 

  @Autowired
  public void setService(CurrencyService service){
    this.service = service;
  }
}
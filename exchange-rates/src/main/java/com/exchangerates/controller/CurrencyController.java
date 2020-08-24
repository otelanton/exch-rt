package com.exchangerates.controller;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rate;
import com.exchangerates.exception.CurrencyNotFoundException;
import com.exchangerates.exception.DateOutOfRangeException;
import com.exchangerates.exception.RateNotFoundException;
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

  private RatesService service;
  private CurrencyModelAssembler currencyModelAssembler;

  @RequestMapping(value = "/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<EntityModel<Currency>> getCurrency(@PathVariable String charCode, Pageable pageable){
    Currency currency = getCurrencyDto(charCode);

    return new ResponseEntity<>( currencyModelAssembler.toModel(currency, charCode, pageable), HttpStatus.OK);
  }

  @RequestMapping(value = "/all", method = RequestMethod.GET)
  public ResponseEntity<CollectionModel<EntityModel<Currency>>> getAllCurrencies(Pageable pageable){
    List<Currency> allCurrencyDto = service.getAllCurrencies();
    return new ResponseEntity<>( currencyModelAssembler.toCollectionModel(allCurrencyDto, pageable), HttpStatus.OK);
  }

  private Currency getCurrencyDto(String charCode){
    Currency currency = service.getCurrencyDto(charCode);

    if(currency == null){
      throw new CurrencyNotFoundException("Currency wasn't found.", charCode);
    }

    return currency;
  }

  /*
   * Setter methods for autowiring dependencies 
  */ 

  @Autowired
  public void setService(RatesService service){
    this.service = service;
  }

  @Autowired
  public void setModelAssembler(CurrencyModelAssembler assm){
    this.currencyModelAssembler = assm;
  }

}
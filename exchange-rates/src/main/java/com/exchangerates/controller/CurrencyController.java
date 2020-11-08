package com.exchangerates.controller;

import com.exchangerates.domain.Currency;
import com.exchangerates.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping(path = "/currencies")
public class CurrencyController {

  private CurrencyService service;

  @Autowired
  CurrencyController(CurrencyService service){
    this.service = service;
  }

  @RequestMapping(value = "/single", method = RequestMethod.GET)
  public ResponseEntity<EntityModel<Currency>> getCurrency(@NotBlank @RequestParam String charCode){
    EntityModel<Currency> currency = service.getCurrency(charCode);

    return new ResponseEntity<>(currency, HttpStatus.OK);
  }

  @RequestMapping(value = "/all", method = RequestMethod.GET)
  public ResponseEntity<CollectionModel<EntityModel<Currency>>> getAllCurrencies(){
    CollectionModel<EntityModel<Currency>> allCurrencies = service.getAllCurrencies();

    return new ResponseEntity<>(allCurrencies, HttpStatus.OK);
  }
}
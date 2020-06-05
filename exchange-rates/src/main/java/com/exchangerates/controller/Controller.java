package com.exchangerates.controller;

import java.util.List;

import com.exchangerates.entities.Rates;
import com.exchangerates.service.RatesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/a")
public class Controller {

  private RatesService service;

  @RequestMapping(value="/b", method=RequestMethod.GET)
  public void createRecord(){
    service.build();
  }

  @RequestMapping(value="/c/{charCode}", method=RequestMethod.GET)
  public List<Rates> getRecords(@PathVariable String charCode){
    return service.get(charCode);
  }

  @Autowired
  public void setService(RatesService service){
    this.service = service;
  }
}
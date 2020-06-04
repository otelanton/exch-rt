package com.exchangerates.controller;

import com.exchangerates.service.RatesService;

import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  public void setService(RatesService service){
    this.service = service;
  }
}
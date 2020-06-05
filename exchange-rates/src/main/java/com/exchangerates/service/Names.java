package com.exchangerates.service;

public enum Names {
  USD("USD"),
  RUB("RUB"),
  UAH("UAH"),
  RON("RON"),
  EUR("EUR");

  private String rateAbbr;

  Names(String rateAbbr){
    this.rateAbbr = rateAbbr;
  }

  public String getRateAbbr(){
    return this.rateAbbr;
  }
}
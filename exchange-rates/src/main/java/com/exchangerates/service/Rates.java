package com.exchangerates.service;

public enum Rates {
  USD("USD"),
  EUR("EUR");

  private String rateAbbr;

  Rates(String rateAbbr){
    this.rateAbbr = rateAbbr;
  }

  public String getRateAbbr(){
    return this.rateAbbr;
  }
}
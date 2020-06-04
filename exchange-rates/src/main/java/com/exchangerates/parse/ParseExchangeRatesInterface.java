package com.exchangerates.parse;

public interface ParseExchangeRatesInterface {
  public float getRate(String charCode);
  public int getNominal(String charCode);
}
package com.exchangerates.parse;

import java.io.IOException;

import org.jsoup.Jsoup;

public class ParseExchangeRates implements ParseExchangeRatesInterface{
  
  private final String URL = "http://www.bnm.md/ru/content/obmennye-stavki";

  public ParseExchangeRates(){}

  public float getRate(String abbr){
    float rateValue = 0;
    try{
      rateValue = Float.parseFloat(
        Jsoup.connect(URL)
          .get()
          .select(queryRate(abbr))
          .last()
          .select("td.rate")
          .text()
        );
    }catch(IOException e){
      System.out.println(e);
    }

    return rateValue;
  }

  private String queryRate(String currencyAbbr) {
    return String.format(
      "div.rates-table > div.first > table.rates > tbody > tr:contains(%s)", currencyAbbr
      );
  }
}
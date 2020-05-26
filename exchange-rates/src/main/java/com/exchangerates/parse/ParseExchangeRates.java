package com.exchangerates.parse;

import java.io.IOException;

import org.jsoup.Jsoup;

public class ParseExchangeRates implements ParseExchangeRatesInterface{
  
  private final String URL = "http://www.bnm.md/ru/content/obmennye-stavki";

  public ParseExchangeRates(){}

  public float getRate() throws IOException {
    return Float.parseFloat(
      Jsoup.connect(URL)
        .get()
        .select(queryRate("USD"))
        .last()
        .select("td.rate")
        .text()
      );
  }

  private String queryRate(String currencyAbbr) {
    return String.format(
      "div.rates-table > div.first > table.rates > tbody > tr:contains(%s)", currencyAbbr
      );
  }
}
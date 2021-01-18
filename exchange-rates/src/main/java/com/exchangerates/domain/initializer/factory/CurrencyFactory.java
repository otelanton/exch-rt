package com.exchangerates.domain.initializer.factory;

import com.exchangerates.domain.Currency;
import com.exchangerates.domain.initializer.CurrencyMap;
import com.exchangerates.domain.parse.ExchangeRatesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.time.LocalDate;

@Qualifier("currency_f")
@Component
public class CurrencyFactory implements EntitiesFactory{
  private ExchangeRatesParser parser;
  private CurrencyMap currencyMap;

  @Autowired
  public CurrencyFactory(ExchangeRatesParser parser, CurrencyMap currencyMap){
    this.parser = parser;
    this.currencyMap = currencyMap;
  }

  @Override
  public Currency getInstance(LocalDate date, Element xmlElement){
    int nominal = parseNominal(xmlElement);
    int numCode = parseNumCode(xmlElement);
    String charCode = parseCharCode(xmlElement);
    String name = parseName(xmlElement);
    Currency currency = new Currency(numCode, charCode, nominal, name);
    currencyMap.add(currency);
    return currency;
  }

  private int parseNominal(Element xmlElement){
    return Integer.parseInt(parser.parseNominal(xmlElement));
  }

  private int parseNumCode(Element xmlElement){
    return Integer.parseInt(parser.parseNumCode(xmlElement));
  }

  private String parseCharCode(Element xmlElement){
    return parser.parseCharCode(xmlElement);
  }

  private String parseName(Element xmlElement){
    return parser.parseName(xmlElement);
  }
}

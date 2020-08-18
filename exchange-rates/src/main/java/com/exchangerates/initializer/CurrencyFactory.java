package com.exchangerates.initializer;

import com.exchangerates.entities.Currency;
import com.exchangerates.parse.ParseExchangeRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.time.LocalDate;

@Component
class CurrencyFactory {
  private ParseExchangeRates parser;

  @Autowired
  CurrencyFactory(ParseExchangeRates parser){
    this.parser = parser;
  }

  public Currency createNewCurrency(Element xmlElement){
    int nominal = parseNominal(xmlElement);
    int numCode = parseNumCode(xmlElement);
    String charCode = parseCharCode(xmlElement);
    String name = parseName(xmlElement);

    return new Currency(numCode, charCode, nominal, name);
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

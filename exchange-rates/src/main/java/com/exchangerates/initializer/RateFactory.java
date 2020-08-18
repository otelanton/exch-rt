package com.exchangerates.initializer;

import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rate;
import com.exchangerates.parse.ParseExchangeRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.time.LocalDate;

@Component
class RateFactory {

  private ParseExchangeRates parser;
  private DataAccessObject dao;

  @Autowired
  RateFactory(ParseExchangeRates parser, DataAccessObject dao){
    this.parser = parser;
    this.dao = dao;
  }

  public Rate createNewRate(LocalDate date, Element xmlElement){
    float newValue = getNewValue(xmlElement);
    Currency currency = getCurrency(xmlElement);
    float difference = getDifferenceBetweenRates(newValue, currency.getId());

    return new Rate(newValue, date, currency, difference);
  }

  private float getDifferenceBetweenRates(float newRateValue, int id){
    float difference = 0;

    Float latestRateValue = dao.getLatestRateByCurrencyId(id);
    if (latestRateValue != null){
      difference = RateValueDifference.calculateDifferenceBetweenRates(newRateValue, latestRateValue);
    }

    return difference;
  }

  private float getNewValue(Element xmlElement){
    return Float.parseFloat(parser.parseValue(xmlElement));
  }

  private Currency getCurrency(Element xmlElement){
    return dao.getCurrencyByCharCode(parser.parseCharCode(xmlElement));
  }
}

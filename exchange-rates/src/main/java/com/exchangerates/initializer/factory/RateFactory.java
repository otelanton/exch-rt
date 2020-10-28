package com.exchangerates.initializer.factory;

import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.domain.Currency;
import com.exchangerates.domain.Rate;
import com.exchangerates.initializer.CurrencyMap;
import com.exchangerates.parse.ExchangeRatesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.math.BigDecimal;
import java.time.LocalDate;

@Qualifier("rate_f")
@Component
class RateFactory implements EntitiesFactory {

  private ExchangeRatesParser parser;
  private DataAccessObject dao;
  private AverageFactory averageFactory;
  private CurrencyMap currencyMap;

  @Autowired
  RateFactory(ExchangeRatesParser parser, DataAccessObject dao, AverageFactory averageFactory, CurrencyMap currencyMap){
    this.parser = parser;
    this.dao = dao;
    this.averageFactory = averageFactory;
    this.currencyMap = currencyMap;
  }

  @Override
  public Rate getInstance(LocalDate date, Element xmlElement){
    BigDecimal newValue = getNewValue(xmlElement);
    Currency currency = getCurrency(xmlElement);
    BigDecimal difference = getDifferenceBetweenRates(newValue, currency.getId());
    averageFactory.create(currency.getCharCode(), newValue);
    return new Rate(newValue, date, currency, difference);
  }

  private BigDecimal getDifferenceBetweenRates(BigDecimal newRateValue, int id){
    BigDecimal difference = BigDecimal.ZERO;

    BigDecimal latestRateValue = dao.getLatestRateByCurrencyId(id);
    if (latestRateValue != null){
      difference = newRateValue.subtract(latestRateValue);
    }

    return difference;
  }

  private BigDecimal getNewValue(Element xmlElement){
    return new BigDecimal(parser.parseValue(xmlElement));
  }

  private Currency getCurrency(Element xmlElement){
    String charCode = parser.parseCharCode(xmlElement);
    return currencyMap.getCurrency(charCode);
  }
}

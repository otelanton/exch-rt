package com.exchangerates.initializer.factory;

import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.domain.Average;
import com.exchangerates.domain.Currency;
import com.exchangerates.domain.Month;
import com.exchangerates.initializer.CurrencyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AverageFactory {
  private Map<Integer, Month> monthMap;
  private CurrencyMap currencyMap;
  private DataAccessObject dataAccessObject;

  @Autowired
  public AverageFactory(CurrencyMap currencyMap, DataAccessObject dataAccessObject){
    this.dataAccessObject = dataAccessObject;
    this.monthMap = dataAccessObject.getAllMonths().stream().collect(Collectors.toMap(Month::getId, Function.identity()));
    this.currencyMap = currencyMap;
  }

  public void create(String code, BigDecimal rateValue){
    Currency currency = currencyMap.getCurrency(code);
    Month month = monthMap.get(LocalDate.now().getMonthValue());
    BigDecimal value = averageValue(currency.getId(), month.getId(), rateValue);
    persist(value, currency, month);
  }

  private void persist(BigDecimal value, Currency currency, Month month){
    Average average = dataAccessObject.getAverage(currency.getId(), month.getId());
    if(average == null){
      average = new Average(value, currency, month);
    } else{
      average.setValue(value);
    }
    dataAccessObject.save(average);
  }

  private BigDecimal averageValue(int currencyId, int monthId, BigDecimal rateValue){
    //get current average value
    BigDecimal average = dataAccessObject.getAverageValue(currencyId, monthId);
    //if doesn't exist then current rate value is the average itself
    if(average == null){
      return rateValue;
    }
    BigDecimal sum = average.add(rateValue);
    return sum.divide(new BigDecimal(2), 4, RoundingMode.HALF_UP);
  }
}

package com.exchangerates;

import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.dao.DataAccessObjectImpl;
import com.exchangerates.entities.Rates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


@Component
public class RateValueDifference {

  private static DataAccessObject dao;

  public static float getDifferenceBetweenRates(int foreignKeyCurrencyId, float newEntityRateValue){
    float differenceBetWeenRateValues = 0;

    Rates latestRateEntity = dao.getLatestForCurrencyRate(foreignKeyCurrencyId);

    if(latestRateEntity != null){
      float difference = newEntityRateValue - latestRateEntity.getRate();

      String formattedValueString = changeDecimalFormatSymbol().format(difference);

      differenceBetWeenRateValues = Float.parseFloat(formattedValueString);
    }

    return differenceBetWeenRateValues;
  }


  private static DecimalFormat changeDecimalFormatSymbol(){
    Locale currentLocale = Locale.getDefault();
    DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
    otherSymbols.setDecimalSeparator('.');
    return new DecimalFormat("#0.0000", otherSymbols);
  }

  @Autowired
  public void setDataAccessObject(DataAccessObjectImpl dao){
    RateValueDifference.dao = dao;
  }
}

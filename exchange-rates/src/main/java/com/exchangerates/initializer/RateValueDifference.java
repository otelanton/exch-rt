package com.exchangerates.initializer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

class RateValueDifference {

  public static float calculateDifferenceBetweenRates(float newEntityRateValue, float latestEntityRateValue) {
    if (newEntityRateValue <= 0 || latestEntityRateValue <= 0) {
      throw new IllegalArgumentException("Rate value must be greater than 0.");
    }

    float difference = newEntityRateValue - latestEntityRateValue;

    String formattedValueString = changeDecimalFormatSymbol().format(difference);

    return Float.parseFloat(formattedValueString);
  }

  private static DecimalFormat changeDecimalFormatSymbol() {

    final String PATTERN = "#0.0000";
    final char SEPARATOR = '.';

    Locale currentLocale = Locale.getDefault();
    DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
    otherSymbols.setDecimalSeparator(SEPARATOR);
    return new DecimalFormat(PATTERN, otherSymbols);
  }
}
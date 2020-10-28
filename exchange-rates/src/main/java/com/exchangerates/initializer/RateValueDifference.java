package com.exchangerates.initializer;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

class RateValueDifference {

  public static BigDecimal calculateDifferenceBetweenRates(BigDecimal newEntityRateValue, BigDecimal latestEntityRateValue) {
    if (newEntityRateValue.compareTo(BigDecimal.ZERO) <= 0 || latestEntityRateValue.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Rate value must be greater than 0.");
    }

    BigDecimal difference = newEntityRateValue.subtract(latestEntityRateValue);

    String formattedValueString = changeDecimalFormatSymbol().format(difference);

    return difference;
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
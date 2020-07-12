package com.exchangerates.parse;

import org.w3c.dom.Element;

public interface ParseExchangeRates {
  public float getRate(String charCode);
  public String getElementText(String tagName, Element element);
}
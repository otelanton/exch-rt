package com.exchangerates.domain.parse;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;

public interface ExchangeRatesParser {

  float getRate(String charCode);

  String parseNominal(Element xmlElement);

  String parseName(Element xmlElement);

  String parseValue(Element xmlElement);

  String parseCharCode(Element xmlElement);

  String parseNumCode(Element xmlElement);

  NodeList getXmlDocumentNodes(LocalDate date);
}
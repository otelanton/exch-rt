package com.exchangerates.initializer;

import java.time.LocalDate;
// import java.time.Month;

import javax.annotation.PostConstruct;

import com.exchangerates.RateValueDifference;
import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.dao.DataAccessObjectImpl;
import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rate;
import com.exchangerates.parse.ParseExchangeRates;
import com.exchangerates.parse.document.XMLDocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class InitialRates {
  
  private final ParseExchangeRates parser;
  private final XMLDocument document;
  private final DataAccessObject dao;

  @Autowired
  public InitialRates(ParseExchangeRates parser, XMLDocument document, DataAccessObjectImpl dao) {
    this.parser = parser;
    this.document = document;
    this.dao = dao;
  }

  @PostConstruct
  private void init(){
    parseAndPersistInitialRates();
  }

  private void parseAndPersistInitialRates(){
    LocalDate end = LocalDate.now();
    // LocalDate start = end.minusMonths(6);
    LocalDate start = end.minusDays(8);
    Document doc;
    for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)){
      doc = document.createDocument(date);
      NodeList list = getElementsList(doc);
//      for(int i = 0; i < list.getLength(); i++){
      for(int i = 0; i < 2; i++){
        if(list.item(i).getNodeType() == Node.ELEMENT_NODE){
          Element element = (Element) list.item(i);
          float newRateValue = getNewRateValue(element);
          Currency currencyAsForeignKey = parseCurrencyCharCodeAndGet(element);
          float difference = getDifferenceBetweenRates(newRateValue, currencyAsForeignKey.getId());
          Rate rate = new Rate(newRateValue, date, currencyAsForeignKey, difference);
          dao.save(rate);
        }
      }
    }
  }

  private float getDifferenceBetweenRates(float newRateValue, int id){
    float difference = 0;

    Float latestRateValue = dao.getLatestForCurrencyRate(id);
    if (latestRateValue != null){
      difference = RateValueDifference.getDifferenceBetweenRates(newRateValue, latestRateValue);
    }

    return difference;
  }

  private Currency parseCurrencyCharCodeAndGet(Element element){
    final String CHARCODE_TAGNAME = "CharCode";

    String parsedNodeCharCode = parser.getElementText(CHARCODE_TAGNAME, element);

    return dao.getCurrencyByCharCode(parsedNodeCharCode);
  }

  private float getNewRateValue(Element element){
    final String VALUE_TAGNAME = "Value";

    String parsedNodeRateValue = parser.getElementText(VALUE_TAGNAME, element);

    return Float.parseFloat(parsedNodeRateValue);
  }

  private NodeList getElementsList(Document document){
    document.getDocumentElement().normalize();

    return document.getElementsByTagName("Valute");
  }
}


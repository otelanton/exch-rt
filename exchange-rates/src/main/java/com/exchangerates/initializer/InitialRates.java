package com.exchangerates.initializer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Locale;
// import java.time.Month;

import javax.annotation.PostConstruct;

import com.exchangerates.RateValueDifference;
import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.dao.DataAccessObjectImpl;
import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;
import com.exchangerates.parse.ParseExchangeRates;
import com.exchangerates.parse.document.XMLDocument;
import com.exchangerates.ratescreator.TabelRecordCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class InitialRates {
  
  private ParseExchangeRates parser;
  private XMLDocument document;
  private TabelRecordCreator creator;
  private DataAccessObject dao;

  @Autowired
  public InitialRates(ParseExchangeRates parser, XMLDocument document, TabelRecordCreator creator, DataAccessObjectImpl dao) {
    this.parser = parser;
    this.document = document;
    this.creator = creator;
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
    Document doc = null;
    for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)){
      doc = document.createDocument(date);
      NodeList list = getElementsList(doc);
      for(int i = 0; i < list.getLength(); i++){
        if(list.item(i).getNodeType() == Node.ELEMENT_NODE){
          float newRateValue = getNewRateValue((Element) list.item(i));
          String parsedNodeCharCode = parser.getElementText("CharCode", (Element) list.item(i));
          Currency currencyAsForeignKey = dao.getCurrencyByCharCode(parsedNodeCharCode);
          float difference = RateValueDifference.getDifferenceBetweenRates(currencyAsForeignKey.getId(), newRateValue);
          Rates rate = creator.createNewTableRecord(newRateValue, date, currencyAsForeignKey, difference);
          dao.save(rate);
        }
      }
    }
  }

  private float getNewRateValue(Element element){
    String parsedNodeRateValue = parser.getElementText("Value", element);
    return Float.parseFloat(parsedNodeRateValue);
  }

  private NodeList getElementsList(Document document){
    document.getDocumentElement().normalize();
    return document.getElementsByTagName("Valute");
  }
}


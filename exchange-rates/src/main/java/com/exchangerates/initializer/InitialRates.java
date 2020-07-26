package com.exchangerates.initializer;

import java.time.LocalDate;
// import java.time.Month;

import javax.annotation.PostConstruct;

import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.dao.DataAccessObjectImpl;
import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;
import com.exchangerates.parse.ParseExchangeRates;
import com.exchangerates.parse.document.XMLDocument;
import com.exchangerates.ratescreator.TabelRecordCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    LocalDate start = end.minusDays(4);
    Document doc = null;
    for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)){
      doc = document.createDocument(date);
      NodeList list = getElementsList(doc);
      for(int i = 0; i < list.getLength(); i++){
        if(list.item(i).getNodeType() == Node.ELEMENT_NODE){
          String parsedNodeRateValue = parser.getElementText("Value", (Element) list.item(i));
          String parsedNodeCharCode = parser.getElementText("CharCode", (Element) list.item(i));
          Currency currencyAsForeightKey = dao.getCurrencyByCharCode(parsedNodeCharCode);
          Rates rate = creator.createNewTableRecord(Float.parseFloat(parsedNodeRateValue), date, currencyAsForeightKey);
          dao.save(rate);
        }
      }
    }
  }

  private NodeList getElementsList(Document document){
    document.getDocumentElement().normalize();
    return document.getElementsByTagName("Valute");
  }
}


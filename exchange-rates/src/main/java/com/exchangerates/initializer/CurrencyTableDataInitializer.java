package com.exchangerates.initializer;

import javax.annotation.PostConstruct;

import com.exchangerates.cache.InternalCache;
import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.dao.DataAccessObjectImpl;
import com.exchangerates.entities.Currency;
import com.exchangerates.parse.ParseExchangeRates;
import com.exchangerates.parse.ParseExchangeRatesImpl;
import com.exchangerates.parse.document.XMLDocument;
import com.exchangerates.ratescreator.TabelRecordCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

@Component
public class CurrencyTableDataInitializer {

  private final String CODE              = "NumCode";
  private final String CHAR_CODE         = "CharCode";
  private final String NOMINAL           = "Nominal";
  private final String NAME              = "Name";
  private final String ELEMENTS_TAG_NAME = "Valute";

  private Document document;
  private DataAccessObject dao;
  private ParseExchangeRates parser;
  private TabelRecordCreator tableRecordCreator;
  private InternalCache cache;

  @Autowired
  public CurrencyTableDataInitializer(XMLDocument document) {
    this.document = document.createDocument();
  }

  @PostConstruct
  private void init() {
    this.initializeData();
  }

  private void initializeData() {
    NodeList list = getElements();
    for(int i = 0; i < list.getLength(); i++){
      Node valuteNode = list.item(i);
      if(valuteNode.getNodeType() == Node.ELEMENT_NODE){
        Element element = (Element) valuteNode;
        dao.save(create(element));
      }
    }
  }

  private NodeList getElements(){
    document.getDocumentElement().normalize();
    return document.getElementsByTagName(ELEMENTS_TAG_NAME);
  }

  private Currency create(Element element){
    int nodeNominal = Integer.parseInt(parser.getElementText(NOMINAL, element));
    int nodeNumCode = Integer.parseInt(parser.getElementText(CODE, element));
    String nodeCharCode = parser.getElementText(CHAR_CODE, element);
    String nodeName = parser.getElementText(NAME, element);

    return tableRecordCreator.createNewTableRecord(nodeNumCode, nodeCharCode, nodeNominal, nodeName);
  }

  @Autowired
  public void setDataAccessObject(DataAccessObjectImpl dao){
    this.dao = dao;
  }

  @Autowired
  public void setRatesRecordCreator(TabelRecordCreator tableRecordCreator){
    this.tableRecordCreator = tableRecordCreator;
  }

  @Autowired
  public void setParser(ParseExchangeRatesImpl parser){
    this.parser = parser;
  }

  @Autowired
  public void setCache(InternalCache cache){
    this.cache = cache;
  }
}
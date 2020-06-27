package com.exchangerates.initializer;

import javax.annotation.PostConstruct;

import com.exchangerates.entities.Currency;
import com.exchangerates.parse.document.XMLDocument;
import com.exchangerates.ratescreator.RatesRecordCreator;
import com.exchangerates.repositories.CurrencyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

@Component
public class CurrencyTableDataInitializer {
  private Document document;
  private CurrencyRepository repository;
  private RatesRecordCreator ratesRecordCreator;

  @Autowired
  public CurrencyTableDataInitializer(XMLDocument document) {
    this.document = document.createDocument();
  }

  @PostConstruct
  private void execute() {
    this.initializeData();
  }

  private void initializeData() {
    document.getDocumentElement().normalize();
    NodeList list = document.getElementsByTagName("Valute");
    for(int i = 0; i < list.getLength(); i++){
      Node valuteNode = list.item(i);
      if(valuteNode.getNodeType() == Node.ELEMENT_NODE){
        Element element = (Element) valuteNode;
        repository.save(ratesRecordCreator.createNewRatesTableRecord(
          Integer.parseInt(getText("NumCode", element)),
          getText("CharCode", element),
          Integer.parseInt(getText("Nominal", element)),
          getText("Name", element)
          )
        );
      }
    }
  }

  private String getText(String tagName, Element element){
    return element.getElementsByTagName(tagName).item(0).getTextContent();
  }

  @Autowired
  public void setCurrencyRepository(CurrencyRepository repository){
    this.repository = repository;
  }

  @Autowired
  public void setRatesRecordCreator(RatesRecordCreator ratesRecordCreator){
    this.ratesRecordCreator = ratesRecordCreator;
  }
}
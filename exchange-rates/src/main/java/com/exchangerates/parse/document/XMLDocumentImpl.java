package com.exchangerates.parse.document;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@Component
public class XMLDocumentImpl implements XMLDocument{

  public Document createDocument(LocalDate date){
    Document document = null;
    try{
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      document = dBuilder.parse(new URL(this.formatUrl(this.formatDate(date))).openStream());
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }

    return document;
  }

  public Document createDocument(){
    Document document = null;
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      document = dBuilder.parse(new URL(this.formatUrl(this.formatDate())).openStream());
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }

    return document;
  }

  private final String URL = "http://www.bnm.md/en/official_exchange_rates?get_xml=1&date=";

  private String formatDate(LocalDate date){
    return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }

  private String formatDate(){
    return LocalDate.now()
      .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }

  private String formatUrl(String dateUrlQuery) {
    return this.URL + dateUrlQuery;
  }
}
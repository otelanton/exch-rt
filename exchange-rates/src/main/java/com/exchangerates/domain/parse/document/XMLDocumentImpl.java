package com.exchangerates.domain.parse.document;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@Component
public class XMLDocumentImpl implements ExchangeRatesDocument{

  @Value("${rates.url.xml}")
  private String url;
  @Value("${dateformat}")
  private String dateFormat;

  public Document createDocument(LocalDate date){
    Document document = null;
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    try{
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      InputStream xmlInputStream = getURL(date).openStream();
      document = documentBuilder.parse(xmlInputStream);
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }

    return document;
  }

  private DateTimeFormatter getDateTimeFormatter(){
    return DateTimeFormatter.ofPattern(this.dateFormat);
  }

  private URL getURL(LocalDate date) throws MalformedURLException {
    String urlRequestDate = date.format(getDateTimeFormatter());
    String requestUrl = this.url + urlRequestDate;
    return new URL(requestUrl);
  }
}
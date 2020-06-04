package com.exchangerates.parse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ParseExchangeRates implements ParseExchangeRatesInterface {

  public ParseExchangeRates() {
  }

  public float getRate(String charCode) {
    return Float.parseFloat(parseXMLRates(charCode));
  }

  public int getNominal(String charCode) {
    return Integer.parseInt(this.parseNominal(charCode));
  }

  private final String URL = "http://www.bnm.md/ru/official_exchange_rates?get_xml=1&date=";

  private String formatedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

  private String formatUrl(String dateUrlQuery) {
    return this.URL + dateUrlQuery;
  }

  private final Document document = this.getDocument();

  private String parseXMLRates(String charCode) {
    String rateValue = "";

    // format path string to look for rate value in node with requested charcode
    String xPathValue = String.format("/ValCurs/Valute[CharCode='%s']/Value[text()]", charCode);

    try {
      if(this.document != null)
        rateValue = evaluateXPath(this.document, xPathValue);
      else System.err.println("Document object is empty");
    } catch (Exception e) {
      e.printStackTrace();
    }

    return rateValue;
  }

  private String parseNominal(String charCode) {
    String nominalString = "";

    // format path string to look for nominal value in node with requested charcode
    String xPathNominal = String.format("/ValCurs/Valute[CharCode='%s']/Nominal[text()]", charCode);
    try {
      if(this.document != null)
        nominalString = evaluateXPath(this.document, xPathNominal);
      else System.err.println("Document object is empty");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return nominalString;
  }

  private Document getDocument() {
    Document document = null;
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      document = dBuilder.parse(new java.net.URL(this.formatUrl(this.formatedDate)).openStream());
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }

    return document;
  }

  private String evaluateXPath(Document document, String xpathExpression) throws Exception {
    // Create XPathFactory object
    XPathFactory xpathFactory = XPathFactory.newInstance();
      
    // Create XPath object
    XPath xpath = xpathFactory.newXPath();

    String value = "";
    try{
      // Create XPathExpression object
      XPathExpression expr = xpath.compile(xpathExpression);
        
      // Evaluate expression result on XML document
      value = (String) expr.evaluate(document, XPathConstants.STRING);
              
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    return value;
  }
}
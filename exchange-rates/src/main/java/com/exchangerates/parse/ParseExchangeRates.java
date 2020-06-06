package com.exchangerates.parse;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.exchangerates.parse.document.XMLDocument;

import org.w3c.dom.Document;

public class ParseExchangeRates implements ParseExchangeRatesInterface {

  public ParseExchangeRates() {
  }

  public float getRate(String charCode) {
    return Float.parseFloat(parseXMLRates(charCode));
  }

  public int getNominal(String charCode) {
    return Integer.parseInt(this.parseNominal(charCode));
  }

  private XMLDocument document = new XMLDocument();
  private Document doc = document.createDocument();

  private String parseXMLRates(String charCode) {
    String rateValue = "";

    // format path string to look for rate value in node with requested charcode
    String xPathValue = String.format("/ValCurs/Valute[CharCode='%s']/Value[text()]", charCode);

    try {
      if(this.doc != null)
        rateValue = evaluateXPath(this.doc, xPathValue);
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
      if(this.doc != null)
        nominalString = evaluateXPath(this.doc, xPathNominal);
      else System.err.println("Document object is empty");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return nominalString;
  }

  private String evaluateXPath(Document document, String xpathExpression) throws Exception {
    // Create XPathFactory object
    XPathFactory xpathFactory = XPathFactory.newInstance();
      
    // Create XPath object
    XPath xpath = xpathFactory.newXPath();

    String value = "";
    try {
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
package com.exchangerates.parse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.exchangerates.parse.document.XMLDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class ParseExchangeRatesImpl implements ParseExchangeRates {

  private static final Logger LOGGER = LoggerFactory.getLogger(ParseExchangeRates.class);

  public ParseExchangeRatesImpl() {
  }

  public float getRate(String charCode) {
    if(charCode == null || charCode.isEmpty())
      LOGGER.error("Charcode is empty ", new IllegalArgumentException());
    return Float.parseFloat(parseXMLRates(charCode));
  }

  private Document doc;

  private String parseXMLRates(String charCode) {
    String rateValue = "";

    // format path string to look for rate value in node with requested charcode
    String xPathValue = String.format("/ValCurs/Valute[CharCode='%s']/Value[text()]", charCode);

    try {
      if(this.doc != null)
        rateValue = evaluateXPath(this.doc, xPathValue);
      else LOGGER.error("Document object is empty");
    } catch (Exception e) {
      e.printStackTrace();
    }

    return rateValue;
  }

  public String getElementText(String tagName, Element element){
    return element.getElementsByTagName(tagName).item(0).getTextContent();
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

  @Autowired
  public void setDocument(XMLDocument document){
    this.doc = document.createDocument();
  }
}
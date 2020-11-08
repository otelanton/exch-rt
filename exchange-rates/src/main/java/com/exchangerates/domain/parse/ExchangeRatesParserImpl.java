package com.exchangerates.domain.parse;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.exchangerates.domain.parse.document.ExchangeRatesDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;

@Component
public class ExchangeRatesParserImpl implements ExchangeRatesParser {

  private ExchangeRatesDocument exchangeRatesDocument;

  private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRatesParser.class);

  //Xml document's tag names required for retrieving values contained in whose tags
  private enum Tags {
    //name of the head tag of the node
    VALUTE("Valute"),

    //name of the tag with rate's charcode
    CHARCODE("CharCode"),

    //name of the tag with rate's value
    VALUE("Value"),

    //name of the tag with rate's name
    NAME("Name"),

    //name of the tag with rate's numcode
    NUMCODE("NumCode"),

    //name of the tag with rate's nominal
    NOMINAL("Nominal");

    private String tag;

    Tags(String tag){
      this.tag = tag;
    }

    public String getTag(){
      return tag;
    }
  }

  public String parseNumCode(Element element){
    return getTextFromXmlElement(Tags.NUMCODE, element);
  }

  public String parseCharCode(Element element){
    return getTextFromXmlElement(Tags.CHARCODE, element);
  }

  public String parseName(Element element){
    return getTextFromXmlElement(Tags.NAME, element);
  }

  public String parseNominal(Element element){
    return getTextFromXmlElement(Tags.NOMINAL, element);
  }

  public String parseValue(Element element){
    return getTextFromXmlElement(Tags.VALUE, element);
  }

  public NodeList getXmlDocumentNodes(LocalDate date){
    return exchangeRatesDocument.createDocument(date).getDocumentElement().getElementsByTagName(Tags.VALUTE.getTag());
  }

  public float getRate(String charCode) {
    if(charCode == null || charCode.isEmpty())
      LOGGER.error("Charcode is empty ", new IllegalArgumentException());
    return Float.parseFloat(parseXMLRates(charCode));
  }

  private Document doc;

  //parses value of given tag only in given document element
  private String getTextFromXmlElement(Tags tag, Element xmlElement){
    if(tag == null){
      throw new IllegalArgumentException("Must supply tag");
    }

    if(xmlElement == null){
      throw new IllegalArgumentException("XML element must not be empty");
    }

    return xmlElement.getElementsByTagName(tag.getTag()).item(0).getTextContent();
  }

  private String parseXMLRates(String charCode) {
    if(charCode.isEmpty() || charCode == null){
      throw new IllegalArgumentException("CharCode must not be empty");
    }

    String rateValue = "";
    final String FORMAT = "/ValCurs/Valute[CharCode='%s']/Value[text()]";

    // format path string to look for rate value in node with requested charcode
    String xPathValue = String.format(FORMAT, charCode);

    try {
      if(this.doc != null)
        rateValue = evaluateXPath(this.doc, xPathValue);
      else LOGGER.error("Document object is empty");
    } catch (Exception e) {
      e.printStackTrace();
    }

    return rateValue;
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
  public void setDocument(ExchangeRatesDocument exchangeRatesDocument){
    this.exchangeRatesDocument = exchangeRatesDocument;
  }
}
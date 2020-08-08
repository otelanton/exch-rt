package com.exchangerates.parse;

import org.w3c.dom.Element;

import java.time.LocalDate;

public interface ParseExchangeRates {

  //Xml document's tag names required for retrieving values contained in whose tags
  enum Tags {
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

  public float getRate(String charCode);

  public String getTextFromXmlElement(Tags tag, Element element);
}
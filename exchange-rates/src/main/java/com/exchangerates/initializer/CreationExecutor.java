package com.exchangerates.initializer;

import com.exchangerates.parse.ParseExchangeRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;

@Component
class CreationExecutor {
  private static ParseExchangeRates parser;

  @Autowired
  CreationExecutor(ParseExchangeRates parser){
    this.parser = parser;
  }

  static void execute(Creator creator, LocalDate date){
    NodeList nodeList = parser.getXmlDocumentNodes(date);

    for(int i = 0; i < 2; i++){
      Element xmlElement = (Element) nodeList.item(i);
      creator.create(date, xmlElement);
    }
  }
}

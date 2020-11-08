package com.exchangerates.domain.initializer;

import com.exchangerates.domain.initializer.creators.Creator;
import com.exchangerates.domain.parse.ExchangeRatesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;

@Component
class CreationExecutor {
  private static ExchangeRatesParser parser;

  @Autowired
  CreationExecutor(ExchangeRatesParser parser){
    CreationExecutor.parser = parser;
  }

  static void execute(Creator creator, LocalDate date){
    NodeList nodeList = getNodeList(date);

    for(int i = 0; i < 2; i++){
      Element xmlElement = (Element) nodeList.item(i);
      creator.create(date, xmlElement);
    }
  }

  private static NodeList getNodeList(LocalDate date){
    return parser.getXmlDocumentNodes(date);
  }
}

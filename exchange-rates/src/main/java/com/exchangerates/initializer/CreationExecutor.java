package com.exchangerates.initializer;

import com.exchangerates.parse.ParseExchangeRates.Tags;
import com.exchangerates.parse.document.ExchangeRatesDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;

@Component
class CreationExecutor {
  private static ExchangeRatesDocument exchangeRatesDocument;

  @Autowired
  CreationExecutor(ExchangeRatesDocument exchangeRatesDocument){
    this.exchangeRatesDocument = exchangeRatesDocument;
  }

  static void execute(Creator creator, LocalDate date){
    NodeList nodeList = exchangeRatesDocument.createDocument(date).getDocumentElement().getElementsByTagName(Tags.VALUTE.getTag());

    for(int i = 0; i < 2; i++){
      Element xmlElement = (Element) nodeList.item(i);
      creator.create(date, xmlElement);
    }
  }
}

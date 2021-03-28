package com.exchangerates.domain.initializer;

/*
* Iterates over NodeList
*/

import com.exchangerates.domain.IEntity;
import com.exchangerates.domain.initializer.factory.EntitiesFactory;
import com.exchangerates.domain.parse.ExchangeRatesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreationExecutor{
  private static ExchangeRatesParser parser;

  @Autowired
  CreationExecutor(ExchangeRatesParser parser){
    CreationExecutor.parser = parser;
  }

  public static <T extends IEntity> List<T> execute(LocalDate date, EntitiesFactory factory, Class<T> klazz){
    NodeList nodeList = getNodeList(date);

    List<T> entities = new ArrayList<>();
    for(int i = 0; i < 2; i++){
      Element xmlElement = (Element) nodeList.item(i);
      entities.add(klazz.cast(factory.getInstance(date, xmlElement)));
    }

    return entities;
  }

  private static NodeList getNodeList(LocalDate date){
    return parser.getXmlDocumentNodes(date);
  }
}

package com.exchangerates.domain.parse;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExchangeRatesParserImplTest {
  private ExchangeRatesParser parser;
  private Document document;

  @BeforeEach
  public void init() throws ParserConfigurationException, IOException, SAXException{
    parser = new ExchangeRatesParserImpl();
    String xmlString = """
        <Valute>
          <NumCode>978</NumCode>
          <CharCode>EUR</CharCode>
          <Nominal>1</Nominal>
          <Name>Euro</Name>
          <Value>19.5379</Value>
        </Valute>
      """;
    document = DocumentBuilderFactory.newInstance()
      .newDocumentBuilder()
      .parse(new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  void parseTest()  {
    Assert.assertEquals(978, Integer.parseInt(parser.parseNumCode(document.getDocumentElement())));
    Assert.assertEquals("Euro", parser.parseName(document.getDocumentElement()));
    Assert.assertEquals("EUR", parser.parseCharCode(document.getDocumentElement()));
    Assert.assertEquals(1, Integer.parseInt(parser.parseNominal(document.getDocumentElement())));
    Assert.assertEquals("19.5379", parser.parseValue(document.getDocumentElement()));
  }

  @Test
  void exceptionTest(){
    Assert.assertThrows(IllegalArgumentException.class, () -> parser.parseName(null));
  }
}

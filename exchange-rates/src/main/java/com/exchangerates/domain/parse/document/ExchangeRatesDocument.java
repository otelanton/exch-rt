package com.exchangerates.domain.parse.document;

import org.w3c.dom.Document;
import java.time.LocalDate;

public interface ExchangeRatesDocument {
  Document createDocument(LocalDate date);
}
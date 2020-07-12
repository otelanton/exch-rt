package com.exchangerates.parse.document;

import org.w3c.dom.Document;
import java.time.LocalDate;

public interface XMLDocument {
  public Document createDocument(LocalDate date);
  public Document createDocument();
}
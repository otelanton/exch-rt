package com.exchangerates.initializer;

import org.w3c.dom.Element;

import java.time.LocalDate;

public interface Creator {
  void create(LocalDate date, Element xmlNodeElement);
}

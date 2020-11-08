package com.exchangerates.domain.initializer.creators;

import org.w3c.dom.Element;

import java.time.LocalDate;

public interface Creator {
  void create(LocalDate date, Element xmlNodeElement);
}

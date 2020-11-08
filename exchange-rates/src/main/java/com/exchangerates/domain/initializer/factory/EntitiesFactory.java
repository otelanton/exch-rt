package com.exchangerates.domain.initializer.factory;

import com.exchangerates.domain.IEntity;
import org.w3c.dom.Element;

import java.time.LocalDate;

public interface EntitiesFactory {
  IEntity getInstance(LocalDate date, Element xmlElement);
}

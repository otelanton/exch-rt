package com.exchangerates.domain.initializer.creators;

import com.exchangerates.domain.IEntity;

import java.time.LocalDate;
import java.util.List;

public interface Creator {
  <T extends IEntity> List<T> create(LocalDate date);
}

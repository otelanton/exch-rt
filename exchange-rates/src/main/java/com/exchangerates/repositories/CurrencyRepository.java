package com.exchangerates.repositories;

import com.exchangerates.entities.Currency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
  Currency findByAbbreviation(String abbreviation);  
}
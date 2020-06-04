package com.exchangerates.repositories;

import com.exchangerates.entities.Rates;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RatesRepository extends JpaRepository<Rates, Integer> {
  
}
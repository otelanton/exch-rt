package com.exchangerates.repositories;

import com.exchangerates.entities.USD;
import org.springframework.data.jpa.repository.JpaRepository;

public interface USDRepository extends JpaRepository<USD, Integer> {
  
}
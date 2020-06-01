package com.exchangerates.repositories;

import com.exchangerates.entities.EUR;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EURRepository extends JpaRepository<EUR, Integer> {
  
}
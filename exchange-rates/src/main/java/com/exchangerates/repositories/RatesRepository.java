package com.exchangerates.repositories;

import java.util.List;

import com.exchangerates.entities.Rates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatesRepository extends JpaRepository<Rates, Integer> {
  @Query("select r from Rates r left join fetch r.currency where r.currency.id = :id")
  List<Rates> findAllByCurrency(@Param("id") int id);
}
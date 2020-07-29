package com.exchangerates.repositories;

import java.util.List;

import com.exchangerates.entities.Rate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RateRepository extends JpaRepository<Rate, Integer> {
  @Query("select r from Rate r left join fetch r.currency where r.currency.id = :id")
  List<Rate> findAllByCurrency(@Param("id") int id);
  List<Rate> findAllByCurrency_CharCode(String charCode);
  Page<Rate> findAllByCurrency_CharCode(String charCode, Pageable page);
  Rate findTopByCurrencyIdOrderByIdDesc(int id);
}
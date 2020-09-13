package com.exchangerates.repositories;

import java.time.LocalDate;
import java.util.List;

import com.exchangerates.entities.Rate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RateRepository extends JpaRepository<Rate, Integer> {

  Page<Rate> findAllByCurrency_CharCode(String charCode, Pageable page);

  @Query("select r from Rate r left join fetch r.currency where r.currency.id = :id")
  List<Rate> findAllByCurrency(@Param("id") int id);

  @Query(nativeQuery = true,
          value = "select * from rate where currency_id = :id")
  List<Rate> findAllByCurrencyId(int id);

  @Query(nativeQuery = true,
      value = "select value from rate where currency_id = :id order by rate.id desc limit 1")
  Float findLatestByCurrencyId(int id);

  @Query(nativeQuery = true,
      value = "select * from rate where (date between :startDate and :endDate) and currency_id = :id")
  List<Rate> findInRange(LocalDate startDate, LocalDate endDate, int id);

  @Query(nativeQuery = true,
      value = "select * from rate where currency_id = :id order by month(:month)")
  List<Rate> findByMonth(int id, int month);
}
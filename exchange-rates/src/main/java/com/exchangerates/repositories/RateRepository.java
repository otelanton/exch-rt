package com.exchangerates.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.exchangerates.domain.Rate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {

  Page<Rate> findAllByCurrency_CharCode(String charCode, Pageable page);

  @Query("select r from Rate r left join fetch r.currency where r.currency.id = :id")
  List<Rate> findAllByCurrency(@Param("id") int id);

  @Query(nativeQuery = true,
      value = "select value from rate where currency_id = :currencyId order by rate.id desc limit 1")
  BigDecimal findLatestByCurrencyId(int currencyId);

  @Query(nativeQuery = true,
      value = "select * from rate where (date between :startDate and :endDate) and currency_id = :currencyId")
  List<Rate> findInRange(LocalDate startDate, LocalDate endDate, int currencyId);

  @Modifying
  @Query(nativeQuery = true,
    value = "delete from rate where currency_id = :currencyId limit 1")
  void deleteFirstRateByCurrencyId(int currencyId);
}
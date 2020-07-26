package com.exchangerates.repositories;

import java.util.List;

import com.exchangerates.entities.Currency;
import com.exchangerates.entities.DTO.CurrencyDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
  @Query("select c from Currency c left join fetch c.rates where c.charCode = :charCode")
  Currency findCurrencyByCharCode(@Param("charCode") String charCode);  
  List<CurrencyDTO> findBy();
  CurrencyDTO findByCharCode(String charCode);
}
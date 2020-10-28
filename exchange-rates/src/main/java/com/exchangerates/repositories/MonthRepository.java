package com.exchangerates.repositories;

import com.exchangerates.domain.Month;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthRepository extends JpaRepository<Month, Integer> {
}

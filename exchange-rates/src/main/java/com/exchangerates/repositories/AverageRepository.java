package com.exchangerates.repositories;

import com.exchangerates.domain.Average;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AverageRepository extends JpaRepository<Average, Long> {
  @Query("Select a.value from Average a where a.month.id = :monthId and a.foreignKey.id = :id")
  BigDecimal findValueByForeignKeyIdAndMonthId(@Param("id") int id, @Param("monthId") int monthId);
  Average findAverageByForeignKeyIdAndMonthId(int foreignKeyId, int monthId);
}

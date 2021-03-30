package com.account.accountservice.repository;

import com.account.accountservice.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
  void deleteAllByUserId(long userId);
  List<Alert> getByUserId(long id);
  Alert getFirstByUserIdOrderByIdDesc(long id);
}

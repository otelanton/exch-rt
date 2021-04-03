package com.notificationservice.repository;

import com.notificationservice.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
  List<Alert> findAllByUserID(long userID);
  void deleteAllByUserID(long userID);
  void deleteByAlertID(long id);
  List<Alert> findAllByCurrency(String currency);
}

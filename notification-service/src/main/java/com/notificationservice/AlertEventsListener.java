package com.notificationservice;

import com.notificationservice.domain.Alert;
import com.notificationservice.domain.event.UserUpdatedEvent;
import com.notificationservice.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlertEventsListener {

  @Autowired
  private AlertRepository alertRepository;

  @KafkaListener(topics = "alert.created", containerFactory = "kafkaListenerContainerFactory")
  public void create(Alert createdAlert) {
    alertRepository.save(createdAlert);
  }

  @Transactional
  @KafkaListener(topics = "user.updated", containerFactory = "userUpdatedEventContainerFactory")
  public void update(UserUpdatedEvent event) {
    List<Alert> alerts = alertRepository.findAllByUserID(event.getUserID());
    if (alerts.size() != 0) {
      alerts = alerts.stream().map(a -> {
        if (!event.getEmail().isEmpty()) {
          a.setEmail(event.getEmail());
        }
        if (!event.getUsername().isEmpty()) {
          a.setUsername(event.getUsername());
        }
        return a;
      }).collect(Collectors.toCollection(ArrayList::new));
      alertRepository.saveAll(alerts);
    }
  }

  @Transactional
  @KafkaListener(topics = "user.deleted", containerFactory = "deleteEventContainerFactory")
  public void deleteAllAlerts(long deletedUserID) {
    alertRepository.deleteAllByUserID(deletedUserID);
  }

  @Transactional
  @KafkaListener(topics = "alert.deleted", containerFactory = "deleteEventContainerFactory")
  public void deleteAlert(String alertID) {
    String stringID = alertID.split(":")[1].replace("}", "");
    long id = Long.parseLong(stringID);
    alertRepository.deleteByAlertID(id);
  }
}

package com.notificationservice;

import com.notificationservice.domain.event.UserUpdatedEvent;
import com.notificationservice.domain.Alert;
import com.notificationservice.repository.AlertRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Service {

  @Autowired
  private AlertRepository alertRepository;

  @KafkaListener(topics = "alert.created", containerFactory = "kafkaListenerContainerFactory")
  public void create(Alert consumerRecord) {
    System.out.println(consumerRecord);
    Alert alert = (Alert) consumerRecord;
    System.out.println(alert);
//    alertRepository.save(alert);
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

  @KafkaListener(topics = "user.deleted", containerFactory = "deleteEventContainerFactory")
  public void deleteAllAlerts(long deletedUserID) {
    System.out.println(deletedUserID);
    alertRepository.deleteAllByUserID(deletedUserID);
  }

  @KafkaListener(topics = "alert.deleted", containerFactory = "deleteEventContainerFactory")
  public void deleteAlert(ConsumerRecord<Long, String> alertID) {
    String s = alertID.value();
    String[] ss = s.split(":");
    long l = Long.parseLong(ss[1].replace("}", ""));
    System.out.println(l);
    alertRepository.deleteBySubscription(l);
  }
}

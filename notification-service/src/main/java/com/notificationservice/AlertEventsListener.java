package com.notificationservice;

import com.notificationservice.domain.Alert;
import com.notificationservice.domain.event.NewRateCreatedEvent;
import com.notificationservice.domain.event.UserUpdatedEvent;
import com.notificationservice.repository.AlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AlertEventsListener {

  private AlertRepository alertRepository;
  private JavaMailSender javaMailSender;
  private SimpleMailMessage emailMessage;

  public AlertEventsListener(AlertRepository alertRepository, JavaMailSender javaMailSender) {
    this.alertRepository = alertRepository;
    this.javaMailSender = javaMailSender;
  }
  @KafkaListener(topics = "alert.created", containerFactory = "kafkaListenerContainerFactory")
  public void create(Alert createdAlert) {
    alertRepository.save(createdAlert);
    log.info(String.format("Consumed %s event: %s%n", Alert.class.getSimpleName(), createdAlert.toString()));
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
      log.info(String.format("Consumed %s event: %s%n", UserUpdatedEvent.class.getSimpleName(), event.toString()));
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

  @KafkaListener(topics = "rates", containerFactory = "rateEventContainerFactory")
  public void rates(List<NewRateCreatedEvent> newRateCreatedEvents) {
    List<Alert> alerts = alertRepository.findAll();
    if(!alerts.isEmpty()) {
      newRateCreatedEvents.forEach(event -> {
        alerts.stream()
            .filter(alert -> alert.getCurrency().equals(event.getCurrency()))
            .forEach(alert -> {
              if (event.getRate().compareTo(alert.getHigh()) > 0) {
                emailMessage = new SimpleMailMessage();
                emailMessage.setTo(alert.getEmail());
                emailMessage.setSubject("Alert");
                emailMessage.setText(
                    String.format("Your subscription currency %s raised higher than %s and now is %s", alert.getCurrency(), alert.getHigh(), event.getRate()));
                javaMailSender.send(emailMessage);
                log.info(String.format("Consumed %s event: %s%n", NewRateCreatedEvent.class.getSimpleName(), event.toString()));
              }
              if (event.getRate().compareTo(alert.getLow()) < 0) {
                emailMessage = new SimpleMailMessage();
                emailMessage.setTo(alert.getEmail());
                emailMessage.setSubject("Alert");
                emailMessage.setText(
                    String.format("Your subscription currency %s fell lower than %s and now is %s", alert.getCurrency(), alert.getLow(), event.getRate()));
                javaMailSender.send(emailMessage);
                log.info(String.format("Consumed %s event: %s%n", NewRateCreatedEvent.class.getSimpleName(), event.toString()));
              }
            });
      });
    }
  }
}

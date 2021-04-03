package com.account.accountservice.service;

import com.account.accountservice.domain.events.DeleteEvent;
import com.account.accountservice.util.Patcher;
import com.account.accountservice.configuration.JwtConfig;
import com.account.accountservice.domain.*;
import com.account.accountservice.domain.events.Event;
import com.account.accountservice.domain.events.AlertCreatedEvent;
import com.account.accountservice.domain.events.UserUpdatedEvent;
import com.account.accountservice.repository.AlertRepository;
import com.account.accountservice.repository.UserRepository;
import com.account.accountservice.util.UserValidator;
import com.account.accountservice.util.Views;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.json.JsonMergePatch;
import javax.json.JsonObject;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {

  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private Patcher patcher;
  private JwtConfig jwtConfig;
  private UserValidator userValidator;
  private AlertRepository alertRepository;
  private KafkaTemplate<Long, Event> kafkaTemplate;
  private ObjectMapper objectMapper;

  @Transactional
  public void subscribe(AlertDTO dto, String header) {
    User user = userRepository.findByUsername(jwtConfig.getUsernameFromToken(header));
    Alert alert = new Alert(
        user,
        dto.getCurrency(),
        dto.getLow(),
        dto.getHigh()
    );
    alertRepository.save(alert);
    alert = alertRepository.getFirstByUserIdOrderByIdDesc(user.getId());
    user.addAlert(alert);
    AlertCreatedEvent n = new AlertCreatedEvent(alert, user);
    kafkaTemplate.send(new ProducerRecord<>("alert.created",1L, new AlertCreatedEvent(alert, user)));
    log.info(String.format("Posted %s event: %s%n", AlertCreatedEvent.class.getSimpleName(), alert.toString()));
  }

  @Transactional
  public User registerNewUser(User newUser){
    userValidator.validateUser(newUser);
    String encryptedPassword = passwordEncoder.encode(newUser.getPassword());
    newUser.setPassword(encryptedPassword);
    userRepository.save(newUser);
    return newUser;
  }

  @Transactional
  public void updateUser(String header, JsonMergePatch jsonMergePatch) {
    JsonObject jsonObject = jsonMergePatch.toJsonValue().asJsonObject();
    this.validateJsonMergePatch(jsonObject);
    String username = jwtConfig.getUsernameFromToken(header);
    User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException(username);
    }

    User patchedUser = patcher.mergePatch(jsonMergePatch, user);
    UserUpdatedEvent userUpdatedEvent = new UserUpdatedEvent(patchedUser);
    kafkaTemplate.send(new ProducerRecord<>("user.updated", userUpdatedEvent));
    log.info(String.format("Posted %s event: %s%n", UserUpdatedEvent.class.getSimpleName(), userUpdatedEvent.toString()));
    userRepository.save(patchedUser);
  }

  @Transactional
  public User updateUser(Long id, JsonMergePatch jsonMergePatch) {
    JsonObject jsonObject = jsonMergePatch.toJsonValue().asJsonObject();
    this.validateJsonMergePatch(jsonObject);
    User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id.toString()));
    User patchedUser = patcher.mergePatch(jsonMergePatch, user);
    kafkaTemplate.send(new ProducerRecord<>("user.updated", new UserUpdatedEvent(patchedUser)));
    userRepository.save(patchedUser);

    return patchedUser;
  }

  @Transactional
  public void deleteUser(String headerValue) {
    User loggedInUser = userRepository.findByUsername(jwtConfig.getUsernameFromToken(headerValue));
    userRepository.deleteById(loggedInUser.getId());
    DeleteEvent deleteEvent = new DeleteEvent(loggedInUser.getId());
    kafkaTemplate.send(new ProducerRecord<>("user.deleted", deleteEvent));
    log.info(String.format("Posted %s event: %s%n", DeleteEvent.class.getSimpleName(), deleteEvent.toString()));
  }

  @Transactional
  public void deleteAlert(String headerValue, long id) {
    Alert alert = alertRepository.getOne(id);
    User u = userRepository.findByAlertsId(id);
    u.removeAlert(alert);
    alertRepository.delete(alert);
    DeleteEvent deleteEvent = new DeleteEvent(id);
    kafkaTemplate.send(new ProducerRecord<>("alert.deleted", deleteEvent));
    log.info(String.format("Posted %s event: %s%n", DeleteEvent.class.getSimpleName(), deleteEvent.toString()));
  }

  public UserDetails getUser(String username) {
    return userRepository.findByUsername(username);
  }

  public String getSelf(String header) {
    objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    String username = jwtConfig.getUsernameFromToken(header);
    User self = userRepository.findByUsername(username);
    if (self == null) throw new UsernameNotFoundException(username);
    String selfJson = "";
    try {
      selfJson = objectMapper.writerWithView(Views.UserGet.class).writeValueAsString(self);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return selfJson;
  }

  public void validateJsonMergePatch(JsonObject jsonObject) {
    jsonObject.forEach((key, value) -> userValidator.validateField(value.toString(), key));
  }
}
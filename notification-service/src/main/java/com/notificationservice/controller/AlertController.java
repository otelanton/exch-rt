package com.notificationservice.controller;

import com.notificationservice.domain.Alert;
import com.notificationservice.repository.AlertRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/subscription")
@AllArgsConstructor
public class AlertController {

  private AlertRepository alertRepository;

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public ResponseEntity<?> add(@RequestBody Alert subscription) {
//    Jwts
    alertRepository.save(subscription);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}

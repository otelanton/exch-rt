package com.account.accountservice.controller;

import com.account.accountservice.domain.AlertDTO;
import com.account.accountservice.domain.User;
import com.account.accountservice.service.AccountService;
import com.account.accountservice.util.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.JsonMergePatch;
import javax.validation.Valid;

@RestController
public class AccountController {
  private AccountService accountService;

  @Autowired
  public AccountController(AccountService accountService){
    this.accountService = accountService;
  }

  @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
  public ResponseEntity<?> subscribe(@Valid @RequestBody AlertDTO subscription, @RequestHeader("authorization") String authHeader) {
    accountService.subscribe(subscription, authHeader);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @JsonView(Views.UserGet.class)
  public ResponseEntity<?> getUser(@RequestHeader("authorization")String header) {
    return new ResponseEntity<>(accountService.getSelf(header), HttpStatus.OK);
  }

  @RequestMapping(value = "/registration", method = RequestMethod.POST)
  public ResponseEntity<?> registration(@RequestBody User user){
    accountService.registerNewUser(user);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/update", method = RequestMethod.PATCH, consumes = "application/merge-patch+json")
  public ResponseEntity<Void> update(@RequestHeader("authorization") String token, @RequestBody JsonMergePatch mergePatchDocument) {
    accountService.updateUser(token, mergePatchDocument);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@RequestHeader("authorization") String header) {
    accountService.deleteUser(header);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value = "/delete/alert", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteAlert(@RequestHeader("authorization") String header, @RequestParam long id) {
    accountService.deleteAlert(header, id);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/confirm", method = RequestMethod.GET)
  public String confirm(@RequestParam String token){
    accountService.confirmUser(token);
    return "/registration";
  }
}


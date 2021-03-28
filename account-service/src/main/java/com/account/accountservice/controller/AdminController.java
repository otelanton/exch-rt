package com.account.accountservice.controller;

import com.account.accountservice.domain.User;
import com.account.accountservice.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.JsonMergePatch;

@RestController(value = "/admin")
@AllArgsConstructor
public class AdminController {

  private AccountService accountService;

  @RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH, consumes = "application/merge-patch+json")
  public ResponseEntity<User> updateById(@RequestParam Long id, @RequestBody JsonMergePatch mergePatchDocument) {
    return new ResponseEntity<>(accountService.updateUser(id, mergePatchDocument), HttpStatus.ACCEPTED);
  }

  @RequestMapping(value = "/get/user", method = RequestMethod.GET)
  public ResponseEntity<?> getUsername(@PathVariable("username") String username) {
    return new ResponseEntity<>(accountService.getUser(username), HttpStatus.OK);
  }

}

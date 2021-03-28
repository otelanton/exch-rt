package com.account.accountservice.util;

import com.account.accountservice.domain.User;
import com.account.accountservice.exception.UserAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.json.JsonMergePatch;
import javax.json.JsonValue;

@Component
@AllArgsConstructor
public class Patcher {

  private final ObjectMapper objectMapper;
  private PasswordEncoder passwordEncoder;

  public User mergePatch(JsonMergePatch mergePatch, User targetBean) {
    JsonValue target = objectMapper.convertValue(targetBean, JsonValue.class);

    JsonValue patched = applyMergePatch(mergePatch, target);
    User u = objectMapper.convertValue(patched, User.class);
    if (mergePatch.toJsonValue().asJsonObject().containsKey("password")) {
      u.setPassword(passwordEncoder.encode(mergePatch.toJsonValue().asJsonObject().getString("password")));
    }
    return u;
  }

  private JsonValue applyMergePatch(JsonMergePatch mergePatch, JsonValue target) {
    try {
      return mergePatch.apply(target);
    } catch (Exception e) {
      throw new UserAlreadyExistsException("jui", "jui");
    }
  }
}

package com.account.accountservice.controller;

import com.account.accountservice.domain.User;
import com.account.accountservice.repository.UserRepository;
import com.account.accountservice.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
  private User mockUser;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private AccountService accountService;
  @MockBean
  private UserRepository userRepository;

  @BeforeEach
  void setup(){
    mockUser = new User();
    mockUser.setUsername("UserName");
    mockUser.setPassword("password");
    mockUser.setEmail("...@mail.ru");
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void registrationSuccessfulTest() throws Exception{

    mockMvc.perform(post("/registration")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(mockUser)))
      .andExpect(status().isOk()
    );
  }
}

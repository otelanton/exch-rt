package com.account.accountservice.service.registration;

import com.account.accountservice.domain.User;
import com.account.accountservice.repository.UserRepository;
import com.account.accountservice.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.json.Json;
import javax.json.JsonMergePatch;
import java.security.Principal;

@SpringBootTest
public class AccountServiceTest {

  @Autowired
  private AccountService accountService;
  @MockBean
  private UserRepository userRepository;
  private User mockUser;
  @Mock
  private Principal principal;
//  @Spy
//  private JsonMergePatch jsonMergePatch;
//  @Mock
//  private Patcher patcher;

  @BeforeEach
  void setup(){
    mockUser = new User();
    mockUser.setUsername("UserName1");
    mockUser.setPassword("passworD1");
    mockUser.setEmail("email@mail.com");
  }

  @Test
  void userRegistration_True(){
    Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(mockUser);
    Assertions.assertNotNull(userRepository.findByUsername(Mockito.anyString()));
    Mockito.verify(userRepository, Mockito.times(10)).save(mockUser);

  }
//
//  @Test
//  void userRegistration_False(){
//    Mockito.when(userRepository.findByUsername("UserName"))
//        .thenReturn(mockUser);
//
//    Assertions.assertNotNull(userRepository.findByUsername("UserName"));
//
//  }

  @Test
  void userUpdate_Success() {
//    Mockito.when(userRepository.findByUsername("UserName1")).thenReturn(mockUser);
    Mockito.when(principal.getName()).thenReturn("UserName1");

    JsonMergePatch mergePatch = Json.createMergePatch(
        Json.createObjectBuilder()
          .add("username", "newUserName1")
          .add("password", "newPassword1")
          .add("email", "newEmail@new.com")
        .build()
    );

//    User u = accountService.updateUser(principal, mergePatch);

//    Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
//    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
//    Mockito.verify(patcher, Mockito.times(1)).mergePatch(mergePatch, mockUser, User.class);

//    Assertions.assertEquals("newUserName1", u.getUsername());
//    Assertions.assertEquals("newPassword1", u.getPassword());
//    Assertions.assertEquals("newEmail@new.com", u.getEmail());
  }
}
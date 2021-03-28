package com.account.accountservice.domain;

import com.account.accountservice.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class ConfirmationToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String confirmationToken;
//  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//  @JoinColumn(nullable = false, name = "user_id")
//  private User user;

  public ConfirmationToken() {
    this.confirmationToken = UUID.randomUUID().toString();
  }
}

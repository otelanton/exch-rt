package com.account.accountservice.domain;

import com.account.accountservice.util.CustomAuthorityDeserializer;
import com.account.accountservice.util.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView(Views.UserGet.class)
  private long id;
  @Column(unique = true)
  @JsonView(Views.UserGet.class)
  private String username;
  private String password;
  @Column(unique = true)
  @JsonView(Views.UserGet.class)
  private String email;
  @JsonIgnore
  private Roles userRole = Roles.ROLE_USER;
  private boolean enabled = true;
  @OneToMany(orphanRemoval = true, mappedBy = "user", fetch = FetchType.LAZY)
  @JsonManagedReference
  @JsonView(Views.UserGet.class)
  private List<Alert> alerts;

  @Override
  @JsonDeserialize(using = CustomAuthorityDeserializer.class)
  public Collection<? extends GrantedAuthority> getAuthorities() {
    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userRole.name());
    return Collections.singletonList(simpleGrantedAuthority);
  }

  public void addAlert(Alert alert) {
    this.alerts.add(alert);
  }

  public void removeAlert(Alert alert) {
    this.alerts.remove(alert);
    alert.setUser(null);
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}

package com.gatewayapi;

import com.gatewayapi.configuration.JwtConfig;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthenticationManagerImpl implements ReactiveAuthenticationManager {

  private JwtConfig jwtConfig;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    String authToken = authentication.getCredentials().toString();

    try {
      Claims claims = jwtConfig.getAllClaimsFromToken(authToken);
      List<String> authorities = (List<String>) claims.get("authorities");
      return Mono.just(
          new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
      );
    } catch (Exception e) {
      return Mono.empty();
    }
  }
}

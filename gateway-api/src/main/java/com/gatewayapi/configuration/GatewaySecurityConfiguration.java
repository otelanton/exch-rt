package com.gatewayapi.configuration;

import com.gatewayapi.AuthenticationManagerImpl;
import com.gatewayapi.SecurityContextRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@AllArgsConstructor
public class GatewaySecurityConfiguration {

  private JwtConfig jwtConfig;
  private AuthenticationManagerImpl authenticationManagerImpl;
  private SecurityContextRepository securityContextRepository;

  @Bean
  SecurityWebFilterChain configure(ServerHttpSecurity security) {
    return security.csrf().disable().requestCache().requestCache(NoOpServerRequestCache.getInstance())
        .and()
        .exceptionHandling().authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
        .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
        .and()
        .formLogin().disable()
        .httpBasic().disable()
        .authenticationManager(authenticationManagerImpl)
        .securityContextRepository(securityContextRepository)
        .authorizeExchange()
          .pathMatchers(HttpMethod.POST,"/registration", "/login").permitAll()
          .pathMatchers("/delete", "/update", "/get", "/subscribe").authenticated()
          .pathMatchers("/update/**").hasRole("ADMIN")
        .anyExchange().permitAll()
        .and()
        .build();
  }
}

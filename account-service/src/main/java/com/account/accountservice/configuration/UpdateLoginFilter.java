package com.account.accountservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(1)
public class UpdateLoginFilter extends GenericFilterBean {

  private AuthenticationManager authenticationManager;
  private JwtConfig jwtConfig;

  public UpdateLoginFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
    this.authenticationManager = authenticationManager;
    this.jwtConfig = jwtConfig;
  }

  @Override
  public void doFilter(ServletRequest httpServletRequest, ServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    System.out.println("called");
    if (httpServletRequest instanceof HttpServletRequest) {
      String url = ((HttpServletRequest)httpServletRequest).getRequestURI().toString();
      if (url.equals("/update")) {
        System.out.println("called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.setAuthenticated(false);
      }
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

  @Getter
  @Setter
  private static class Credentials {
    private String username;
    private String password;

  }
}

package com.account.accountservice.configuration;

import com.account.accountservice.service.userdetails.UserDetailsServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private UserDetailsServiceImp userDetailsServiceImp;
  private JwtConfig jwtConfig;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and()
        .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
//        .addFilter(new UpdateLoginFilter(authenticationManager(), jwtConfig))
        .authorizeRequests()
//        .antMatchers(HttpMethod.POST,"/registration").permitAll()
        .antMatchers(HttpMethod.POST, "/login").permitAll()
//        .antMatchers("/subscribe").authenticated()
        .and().httpBasic().and()
        .formLogin().disable()
        .httpBasic().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsServiceImp).passwordEncoder(new BCryptPasswordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public FilterRegistrationBean registrationBean() throws Exception {
    FilterRegistrationBean<UpdateLoginFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new UpdateLoginFilter(authenticationManager(), jwtConfig));
    registrationBean.addUrlPatterns("/update");

    return registrationBean;
  }
}

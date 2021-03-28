package com.account.accountservice.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@ToString
@Component
public class JwtConfig {

  @Value("${springbootwebfluxjjwt.jjwt.secret}")
  private String secret;
  @Value("${security.jwt.expiration:#{60*5*1000}}")
  private int expiration;
  @Value("${security.jwt.header:Authorization}")
  private String header;
  @Value("${security.jwt.prefix:Bearer }")
  private String prefix;
  @Value("${security.jwt.uri:/login}")
  private String Uri;

  public Claims getClaimsFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public String getUsernameFromToken(String header) {
    return getClaimsFromToken(getToken(header)).getSubject();
  }

  public Date getExpiriation(String token) {
    return getClaimsFromToken(token).getExpiration();
  }

  public int getExpiriation() {return expiration;}
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpiriation(token);
    return expiration.before(new Date());
  }

  private String doGenerateToken(Map<String, Object> claims, String username) {

    final Date createdDate = new Date();
    final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000L);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(createdDate)
        .setExpiration(expirationDate)
        .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
        .compact();
  }

  public Boolean validateToken(String token) {
    return !isTokenExpired(token);
  }

  public String getToken(String header) {
    return header.replace(this.getPrefix(), "");
  }

}

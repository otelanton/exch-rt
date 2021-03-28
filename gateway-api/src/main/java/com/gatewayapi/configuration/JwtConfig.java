package com.gatewayapi.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@Component
public class JwtConfig {

  @Value("${springbootwebfluxjjwt.jjwt.secret}")
  private String secret;
  @Value("${springbootwebfluxjjwt.jjwt.expiration}")
  private String expirationTime;
  @Value("${security.jwt.header:Authorization}")
  private String header;
  @Value("${security.jwt.prefix:Bearer }")
  private String prefix;
  @Value("${security.jwt.uri:/login}")
  private String Uri;

  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public String getUsernameFromToken(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  public Date getExpirationDateFromToken(String token) {
    return getAllClaimsFromToken(token).getExpiration();
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

//  public String generateToken(User user) {
//    Map<String, Object> claims = new HashMap<>();
//    claims.put("role", user.getRoles());
//    return doGenerateToken(claims, user.getUsername());
//  }

  private String doGenerateToken(Map<String, Object> claims, String username) {
    Long expirationTimeLong = Long.parseLong(expirationTime); //in second

    final Date createdDate = new Date();
    final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);

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
}

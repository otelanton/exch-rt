package com.exchangerates;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CaffeineCacheConfiguration {

  @Bean
  @Primary
  public CacheManager ratesCacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("rates");
    cacheManager.setCaffeine(ratesCaffeineCacheBuilder());
    return cacheManager;
  }

  @Bean
  public CacheManager currenciesCacheManager(){
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("currencies", "currenciesDTO");
    cacheManager.setCaffeine(currenciesCaffeineCacheBuilder());
    return cacheManager;
  }

  Caffeine<Object, Object> currenciesCaffeineCacheBuilder(){
    return Caffeine.newBuilder()
      .maximumSize(500)
      .recordStats();
  }

  Caffeine<Object, Object> ratesCaffeineCacheBuilder() {
    return Caffeine.newBuilder()
      .initialCapacity(100)
      .maximumSize(10000)
      .expireAfterWrite(55, TimeUnit.SECONDS)
      // .expireAfterWrite(86300, TimeUnit.SECONDS)
      .removalListener((Object key, Object value, RemovalCause cause) -> {
        System.out.format("removal listerner called with key [%s], cause [%s], evicted [%S]\n", 
          key, cause.toString(), cause.wasEvicted());
      })
      .recordStats();
  }
}
package com.exchangerates;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineCacheConfiguration {

  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("rates", "currencies");
    cacheManager.setCaffeine(caffeineCacheBuilder());
    return cacheManager;
  }

  Caffeine<Object, Object> caffeineCacheBuilder() {
    return Caffeine.newBuilder()
      .initialCapacity(100)
      .maximumSize(1000)
      .expireAfterWrite(25, TimeUnit.SECONDS)
      // .expireAfterWrite(86300, TimeUnit.SECONDS)
      .removalListener((Object key, Object value, RemovalCause cause) -> {
        System.out.format("removal listerner called with key [%s], cause [%s], evicted [%S]\n", 
          key, cause.toString(), cause.wasEvicted());
      })
      .recordStats();
  }
}
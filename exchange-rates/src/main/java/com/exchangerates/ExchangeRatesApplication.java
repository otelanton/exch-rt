package com.exchangerates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@EnableCaching
public class ExchangeRatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRatesApplication.class, args);
	}

}
package com.exchangerates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@SpringBootApplication
@ComponentScan
@EnableCaching
public class ExchangeRatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRatesApplication.class, args);
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

}

//TODO: Request specific exchange rates by setting the symbols parameter. GET /latest?symbols=USD,GBP
//TODO: Limit results to specific exchange rates to save bandwidth with the symbols parameter. GET /history?start_at=2018-01-01&end_at=2018-09-01&symbols=ILS,JPY

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

//TODO: add monthly average
//TODO: add change in rate value in percentages ?????????
//TODO: Quote against a different currency by setting the base parameter GET /latest?base=USD
//TODO: Request specific exchange rates by setting the symbols parameter. GET /latest?symbols=USD,GBP
//TODO: Limit results to specific exchange rates to save bandwidth with the symbols parameter. GET /history?start_at=2018-01-01&end_at=2018-09-01&symbols=ILS,JPY
//TODO: Quote the historical rates against a different currency. GET /history?start_at=2018-01-01&end_at=2018-09-01&base=USD
package com.exchangerates.service;

import com.exchangerates.cache.InternalCache;
import com.exchangerates.entities.Currency;
import com.exchangerates.exception.CurrencyNotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CurrencyServiceTest {

  @Autowired
  private CurrencyService currencyService;

  @MockBean
  private InternalCache cache;

  private Currency dollarTestCurrency = new Currency(896,"USD",1,"US Dollar");
  private Currency euroTestCurrency = new Currency(933,"EUR",1,"Euro");

  @Test
  void getCurrencyTest(){
    Mockito.when(cache.getCurrency(Mockito.eq("usd")))
            .thenReturn(dollarTestCurrency);

    EntityModel<Currency> currencyEntityModel = currencyService.getCurrency("usd");

    Mockito.verify(cache, Mockito.times(1)).getCurrency(Mockito.anyString());

    Assert.assertNotNull(currencyEntityModel);
    Assert.assertEquals(currencyEntityModel.getContent().getCharCode(), "USD");
    Assert.assertEquals(currencyEntityModel.getContent().getName(), "US Dollar");
    Assert.assertEquals(currencyEntityModel.getContent().getCode(), 896);
    Assert.assertEquals(currencyEntityModel.getContent().getNominal(), 1);
    Assert.assertTrue(currencyEntityModel.hasLinks());
  }

  @Test
  void getCurrencyThrowsCurrencyNotFoundExceptionTest(){
    Mockito.when(cache.getCurrency(Mockito.anyString()))
            .thenReturn(null);

    Assert.assertThrows(CurrencyNotFoundException.class, () -> currencyService.getCurrency(Mockito.anyString()));
  }

  @Test
  void getAllCurrencies(){
    Mockito.when(cache.getAllCurrencies())
            .thenReturn(new ArrayList<>(List.of(
                    dollarTestCurrency,
                    euroTestCurrency
            )));

    CollectionModel<EntityModel<Currency>> collectionModel = currencyService.getAllCurrencies();

    Mockito.verify(cache, Mockito.times(1)).getAllCurrencies();

    Assert.assertNotNull(collectionModel);
    Assert.assertTrue(collectionModel.hasLinks());
    Assert.assertFalse(collectionModel.getContent().isEmpty());
    Assert.assertEquals(collectionModel.getContent().size(), 2);
    Assert.assertEquals(collectionModel.iterator().next().getContent().getName(), "US Dollar");
    Assert.assertEquals(collectionModel.iterator().next().getContent().getCharCode(), "USD");
  }

}

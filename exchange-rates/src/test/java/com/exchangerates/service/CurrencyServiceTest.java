package com.exchangerates.service;

import com.exchangerates.domain.Currency;
import com.exchangerates.exception.CurrencyNotFoundException;
import com.exchangerates.initializer.CurrencyMap;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class CurrencyServiceTest {

  @Autowired
  private CurrencyService currencyService;
  @MockBean
  private CurrencyMap map;

  private Currency dollarTestCurrency = new Currency(896,"USD",1,"US Dollar");
  private Currency euroTestCurrency = new Currency(933,"EUR",1,"Euro");

  @Test
  void getCurrencyTest(){
    Mockito.when(map.getCurrency(Mockito.eq("usd")))
            .thenReturn(dollarTestCurrency);

    EntityModel<Currency> currencyEntityModel = currencyService.getCurrency("usd");

    Mockito.verify(map, Mockito.times(1)).getCurrency(Mockito.anyString());

    Assert.assertNotNull(currencyEntityModel);
    Assert.assertEquals(currencyEntityModel.getContent().getCharCode(), "USD");
    Assert.assertEquals(currencyEntityModel.getContent().getName(), "US Dollar");
    Assert.assertEquals(currencyEntityModel.getContent().getCode(), 896);
    Assert.assertEquals(currencyEntityModel.getContent().getNominal(), 1);
    Assert.assertTrue(currencyEntityModel.hasLinks());
  }

  @Test
  void getCurrencyThrowsCurrencyNotFoundExceptionTest(){
    Mockito.when(map.getCurrency(Mockito.anyString()))
            .thenReturn(null);

    Assert.assertThrows(CurrencyNotFoundException.class, () -> currencyService.getCurrency(Mockito.anyString()));
  }

  @Test
  void getAllCurrencies(){
    Mockito.when(map.getAllCurrencies())
            .thenReturn(new ArrayList<>(List.of(
                    dollarTestCurrency,
                    euroTestCurrency
            )));

    CollectionModel<EntityModel<Currency>> collectionModel = currencyService.getAllCurrencies();

    Mockito.verify(map, Mockito.times(1)).getAllCurrencies();

    Assert.assertNotNull(collectionModel);
    Assert.assertTrue(collectionModel.hasLinks());
    Assert.assertFalse(collectionModel.getContent().isEmpty());
    Assert.assertEquals(collectionModel.getContent().size(), 2);
    Assert.assertEquals(collectionModel.iterator().next().getContent().getName(), "US Dollar");
    Assert.assertEquals(collectionModel.iterator().next().getContent().getCharCode(), "USD");
  }

}

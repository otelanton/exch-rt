package com.exchangerates.service;

import com.exchangerates.cache.InternalCache;
import com.exchangerates.dao.DataAccessObjectImpl;
import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rate;
import com.exchangerates.exception.DateOutOfRangeException;
import com.exchangerates.exception.RateNotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class RateServiceTest {

  @Autowired
  private RateService service;

  @MockBean
  private DataAccessObjectImpl dao;
  @MockBean
  private InternalCache cache;

  private Currency dollarTestCurrency = new Currency(896,"USD",1,"US Dollar");

  @Test
  void averageTest(){
    Mockito.when(cache.getCurrency(Mockito.anyString()))
            .thenReturn(dollarTestCurrency);

    Mockito.when(dao.getRateForCurrencyByMonth(Mockito.anyInt(), Mockito.anyInt()))
            .thenReturn(new ArrayList<>(List.of(
                    new Rate((float)16.9656, LocalDate.now(), dollarTestCurrency, (float)0.0666),
                    new Rate((float)16.8656, LocalDate.now().minusDays(1), dollarTestCurrency, (float)0.1666),
                    new Rate((float)16.9036, LocalDate.now().minusDays(2), dollarTestCurrency, (float)-0.0666)))
            );

    String currentMonth = Month.of(LocalDate.now().getMonthValue()).name();

    double average = service.getRateAverageForMonth(Mockito.anyString(), currentMonth);

    Mockito.verify(dao, Mockito.times(1)).getRateForCurrencyByMonth(Mockito.anyInt(), Mockito.anyInt());
    Assert.assertNotEquals(0, average);
  }

  @Test
  void averageThrowsDateOutOfRangeExceptionTest(){
    String charCodeParamString = "test";
    String monthParamValueCausesException = Month.of(LocalDate.now().minusMonths(7).getMonthValue()).name();

    Assert.assertThrows(DateOutOfRangeException.class, () -> service.getRateAverageForMonth(charCodeParamString, monthParamValueCausesException));
  }

  @Test
  void getRateInRangeTest(){
    Mockito.when(cache.getCurrency(Mockito.anyString()))
            .thenReturn(dollarTestCurrency);

    Mockito.when(dao.getRateInRange(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.anyInt()))
            .thenReturn(new ArrayList<>(List.of(
                    new Rate((float)16.9656, LocalDate.now(), dollarTestCurrency, (float)0.0666),
                    new Rate((float)16.8656, LocalDate.now().minusDays(1), dollarTestCurrency, (float)0.1666),
                    new Rate((float)16.9036, LocalDate.now().minusDays(2), dollarTestCurrency, (float)-0.0666),
                    new Rate((float)16.9656, LocalDate.now().minusDays(3), dollarTestCurrency, (float)0.0666),
                    new Rate((float)16.8656, LocalDate.now().minusDays(4), dollarTestCurrency, (float)0.1666),
                    new Rate((float)16.9036, LocalDate.now().minusDays(5), dollarTestCurrency, (float)-0.0666)))
            );

    String testStartDateParam = LocalDate.now().minusDays(5).toString();
    String testEndDateParam = LocalDate.now().toString();

    CollectionModel<Rate> list = service.getRatesInRage(testStartDateParam, testEndDateParam, Mockito.anyString());

    Mockito.verify(cache, Mockito.times(1)).getCurrency(Mockito.anyString());
    Mockito.verify(dao, Mockito.times(1)).getRateInRange(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.anyInt());

    Assert.assertFalse(list.getContent().isEmpty());
    Assert.assertEquals(6, list.getContent().size());
    Assert.assertTrue(list.hasLinks());

  }

  @Test
  void getRateInRangeThrowsDateOutOfRangeExceptionAfterRangeDateTest(){
    String dateParamValueCausesException = LocalDate.now().plusDays(1).toString();
    String okDateParamValue = LocalDate.now().toString();
    String charCodeParamString = "test";

    Assert.assertThrows(DateOutOfRangeException.class, () -> service.getRatesInRage(okDateParamValue, dateParamValueCausesException, charCodeParamString));
  }

  @Test
  void getRateInRangeThrowsDateOutOfRangeExceptionBeforeRangeDateTest(){
    String dateParamValueCausesException = LocalDate.now().minusMonths(7).toString();
    String okDateParamValue = LocalDate.now().toString();
    String charCodeParamString = "test";

    Assert.assertThrows(DateOutOfRangeException.class, () -> service.getRatesInRage(dateParamValueCausesException, okDateParamValue, charCodeParamString));
  }

  @Test
  void getPagedRatesTest(){
    Mockito.when(cache.getPagedRates(Mockito.anyString(), Mockito.any(PageRequest.class)))
            .thenReturn(new PageImpl<>(new ArrayList<>(List.of(
                    new Rate((float)16.9656, LocalDate.now(), dollarTestCurrency, (float)0.0666),
                    new Rate((float)16.8656, LocalDate.now().minusDays(1), dollarTestCurrency, (float)0.1666),
                    new Rate((float)16.9036, LocalDate.now().minusDays(2), dollarTestCurrency, (float)-0.0666))))
            );

    String charCodeParamString = "test";

    PagedModel<EntityModel<Rate>> pagedModel = service.getPagedRates(charCodeParamString, 0, 1);

    Mockito.verify(cache, Mockito.times(1)).getPagedRates(Mockito.anyString(), Mockito.any(PageRequest.class));
    Assert.assertNotNull(pagedModel);
    Assert.assertTrue(pagedModel.hasLinks());
  }

  @Test
  void getPagedRatesThrowsRateNotFoundException(){
    Mockito.when(cache.getPagedRates(Mockito.anyString(), Mockito.any(PageRequest.class)))
            .thenReturn(Page.empty());

    String charCodeParamString = "test";

    Assert.assertThrows(RateNotFoundException.class, () -> service.getPagedRates(charCodeParamString, 1, 1));
  }
}

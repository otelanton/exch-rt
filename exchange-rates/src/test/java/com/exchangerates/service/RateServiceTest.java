package com.exchangerates.service;

import com.exchangerates.dao.DataAccessObjectImpl;
import com.exchangerates.domain.Currency;
import com.exchangerates.domain.Rate;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

@SpringBootTest
@ActiveProfiles("test")
public class RateServiceTest {

  @Autowired
  private RateService service;

  @MockBean
  private DataAccessObjectImpl dao;
  @MockBean
  private CurrencyService currencyService;

  private Currency dollarTestCurrency = new Currency(896,"USD",1,"US Dollar");

  @Test
  void getAverageTest(){
    Mockito.when(dao.getAverageValue(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new BigDecimal("16.9582"));
    int currentMonth = Month.of(LocalDate.now().getMonthValue()).getValue();

    BigDecimal average = service.getRateAverageForMonth(Mockito.anyString(), currentMonth);

    Mockito.verify(dao, Mockito.times(1)).getAverageValue(Mockito.anyInt(), Mockito.anyInt());
    Assert.assertNotNull(average);
    Assert.assertNotEquals(BigDecimal.ZERO, average);
  }

  @Test
  void getRateInRangeTest(){
    Mockito.when(dao.getRateInRange(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.anyInt()))
            .thenReturn(Arrays.asList(
                    new Rate(BigDecimal.valueOf(16.9656), LocalDate.now(), dollarTestCurrency, BigDecimal.valueOf(0.0666)),
                    new Rate(BigDecimal.valueOf(16.8656), LocalDate.now().minusDays(1), dollarTestCurrency, BigDecimal.valueOf(0.1666)),
                    new Rate(BigDecimal.valueOf(16.9036), LocalDate.now().minusDays(2), dollarTestCurrency, BigDecimal.valueOf(-0.0666)),
                    new Rate(BigDecimal.valueOf(16.9656), LocalDate.now().minusDays(3), dollarTestCurrency, BigDecimal.valueOf(0.0666)),
                    new Rate(BigDecimal.valueOf(16.8656), LocalDate.now().minusDays(4), dollarTestCurrency, BigDecimal.valueOf(0.1666)),
                    new Rate(BigDecimal.valueOf(16.9036), LocalDate.now().minusDays(5), dollarTestCurrency, BigDecimal.valueOf(-0.0666)))
            );

    String testStartDateParam = LocalDate.now().minusDays(5).toString();
    String testEndDateParam = LocalDate.now().toString();

    CollectionModel<Rate> list = service.getRatesInRage(testStartDateParam, testEndDateParam, Mockito.anyString());

    Mockito.verify(dao, Mockito.times(1)).getRateInRange(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.anyInt());

    Assert.assertFalse(list.getContent().isEmpty());
    Assert.assertEquals(6, list.getContent().size());
    Assert.assertTrue(list.hasLinks());

  }

  @Test
  void getPagedRatesTest(){
    Mockito.when(dao.getPagedRateByCharCode(Mockito.anyString(), Mockito.any(PageRequest.class)))
            .thenReturn(new PageImpl<>(Arrays.asList(
                    new Rate(BigDecimal.valueOf(16.9656), LocalDate.now(), dollarTestCurrency, BigDecimal.valueOf(0.0666)),
                    new Rate(BigDecimal.valueOf(16.8656), LocalDate.now().minusDays(1), dollarTestCurrency, BigDecimal.valueOf(0.1666)),
                    new Rate(BigDecimal.valueOf(16.9036), LocalDate.now().minusDays(2), dollarTestCurrency, BigDecimal.valueOf(-0.0666))))
            );

    String charCodeParamString = "test";

    PagedModel<EntityModel<Rate>> pagedModel = service.getPagedRates(charCodeParamString, 0, 1);

    Mockito.verify(dao, Mockito.times(1)).getPagedRateByCharCode(Mockito.anyString(), Mockito.any(PageRequest.class));
    Assert.assertNotNull(pagedModel);
    Assert.assertTrue(pagedModel.hasLinks());
  }
}

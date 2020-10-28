package com.exchangerates.exception;

import com.exchangerates.dao.DataAccessObjectImpl;
import com.exchangerates.service.RateService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;

@SpringBootTest
@ActiveProfiles("test")
public class RateServiceExceptionsTest {
  @Autowired
  private RateService service;

  @MockBean
  private DataAccessObjectImpl dao;

  @Test
  void averageThrowsDateOutOfRangeExceptionTest(){
    String charCodeParamString = "test";
    int monthParamValueCausesException = Month.of(LocalDate.now().minusMonths(7).getMonthValue()).getValue();

    Assert.assertThrows(DateOutOfRangeException.class, () -> service.getRateAverageForMonth(charCodeParamString, monthParamValueCausesException));
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
  void getRateInRangeThrowsDateTimeParseExceptionTest() {
    String invalidParam = "invalid";
    String secondParam = "2020-09-20";
    String thirdParam = "test";

    Assert.assertThrows(DateTimeParseException.class, () -> service.getRatesInRage(invalidParam, secondParam, thirdParam));
  }

  @Test
  void getPagedRatesThrowsRateNotFoundException(){
    Mockito.when(dao.getPagedRateByCharCode(Mockito.anyString(), Mockito.any(PageRequest.class)))
      .thenReturn(Page.empty()
    );

    String charCodeParamString = "test";

    Assert.assertThrows(RateNotFoundException.class, () -> service.getPagedRates(charCodeParamString, 1, 1));
  }
}

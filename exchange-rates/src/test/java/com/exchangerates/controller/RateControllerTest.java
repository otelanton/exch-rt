package com.exchangerates.controller;

import com.exchangerates.domain.dao.DataAccessObjectImpl;
import com.exchangerates.service.RateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RateController.class)
class RateControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private DataAccessObjectImpl dao;
  @MockBean
  private RateService service;

  @Test
  void getPagedRatesTest() throws Exception {
    mockMvc.perform(get("/rate/page")
        .param("charCode", "USD")
        .param("size", "5")
        .param("page", "0"))
      .andExpect(status().isOk()
    );

    Mockito.verify(service, Mockito.times(1)).getPagedRates(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
  }

  @Test
  void getRateInRangeTest() throws Exception {
    mockMvc.perform(get("/rate/range")
        .param("charCode", "USD")
        .param("startDate", "2020-09-09")
        .param("endDate", "2020-09-13"))
      .andExpect(status().isOk()
    );

    Mockito.verify(service, Mockito.times(1)).getRatesInRage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
  }

  @Test
  void getAverageTest() throws Exception {
    Mockito.when(service.getRateAverageForMonth(Mockito.anyString(), Mockito.anyInt()))
      .thenReturn(BigDecimal.ZERO);

    mockMvc.perform(get("/rate/average")
      .param("charCode", "USD")
      .param("month", "1"))
      .andExpect(status().isOk()
    );

    Mockito.verify(service, Mockito.times(1)).getRateAverageForMonth(Mockito.anyString(), Mockito.anyInt());
  }
}
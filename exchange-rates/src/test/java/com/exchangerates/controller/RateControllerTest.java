package com.exchangerates.controller;

import com.exchangerates.cache.InternalCache;
import com.exchangerates.dao.DataAccessObjectImpl;
import com.exchangerates.service.RateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RateController.class)
class RateControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private InternalCache cache;
  @MockBean
  private DataAccessObjectImpl dao;
  @MockBean
  private RateService service;

  @Test
  void getPagedRatesTest() throws Exception {
    mockMvc.perform(get("/rate/page/{charCode}", "USD")
        .param("size", "5")
        .param("page", "0"))
      .andExpect(status().isOk()
    );

    Mockito.verify(service, Mockito.times(1)).getPagedRates(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
  }

  @Test
  void getRateInRangeTest() throws Exception {
    mockMvc.perform(get("/rate/range/{charCode}", "USD")
        .param("startDate", "2020-09-09")
        .param("endDate", "2020-09-13"))
      .andExpect(status().isOk()
    );

    Mockito.verify(service, Mockito.times(1)).getRatesInRage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
  }

  @Test
  void getAverageTest() throws Exception {
    mockMvc.perform(get("/rate/average/{charCode}", "USD")
        .param("month", "1"))
      .andExpect(status().isOk()
    );

    Mockito.verify(service, Mockito.times(1)).getRateAverageForMonth(Mockito.anyString(), Mockito.anyInt());
  }
}
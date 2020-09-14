package com.exchangerates.controller;

import com.exchangerates.cache.InternalCache;
import com.exchangerates.dao.DataAccessObjectImpl;
import com.exchangerates.repositories.CurrencyRepository;
import com.exchangerates.repositories.RateRepository;
import com.exchangerates.service.RateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MissingServletRequestParameterException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RateController.class)
class RateControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private InternalCache cache;
  @MockBean
  private RateRepository rateRepository;
  @MockBean
  private CurrencyRepository currencyRepository;
  @MockBean
  private DataAccessObjectImpl dao;
  @MockBean
  private RateService service;

  @Test
  void missingServletRequestParameterTest() throws Exception {
    mockMvc.perform(get("/rate/page/{charCode}", "kkk")
                  .param("size", "5"))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof MissingServletRequestParameterException))
            .andExpect(jsonPath("$.rejectedValue").value("page")
    );
  }

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
        .param("month", "september"))
      .andExpect(status().isOk()
    );

    Mockito.verify(service, Mockito.times(1)).getRateAverageForMonth(Mockito.anyString(), Mockito.anyString());
  }
}
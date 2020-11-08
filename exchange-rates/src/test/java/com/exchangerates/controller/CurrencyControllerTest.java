package com.exchangerates.controller;

import com.exchangerates.domain.dao.DataAccessObjectImpl;
import com.exchangerates.domain.repositories.CurrencyRepository;
import com.exchangerates.domain.repositories.RateRepository;
import com.exchangerates.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private RateRepository rateRepository;
  @MockBean
  private CurrencyRepository currencyRepository;
  @MockBean
  private DataAccessObjectImpl dao;
  @MockBean
  private CurrencyService service;

  @Test
  void getCurrencyTest() throws Exception {
    mockMvc.perform(get("/currencies/single")
        .param("charCode", "USD"))
      .andExpect(status().isOk()
    );

    Mockito.verify(service, Mockito.times(1)).getCurrency(Mockito.anyString());
  }

  @Test
  void getAllCurrenciesTest() throws Exception {
    mockMvc.perform(get("/currencies/all" ))
      .andExpect(status().isOk()
    );

    Mockito.verify(service, Mockito.times(1)).getAllCurrencies();
  }
}


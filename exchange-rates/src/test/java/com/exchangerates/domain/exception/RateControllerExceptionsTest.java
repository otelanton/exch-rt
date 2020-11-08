package com.exchangerates.domain.exception;

import com.exchangerates.controller.RateController;
import com.exchangerates.domain.dao.DataAccessObjectImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RateController.class)
public class RateControllerExceptionsTest {
  private final String JSON_PATH_REJECTED_VALUE = "$.rejectedValue";
  private final String JSON_PATH_STATUS = "$.status";
  private final String STATUS_BAD_REQUEST = "BAD_REQUEST";

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private DataAccessObjectImpl dao;

  @Test
  void getPagedRatesMissingServletRequestParameterTest() throws Exception {
    mockMvc.perform(get("/rate/page/{charCode}", "code")
        .param("size", "5"))
      .andDo(print())
      .andExpect(status().is4xxClientError())
      .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof MissingServletRequestParameterException))
      .andExpect(jsonPath(JSON_PATH_REJECTED_VALUE).value("page"))
      .andExpect(jsonPath(JSON_PATH_STATUS).value(STATUS_BAD_REQUEST)
    );
  }

  @Test
  void getPagedRatesMethodArgumentTypeMismatchExceptionTest() throws Exception {
    mockMvc.perform(get("/rate/page/{charCode}", "code")
        .param("page", "0")
        .param("size", "reject"))
      .andDo(print())
      .andExpect(status().is4xxClientError())
      .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentTypeMismatchException))
      .andExpect(jsonPath(JSON_PATH_REJECTED_VALUE).value("reject"))
      .andExpect(jsonPath(JSON_PATH_STATUS).value(STATUS_BAD_REQUEST)
    );
  }

  @Test
  void getAverageMethodArgumentTypeMismatchExceptionTest() throws Exception {
    mockMvc.perform(get("/rate/average/{charCode}", "code")
        .param("month", "reject"))
      .andDo(print())
      .andExpect(status().is4xxClientError())
      .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentTypeMismatchException))
      .andExpect(jsonPath(JSON_PATH_REJECTED_VALUE).value("reject"))
      .andExpect(jsonPath(JSON_PATH_STATUS).value(STATUS_BAD_REQUEST)
    );
  }

  @Test
  void getAverageMissingServletRequestParameterExceptionTest() throws Exception {
    mockMvc.perform(get("/rate/average/{charCode}", "code"))
      .andDo(print())
      .andExpect(status().is4xxClientError())
      .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof MissingServletRequestParameterException))
      .andExpect(jsonPath(JSON_PATH_REJECTED_VALUE).value("month"))
      .andExpect(jsonPath(JSON_PATH_STATUS).value(STATUS_BAD_REQUEST)
    );
  }

  @Test
  void getRatesInRangeConstraintViolationExceptionTest() throws Exception {
    mockMvc.perform(get("/rate/range/{charCode}", "code")
        .param("startDate", "")
        .param("endDate", "2020-09-20"))
      .andDo(print())
      .andExpect(status().is4xxClientError())
      .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ConstraintViolationException))
      .andExpect(jsonPath(JSON_PATH_REJECTED_VALUE).value("blank"))
      .andExpect(jsonPath(JSON_PATH_STATUS).value(STATUS_BAD_REQUEST)
    );
  }
}

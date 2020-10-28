package com.exchangerates.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.domain.Rate;
import com.exchangerates.exception.DateOutOfRangeException;
import com.exchangerates.exception.RateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
public class RateService {

  private DataAccessObject dao;
  private RatesPagedModelAssembler ratesPagedModelAssembler;
  private CurrencyService currencyService;

  @Autowired
  RateService(RatesPagedModelAssembler ratesPagedModelAssembler, DataAccessObject dao, CurrencyService currencyService) {
    this.ratesPagedModelAssembler = ratesPagedModelAssembler;
    this.dao = dao;
    this.currencyService = currencyService;
  }

  public PagedModel<EntityModel<Rate>> getPagedRates(String charCode, int page, int size){
    PageRequest pageable = PageRequest.of(page, size);

    Page<Rate> ratePage = dao.getPagedRateByCharCode(charCode, pageable);

    if(!ratePage.hasContent()){
      throw new RateNotFoundException("Rate wasn't found", charCode);
    }

    return ratesPagedModelAssembler.toModel(ratePage, charCode, pageable);
  }

  public BigDecimal getRateAverageForMonth(String charCode, int month){
    int thisMonthNumericValue = LocalDate.now().getMonthValue();
    int halfYearBeforeMonthNumericValue = LocalDate.now().minusMonths(6).getMonthValue();

    if(isMonthInRange(month, thisMonthNumericValue, halfYearBeforeMonthNumericValue)){
      throw new DateOutOfRangeException("Month is out of range", month);
    }

    int currencyId = currencyService.getCurrencyId(charCode);
    return dao.getAverageValue(currencyId, thisMonthNumericValue);
  }

  private boolean isMonthInRange(int month, int thisMonthNumericValue, int halfYearBeforeMonthNumericValue) {
    return month > thisMonthNumericValue || month < halfYearBeforeMonthNumericValue;
  }

  //  @Transactional
  public CollectionModel<Rate> getRatesInRage(String startDate, String endDate, String charCode){
    LocalDate today = LocalDate.now();
    LocalDate endDateRangeLimit = LocalDate.parse(endDate);
    LocalDate startDateRangeLimit = LocalDate.parse(startDate);

    if(endDateRangeLimit.isAfter(today)){
      throw new DateOutOfRangeException("Date is out of range", endDate);
    }
    if(startDateRangeLimit.isBefore(today.minusMonths(6))){
      throw new DateOutOfRangeException("Date is out of range",startDate);
    }

    List<Rate> rateInRange = getRates(startDateRangeLimit, endDateRangeLimit, charCode);
    return ratesPagedModelAssembler.toCollectionModel(rateInRange, startDate, endDate, charCode);
  }

  private List<Rate> getRates(LocalDate startLimit, LocalDate endLimit, String charCode){
    int currencyId = currencyService.getCurrencyId(charCode);
    return dao.getRateInRange(startLimit, endLimit, currencyId);
  }
}
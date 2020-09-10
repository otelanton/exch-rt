package com.exchangerates.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.cache.InternalCache;
import com.exchangerates.entities.Rate;
import com.exchangerates.exception.DateOutOfRangeException;
import com.exchangerates.exception.RateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
public class RateService {

  private InternalCache cache;
  private DataAccessObject dao;
  private RatesPagedModelAssembler ratesPagedModelAssembler;
  private CurrencyService currencyService;

  @Autowired
  RateService(RatesPagedModelAssembler ratesPagedModelAssembler, InternalCache cache, DataAccessObject dao, CurrencyService currencyService) {
    this.ratesPagedModelAssembler = ratesPagedModelAssembler;
    this.cache = cache;
    this.dao = dao;
    this.currencyService = currencyService;
  }

  public PagedModel<EntityModel<Rate>> getPagedRates(String charCode, int page, int size){
    PageRequest pageable = PageRequest.of(page, size, Sort.by("date").descending());

    Page<Rate> ratePage = cache.getPagedRates(charCode, pageable);

    if(!ratePage.hasContent()){
      throw new RateNotFoundException("Rate wasn't found.", charCode);
    }

    return ratesPagedModelAssembler.toModel(ratePage, charCode, pageable);
  }

  public double getRateAverageForMonth(String charCode, String month){
    int requestedMonthNumericValue = Month.valueOf(month.toUpperCase()).getValue();
    int thisMonthNumericValue = LocalDate.now().getMonthValue();
    int halfYearBeforeMonthNumericValue = LocalDate.now().minusMonths(6).getMonthValue();

    if(requestedMonthNumericValue > thisMonthNumericValue || requestedMonthNumericValue < halfYearBeforeMonthNumericValue){
      throw new DateOutOfRangeException("Month is out of range.", requestedMonthNumericValue);
    }

    int currencyId = currencyService.getCurrencyId(charCode);

    List<Rate> listOfRateForMonth = dao.getRateForCurrencyByMonth(currencyId, requestedMonthNumericValue);

    return listOfRateForMonth.stream()
            .mapToDouble(Rate::getValue)
            .average()
            .getAsDouble();
  }

//  @Transactional
  public CollectionModel<Rate> getRatesInRage(String startDate, String endDate, String charCode){

    LocalDate today = LocalDate.now();
    LocalDate endDateRangeLimit = LocalDate.parse(endDate);
    LocalDate startDateRangeLimit = LocalDate.parse(startDate);

    if(endDateRangeLimit.isAfter(today)){
      throw new DateOutOfRangeException("Date is out of range.", endDate);
    }

    if(startDateRangeLimit.isBefore(today.minusMonths(6))){
      throw new DateOutOfRangeException("Date is out of range.",startDate);
    }

    List<Rate> rateInRange = getRateInRange(startDateRangeLimit, endDateRangeLimit, charCode);

    return ratesPagedModelAssembler.toCollectionModel(rateInRange, startDate, endDate, charCode);
  }

  private List<Rate> getRateInRange(LocalDate startLimit, LocalDate endLimit, String charCode){
    int currencyId = currencyService.getCurrencyId(charCode);

    return dao.getRateInRange(startLimit, endLimit, currencyId);
  }
}
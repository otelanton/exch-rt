package com.exchangerates.controller;

import com.exchangerates.entities.Rate;
import com.exchangerates.exception.DateOutOfRangeException;
import com.exchangerates.exception.RateNotFoundException;
import com.exchangerates.service.RatesService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/rate")
public class RateController {
  private RatesService ratesService;
  private RatesPagedModelAssembler ratesPagedModelAssembler;

  @Autowired
  public RateController(RatesService ratesService, RatesPagedModelAssembler ratesPagedModelAssembler){
    this.ratesService = ratesService;
    this.ratesPagedModelAssembler = ratesPagedModelAssembler;
  }

  @RequestMapping(value = "/week/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<PagedModel<EntityModel<Rate>>> getPagedRatesOneWeek(
          @PathVariable String charCode,
          @PageableDefault(size = 7) Pageable pageable) {
    return new ResponseEntity<>(getPagedModel(charCode, pageable), HttpStatus.OK);
  }

  @RequestMapping(value = "/month/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<PagedModel<EntityModel<Rate>>> getPagedRatesOneMonth(
          @PathVariable String charCode,
          @PageableDefault(size = 30) Pageable pageable) {
    return new ResponseEntity<>(getPagedModel(charCode, pageable), HttpStatus.OK);
  }

  @RequestMapping(value = "/days/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<PagedModel<EntityModel<Rate>>> getPagedRatesTwoDays(
          @PathVariable String charCode,
          @PageableDefault(size = 2) Pageable pageable) {
    return new ResponseEntity<>(getPagedModel(charCode, pageable), HttpStatus.OK);
  }

  @RequestMapping(value = "/page/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<PagedModel<EntityModel<Rate>>> getPagedRates(
          @PathVariable String charCode, @javax.validation.constraints.NotNull @RequestParam Integer page, @NotNull @RequestParam Integer size) {

    PageRequest pageable = PageRequest.of(page, size, Sort.by("date").descending());

    return new ResponseEntity<>(getPagedModel(charCode, pageable), HttpStatus.OK);
  }

  private PagedModel<EntityModel<Rate>> getPagedModel(String charCode, Pageable pageable){
    Page<Rate> page = ratesService.getPagedRates(charCode, pageable);
    if(!page.hasContent()){
      throw new RateNotFoundException("Rate wasn't found.", charCode);
    }

    return ratesPagedModelAssembler.toModel(page, charCode, pageable);
  }

  @RequestMapping(value = "/range/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<CollectionModel<Rate>> getRateInRange(
          @PathVariable String charCode,
          @RequestParam String startDate,
          @RequestParam String endDate,
          Pageable pageable) {

    LocalDate today = LocalDate.now();
    LocalDate endDateRangeLimit = LocalDate.parse(endDate);
    LocalDate startDateRangeLimit = LocalDate.parse(startDate);

    if(endDateRangeLimit.isAfter(today)){
      throw new DateOutOfRangeException("Date is out of range.", endDate);
    }

    if(startDateRangeLimit.isBefore(today.minusMonths(6))){
      throw new DateOutOfRangeException("Date is out of range.",startDate);
    }

    List<Rate> ratesInRange = ratesService.getRatesInRage(startDateRangeLimit, endDateRangeLimit, charCode);

    return new ResponseEntity<>(ratesPagedModelAssembler
            .toCollectionModel(ratesInRange, startDate, endDate, charCode, pageable),
            HttpStatus.OK);
  }

  @RequestMapping(value = "/average/{charCode}", method = RequestMethod.GET)
  public float getAverage(@PathVariable String charCode, @RequestParam String month){
    int currencyId = ratesService.getCurrencyDto(charCode).getId();

    int requestedMonthNumericValue = Month.valueOf(month.toUpperCase()).getValue();
    int thisMonthNumericValue = LocalDate.now().getMonthValue();
    int halfYearBeforeMonthNumericValue = LocalDate.now().minusMonths(6).getMonthValue();

    if(requestedMonthNumericValue > thisMonthNumericValue || requestedMonthNumericValue < halfYearBeforeMonthNumericValue){
      throw new DateOutOfRangeException("Month is out of range.", requestedMonthNumericValue);
    }
    return ratesService.getRateAverageForMonth(currencyId, requestedMonthNumericValue);
  }

}

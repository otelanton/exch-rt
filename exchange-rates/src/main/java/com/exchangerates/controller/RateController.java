package com.exchangerates.controller;

import com.exchangerates.domain.Rate;
import com.exchangerates.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Validated
@RestController
@RequestMapping(path = "/rate")
public class RateController {
  private RateService rateService;

  @Autowired
  public RateController(RateService rateService){
    this.rateService = rateService;
  }

  @RequestMapping(value = "/page", method = RequestMethod.GET)
  public ResponseEntity<PagedModel<EntityModel<Rate>>> getPagedRates(
      @NotBlank @RequestParam String charCode, @NotNull @RequestParam Integer page, @NotNull @RequestParam Integer size) {
    PagedModel<EntityModel<Rate>> entityModelPagedModel = rateService.getPagedRates(charCode, page, size);

    return new ResponseEntity<>(entityModelPagedModel, HttpStatus.OK);
  }

  @RequestMapping(value = "/range", method = RequestMethod.GET)
  public ResponseEntity<CollectionModel<Rate>> getRateInRange(
      @NotBlank @RequestParam String charCode, @NotBlank @RequestParam String startDate, @NotBlank @RequestParam String endDate) {
    CollectionModel<Rate> rateInRange = rateService.getRatesInRage(startDate, endDate, charCode);

    return new ResponseEntity<>(rateInRange, HttpStatus.OK);
  }

  @RequestMapping(value = "/average", method = RequestMethod.GET)
  public ResponseEntity<String> getAverage(@NotBlank @RequestParam String charCode, @NotNull @RequestParam Integer month){
    BigDecimal averageRateValue = rateService.getRateAverageForMonth(charCode, month);

    return new ResponseEntity<>(averageRateValue.toString(), HttpStatus.OK);
  }
}
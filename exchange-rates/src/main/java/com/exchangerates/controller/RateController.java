package com.exchangerates.controller;

import com.exchangerates.entities.Rate;
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

@Validated
@RestController
@RequestMapping(path = "/rate")
public class RateController {
  private RateService rateService;

  @Autowired
  public RateController(RateService rateService){
    this.rateService = rateService;
  }

  @RequestMapping(value = "/page/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<PagedModel<EntityModel<Rate>>> getPagedRates(
      @PathVariable String charCode, @NotNull @RequestParam Integer page, @NotNull @RequestParam Integer size) {

    PagedModel<EntityModel<Rate>> entityModelPagedModel = rateService.getPagedRates(charCode, page, size);

    return new ResponseEntity<>(entityModelPagedModel, HttpStatus.OK);
  }

  @RequestMapping(value = "/range/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<CollectionModel<Rate>> getRateInRange(
      @PathVariable String charCode, @RequestParam String startDate, @RequestParam String endDate) {

    CollectionModel<Rate> rateInRange = rateService.getRatesInRage(startDate, endDate, charCode);

    return new ResponseEntity<>(rateInRange, HttpStatus.OK);
  }

  @RequestMapping(value = "/average/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<Double> getAverage(@PathVariable String charCode, @NotNull @RequestParam Integer month){
    double averageRateValue = rateService.getRateAverageForMonth(charCode, month);

    return new ResponseEntity<>(averageRateValue, HttpStatus.OK);
  }

}

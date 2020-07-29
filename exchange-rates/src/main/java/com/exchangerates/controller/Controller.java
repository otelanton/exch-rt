package com.exchangerates.controller;

import java.util.ArrayList;
import java.util.List;

import com.exchangerates.entities.DTO.CurrencyDTO;
import com.exchangerates.entities.Rate;
import com.exchangerates.service.RatesService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/exchange_rates")
public class Controller {

  private final int WEEK  = 7;
  private final int MONTH = 30;

  private RatesService service;
  private ModelAssembler assm;
  private RatesPagedModelAssembler ratesPagedModelAssembler;

  @RequestMapping(value = "/dto/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<EntityModel<CurrencyDTO>> dto(@PathVariable String charCode, Pageable pageable){
    CurrencyDTO model = service.getDto(charCode);
    return new ResponseEntity<>( assm.toModel(model, charCode, pageable), HttpStatus.OK);
  }

  @RequestMapping(value = "/currs", method = RequestMethod.GET)
  public ResponseEntity<CollectionModel<EntityModel<CurrencyDTO>>> allCurrencies(Pageable pageable){

    List<CurrencyDTO> list = service.getAllCurrencies();
    List<EntityModel<CurrencyDTO>> dtos = new ArrayList<>();

    for(CurrencyDTO entity : list){
      String code = entity.getCharCode();
      dtos.add(assm.toModel(entity, code, pageable));
    }

    return new ResponseEntity<>( CollectionModel.of(dtos, linkTo(methodOn(Controller.class).allCurrencies(pageable)).withSelfRel()), HttpStatus.OK);
  }

  @RequestMapping(value = "/page/week/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<PagedModel<EntityModel<Rate>>> pagedRatesOneWeek(
                @PathVariable String charCode, 
                @PageableDefault(size = WEEK) Pageable pageable) {
    return new ResponseEntity<>(getPagedModel(charCode, pageable), HttpStatus.OK);
  }

  @RequestMapping(value = "/page/month/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<PagedModel<EntityModel<Rate>>> pagedRatesOneMonth(
                @PathVariable String charCode, 
                @PageableDefault(size = MONTH) Pageable pageable) {
    return new ResponseEntity<>(getPagedModel(charCode, pageable), HttpStatus.OK);
  }

  @RequestMapping(value = "/page/days/{charCode}", method = RequestMethod.GET)
  public ResponseEntity<PagedModel<EntityModel<Rate>>> pagedRatesTwoDays(
                @PathVariable String charCode, 
                @PageableDefault(size = 2) Pageable pageable) {
    return new ResponseEntity<>(getPagedModel(charCode, pageable), HttpStatus.OK);
  }

  private PagedModel<EntityModel<Rate>> getPagedModel(String charCode, Pageable pageable){
    Page<Rate> page = service.getPagedRates(charCode, pageable);
    return ratesPagedModelAssembler.toModel(page, charCode, pageable);
  }

  /*
   * Setter methods for autowiring dependencies 
  */ 

  @Autowired
  public void setService(RatesService service){
    this.service = service;
  }

  @Autowired
  public void setModelAssembler(ModelAssembler assm){
    this.assm = assm;
  }

  @Autowired
  public void setRatesPagedModelAssembler(RatesPagedModelAssembler ratesPagedModelAssembler){
    this.ratesPagedModelAssembler = ratesPagedModelAssembler;
  }
}
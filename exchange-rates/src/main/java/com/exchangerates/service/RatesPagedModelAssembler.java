package com.exchangerates.service;

import com.exchangerates.controller.CurrencyController;
import com.exchangerates.controller.RateController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import com.exchangerates.domain.Rate;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class RatesPagedModelAssembler {

  private PagedResourcesAssembler<Rate> assembler;

  @Autowired
  public RatesPagedModelAssembler(PagedResourcesAssembler<Rate> assembler){
    this.assembler = assembler;
  }

  public PagedModel<EntityModel<Rate>> toModel(Page<Rate> page, String charCode, Pageable pageable){
    return addLinksToPage(page, charCode, pageable);
  }

  public CollectionModel<Rate> toCollectionModel(List<Rate> rates, String startDate, String endDate, String charCode){

    CollectionModel<Rate> cm = CollectionModel.of(rates,
            linkTo(methodOn(RateController.class).getPagedRates(charCode, null, null)).withRel("page"),
            linkTo(methodOn(CurrencyController.class).getAllCurrencies()).withRel("currencies"),
            linkTo(methodOn(CurrencyController.class).getCurrency(charCode)).withRel("currency"),
            linkTo(methodOn(RateController.class).getRateInRange(charCode, null, null)).withSelfRel(),
            linkTo(methodOn(RateController.class).getAverage(charCode, null)).withRel("average")
    );

    return cm;
  }

  private PagedModel<EntityModel<Rate>> addLinksToPage(Page<Rate> page, String charCode, Pageable pageable){

    PagedModel<EntityModel<Rate>> model = assembler.toModel(page);

    model.add(linkTo(methodOn(RateController.class).getPagedRates(charCode, pageable.getPageNumber(), pageable.getPageSize())).withRel("page"));
    model.add(linkTo(methodOn(RateController.class).getAverage(charCode, null)).withRel("average"));
    model.add(
      linkTo(methodOn(CurrencyController.class).getAllCurrencies()).withRel("currencies"),
      linkTo(methodOn(CurrencyController.class).getCurrency(charCode)).withRel("currency")
    );
    return model;
  }
}
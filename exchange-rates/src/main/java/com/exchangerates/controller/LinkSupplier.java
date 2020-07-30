package com.exchangerates.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

class LinkSupplier {

  private static final Class<Controller> CONTROLLER = Controller.class;

  public static Link supply(LinkType type, String charCode, Pageable pageable){
    return resolve(type, charCode, pageable);
  }
  
  private static Link resolve(LinkType type, String charCode, Pageable pageable){
    return switch (type) {
      case DAYS -> linkTo(methodOn(CONTROLLER).getPagedRatesTwoDays(charCode, pageable)).withRel("two_days");
      case WEEK -> linkTo(methodOn(CONTROLLER).getPagedRatesOneWeek(charCode, pageable)).withRel("week");
      case MONTH -> linkTo(methodOn(CONTROLLER).getPagedRatesOneMonth(charCode, pageable)).withRel("month");
      case LIST_CURRS -> linkTo(methodOn(CONTROLLER).getAllCurrencies(pageable)).withRel("currencies");
      case CURRENCY -> linkTo(methodOn(CONTROLLER).getCurrency(charCode, pageable)).withRel("currency");
      case SELF_CURRENCY -> linkTo(methodOn(CONTROLLER).getCurrency(charCode, pageable)).withSelfRel();
    };
  }
}
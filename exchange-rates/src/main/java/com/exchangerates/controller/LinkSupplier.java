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
    switch(type){
      case DAYS:
        return linkTo(methodOn(CONTROLLER).pagedRatesTwoDays(charCode, pageable)).withRel("two_days");
      case WEEK:
        return linkTo(methodOn(CONTROLLER).pagedRatesOneWeek(charCode, pageable)).withRel("week");
      case MONTH:
        return linkTo(methodOn(CONTROLLER).pagedRatesOneMonth(charCode, pageable)).withRel("month");
      case LIST_CURRS:
        return linkTo(methodOn(CONTROLLER).allCurrencies(pageable)).withRel("currencies");
      case CURRENCY:
        return linkTo(methodOn(CONTROLLER).dto(charCode, pageable)).withRel("currency");
      case SELF_CURRENCY:
        return linkTo(methodOn(CONTROLLER).dto(charCode, pageable)).withSelfRel();
      default:
        return null;
    }
  }
}
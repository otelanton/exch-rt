package com.exchangerates.service;

import com.exchangerates.controller.CurrencyController;
import com.exchangerates.controller.RateController;
import com.exchangerates.entities.Currency;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class CurrencyModelAssembler {

  public CollectionModel<EntityModel<Currency>> toCollectionModel(List<Currency> currencies){
    return createCollectionModel(currencies);
  }

  public EntityModel<Currency> toModel(Currency entity, String charCode){
    return createModel(entity, charCode);
  }

  private CollectionModel<EntityModel<Currency>> createCollectionModel(List<Currency> currencies){
    return CollectionModel.of(
        currencies.stream()
        .map(entity -> createModel(entity, entity.getCharCode()))
        .collect(Collectors.toList()),
      WebMvcLinkBuilder.linkTo(methodOn(CurrencyController.class).getAllCurrencies()).withSelfRel());
  }

  private EntityModel<Currency> createModel(Currency entity, String charCode){

    return EntityModel.of(entity,
        linkTo(methodOn(RateController.class).getPagedRates(charCode, null, null)).withRel("page"),
        linkTo(methodOn(RateController.class).getRateInRange(charCode, null, null)).withRel("range"),
        linkTo(methodOn(CurrencyController.class).getAllCurrencies()).withRel("currencies"),
        linkTo(methodOn(CurrencyController.class).getCurrency(charCode)).withSelfRel()
    );
  }
}
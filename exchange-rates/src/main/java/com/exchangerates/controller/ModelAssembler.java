package com.exchangerates.controller;

import com.exchangerates.entities.DTO.CurrencyDTO;

import org.springframework.data.domain.Pageable;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
class ModelAssembler{

  public EntityModel<CurrencyDTO> toModel(CurrencyDTO entity, String charCode, Pageable pageable){
    return createModel(entity, charCode, pageable);
  }

  private EntityModel<CurrencyDTO> createModel(CurrencyDTO entity, String charCode, Pageable pageable){
    return EntityModel.of(entity, 
        LinkSupplier.supply(LinkType.DAYS, charCode, pageable),
        LinkSupplier.supply(LinkType.WEEK, charCode, pageable),
        LinkSupplier.supply(LinkType.MONTH, charCode, pageable),
        LinkSupplier.supply(LinkType.LIST_CURRS, charCode, pageable),
        LinkSupplier.supply(LinkType.SELF_CURRENCY, charCode, pageable)
        );
  }
}
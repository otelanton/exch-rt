package com.exchangerates.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.exchangerates.entities.Rate;

@Component
class RatesPagedModelAssembler {

  private final PagedResourcesAssembler<Rate> assembler;

  @Autowired
  public RatesPagedModelAssembler(PagedResourcesAssembler<Rate> assembler){
    this.assembler = assembler;
  }

  public PagedModel<EntityModel<Rate>> toModel(Page<Rate> page, String charCode, Pageable pageable){

    return addLinksToPage(page, charCode, pageable);
  }
 
  private PagedModel<EntityModel<Rate>> addLinksToPage(Page<Rate> page, String charCode, Pageable pageable){

    Assert.notNull(page, "Page must not be null");
    Assert.hasText(charCode, "CharCode must be specified");
    Assert.notNull(pageable, "Pageable must not be null");

    PagedModel<EntityModel<Rate>> model = assembler.toModel(page);

    addCommonLinks(model, charCode, pageable);

    switch (page.getSize()) {
      case 2 -> addLinksToDaysPage(model, charCode, pageable);
      case 7 -> addLinksToWeekPage(model, charCode, pageable);
      case 30 -> addLinksToMonthPage(model, charCode, pageable);
    }
    return model;
  }

  /**
   * Adds to {@link PagedModel} links witch are common for all pages.
   * 
   * @param model PagedModel where links added to.
   * @param charCode  must not be {@literal null}.
   * @param pageable must not be {@literal null}.
   */
  private void addCommonLinks(PagedModel<EntityModel<Rate>> model, String charCode, Pageable pageable){

    Assert.notNull(model, "Model must not be null");

    model.add(
      LinkSupplier.supply(LinkType.LIST_CURRS, charCode, pageable),
      LinkSupplier.supply(LinkType.CURRENCY, charCode, pageable)
    );
  }

  /**
   * Adds to {@link PagedModel} links to week and month sized pages.
   * 
   * @param model must not be {@literal null}.
   * @param charCode must not be {@literal null}.
   * @param pageable must not be {@literal null}.
   */
  private void addLinksToDaysPage(PagedModel<EntityModel<Rate>> model, String charCode, Pageable pageable){

    Assert.notNull(model, "Model must not be null");

    model.add(
      LinkSupplier.supply(LinkType.WEEK, charCode, pageable), 
      LinkSupplier.supply(LinkType.MONTH, charCode, pageable)
    );
  }

  /**
   * Adds to {@link PagedModel} links to week and days sized pages.
   * 
   * @param model must not be {@literal null}.
   * @param charCode must not be {@literal null}.
   * @param pageable must not be {@literal null}.
   */
  private void addLinksToMonthPage(PagedModel<EntityModel<Rate>> model, String charCode, Pageable pageable){

    Assert.notNull(model, "Model must not be null");

    model.add(
      LinkSupplier.supply(LinkType.DAYS, charCode, pageable), 
      LinkSupplier.supply(LinkType.WEEK, charCode, pageable)
    );
  }

  /**
   * Adds to {@link PagedModel} links to month and days sized pages.
   * 
   * @param model must not be {@literal null}.
   * @param charCode must not be {@literal null}.
   * @param pageable must not be {@literal null}.
   */
  private void addLinksToWeekPage(PagedModel<EntityModel<Rate>> model, String charCode, Pageable pageable){

    Assert.notNull(model, "Model must not be null");

    model.add(
      LinkSupplier.supply(LinkType.DAYS, charCode, pageable), 
      LinkSupplier.supply(LinkType.MONTH, charCode, pageable)
    );
  }
}
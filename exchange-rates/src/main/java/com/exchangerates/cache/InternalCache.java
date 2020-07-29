package com.exchangerates.cache;

import java.util.List;

import com.exchangerates.entities.DTO.CurrencyDTO;
import com.exchangerates.dao.DataAccessObject;
import com.exchangerates.dao.DataAccessObjectImpl;
import com.exchangerates.entities.Currency;
import com.exchangerates.entities.Rates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring caching feature works over AOP proxies, thus internal calls to cached
 * methods don't work. That's why this internal bean is created: it "proxifies"
 * build() and get() methods to real AOP proxified cacheable bean methods
 * getListOfAllCurrencies and getCurrency.
 */

@EnableCaching
@Service
@Transactional
public class InternalCache {
  private static final Logger logger = LoggerFactory.getLogger(InternalCache.class);

  private DataAccessObject dao;

  @Cacheable(unless = "#result == null", cacheManager = "currenciesCacheManager", value = "currenciesDTO")
  public List<CurrencyDTO> getAllCurrenciesDTO() {
    logger.info("\n============MISS============");
    List<CurrencyDTO> list  = dao.getAllCurrencyDto();
    return list;
  }

  @Cacheable(unless = "#result == null", cacheManager = "currenciesCacheManager", value = "currencies")
  public List<Currency> getAllCurrencies(){
    logger.info("\n============MISS============");
    return dao.getAllCurrencies();
  }

  @Cacheable(unless = "#result == null", value = "rates")
  public Currency getCurrencyByCharCode(String charCode){
    logger.info("\n\tGetting rate by cahrcode: {}. Cache MISS", charCode);
    Currency fk = dao.getCurrencyByCharCode(charCode);
    if(fk == null){
      logger.error("\nCURRENCY WASN'T FOUND: " + charCode);
      return null;
    } 
    return fk;
  }

  @Cacheable(unless = "#result == null", value = "rates")
  public List<Rates> getExchangeRates(String charCode){
    logger.info("\n\tCahce Miss " + charCode);
    return dao.getAllExchangeRatesForCurrency(charCode);
  }


  @Cacheable(value = "rates")
  public Page<Rates> getPagedRates(String charCode, Pageable page){
    return dao.getPagedRatesByCharCode(charCode, page);
  }

  @Cacheable(cacheManager = "currenciesCacheManager", value = "currenciesDTO")
  public CurrencyDTO getDto(String charCode){
    return dao.getCurrencyAsDto(charCode);
  }

  /*
   * Setter methods for autowiring dependencies 
  */ 

  @Autowired
  public void setDataAccessObject(DataAccessObjectImpl dao){
    this.dao = dao;
  }
}
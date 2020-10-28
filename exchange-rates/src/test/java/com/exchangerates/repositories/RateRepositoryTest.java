package com.exchangerates.repositories;

import com.exchangerates.domain.Currency;
import com.exchangerates.domain.Rate;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PagedResourcesAssembler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RateRepositoryTest {
  @Mock
  private Rate rate;
  @MockBean
  private PagedResourcesAssembler<Rate> a;
  @Autowired
  private RateRepository repository;
  @Autowired
  private CurrencyRepository currencyRepository;

  @BeforeEach
  void setUp(){
    Currency c = new Currency(896, "USD", 1, "US Dollar");
    currencyRepository.save(c);
    repository.saveAll(Arrays.asList(
      new Rate(new BigDecimal("16.9999"), LocalDate.now().minusDays(5), c, new BigDecimal("0.9651")),
      new Rate(new BigDecimal("16.8452"), LocalDate.now().minusDays(4), c, new BigDecimal("0.9123")),
      new Rate(new BigDecimal("16.9274"), LocalDate.now().minusDays(3), c, new BigDecimal("0.8740")),
      new Rate(new BigDecimal("16.9109"), LocalDate.now().minusDays(2), c, new BigDecimal("0.8740")),
      new Rate(new BigDecimal("16.9789"), LocalDate.now().minusDays(1), c, new BigDecimal("0.8740")),
      new Rate(new BigDecimal("16.9366"), LocalDate.now(), c, new BigDecimal("0.8740"))
      )
    );
  }

  @Test
  void findLatestRateTest(){
    int id = getId();
    BigDecimal latestRate = repository.findLatestByCurrencyId(id);
    Assert.assertNotNull(latestRate);
    Assert.assertEquals(0, latestRate.compareTo(new BigDecimal("16.9366")));
  }

  @Test
  void findInRangeTest(){
    int id = getId();
    List<Rate> range = repository.findInRange(LocalDate.now().minusDays(3), LocalDate.now(), id);
    Assert.assertFalse(range.isEmpty());
    Assert.assertEquals(4, range.size());
  }

  @Test
  void deleteFirstRateByCurrencyIdTest(){
    int j = 2;
    int id = getId();
    List<Rate> beforeDelete = repository.findAllByCurrency(id);
    repository.deleteFirstRateByCurrencyId(id);
    List<Rate> afterDelete = repository.findAllByCurrency(id);
    Assert.assertNotEquals(beforeDelete.size(), afterDelete.size());
    Assert.assertTrue(afterDelete.size() < beforeDelete.size());
    for(int i = 0; i < afterDelete.size(); i++) {
      Assert.assertEquals(j++, afterDelete.get(i).getId());
    }
  }

  int getId(){
    return currencyRepository.findAll().get(0).getId();
  }
}

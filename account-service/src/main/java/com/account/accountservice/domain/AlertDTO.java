package com.account.accountservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AlertDTO {

  private BigDecimal low;
  private BigDecimal high;
  @NotBlank
  private String currency;

  public AlertDTO(BigDecimal low, BigDecimal high, String currency) {
    if(low == null) {
      this.low = new BigDecimal(Integer.MAX_VALUE);
    }
    if(high == null) {
      this.high = new BigDecimal(Integer.MIN_VALUE);
    }
    this.currency = currency;
  }
}

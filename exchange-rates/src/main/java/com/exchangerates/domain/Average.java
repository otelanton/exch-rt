package com.exchangerates.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Average implements IEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(precision = 9, scale = 4)
  private BigDecimal value;
  @ManyToOne(fetch = FetchType.LAZY)
  private Currency foreignKey;
  @ManyToOne(fetch = FetchType.LAZY)
  private Month month;

  public Average(){}

  public Average(BigDecimal value, Currency currency, Month month){
    this.value = value;
    this.foreignKey = currency;
    this.month = month;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public Currency getForeignKey() {
    return foreignKey;
  }

  public void setForeignKey(Currency foreignKey) {
    this.foreignKey = foreignKey;
  }

  public Month getMonth() {
    return month;
  }

  public void setMonth(Month month) {
    this.month = month;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Average average = (Average) o;
    return id == average.id &&
      Objects.equals(value, average.value);
  }

  @Override
  public int hashCode() {
    int hashCode = Long.hashCode(id);
    hashCode = 31 * hashCode + value.hashCode();

    return hashCode;
  }
}

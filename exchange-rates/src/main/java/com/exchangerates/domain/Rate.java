package com.exchangerates.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Rate implements IEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(nullable = false, precision = 9, scale = 4)
  private BigDecimal value;
  @Column(nullable = false)
  private LocalDate date;
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Currency currency;
  @Column(precision = 9, scale = 4)
  private BigDecimal difference;

  public Rate() {}

  public Rate(BigDecimal value, LocalDate date, Currency currency, BigDecimal difference) {
    this.value = value;
    this.date = date;
    this.currency = currency;
    this.difference = difference;
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

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate localDate) {
    this.date = localDate;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public BigDecimal getDifference() {
    return difference;
  }

  public void setDifference(BigDecimal difference) {
    this.difference = difference;
  }

  @Override
  public String toString() {
    return "CurrencyRates [currency=" + currency.getCharCode() + ", date=" + date + ", id=" + id + ", rate=" + value.toString()
        + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj) 
      return true;
    if(!(obj instanceof Rate))
      return false;
    Rate o = (Rate) obj;

    return this.currency.getId() == o.currency.getId()
      && this.value.equals(o.value)
      && this.date == o.date
      && this.id == o.id
      && this.difference.equals(o.difference);
  }

  @Override
  public int hashCode() {
    int hashCode = Long.hashCode(id);

    hashCode = 31 * hashCode + value.hashCode();
    hashCode = 31 * hashCode + date.hashCode();
    hashCode = 31 * hashCode + currency.hashCode();
    hashCode = 31 * hashCode + difference.hashCode();

    return hashCode;
  }
}
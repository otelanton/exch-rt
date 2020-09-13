package com.exchangerates.entities;

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
public class Rate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(nullable = false)
  private float value;
  @Column(nullable = false)
  private LocalDate date;
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Currency currency;
  private float difference;

  public Rate() {}

  public Rate(float value, LocalDate date, Currency currency, float difference) {
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

  public float getValue() {
    return value;
  }

  public void setValue(float value) {
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

  public float getDifference() {
    return difference;
  }

  public void setDifference(float difference) {
    this.difference = difference;
  }

  @Override
  public String toString() {
    return "CurrencyRates [currency=" + currency.getCharCode() + ", date=" + date + ", id=" + id + ", rate=" + value
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
      && (Float.compare(this.value, o.value) == 0)
      && this.date == o.date
      && this.id == o.id
      && (Float.compare(this.difference, o.difference) == 0);
  }

  @Override
  public int hashCode() {
    int hashCode = Long.hashCode(id);

    hashCode = 31 * hashCode + Float.hashCode(value);
    hashCode = 31 * hashCode + date.hashCode();
    hashCode = 31 * hashCode + currency.hashCode();

    return hashCode;
  }
}
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

  public Rate(float value, LocalDate date, Currency currency) {
    this.value = value;
    this.date = date;
    this.currency = currency;
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
    if(obj == null) 
      return false;
    if(this == obj) 
      return true;
    if(getClass() != obj.getClass()) 
      return false;
    Rate o = (Rate) obj;
    if(this.value != o.value)
      return false;
    if(this.date != o.date) 
      return false;
    if(this.currency != o.currency)
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 11;
    hashCode = (int) (31 * hashCode + this.value);
    hashCode = 31 * hashCode + (int) this.id;
    hashCode = 31 * hashCode + this.date.hashCode();
    hashCode = 31 * hashCode + this.currency.hashCode();
    return hashCode;
  }
}
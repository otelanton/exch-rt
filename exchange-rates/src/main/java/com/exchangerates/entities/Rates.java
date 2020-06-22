package com.exchangerates.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Rates {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(nullable = false)
  private float rate;
  @Column(nullable = false)
  private LocalDate date;
  @ManyToOne(fetch = FetchType.LAZY)
  private Currency currency;

  public Rates() {
  }

  public Rates(float rate, LocalDate date, Currency currency) {
    this.rate = rate;
    this.date = date;
    this.currency = currency;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public float getRate() {
    return rate;
  }

  public void setRate(float rate) {
    this.rate = rate;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate localDate) {
    this.date = localDate;
  }

  // public Currency getCurrency() {
  // return currency;
  // }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  @Override
  public String toString() {
    return "CurrencyRates [currency=" + currency.getCharCode() + ", date=" + date + ", id=" + id + ", rate=" + rate
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
    Rates o = (Rates) obj;
    if(this.rate != o.rate) 
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
    hashCode = (int) (31 * hashCode + this.rate);
    hashCode = 31 * hashCode + this.id;
    hashCode = 31 * hashCode + this.date.hashCode();
    hashCode = 31 * hashCode + this.currency.hashCode();
    return hashCode;
  }
}
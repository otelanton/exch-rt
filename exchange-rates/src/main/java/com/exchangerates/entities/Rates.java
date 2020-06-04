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
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private int id;
  @Column(nullable = false)
  private float rate;
  @Column(nullable = false)
  private LocalDate date;
  @ManyToOne(fetch=FetchType.LAZY)
  private Currency currency;
  
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

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  @Override
  public String toString() {
    return "CurrencyRates [currency=" 
      + currency.getAbbreviation() 
      + ", date=" 
      + date + ", id=" 
      + id 
      + ", rate=" 
      + rate 
      + "]";
  }
  
}
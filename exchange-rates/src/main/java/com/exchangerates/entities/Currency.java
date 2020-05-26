package com.exchangerates.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Currency {
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private int id;
  @Column(nullable = false, length = 30)
  private String currencyName;
  @Column(nullable = false, length = 3)
  private String abbreviation;
  @Column(nullable = false)
  private int currencyCode;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return currencyName;
  }

  public void setName(String name) {
    this.currencyName = name;
  }

  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public int getcurrencyCode() {
    return currencyCode;
  }

  public void setcurrencyCode(int currencyCode) {
    this.currencyCode = currencyCode;
  }
}
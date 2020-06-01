package com.exchangerates.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Currency {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(nullable = false, length = 30)
  private String currencyName;
  @Column(nullable = false, length = 3)
  private String abbreviation;
  @Column(nullable = false)
  private int currencyCode;
  // @OneToMany(mappedBy = "currency", orphanRemoval = true)
  // private List<CurrencyRates> rates = new ArrayList<>();

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

  // public List<CurrencyRates> getRates() {
  //   return rates;
  // }

  // public void setRates(List<CurrencyRates> rates) {
  //   this.rates = rates;
  // }

  // public void addRate(CurrencyRates currencyRates){
  //   rates.add(currencyRates);
  //   currencyRates.setCurrency(this);
  // }

  // public void removeRate(CurrencyRates currencyRates){
  //   rates.remove(currencyRates);
  //   currencyRates.setCurrency(null);
  // }
}
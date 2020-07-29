package com.exchangerates.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
  private String charCode;
  @Column(nullable = false)
  private int currencyCode;
  @Column(nullable = false)
  private int nominal;
  @OneToMany(mappedBy = "currency",
    orphanRemoval = true, 
    cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY)
  private List<Rate> rates = new ArrayList<>();

  public Currency() {}

  public Currency(int currencyCode, String charCode, int nominal, String currencyName){
    this.currencyCode = currencyCode;
    this.charCode = charCode;
    this.nominal = nominal;
    this.currencyName = currencyName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCharCode() {
    return charCode;
  }

  public void setCharCode(String charCode) {
    this.charCode = charCode;
  }

  public int getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(int currencyCode) {
    this.currencyCode = currencyCode;
  }

  public int getNominal() {
    return nominal;
  }

  public void setNominal(int nominal) {
    this.nominal = nominal;
  }

  public String getCurrencyName() {
    return currencyName;
  }

  public void setCurrencyName(String currencyName) {
    this.currencyName = currencyName;
  }

  public List<Rate> getRates() {
    return rates;
  }

  public void setRates(List<Rate> rates) {
    this.rates = rates;
  }

  public void addRate(Rate currencyRates) {
    rates.add(currencyRates);
    currencyRates.setCurrency(this);
  }

  public void removeRate(Rate currencyRate) {
    rates.remove(currencyRate);
    currencyRate.setCurrency(null);
  }

  public void removeRate(int index) {
    Rate r = rates.get(0);
    r.setCurrency(null);
    rates.remove(r);
  }

  @Override
  public String toString() {
    return "Currency [charCode=" + charCode + ", currencyCode=" + currencyCode + ", currencyName=" + currencyName
        + ", id=" + id + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null) 
      return false;
    if(this == obj) 
      return true;
    if(getClass() != obj.getClass()) 
      return false;
    Currency o = (Currency) obj;
    if(this.currencyCode != o.currencyCode) 
      return false;
    if(this.currencyName != o.currencyName) 
      return false;
    if(this.nominal != o.nominal) 
      return false;
    if(this.charCode != o.charCode)
      return false;
    if(this.id != o.id) 
      return false;
    if(!this.rates.equals(o.rates)) 
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 11;
    hashCode = 31 * hashCode + this.currencyName.hashCode();
    hashCode = 31 * hashCode + this.charCode.hashCode();
    hashCode = 31 * hashCode + this.currencyCode;
    hashCode = 31 * hashCode + this.id;
    return hashCode;
  }
}
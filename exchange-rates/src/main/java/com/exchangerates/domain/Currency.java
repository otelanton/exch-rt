package com.exchangerates.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Currency implements IEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(nullable = false, length = 30)
  private String name;
  @Column(nullable = false, length = 3)
  private String charCode;
  @Column(nullable = false)
  private int code;
  @Column(nullable = false)
  private int nominal;
  @JsonIgnore
  @OneToMany(mappedBy = "currency",
    orphanRemoval = true, 
    cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY)
  private List<Rate> rates = new ArrayList<>();
  @JsonIgnore
  @OneToMany(mappedBy = "foreignKey",
    orphanRemoval = true,
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY)
  private List<Average> averages = new ArrayList<>();

  public Currency() {}

  public Currency(int code, String charCode, int nominal, String name){
    this.code = code;
    this.charCode = charCode;
    this.nominal = nominal;
    this.name = name;
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

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public int getNominal() {
    return nominal;
  }

  public void setNominal(int nominal) {
    this.nominal = nominal;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public List<Average> getAverages(){
    return averages;
  }

  public void setAverages(List<Average> averages){
    this.averages = averages;
  }

  public void addAverage(Average average){
    averages.add(average);
    average.setForeignKey(this);
  }

  public void removeAverage(Average average){
    average.setForeignKey(null);
    averages.remove(average);
  }

  @Override
  public String toString() {
    return "Currency [charCode=" + charCode + ", currencyCode=" + code + ", currencyName=" + name
        + ", id=" + id + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj) 
      return true;
    if(!(obj instanceof Currency))
      return false;

    Currency o = (Currency) obj;

    return this.code == o.code
      && this.name.equals(o.name)
      && this.charCode.equals(o.charCode)
      && this.nominal == o.nominal
      && this.id == o.id;
  }

  @Override
  public int hashCode() {
    int hashCode = Integer.hashCode(id);

    hashCode = 31 * hashCode + name.hashCode();
    hashCode = 31 * hashCode + charCode.hashCode();
    hashCode = 31 * hashCode + Integer.hashCode(code);
    hashCode = 31 * hashCode + Integer.hashCode(nominal);

    return hashCode;
  }
}
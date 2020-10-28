package com.exchangerates.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Month {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(nullable = false, length = 3)
  private String code;
  @Column(nullable = false, length = 10)
  private String name;
  @OneToMany(mappedBy = "month",
    fetch = FetchType.LAZY)
  private List<Average> averages = new ArrayList<>();

  public Month(){}

  public Month(int id, String code, String name) {
    this.id = id;
    this.code = code;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Average> getAverages() {
    return averages;
  }

  public void setAverages(List<Average> averages) {
    this.averages = averages;
  }

  public void addAverage(Average average){
    averages.add(average);
    average.setMonth(this);
  }

  public void removeAverage(Average average){
    averages.remove(average);
    average.setMonth(null);
  }

  @Override
  public boolean equals(Object obj){
    if(this == obj)
      return true;
    if(!(obj instanceof Month))
      return false;
    Month o = (Month) obj;

    return this.id == o.id
      && this.code.equals(o.code)
      && this.name.equals(o.name);
  }

  @Override
  public int hashCode(){
    int hashCode = Integer.hashCode(id);
    hashCode = 31 * hashCode + code.hashCode();
    hashCode = 31 * hashCode + name.hashCode();

    return hashCode;
  }
}

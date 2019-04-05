package com.cqeec.pojo;

import java.lang.Integer;
import java.lang.Override;
import java.lang.String;

public class Role {
  private String name;

  private Integer id;

  private String desc_;

  public Role(String name, Integer id, String desc_) {
    this.name=name;
    this.id=id;
    this.desc_=desc_;
  }

  public Role() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name=name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id=id;
  }

  public String getDesc_() {
    return desc_;
  }

  public void setDesc_(String desc_) {
    this.desc_=desc_;
  }

  @Override
  public String toString() {
    return "["+"name"+"="+this.name+","+"id"+"="+this.id+","+"desc_"+"="+this.desc_+"]";
  }
}

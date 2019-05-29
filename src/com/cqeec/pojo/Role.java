package com.cqeec.pojo;

import java.lang.Long;
import java.lang.Override;
import java.lang.String;

public class Role {
  private String sd_dasd;

  private String name;

  private Long id;

  private String desc_;

  public Role(String sd_dasd, String name, Long id, String desc_) {
    this.sd_dasd=sd_dasd;
    this.name=name;
    this.id=id;
    this.desc_=desc_;
  }

  public Role() {
  }

  public String getSdDasd() {
    return sd_dasd;
  }

  public void setSdDasd(String sd_dasd) {
    this.sd_dasd=sd_dasd;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name=name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id=id;
  }

  public String getDesc() {
    return desc_;
  }

  public void setDesc(String desc_) {
    this.desc_=desc_;
  }

  @Override
  public String toString() {
    return "["+"sd_dasd"+"="+this.sd_dasd+","+"name"+"="+this.name+","+"id"+"="+this.id+","+"desc_"+"="+this.desc_+"]";
  }
}

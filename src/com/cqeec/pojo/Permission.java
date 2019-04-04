package com.cqeec.pojo;

import java.lang.Long;
import java.lang.Override;
import java.lang.String;

public class Permission {
  private String name;

  private Long id;

  private String desc_;

  private String url;

  public Permission(String name, Long id, String desc_, String url) {
    this.name=name;
    this.id=id;
    this.desc_=desc_;
    this.url=url;
  }

  public Permission() {
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

  public String getDesc_() {
    return desc_;
  }

  public void setDesc_(String desc_) {
    this.desc_=desc_;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url=url;
  }

  @Override
  public String toString() {
    return "["+"name"+"="+this.name+","+"id"+"="+this.id+","+"desc_"+"="+this.desc_+","+"url"+"="+this.url+"]";
  }
}

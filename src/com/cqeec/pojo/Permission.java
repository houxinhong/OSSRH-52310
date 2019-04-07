package com.cqeec.pojo;

import java.lang.Long;
import java.lang.Override;
import java.lang.String;

public class Permission {
  private String name;

  private Long id_dsd_dsss;

  private String desc_;

  private String url;

  public Permission(String name, Long id_dsd_dsss, String desc_, String url) {
    this.name=name;
    this.id_dsd_dsss=id_dsd_dsss;
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

  public Long getIdDsdDsss() {
    return id_dsd_dsss;
  }

  public void setIdDsdDsss(Long id_dsd_dsss) {
    this.id_dsd_dsss=id_dsd_dsss;
  }

  public String getDesc() {
    return desc_;
  }

  public void setDesc(String desc_) {
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
    return "["+"name"+"="+this.name+","+"id_dsd_dsss"+"="+this.id_dsd_dsss+","+"desc_"+"="+this.desc_+","+"url"+"="+this.url+"]";
  }
}

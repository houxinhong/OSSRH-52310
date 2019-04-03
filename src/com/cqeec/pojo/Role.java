package com.cqeec.pojo;

import java.lang.Long;
import java.lang.String;

public class Role {
  private String name;

  private Long id;

  private String desc_;

  public Role(String name, Long id, String desc_) {
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

@Override
public String toString() {
	return "Role [name=" + name + ", id=" + id + ", desc_=" + desc_ + "]";
}
  
}

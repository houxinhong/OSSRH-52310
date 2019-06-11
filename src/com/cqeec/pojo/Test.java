package com.cqeec.pojo;

import java.lang.Override;
import java.lang.String;

public class Test {
  private String name;

  private String id;

  public Test(String name, String id) {
    this.name=name;
    this.id=id;
  }

  public Test() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name=name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id=id;
  }

  @Override
  public String toString() {
    return "["+"name"+"="+this.name+","+"id"+"="+this.id+"]";
  }
}

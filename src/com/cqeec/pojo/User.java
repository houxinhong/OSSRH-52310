package com.cqeec.pojo;

import java.lang.Long;
import java.lang.Override;
import java.lang.String;

public class User {
  private String password;

  private String salt;

  private String name;

  private Long id;

  public User(String password, String salt, String name, Long id) {
    this.password=password;
    this.salt=salt;
    this.name=name;
    this.id=id;
  }

  public User() {
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password=password;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt=salt;
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

  @Override
  public String toString() {
    return "["+"password"+"="+this.password+","+"salt"+"="+this.salt+","+"name"+"="+this.name+","+"id"+"="+this.id+"]";
  }
}

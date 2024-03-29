package com.cqeec.pojo;

import java.lang.Long;
import java.lang.Override;
import java.lang.String;

public class UserRole {
  private Long uid;

  private Long id;

  private Long rid;

  public UserRole(Long uid, Long id, Long rid) {
    this.uid=uid;
    this.id=id;
    this.rid=rid;
  }

  public UserRole() {
  }

  public Long getUid() {
    return uid;
  }

  public void setUid(Long uid) {
    this.uid=uid;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id=id;
  }

  public Long getRid() {
    return rid;
  }

  public void setRid(Long rid) {
    this.rid=rid;
  }

  @Override
  public String toString() {
    return "["+"uid"+"="+this.uid+","+"id"+"="+this.id+","+"rid"+"="+this.rid+"]";
  }
}

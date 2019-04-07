package com.cqeec.pojo;

import java.lang.Long;
import java.lang.Override;
import java.lang.String;

public class RolePermission {
  private Long pid;

  private Long id;

  private Long rid;

  public RolePermission(Long pid, Long id, Long rid) {
    this.pid=pid;
    this.id=id;
    this.rid=rid;
  }

  public RolePermission() {
  }

  public Long getPid() {
    return pid;
  }

  public void setPid(Long pid) {
    this.pid=pid;
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
    return "["+"pid"+"="+this.pid+","+"id"+"="+this.id+","+"rid"+"="+this.rid+"]";
  }
}

package com.cqeec.pojo;

import java.lang.Long;

public class RolePermission {
  private Long pid;

  private Long id;

  private Long rid;

  RolePermission(Long pid, Long id, Long rid) {
    this.pid=pid;
    this.id=id;
    this.rid=rid;
  }

  RolePermission() {
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
}

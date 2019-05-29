package com.cqeec.pojo;

import java.lang.Long;
import java.lang.Override;
import java.lang.String;

import com.cqeec.annotation.Id;
import com.cqeec.annotation.Table;
@Table("permission")
public class Permissions {
  private String name;
  @Id("id")
  private Long idss;

  private String desc_;

  private String url;


  public Permissions() {
  }


public Permissions(String name, Long idss, String desc_, String url) {
	this.name = name;
	this.idss = idss;
	this.desc_ = desc_;
	this.url = url;
}


/**
 * @return the name
 */
public String getName() {
	return name;
}


/**
 * @param name the name to set
 */
public void setName(String name) {
	this.name = name;
}


/**
 * @return the idss
 */
public Long getIdss() {
	return idss;
}


/**
 * @param idss the idss to set
 */
public void setIdss(Long idss) {
	this.idss = idss;
}


/**
 * @return the desc_
 */
public String getDesc() {
	return desc_;
}


/**
 * @param desc_ the desc_ to set
 */
public void setDesc(String desc) {
	this.desc_ = desc;
}


/**
 * @return the url
 */
public String getUrl() {
	return url;
}


/**
 * @param url the url to set
 */
public void setUrl(String url) {
	this.url = url;
}


/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
	return "Permissions [name=" + name + ", idss=" + idss + ", desc_=" + desc_ + ", url=" + url + "]";
}
  

}

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author VFE1COB
 *
 */
@XmlRootElement(name="data")
public class WorkItemType{
  @XmlAttribute
private String name;
  @XmlAttribute
private String id;
  @XmlAttribute
private String category;
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
 * @return the id
 */
public String getId() {
  return id;
}
/**
 * @param id the id to set
 */
public void setId(String id) {
  this.id = id;
}
/**
 * @return the category
 */
public String getCategory() {
  return category;
}
/**
 * @param category the category to set
 */
public void setCategory(String category) {
  this.category = category;
}
}

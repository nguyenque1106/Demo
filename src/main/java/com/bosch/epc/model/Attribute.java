/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import org.springframework.stereotype.Component;

/**
 * @author VFE1COB
 */
@Component
public class Attribute {

  private String iD;
  private String name;
  private String type;
  private boolean isCustomAttribute;
  private String workItem;

  /**
   * @return the isCustomAttribute
   */
  public boolean isCustomAttribute() {
    return this.isCustomAttribute;
  }

  /**
   * @param isCustomAttribute the isCustomAttribute to set
   */
  public void setCustomAttribute(final boolean isCustomAttribute) {
    this.isCustomAttribute = isCustomAttribute;
  }

  /**
   * @return the workItem
   */
  public String getWorkItem() {
    return this.workItem;
  }

  /**
   * @param workItem the workItem to set
   */
  public void setWorkItem(final String workItem) {
    this.workItem = workItem;
  }

  /**
   * @return the iD
   */
  public String getiD() {
    return this.iD;
  }

  /**
   * @param iD the iD to set
   */
  public void setiD(final String iD) {
    this.iD = iD;
  }

  /**
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * @param name the name to set
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * @return the type
   */
  public String getType() {
    return this.type;
  }

  /**
   * @param type the type to set
   */
  public void setType(final String type) {
    this.type = type;
  }


}

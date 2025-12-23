/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author VFE1COB
 *
 */
@XmlRootElement(name = "role-definition")
@XmlAccessorType(XmlAccessType.FIELD)
public class RoleDefinitionBean {
  @XmlAttribute(name="role-id")
  private String roleID;
  @XmlAttribute
  private String name;
  @XmlAttribute
  private String description;
  @XmlAttribute
  private String cardinality;

  /**
   * @return the roleID
   */
  public String getRoleID() {
    return roleID;
  }

  /**
   * @param roleID the roleID to set
   */
  public void setRoleID(String roleID) {
    this.roleID = roleID;
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
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the cardinality
   */
  public String getCardinality() {
    return cardinality;
  }

  /**
   * @param cardinality the cardinality to set
   */
  public void setCardinality(String cardinality) {
    this.cardinality = cardinality;
  }
  

}

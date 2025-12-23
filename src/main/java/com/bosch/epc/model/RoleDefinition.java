/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author VFE1COB
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoleDefinition {
  private String name;
  private String description;
  private String roleID;
  private String cardinality;
  //ID before modifying
  private String modifiedID;
  /**
   *  Default constructor to fix Jackson deserialization issue
   */
  public RoleDefinition() {
    
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
  /**
   * @return the modifiedID
   */
  public String getModifiedID() {
    return modifiedID;
  }
  /**
   * @param modifiedID the modifiedID to set
   */
  public void setModifiedID(String modifiedID) {
    this.modifiedID = modifiedID;
  }
}

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author QYU1HC
 */
@Embeddable
public class StagesRoleELMRoleMappingId implements Serializable {

  @JsonProperty("elmRoleId")
  private Integer roleId;

  @JsonProperty("stagesRoleId")
  private Integer stagesId;

  /**
   * the contructor function
   */
  public StagesRoleELMRoleMappingId() {}

  /**
   * @param stagesId
   * @param roleId
   */
  public StagesRoleELMRoleMappingId(final Integer stagesId, final Integer roleId) {
    this.roleId = roleId;
    this.stagesId = stagesId;
  }


  /**
   * @return the roleId
   */
  public Integer getRoleId() {
    return this.roleId;
  }


  /**
   * @param roleId the roleId to set
   */
  public void setRoleId(final Integer roleId) {
    this.roleId = roleId;
  }


  /**
   * @return the stagesId
   */
  public Integer getStagesId() {
    return this.stagesId;
  }


  /**
   * @param stagesId the stagesId to set
   */
  public void setStagesId(final Integer stagesId) {
    this.stagesId = stagesId;
  }


}


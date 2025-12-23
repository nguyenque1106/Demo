/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * @author QYU1HC
 */
@Embeddable
public class StagesRoleELMRoleReqMappingId implements Serializable {

  private Integer stagesRoleId;
  private Integer elmRoleId;
  private Integer requestId;

  /**
   * the deault contructor function
   */
  public StagesRoleELMRoleReqMappingId() {

  }

  /**
   * @param stagesRoleId
   * @param elmRoleId
   * @param requestId
   */
  public StagesRoleELMRoleReqMappingId(final Integer stagesRoleId, final Integer elmRoleId, final Integer requestId) {
    super();
    this.stagesRoleId = stagesRoleId;
    this.elmRoleId = elmRoleId;
    this.requestId = requestId;
  }


  /**
   * @return the stagesRoleId
   */
  public Integer getStagesRoleId() {
    return this.stagesRoleId;
  }


  /**
   * @param stagesRoleId the stagesId to set
   */
  public void setStagesRoleId(final Integer stagesRoleId) {
    this.stagesRoleId = stagesRoleId;
  }


  /**
   * @return the elmRoleId
   */
  public Integer getElmRoleId() {
    return this.elmRoleId;
  }


  /**
   * @param roleId the roleId to set
   */
  public void setElmRoleId(final Integer elmRoleId) {
    this.elmRoleId = elmRoleId;
  }


  /**
   * @return the requestId
   */
  public Integer getRequestId() {
    return this.requestId;
  }


  /**
   * @param requestId the requestId to set
   */
  public void setRequestId(final Integer requestId) {
    this.requestId = requestId;
  }
}

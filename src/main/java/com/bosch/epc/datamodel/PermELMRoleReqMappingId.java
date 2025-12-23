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
public class PermELMRoleReqMappingId implements Serializable {

  private Integer elmRoleId;
  private Integer permissionId;
  private Integer requestId;

  /**
   *
   */
  public PermELMRoleReqMappingId() {
    super();
  }

  /**
   * @param elmRoleId
   * @param permissionId
   * @param requestId
   */
  public PermELMRoleReqMappingId(final Integer elmRoleId, final Integer permissionId, final Integer requestId) {
    super();
    this.elmRoleId = elmRoleId;
    this.permissionId = permissionId;
    this.requestId = requestId;
  }

  /**
   * @return the elmRoleId
   */
  public Integer getElmRoleId() {
    return this.elmRoleId;
  }

  /**
   * @param elmRoleId the elmRoleId to set
   */
  public void setElmRoleId(final Integer elmRoleId) {
    this.elmRoleId = elmRoleId;
  }

  /**
   * @return the permissionId
   */
  public Integer getPermissionId() {
    return this.permissionId;
  }

  /**
   * @param permissionId the permissionId to set
   */
  public void setPermissionId(final Integer permissionId) {
    this.permissionId = permissionId;
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

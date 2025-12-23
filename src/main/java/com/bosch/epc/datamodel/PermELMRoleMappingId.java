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
public class PermELMRoleMappingId implements Serializable {

  private Integer elmRoleId;
  private Integer permissionId;

  // Default constructor
  /**
   * the default contructor
   */
  public PermELMRoleMappingId() {}

  /**
   * @param elmRoleId
   * @param permissionId
   */
  public PermELMRoleMappingId(final Integer elmRoleId, final Integer permissionId) {
    this.elmRoleId = elmRoleId;
    this.permissionId = permissionId;
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
}

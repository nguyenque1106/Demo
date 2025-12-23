/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.PermELMRoleMappingId;
import com.bosch.epc.datamodel.ELMPermissions;

/**
 * @author QYU1HC
 */
public class PermELMRoleResponse {

  private PermELMRoleMappingId id;
  private ELMRole elmRole;
  private ELMPermissions permission;
  private boolean isPermitted;
  private boolean isUnderProgress;

  /**
   *
   */
  public PermELMRoleResponse() {
    super();
  }

  /**
   * @return the id
   */
  public PermELMRoleMappingId getId() {
    return this.id;
  }

  /**
   * @param id the id to set
   */
  public void setId(final PermELMRoleMappingId id) {
    this.id = id;
  }

  /**
   * @return the elmRole
   */
  public ELMRole getElmRole() {
    return this.elmRole;
  }

  /**
   * @param elmRole the elmRole to set
   */
  public void setElmRole(final ELMRole elmRole) {
    this.elmRole = elmRole;
  }

  /**
   * @return the permission
   */
  public ELMPermissions getPermission() {
    return this.permission;
  }

  /**
   * @param permission the permission to set
   */
  public void setPermission(final ELMPermissions permission) {
    this.permission = permission;
  }


  /**
   * @return the isPermitted
   */
  public boolean isPermitted() {
    return this.isPermitted;
  }


  /**
   * @param isPermitted the isPermitted to set
   */
  public void setPermitted(final boolean isPermitted) {
    this.isPermitted = isPermitted;
  }


  /**
   * @return the isUnderProgress
   */
  public boolean isUnderProgress() {
    return this.isUnderProgress;
  }


  /**
   * @param isUnderProgress the isUnderProgress to set
   */
  public void setUnderProgress(final boolean isUnderProgress) {
    this.isUnderProgress = isUnderProgress;
  }
}

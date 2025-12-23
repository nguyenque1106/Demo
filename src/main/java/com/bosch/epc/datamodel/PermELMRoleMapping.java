/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 * @author QYU1HC
 */
@Entity
@Table(name = "perm_role_mapping")
public class PermELMRoleMapping {

  @EmbeddedId
  @AttributeOverrides({
      @AttributeOverride(name = "elmRoleId", column = @Column(name = "elmRoleId")),
      @AttributeOverride(name = "permissionId", column = @Column(name = "permissionId")) })
  private PermELMRoleMappingId id;

  @ManyToOne
  @MapsId("elmRoleId")
  @JoinColumn(name = "elmRoleId")
  private ELMRole elmRole;

  @ManyToOne
  @MapsId("permissionId")
  @JoinColumn(name = "permissionId")
  private ELMPermissions permission;

  private boolean isPermitted;
  private boolean isUnderProgress;

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

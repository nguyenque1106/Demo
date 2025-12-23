/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

/**
 * Composite primary key for role-permission mapping.
 *
 * @author QYU1HC
 */
@Embeddable
public class RolePermMappingId implements Serializable {

  private static final long serialVersionUID = 1L;

  private int roleId;
  private int permissionId;

  /**
   * Default constructor required by JPA.
   */
  public RolePermMappingId() {
    // Default constructor
  }

  /**
   * Constructor with all required fields.
   * 
   * @param roleId the role identifier
   * @param permissionId the permission identifier
   */
  public RolePermMappingId(final int roleId, final int permissionId) {
    this.roleId = roleId;
    this.permissionId = permissionId;
  }

  public int getRoleId() {
    return this.roleId;
  }

  public void setRoleId(final int roleId) {
    this.roleId = roleId;
  }

  public int getPermissionId() {
    return this.permissionId;
  }

  public void setPermissionId(final int permissionId) {
    this.permissionId = permissionId;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    RolePermMappingId that = (RolePermMappingId) obj;
    return (this.roleId == that.roleId) && (this.permissionId == that.permissionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.roleId, this.permissionId);
  }

  @Override
  public String toString() {
    return "RolePermissionMappingId{" + "roleId=" + this.roleId + ", permissionId=" + this.permissionId + '}';
  }
}
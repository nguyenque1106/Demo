/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

/**
 * Composite primary key for role-permission-request mapping.
 *
 * @author QYU1HC
 */
@Embeddable
public class RolePermReqtMappingId implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer roleId;
  private Integer permissionId;
  private Integer requestId;

  /**
   * Default constructor required by JPA.
   */
  public RolePermReqtMappingId() {
    // Default constructor
  }

  /**
   * Constructor with all required fields.
   * 
   * @param roleId the role identifier
   * @param permissionId the permission identifier
   * @param requestId the request identifier
   */
  public RolePermReqtMappingId(final Integer roleId, final Integer permissionId, final Integer requestId) {
    this.roleId = roleId;
    this.permissionId = permissionId;
    this.requestId = requestId;
  }

  public Integer getRoleId() {
    return this.roleId;
  }

  public void setRoleId(final Integer roleId) {
    this.roleId = roleId;
  }

  public Integer getPermissionId() {
    return this.permissionId;
  }

  public void setPermissionId(final Integer permissionId) {
    this.permissionId = permissionId;
  }

  public Integer getRequestId() {
    return this.requestId;
  }

  public void setRequestId(final Integer requestId) {
    this.requestId = requestId;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    RolePermReqtMappingId that = (RolePermReqtMappingId) obj;
    return Objects.equals(this.roleId, that.roleId) && Objects.equals(this.permissionId, that.permissionId) &&
        Objects.equals(this.requestId, that.requestId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.roleId, this.permissionId, this.requestId);
  }

  @Override
  public String toString() {
    return "RolePermissionRequestMappingId{" + "roleId=" + this.roleId + ", permissionId=" + this.permissionId +
        ", requestId=" + this.requestId + '}';
  }
}
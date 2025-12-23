/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 * Entity representing the mapping between roles and permissions.
 *
 * @author QYU1HC
 */
@Entity
@Table(name = "role_perm_act_mapping")
public class RolePermMapping {

  @EmbeddedId
  private RolePermMappingId id;

  @Column(name = "isPermitted", nullable = false)
  private boolean isPermitted;

  @Column(name = "isRequestPresent", nullable = false)
  private int isRequestPresent;

  @ManyToOne
  @MapsId("roleId")
  @JoinColumn(name = "roleId", referencedColumnName = "id")
  private ELMRole role;

  @ManyToOne
  @MapsId("permissionId")
  @JoinColumn(name = "permissionId", referencedColumnName = "id")
  private ELMPermissions permission;


  /**
   * Default constructor required by JPA.
   */
  public RolePermMapping() {
    // Default constructor
  }


  /**
   * Constructor with all required fields.
   *
   * @param role the role
   * @param permission the permission
   * @param isPermitted whether the permission is granted
   * @param isRequestPresent whether a request is present for this mapping
   */
  public RolePermMapping(final ELMRole role, final ELMPermissions permission, final boolean isPermitted,
      final int isRequestPresent) {
    this.role = role;
    this.permission = permission;
    this.isPermitted = isPermitted;
    this.isRequestPresent = isRequestPresent;
    this.id = new RolePermMappingId(role.getId(), permission.getId());
  }

  public RolePermMappingId getId() {
    return this.id;
  }

  public void setId(final RolePermMappingId id) {
    this.id = id;
  }

  public boolean isPermitted() {
    return this.isPermitted;
  }

  public void setPermitted(final boolean isPermitted) {
    this.isPermitted = isPermitted;
  }

  /**
   * @return the isRequestPresent
   */
  public int getIsRequestPresent() {
    return this.isRequestPresent;
  }

  /**
   * @param isRequestPresent the isRequestPresent to set
   */
  public void setIsRequestPresent(final int isRequestPresent) {
    this.isRequestPresent = isRequestPresent;
  }

  public ELMRole getRole() {
    return this.role;
  }


  public void setRole(final ELMRole role) {
    this.role = role;
  }

  public ELMPermissions getPermission() {
    return this.permission;
  }

  public void setPermission(final ELMPermissions permission) {
    this.permission = permission;
  }


  @Override
  public String toString() {
    return "RolePermissionMapping{" + "id=" + this.id + ", isPermitted=" + this.isPermitted + ", isRequestPresent=" +
        this.isRequestPresent + ", role=" + (this.role != null ? this.role.getId() : null) + ", permission=" +
        (this.permission != null ? this.permission.getId() : null) + '}';
  }
}

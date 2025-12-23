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

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Entity representing the mapping between roles, permissions, and requests.
 *
 * @author VFE1COB
 */
@Entity
@Table(name = "role_perm_act_req_mapping")
public class RolePermReqtMapping {

  @EmbeddedId
  private RolePermReqtMappingId id;

  @Column(name = "isPermitted", nullable = false)
  private boolean isPermitted;

  @ManyToOne
  @MapsId("roleId")
  @JoinColumn(name = "roleId", referencedColumnName = "id")
  private ELMRole role;

  @ManyToOne
  @MapsId("permissionId")
  @JoinColumn(name = "permissionId", referencedColumnName = "id")
  private ELMPermissions permission;

  @ManyToOne
  @MapsId("requestId")
  @JoinColumn(name = "requestId", referencedColumnName = "id")
  @JsonBackReference
  private Request request;

  /**
   * Default constructor required by JPA.
   */
  public RolePermReqtMapping() {
    // Default constructor
  }

  /**
   * Constructor with all required fields.
   *
   * @param role the role
   * @param permission the permission
   * @param request the request
   * @param isPermitted whether the permission is granted
   */
  public RolePermReqtMapping(final ELMRole role, final ELMPermissions permission, final Request request,
      final boolean isPermitted) {
    this.role = role;
    this.permission = permission;
    this.request = request;
    this.isPermitted = isPermitted;
    this.id = new RolePermReqtMappingId(role.getId(), permission.getId(), request.getId());
  }

  public RolePermReqtMappingId getId() {
    return this.id;
  }

  public void setId(final RolePermReqtMappingId id) {
    this.id = id;
  }

  public void setPermitted(final boolean isPermitted) {
    this.isPermitted = isPermitted;
  }

  public boolean isPermitted() {
    return this.isPermitted;
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

  public Request getRequest() {
    return this.request;
  }

  public void setRequest(final Request request) {
    this.request = request;
  }

  @Override
  public String toString() {
    return "RolePermissionRequestMapping{" + "id=" + this.id + ", isPermitted='" + this.isPermitted + '\'' + ", role=" +
        (this.role != null ? this.role.getId() : null) + ", permission=" +
        (this.permission != null ? this.permission.getId() : null) + ", request=" +
        (this.request != null ? this.request.getId() : null) + '}';
  }
}
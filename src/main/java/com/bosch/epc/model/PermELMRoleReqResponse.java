/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.PermELMRoleReqMappingId;
import com.bosch.epc.datamodel.ELMPermissions;
import com.bosch.epc.datamodel.Request;

/**
 * @author QYU1HC
 */
public class PermELMRoleReqResponse {

  private PermELMRoleReqMappingId id;
  private ELMRole elmRole;
  private ELMPermissions permission;
  private Request request;
  private boolean isPermitted;

  /**
   *
   */
  public PermELMRoleReqResponse() {
    super();
  }

  /**
   * @param id
   * @param elmRole
   * @param permission
   * @param request
   */
  public PermELMRoleReqResponse(final PermELMRoleReqMappingId id, final ELMRole elmRole, final ELMPermissions permission,
      final Request request) {
    super();
    this.id = id;
    this.elmRole = elmRole;
    this.permission = permission;
    this.request = request;
  }

  /**
   * @return the id
   */
  public PermELMRoleReqMappingId getId() {
    return this.id;
  }

  /**
   * @param id the id to set
   */
  public void setId(final PermELMRoleReqMappingId id) {
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
   * @return the request
   */
  public Request getRequest() {
    return this.request;
  }


  /**
   * @param request the request to set
   */
  public void setRequest(final Request request) {
    this.request = request;
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
}

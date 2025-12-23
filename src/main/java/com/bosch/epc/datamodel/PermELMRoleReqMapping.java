/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author QYU1HC
 */
@Entity
@Table(name = "perm_role_req_mapping")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PermELMRoleReqMapping {

  @EmbeddedId
  @AttributeOverrides({
      @AttributeOverride(name = "elmRoleId", column = @Column(name = "elmRoleId")),
      @AttributeOverride(name = "permissionId", column = @Column(name = "permissionId")),
      @AttributeOverride(name = "requestId", column = @Column(name = "requestId")) })
  private PermELMRoleReqMappingId id;
  private boolean isPermitted;

  @ManyToOne(cascade = CascadeType.MERGE)
  @MapsId("elmRoleId")
  @JoinColumn(name = "elmRoleId")
  private ELMRole elmRole;

  @ManyToOne(cascade = CascadeType.MERGE)
  @MapsId("permissionId")
  @JoinColumn(name = "permissionId")
  private ELMPermissions permission;

  @ManyToOne(cascade = CascadeType.MERGE)
  @MapsId("requestId")
  @JoinColumn(name = "requestId")
  @JsonBackReference
  private Request request;


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



}

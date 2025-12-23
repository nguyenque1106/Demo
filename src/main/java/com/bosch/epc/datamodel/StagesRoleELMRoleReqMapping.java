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

/**
 * @author QYU1HC
 */
@Entity
@Table(name = "stagesrole_role_req_mapping")
public class StagesRoleELMRoleReqMapping {

  @EmbeddedId
  @AttributeOverrides({
      @AttributeOverride(name = "stagesRoleId", column = @Column(name = "stagesRoleId")),
      @AttributeOverride(name = "elmRoleId", column = @Column(name = "elmRoleId")),
      @AttributeOverride(name = "requestId", column = @Column(name = "requestId")) })
 
  private StagesRoleELMRoleReqMappingId id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @MapsId("stagesRoleId")
  @JoinColumn(name = "stagesRoleId")
  private StagesRole stagesRole;
  @Column(name="operation")
  private String operation;
  @ManyToOne(cascade = CascadeType.MERGE)
  @MapsId("elmRoleId")
  @JoinColumn(name = "elmRoleId")
  private ELMRole elmRole;

  @ManyToOne
  @MapsId("requestId")
  @JoinColumn(name = "requestId", nullable = false)
  @JsonBackReference
  private Request request;

  /**
   *
   */
  public StagesRoleELMRoleReqMapping() {
    super();
  }


  /**
   * @return the id
   */
  public StagesRoleELMRoleReqMappingId getId() {
    return this.id;
  }


  /**
   * @param id the id to set
   */
  public void setId(final StagesRoleELMRoleReqMappingId id) {
    this.id = id;
  }


  /**
   * @return the stagesRole
   */
  public StagesRole getStagesRole() {
    return this.stagesRole;
  }


  /**
   * @param stagesRole the stagesRole to set
   */
  public void setStagesRole(final StagesRole stagesRole) {
    this.stagesRole = stagesRole;
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
   * @return the operation
   */
  public String getOperation() {
    return operation;
  }


  /**
   * @param operation the operation to set
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }
}

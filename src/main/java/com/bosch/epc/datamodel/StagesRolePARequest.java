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

import com.bosch.epc.model.StagesRolePARequestID;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author QYU1HC
 */
@Entity
@Table(name = "stagesrole_pa_req_mapping")
public class StagesRolePARequest {

  @EmbeddedId
  private StagesRolePARequestID id;

  @ManyToOne
  @MapsId("stagesRoleId")
  @JoinColumn(name = "stagesRoleId")
  private StagesRole stagesRole;

  @ManyToOne
  @MapsId("projectAreaId")
  @JoinColumn(name = "projectAreaId", nullable = false)
  private ProjectArea projectArea;

  @ManyToOne
  @MapsId("requestId")
  @JoinColumn(name = "requestId", nullable = false)
  @JsonBackReference
  private Request request;

  @Column(name = "operation")
  private String operation;


  /**
   * @return the id
   */
  public StagesRolePARequestID getId() {
    return this.id;
  }


  /**
   * @param id the id to set
   */
  public void setId(final StagesRolePARequestID id) {
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
   * @return the projectArea
   */
  public ProjectArea getProjectArea() {
    return this.projectArea;
  }


  /**
   * @param projectArea the projectArea to set
   */
  public void setProjectArea(final ProjectArea projectArea) {
    this.projectArea = projectArea;
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
    return this.operation;
  }


  /**
   * @param operation the operation to set
   */
  public void setOperation(final String operation) {
    this.operation = operation;
  }
}

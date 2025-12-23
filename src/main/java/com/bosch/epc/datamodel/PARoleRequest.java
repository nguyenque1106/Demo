/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * @author VFE1COB
 *
 */
@Entity
@Table(name="pa_role_req")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PARoleRequest {
  

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name="operation")
  private String operation;
  
  @ManyToOne
  @JoinColumn(name = "projectAreaId")
  private ProjectArea projectArea;
  private String name;
  private String identifier;
  
  @ManyToOne
  @JoinColumn(name = "requestId", nullable = false) // Maps the foreign key column
  @JsonBackReference
  private Request request;

  
  
  /**
   * @return the request
   */
  public Request getRequest() {
    return request;
  }

  
  /**
   * @param request the request to set
   */
  public void setRequest(Request request) {
    this.request = request;
  }
  
  /**
   * @return the projectArea
   */
  public ProjectArea getProjectArea() {
    return projectArea;
  }
  
  /**
   * @param projectArea the projectArea to set
   */
  public void setProjectArea(ProjectArea projectArea) {
    this.projectArea = projectArea;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  /**
   * @return the identifier
   */
  public String getIdentifier() {
    return identifier;
  }
  /**
   * @param identifier the identifier to set
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
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

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author PPT4KOR
 */
@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Workflow {

  @JsonIgnore
  private int id;
  private String workitemType;
  @JsonIgnore
  private String status;
  private String resolution;
  private String statusGroup;
  private List<Role> roles = new ArrayList<>();

  /**
   * Default constructor to fix Jackson deserialization issue
   */
  public Workflow() {}

  /**
   * @return the statusGroup
   */
  public String getStatusGroup() {
    return this.statusGroup;
  }

  /**
   * @param statusGroup the statusGroup to set
   */
  public void setStatusGroup(final String statusGroup) {
    this.statusGroup = statusGroup;
  }


  /**
   * @return the status
   */
  public String getStatus() {
    return this.status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(final String status) {
    this.status = status;
  }

  /**
   * @return the id
   */
  public int getId() {
    return this.id;
  }

  /**
   * @param id the id to set
   */
  public void setId(final int id) {
    this.id = id;
  }


  /**
   * @return the workitemType
   */
  public String getWorkitemType() {
    return this.workitemType;
  }


  /**
   * @param workitemType the workitemType to set
   */
  public void setWorkitemType(final String workitemType) {
    this.workitemType = workitemType;
  }

  /**
   * @return the resolution
   */
  public String getResolution() {
    return this.resolution;
  }

  /**
   * @param resolution the resolution to set
   */
  public void setResolution(final String resolution) {
    this.resolution = resolution;
  }


  /**
   * @return the epcRoles
   */
  public List<Role> getRoles() {
    return this.roles;
  }


  /**
   * @param epcRoles the epcRoles to set
   */
  public void setRoles(final List<Role> epcRoles) {
    this.roles = epcRoles;
  }


}

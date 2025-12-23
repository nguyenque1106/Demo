/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author VFE1COB
 */
public class ProjectAreaRole {

  @JsonIgnore
  private int id;
  private int projectAreaId;
  @JsonIgnore
  private String name;
  @JsonIgnore
  private String identifier;
  private int requestId;

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
   * @return the projectAreaId
   */
  public int getProjectAreaId() {
    return this.projectAreaId;
  }

  /**
   * @param projectAreaId the projectAreaId to set
   */
  public void setProjectAreaId(final int projectAreaId) {
    this.projectAreaId = projectAreaId;
  }

  /**
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * @param name the name to set
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * @return the identifier
   */
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * @param identifier the identifier to set
   */
  public void setIdentifier(final String identifier) {
    this.identifier = identifier;
  }

  /**
   * @return the requestId
   */
  public int getRequestId() {
    return this.requestId;
  }

  /**
   * @param requestId the requestId to set
   */
  public void setRequestId(final int requestId) {
    this.requestId = requestId;
  }


}

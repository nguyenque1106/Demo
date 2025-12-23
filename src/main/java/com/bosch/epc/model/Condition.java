/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author PPT4KOR
 */
@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Condition {

  private String id;
  private String name;
  private String providerId;
  private List<Workflow> epcWorkflows = new ArrayList<>();
  private String createdBy;
  private String modifiedBy;
  private Date createdDate;
  private Date modifiedDate;

  /**
   * Default constructor to fix Jackson deserialization issue
   */
  public Condition() {
  }
  /**
   * @return the id
   */
  public String getId() {
    return id;
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
   * @return the providerId
   */
  public String getProviderId() {
    return providerId;
  }


  /**
   * @param providerId the providerId to set
   */
  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the createdBy
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * @param createdBy the createdBy to set
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * @return the modifiedBy
   */
  public String getModifiedBy() {
    return modifiedBy;
  }

  /**
   * @param modifiedBy the modifiedBy to set
   */
  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  /**
   * @return the createdDate
   */
  public Date getCreatedDate() {
    return createdDate;
  }

  /**
   * @param createdDate the createdDate to set
   */
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * @return the modifiedDate
   */
  public Date getModifiedDate() {
    return modifiedDate;
  }

  /**
   * @param modifiedDate the modifiedDate to set
   */
  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  /**
   * @return the epcWorkflows
   */
  public List<Workflow> getEpcWorkflows() {
    return epcWorkflows;
  }


  /**
   * @param epcWorkflows the epcWorkflows to set
   */
  public void setEpcWorkflows(List<Workflow> epcWorkflows) {
    this.epcWorkflows = epcWorkflows;
  }

}

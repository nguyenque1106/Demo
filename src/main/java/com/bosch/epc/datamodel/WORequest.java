/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author QYU1HC
 */
@Entity
@Table(name = "request")
public class WORequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "changeset_name", nullable = false)
  private String changesetName;
  @Column(name = "workonid", nullable = false)
  private String workonId;
  private String status;
  @Column(name = "createdby")
  private String createdBy;
  @Column(name = "modifiedby")
  private String modifiedBy;
  @Column(name = "creationdate")
  private Date createdDate;
  @Column(name = "modifieddate")
  private Date modifiedDate;

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
   * @return the changesetName
   */
  public String getChangesetName() {
    return this.changesetName;
  }

  /**
   * @param changesetName the changesetName to set
   */
  public void setChangesetName(final String changesetName) {
    this.changesetName = changesetName;
  }

  /**
   * @return the workonId
   */
  public String getWorkonId() {
    return this.workonId;
  }

  /**
   * @param workonId the workonId to set
   */
  public void setWorkonId(final String workonId) {
    this.workonId = workonId;
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
   * @return the createdBy
   */
  public String getCreatedBy() {
    return this.createdBy;
  }

  /**
   * @param createdBy the createdBy to set
   */
  public void setCreatedBy(final String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * @return the modifiedBy
   */
  public String getModifiedBy() {
    return this.modifiedBy;
  }

  /**
   * @param modifiedBy the modifiedBy to set
   */
  public void setModifiedBy(final String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  /**
   * @return the createdDate
   */
  public Date getCreatedDate() {
    return this.createdDate;
  }

  /**
   * @param createdDate the createdDate to set
   */
  public void setCreatedDate(final Date createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * @return the modifiedDate
   */
  public Date getModifiedDate() {
    return this.modifiedDate;
  }

  /**
   * @param modifiedDate the modifiedDate to set
   */
  public void setModifiedDate(final Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

}

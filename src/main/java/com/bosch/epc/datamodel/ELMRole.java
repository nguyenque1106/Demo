/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author QYU1HC
 */
@Entity
@Table(name = "pa_role")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ELMRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "projectAreaId", referencedColumnName = "id", nullable = false)
  @JsonIgnoreProperties({ "elmRoles" })
  private ProjectArea projectArea;

  private String name;
  private String identifier;
  private String createdBy;
  private Date creationDate;
  private String modifiedBy;
  private Date modificationDate;

  /**
   *
   */
  public ELMRole() {}

  /**
   * @param name
   * @param projectArea
   */
  public ELMRole(final String name, final ProjectArea projectArea) {
    this.name = name;
    this.projectArea= projectArea;
    this.creationDate = new Date(System.currentTimeMillis());
  }

  /**
   * @param name
   * @param projectArea
   * @param identifier
   */
  public ELMRole(final String name, final ProjectArea projectArea, final String identifier) {
    this(name, projectArea);
    this.identifier = identifier;
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
   * @return the creationDate
   */
  public Date getCreationDate() {
    return this.creationDate;
  }

  /**
   * @param creationDate the creationDate to set
   */
  public void setCreationDate(final Date creationDate) {
    this.creationDate = creationDate;
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
   * @return the modificationDate
   */
  public Date getModificationDate() {
    return this.modificationDate;
  }

  /**
   * @param modificationDate the modificationDate to set
   */
  public void setModificationDate(final Date modificationDate) {
    this.modificationDate = modificationDate;
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


}

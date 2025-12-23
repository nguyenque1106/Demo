/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;


import java.sql.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author QYU1HC
 */
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "projectarea")
public class ProjectArea {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String uuid;
  private String name;
  private Boolean isArchived;

  @ManyToOne
  @JoinColumn(name = "masterPAId")
  private ProjectArea masterPA;

  @OneToMany(mappedBy = "projectArea", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ELMRole> elmRoles;

  private String createdBy;
  private Date creationDate;
  private String modifiedBy;
  private Date modificationDate;

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
   * @return the uuid
   */
  public String getUuid() {
    return this.uuid;
  }

  /**
   * @param uuid the uuid to set
   */
  public void setUuid(final String uuid) {
    this.uuid = uuid;
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
   * @return the isArchived
   */
  public Boolean getIsArchived() {
    return this.isArchived;
  }

  /**
   * @param isArchived the isArchived to set
   */
  public void setIsArchived(final Boolean isArchived) {
    this.isArchived = isArchived;
  }

  /**
   * @return the masterPA
   */
  public ProjectArea getMasterPA() {
    return this.masterPA;
  }

  /**
   * @param masterPA the masterPA to set
   */
  public void setMasterPA(final ProjectArea masterPA) {
    this.masterPA = masterPA;
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
   * @return the elmRoles
   */
  public List<ELMRole> getElmRoles() {
    return this.elmRoles;
  }


  /**
   * @param elmRoles the elmRoles to set
   */
  public void setElmRoles(final List<ELMRole> elmRoles) {
    this.elmRoles = elmRoles;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProjectArea)) {
      return false;
    }
    ProjectArea projectArea = (ProjectArea) o;
    return Objects.equals(this.uuid, projectArea.uuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.uuid);
  }

}

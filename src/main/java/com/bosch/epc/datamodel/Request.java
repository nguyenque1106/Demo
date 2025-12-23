/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * @author VFE1COB
 */
@Entity
@Table(name = "request")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Request {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String description;
  private String changeset_name;
  private String workonid;
  private String status;
  private String createdBy;

  @Temporal(TemporalType.TIMESTAMP)
  private Date creationDate;
  private String modifiedBy;

  @Temporal(TemporalType.TIMESTAMP)
  private Date modifiedDate;
  @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<PARoleRequest> paRoleReqs;

  @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<StagesRoleELMRoleReqMapping> stagesRoleELMRoleReqMapping;

  @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<StagesRolePARequest> stagesRolePAReqMapping;

  @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<RolePermReqtMapping> rolePermActReqMappings;
  
  @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<AttrPermCondition> attrPermConditionMappings;


  
  /**
   * @return the attrPermConditionMappings
   */
  public List<AttrPermCondition> getAttrPermConditionMappings() {
    return this.attrPermConditionMappings;
  }





  
  /**
   * @param attrPermConditionMappings the attrPermConditionMappings to set
   */
  public void setAttrPermConditionMappings(List<AttrPermCondition> attrPermConditionMappings) {
   this.attrPermConditionMappings = attrPermConditionMappings;
  }





  /**
   * @return the description
   */
  public String getDescription() {
    return this.description;
  }


  /**
   * @param description the description to set
   */
  public void setDescription(final String description) {
    this.description = description;
  }


  /**
   * @return the changeset_name
   */
  public String getChangeset_name() {
    return this.changeset_name;
  }
  /**
   * @param changeset_name the changeset_name to set
   */
  public void setChangeset_name(final String changeset_name) {
    this.changeset_name = changeset_name;
  }
 
  
  /**
   * @return the id
   */
  public int getId() {
    return id;
  }
  
  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }
  /**
   * @return the workonid
   */
  public String getWorkonid() {
    return this.workonid;
  }
  /**
   * @param workonid the workonid to set
   */
  public void setWorkonid(final String workonid) {
    this.workonid = workonid;
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


  /**
   * @return the paRoleReqs
   */
  public List<PARoleRequest> getPaRoleReqs() {
    return this.paRoleReqs;
  }


  /**
   * @param paRoleReqs the paRoleReqs to set
   */
  public void setPaRoleReqs(final List<PARoleRequest> paRoleReqs) {
    this.paRoleReqs = paRoleReqs;
  }


  /**
   * @return the stagesRoleELMRoleReqMapping
   */
  public List<StagesRoleELMRoleReqMapping> getStagesRoleELMRoleReqMapping() {
    return this.stagesRoleELMRoleReqMapping;
  }


  /**
   * @param stagesRoleELMRoleReqMapping the stagesRoleELMRoleReqMapping to set
   */
  public void setStagesRoleELMRoleReqMapping(final List<StagesRoleELMRoleReqMapping> stagesRoleELMRoleReqMapping) {
    this.stagesRoleELMRoleReqMapping = stagesRoleELMRoleReqMapping;
  }


  /**
   * @return the stagesRolePAReqMapping
   */
  public List<StagesRolePARequest> getStagesRolePAReqMapping() {
    return this.stagesRolePAReqMapping;
  }


  /**
   * @param stagesRolePAReqMapping the stagesRolePAReqMapping to set
   */
  public void setStagesRolePAReqMapping(final List<StagesRolePARequest> stagesRolePAReqMapping) {
    this.stagesRolePAReqMapping = stagesRolePAReqMapping;
  }


  /**
   * @return the rolePermActReqMappings
   */
  public List<RolePermReqtMapping> getRolePermActReqMappings() {
    return this.rolePermActReqMappings;
  }
  /**
   * @param rolePermActReqMappings the rolePermActReqMappings to set
   */
  public void setRolePermActReqMappings(final List<RolePermReqtMapping> rolePermActReqMappings) {
    this.rolePermActReqMappings = rolePermActReqMappings;
  }


}

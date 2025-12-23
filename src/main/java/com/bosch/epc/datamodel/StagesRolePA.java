/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.bosch.epc.model.StagesRolePAID;

/**
 * @author QYU1HC
 */
@Entity
@Table(name = "stagesrole_pa_mapping")
public class StagesRolePA {

  @EmbeddedId
  private StagesRolePAID id;

  @ManyToOne
  @MapsId("stagesRoleId")
  @JoinColumn(name = "stagesRoleId")
  private StagesRole stagesRole;

  @ManyToOne
  @MapsId("projectAreaId")
  @JoinColumn(name = "projectAreaId")
  private ProjectArea projectArea;

  private Boolean isUnderProgress;


  /**
   * @return the id
   */
  public StagesRolePAID getId() {
    return this.id;
  }


  /**
   * @param id the id to set
   */
  public void setId(final StagesRolePAID id) {
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
   * @return the isUnderProgress
   */
  public Boolean getIsUnderProgress() {
    return this.isUnderProgress;
  }


  /**
   * @param isUnderProgress the isUnderProgress to set
   */
  public void setIsUnderProgress(final Boolean isUnderProgress) {
    this.isUnderProgress = isUnderProgress;
  }


}

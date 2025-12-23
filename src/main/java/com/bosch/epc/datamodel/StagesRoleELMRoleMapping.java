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

/**
 * @author QYU1HC
 */
@Entity
@Table(name = "stagesrole_role_mapping")
public class StagesRoleELMRoleMapping {

  @EmbeddedId
  private StagesRoleELMRoleMappingId id;

  @ManyToOne
  @MapsId("stagesId")
  @JoinColumn(name = "stagesRoleId")
  private StagesRole stagesRole;

  @ManyToOne
  @MapsId("roleId")
  @JoinColumn(name = "elmRoleId")
  private ELMRole elmRole;

  private int isRequestPresent;


  /**
   * @return the id
   */
  public StagesRoleELMRoleMappingId getId() {
    return this.id;
  }


  /**
   * @param id the id to set
   */
  public void setId(final StagesRoleELMRoleMappingId id) {
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
   * @return the elmRole
   */
  public ELMRole getElmRole() {
    return this.elmRole;
  }


  /**
   * @param elmRole the elmRole to set
   */
  public void setElmRole(final ELMRole elmRole) {
    this.elmRole = elmRole;
  }


  /**
   * @return the isRequestPresent
   */
  public int getIsRequestPresent() {
    return isRequestPresent;
  }


  /**
   * @param isRequestPresent the isRequestActive to set
   */
  public void setIsRequestPresent(int isRequestPresent) {
    this.isRequestPresent = isRequestPresent;
  }


}

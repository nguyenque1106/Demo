/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;


import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.StagesRoleELMRoleMappingId;

/**
 * @author QYU1HC
 */
public class StagesRoleELMRoleResponse {

  private StagesRoleELMRoleMappingId id;
  private com.bosch.epc.datamodel.StagesRole stagesRole;
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
  public com.bosch.epc.datamodel.StagesRole getStagesRole() {
    return this.stagesRole;
  }

  /**
   * @param stagesRole the stagesRole to set
   */
  public void setStagesRole(final com.bosch.epc.datamodel.StagesRole stagesRole) {
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
   * @return the isRequestActive
   */
  public int getIsRequestPresent() {
    return isRequestPresent;
  }

  /**
   * @param isRequestPresent2 the isRequestActive to set
   */
  public void setIsRequestPresent(int isRequestPresent2) {
    this.isRequestPresent = isRequestPresent2;
  }


}

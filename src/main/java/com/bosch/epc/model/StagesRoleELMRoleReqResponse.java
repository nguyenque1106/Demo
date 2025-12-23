/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.StagesRole;
import com.bosch.epc.datamodel.StagesRoleELMRoleReqMappingId;

/**
 * @author QYU1HC
 */
public class StagesRoleELMRoleReqResponse {

  private StagesRoleELMRoleReqMappingId id;
  private com.bosch.epc.datamodel.StagesRole stagesRole;
  private ELMRole elmRole;

  /**
   * the default contructor function
   */
  public StagesRoleELMRoleReqResponse() {
    super();
  }

  /**
   * @param id
   * @param stagesRole
   * @param elmRole
   */
  public StagesRoleELMRoleReqResponse(final StagesRoleELMRoleReqMappingId id, final StagesRole stagesRole,
      final ELMRole elmRole) {
    super();
    this.id = id;
    this.stagesRole = stagesRole;
    this.elmRole = elmRole;
  }

  /**
   * @return the id
   */
  public StagesRoleELMRoleReqMappingId getId() {
    return this.id;
  }

  /**
   * @param id the id to set
   */
  public void setId(final StagesRoleELMRoleReqMappingId id) {
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

}

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * @author QYU1HC
 */
@Embeddable
public class StagesRolePAID implements Serializable {

  /**
   * default value
   */
  private static final long serialVersionUID = 1L;

  private int stagesRoleId;
  private int projectAreaId;


  /**
   * @return the stagesRoleId
   */
  public int getStagesRoleId() {
    return this.stagesRoleId;
  }

  /**
   * @param stagesRoleId the stagesRoleId to set
   */
  public void setStagesRoleId(final int stagesRoleId) {
    this.stagesRoleId = stagesRoleId;
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


}

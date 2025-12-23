/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import java.util.List;

/**
 * This class is used as RequestBody of API Permission and Role have to be depended on ProjectArea,Request, Workflow.
 *
 * @author GHT9HC
 */
public class PermRoleRequest {

  private ProjectAreaRole projectAreaRole;
  private String attrName;
  private List<Workflow> workflows;

  /**
   * @return the projectAreaRole
   */
  public ProjectAreaRole getProjectAreaRole() {
    return this.projectAreaRole;
  }

  /**
   * @param projectAreaRole the projectAreaRole to set
   */
  public void setProjectAreaRole(final ProjectAreaRole projectAreaRole) {
    this.projectAreaRole = projectAreaRole;
  }

  /**
   * @return the attrName
   */
  public String getAttrName() {
    return this.attrName;
  }

  /**
   * @param attrName the attrName to set
   */
  public void setAttrName(final String attrName) {
    this.attrName = attrName;
  }

  /**
   * @return the workflow
   */
  public List<Workflow> getWorkflows() {
    return this.workflows;
  }

  /**
   * @param workflow the workflow to set
   */
  public void setWorkflows(final List<Workflow> workflow) {
    this.workflows = workflow;
  }

}

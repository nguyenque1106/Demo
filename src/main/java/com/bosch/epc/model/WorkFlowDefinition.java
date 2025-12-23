/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import java.util.List;

/**
 * @author VFE1COB
 *
 */
public class WorkFlowDefinition {

  private String id;
  private List<State> stateList;
  private List<Resolution> resolutionList;
  /**
   * @return the id
   */
  public String getId() {
    return id;
  }
  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }
  /**
   * @return the stateList
   */
  public List<State> getStateList() {
    return stateList;
  }
  /**
   * @param stateList the stateList to set
   */
  public void setStateList(List<State> stateList) {
    this.stateList = stateList;
  }
  /**
   * @return the resolutionList
   */
  public List<Resolution> getResolutionList() {
    return resolutionList;
  }
  /**
   * @param resolutionList the resolutionList to set
   */
  public void setResolutionList(List<Resolution> resolutionList) {
    this.resolutionList = resolutionList;
  }
}

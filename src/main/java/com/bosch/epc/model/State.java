/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;


/**
 * @author VFE1COB
 *
 */
public class State {

  private String id;
  private String name;
  private String group;
  private String showResolution;
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
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  /**
   * @return the group
   */
  public String getGroup() {
    return group;
  }
  /**
   * @param group the group to set
   */
  public void setGroup(String group) {
    this.group = group;
  }
  /**
   * @return the showResolution
   */
  public String getShowResolution() {
    return showResolution;
  }
  /**
   * @param showResolution the showResolution to set
   */
  public void setShowResolution(String showResolution) {
    this.showResolution = showResolution;
  }

}

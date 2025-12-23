/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author VFE1COB
 *
 */
@Entity
@Table(name = "attr_perm_lock")
public class AttrPermLock {
  
  /**
   * @return the attrStringId
   */
  public String getAttrStringId() {
    return attrStringId;
  }
  
  /**
   * @param attrStringId the attrStringId to set
   */
  public void setAttrStringId(String attrStringId) {
    this.attrStringId = attrStringId;
  }
  
  /**
   * @return the paRoleId
   */
  public int getPaRoleId() {
    return paRoleId;
  }
  
  /**
   * @param paRoleId the paRoleId to set
   */
  public void setPaRoleId(int paRoleId) {
    this.paRoleId = paRoleId;
  }
  
  /**
   * @return the wiType
   */
  public String getWiType() {
    return wiType;
  }
  
  /**
   * @param wiType the wiType to set
   */
  public void setWiType(String wiType) {
    this.wiType = wiType;
  }
  
  /**
   * @return the wiStatus
   */
  public String getWiStatus() {
    return wiStatus;
  }
  
  /**
   * @param wiStatus the wiStatus to set
   */
  public void setWiStatus(String wiStatus) {
    this.wiStatus = wiStatus;
  }
  
  /**
   * @return the wiResolution
   */
  public String getWiResolution() {
    return wiResolution;
  }
  
  /**
   * @param wiResolution the wiResolution to set
   */
  public void setWiResolution(String wiResolution) {
    this.wiResolution = wiResolution;
  }
  
  /**
   * @return the wiStatusGroup
   */
  public String getWiStatusGroup() {
    return wiStatusGroup;
  }
  
  /**
   * @param wiStatusGroup the wiStatusGroup to set
   */
  public void setWiStatusGroup(String wiStatusGroup) {
    this.wiStatusGroup = wiStatusGroup;
  }
  
  /**
   * @return the isRequestPresent
   */
  public int getIsRequestPresent() {
    return isRequestPresent;
  }
  
  /**
   * @param isRequestPresent the isRequestPresent to set
   */
  public void setIsRequestPresent(int isRequestPresent) {
    this.isRequestPresent = isRequestPresent;
  }
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id; 
  @Column(name= "attrstringid")
  private String attrStringId;
  @Column(name= "pa_role_id")
  private int paRoleId;
  @Column(name= "witype")
  private String wiType;
  @Column(name= "wistatus")
  private String wiStatus;
  @Column(name= "wiresolution")
  private String wiResolution;
  @Column(name= "wistatusgrp")
  private String wiStatusGroup;
  @Column(name= "isRequestPresent")
  private int isRequestPresent;
  
  

}

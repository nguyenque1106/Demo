/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.bean;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the bean class representing the Role property.
 * 
 * @author PPT4KOR
 */
@XmlRootElement(name = "role")
@XmlAccessorType(XmlAccessType.FIELD)
public class RolePermissionBean {

  @XmlAttribute(name = "id")
  private String roleID;

  private Set<TeamOperationBean> operation = new HashSet<>();

  @XmlElement(name = "project-operation")
  private Set<ProjectOperationBean> projectOperationBeans = new HashSet<>();

  /**
   * @return the roleID
   */
  public String getRoleID() {
    return roleID;
  }

  /**
   * @param roleID the roleID to set
   */
  public void setRoleID(String roleID) {
    this.roleID = roleID;
  }

  /**
   * @return the teamOperationBeans
   */
  public Set<TeamOperationBean> getTeamOperationBeans() {
    return operation;
  }

  /**
   * @param teamOperationBeans the teamOperationBeans to set
   */
  public void setTeamOperationBeans(Set<TeamOperationBean> teamOperationBeans) {
    if (teamOperationBeans != null) {
      this.operation = new HashSet<>(teamOperationBeans);
    }
  }


  /**
   * @return the projectOperationBeans
   */
  public Set<ProjectOperationBean> getProjectOperationBeans() {
    return projectOperationBeans;
  }


  /**
   * @param projectOperationBeans the projectOperationBeans to set
   */
  public void setProjectOperationBeans(Set<ProjectOperationBean> projectOperationBeans) {
    if (projectOperationBeans != null) {
      this.projectOperationBeans = new HashSet<>(projectOperationBeans);
    }
  }


}

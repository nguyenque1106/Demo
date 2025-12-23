/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * This is the bean class representing the workflow property.
 *
 * @author pla7kor
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowPropertyBean {

  @XmlAttribute
  private String workItemType;
  @XmlAttribute
  private String statusGroup;
  @XmlAttribute
  private String status;
  @XmlAttribute
  private String resolution;

  private List<RoleBean> role = new ArrayList<>();


  /**
   * @return the roles
   */
  public List<RoleBean> getRoles() {
    return this.role;
  }


  /**
   * @param roles the roles to set
   */
  public void setRoles(final List<RoleBean> roles) {
    if (roles != null) {
      this.role = new ArrayList<>(roles);
    }
  }


  /**
   * @return the workItemType
   */
  public String getWorkItemType() {
    return this.workItemType;
  }


  /**
   * @param workItemType the workItemType to set
   */
  public void setWorkItemType(final String workItemType) {
    this.workItemType = workItemType;
  }

  /**
   * @return the statusGroup
   */
  public String getStatusGroup() {
    return this.statusGroup;
  }


  /**
   * @param statusGroup the statusGroup to set
   */
  public void setStatusGroup(final String statusGroup) {
    this.statusGroup = statusGroup;
  }

  /**
   * @return the status
   */
  public String getStatus() {
    return this.status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(final String status) {
    this.status = status;
  }


  /**
   * @return the resolution
   */
  public String getResolution() {
    return this.resolution;
  }

  /**
   * @param resolution the resolution to set
   */
  public void setResolution(final String resolution) {
    this.resolution = resolution;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.resolution == null) ? 0 : this.resolution.hashCode());
    result = (prime * result) + ((this.role == null) ? 0 : this.role.hashCode());
    result = (prime * result) + ((this.status == null) ? 0 : this.status.hashCode());
    result = (prime * result) + ((this.statusGroup == null) ? 0 : this.statusGroup.hashCode());
    result = (prime * result) + ((this.workItemType == null) ? 0 : this.workItemType.hashCode());
    return result;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    WorkflowPropertyBean other = (WorkflowPropertyBean) obj;

    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.resolution, other.resolution);
    equalsBuilder.append(this.role, other.role);
    equalsBuilder.append(this.status, other.status);
    equalsBuilder.append(this.statusGroup, other.statusGroup);
    equalsBuilder.append(this.workItemType, other.workItemType);
    return equalsBuilder.isEquals();
  }


}

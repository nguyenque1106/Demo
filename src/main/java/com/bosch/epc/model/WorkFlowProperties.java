package com.bosch.epc.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author VFE1COB
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkFlowProperties {

  @XmlAttribute
  private String status;
  @XmlAttribute
  private String resolution;
  @XmlAttribute
  private String statusGroup;
  @XmlAttribute
  private String workItemType;

  @XmlElement(name = "role")
  private List<ALMRole> roles;

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
   * @return the roles
   */

  public List<ALMRole> getRoles() {
    return this.roles;
  }


  /**
   * @param roles the roles to set
   */
  public void setRoles(final List<ALMRole> roles) {
    this.roles = roles;
  }


  
  /**
   * @return the resolution
   */
  public String getResolution() {
    return resolution;
  }


  
  /**
   * @param resolution the resolution to set
   */
  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  // Getters, Setters, etc.
}
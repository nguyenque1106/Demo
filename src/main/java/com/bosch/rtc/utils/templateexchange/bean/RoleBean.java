/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * This is the bean class representing the Roles property.
 *
 * @author pla7kor
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RoleBean {

  @XmlAttribute
  private String id;
  @XmlAttribute
  private String permission;
  @XmlAttribute
  private String conditionScriptName;
  @XmlAttribute
  private String conditionType;
  @XmlAttribute
  private String conditionScript;

  /**
   * @return the id
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id the id to set
   */
  public void setId(final String id) {
    this.id = id;
  }

  /**
   * @return the permission
   */
  public String getPermission() {
    return this.permission;
  }

  /**
   * @param permission the permission to set
   */
  public void setPermission(final String permission) {
    this.permission = permission;
  }

  /**
   * @return the conditionScriptName
   */
  public String getConditionScriptName() {
    return this.conditionScriptName;
  }

  /**
   * @param conditionScriptName the conditionScriptName to set
   */
  public void setConditionScriptName(final String conditionScriptName) {
    this.conditionScriptName = conditionScriptName;
  }

  /**
   * @return the conditionType
   */
  public String getConditionType() {
    return this.conditionType;
  }

  /**
   * @param conditionType the conditionType to set
   */
  public void setConditionType(final String conditionType) {
    this.conditionType = conditionType;
  }

  /**
   * @return the conditionScript
   */
  public String getConditionScript() {
    return this.conditionScript;
  }

  /**
   * @param conditionScript the conditionScript to set
   */
  public void setConditionScript(final String conditionScript) {
    this.conditionScript = conditionScript;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
    result = (prime * result) + ((this.permission == null) ? 0 : this.permission.hashCode());
    result = (prime * result) + ((this.conditionType == null) ? 0 : this.conditionType.hashCode());
    result = (prime * result) + ((this.conditionScript == null) ? 0 : this.conditionScript.hashCode());
    result = (prime * result) + ((this.conditionScriptName == null) ? 0 : this.conditionScriptName.hashCode());
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
    RoleBean other = (RoleBean) obj;

    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, other.id);
    equalsBuilder.append(this.permission, other.permission);
    equalsBuilder.append(this.conditionType, other.conditionType);
    equalsBuilder.append(this.conditionScript, other.conditionScript);
    equalsBuilder.append(this.conditionScriptName, other.conditionScriptName);

    return equalsBuilder.isEquals();
  }


}

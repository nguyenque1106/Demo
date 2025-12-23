/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.bean;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * This is the bean class representing the condition property.
 *
 * @author pla7kor
 */
@XmlRootElement(name = "condition")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionBean {

  @XmlAttribute
  private String id;
  @XmlAttribute
  private String name;
  @XmlAttribute
  private String providerId;
  private Set<WorkflowPropertyBean> workflowProperties = new HashSet<>();

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
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * @param name the name to set
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * @return the providerId
   */
  public String getProviderId() {
    return this.providerId;
  }

  /**
   * @param providerId the providerId to set
   */
  public void setProviderId(final String providerId) {
    this.providerId = providerId;
  }

  /**
   * @return the workflowPropertyBeans
   */
  public Set<WorkflowPropertyBean> getWorkflowPropertyBeans() {
    return this.workflowProperties;
  }

  /**
   * @param workflowPropertyBeans the workflowPropertyBeans to set
   */
  public void setWorkflowPropertyBeans(final Set<WorkflowPropertyBean> workflowPropertyBeans) {
    if (workflowPropertyBeans != null) {
      this.workflowProperties = new HashSet<>(workflowPropertyBeans);
    }
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
    result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
    result = (prime * result) + ((this.providerId == null) ? 0 : this.providerId.hashCode());
    result = (prime * result) + ((this.workflowProperties == null) ? 0 : this.workflowProperties.hashCode());
//    result = (prime * result) + ((this.script == null) ? 0 : this.script.hashCode());
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
    ConditionBean other = (ConditionBean) obj;

    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, other.id);
    equalsBuilder.append(this.name, other.name);
    equalsBuilder.append(this.providerId, other.providerId);
    equalsBuilder.append(this.workflowProperties, other.workflowProperties);
//    equalsBuilder.append(this.script, other.script);

    return equalsBuilder.isEquals();
  }


}

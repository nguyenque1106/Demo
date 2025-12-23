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
 * This is the bean class representing the Project Operation property.
 *
 * @author PPT4KOR
 */
@XmlRootElement(name = "project-operation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectOperationBean {

  @XmlAttribute
  private String id;

  private Set<ActionBean> actions = new HashSet<>();


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
   * @return the actions
   */
  public Set<ActionBean> getActions() {
    return actions;
  }


  /**
   * @param actions the actions to set
   */
  public void setActions(Set<ActionBean> actions) {
    this.actions = actions;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
    result = (prime * result) + ((this.actions == null) ? 0 : this.actions.hashCode());
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
    ProjectOperationBean other = (ProjectOperationBean) obj;

    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, other.id);
    equalsBuilder.append(this.actions, other.actions);
    return equalsBuilder.isEquals();
  }


}

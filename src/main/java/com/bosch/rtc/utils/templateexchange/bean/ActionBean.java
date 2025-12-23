/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.bean;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * This is the bean class representing the Action property.
 *
 * @author PPT4KOR
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ActionBean {

  @XmlAttribute
  private String id;
  

  private Set<ActionBean> action = new HashSet<>();

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
   * @return the subActions
   */
  public Set<ActionBean> getChildActions() {
    return action;
  }


  /**
   * @param subActions the subActions to set
   */
  public void setChildActions(Set<ActionBean> subActions) {
    this.action = subActions;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
    result = (prime * result) + ((this.action == null) ? 0 : this.action.hashCode());
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
    ActionBean other = (ActionBean) obj;

    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, other.id);
    equalsBuilder.append(this.action, other.action);
    return equalsBuilder.isEquals();
  }



}

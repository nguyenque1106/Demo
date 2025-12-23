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
 * This is the bean class representing the pre-condition property.
 *
 * @author pla7kor
 */
@XmlRootElement(name = "precondition")
@XmlAccessorType(XmlAccessType.FIELD)
public class PreConditionBean {

  @XmlAttribute
  private String id;
  @XmlAttribute
  private String name;
  @XmlAttribute
  private String description;

  private Set<AttributeBean> requiredAttributes = new HashSet<>();
  private Set<AttributeBean> readOnlyAttributes = new HashSet<>();

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
   * @return the description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * @param name the name to set
   */
  public void setName(final String name) {
    this.name = name;
  }


  /**
   * @param description the description to set
   */
  public void setDescription(final String description) {
    this.description = description;
  }

  /**
   * @return the attributes
   */
  public Set<AttributeBean> getRequiredAttributes() {
    return this.requiredAttributes;
  }

  /**
   * @param requiredAttributes the requiredAttributes to set
   */
  public void setRequiredAttributes(final Set<AttributeBean> requiredAttributes) {
    if (requiredAttributes != null) {
      this.requiredAttributes = new HashSet<>(requiredAttributes);
    }
  }

  /**
   * @return the readOnlyAttributes
   */
  public Set<AttributeBean> getReadOnlyAttributes() {
    return this.readOnlyAttributes;
  }

  /**
   * @param readOnlyAttributes the readOnlyAttributes to set
   */
  public void setReadOnlyAttributes(final Set<AttributeBean> readOnlyAttributes) {
    if (readOnlyAttributes != null) {
      this.readOnlyAttributes = new HashSet<>(readOnlyAttributes);
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.description == null) ? 0 : this.description.hashCode());
    result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
    result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
    result = (prime * result) + ((this.readOnlyAttributes == null) ? 0 : this.readOnlyAttributes.hashCode());
    result = (prime * result) + ((this.requiredAttributes == null) ? 0 : this.requiredAttributes.hashCode());
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
    PreConditionBean other = (PreConditionBean) obj;

    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, other.id);
    equalsBuilder.append(this.name, other.name);
    equalsBuilder.append(this.readOnlyAttributes, other.readOnlyAttributes);
    equalsBuilder.append(this.requiredAttributes, other.requiredAttributes);
    equalsBuilder.append(this.description, other.description);
    return equalsBuilder.isEquals();
  }


}

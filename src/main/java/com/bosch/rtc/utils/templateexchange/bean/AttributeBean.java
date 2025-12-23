/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.builder.EqualsBuilder;



/**
 * This is the bean class representing the Attributes property.
 *
 * @author pla7kor
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AttributeBean {

  @XmlAttribute
  private String ruleId;

  @XmlElement(name = "attribute")
  private AttributeId attributeId;

  /**
   * Static class which is used to represent the proper xml file structure.
   *
   * @author pla7kor
   */
  private static class AttributeId {

    @XmlAttribute
    private String id;

    /**
     * @param attributeId the attributeId to set
     */
    public void setAttributeId(final String attributeId) {
      this.id = attributeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
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
      AttributeId other = (AttributeId) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(this.id, other.id);

      return equalsBuilder.isEquals();
    }

  }

  /**
   * @return the ruleId
   */
  public String getRuleId() {
    return this.ruleId;
  }

  /**
   * @param ruleId the ruleId to set
   */
  public void setRuleId(final String ruleId) {
    this.ruleId = ruleId;
  }

  /**
   * @param attributeId the attributeId to set
   */
  public void setAttributeId(final String attributeId) {
    this.attributeId = new AttributeId();
    this.attributeId.setAttributeId(attributeId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.attributeId == null) ? 0 : this.attributeId.hashCode());
    result = (prime * result) + ((this.ruleId == null) ? 0 : this.ruleId.hashCode());
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
    AttributeBean other = (AttributeBean) obj;

    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.ruleId, other.ruleId);
    equalsBuilder.append(this.attributeId, other.attributeId);

    return equalsBuilder.isEquals();
  }


}

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
 * Entiry class for EWM Built-in Attributes
 * 
 * @author VFE1COB
 */

@Entity
@Table(name = "attribute_builtin")
public class AttributeBuiltIn {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "stringid")
  private String stringId;
  private String name;

  /**
   *
   */
  public AttributeBuiltIn() {
  }

  /**
   * @param stringId
   * @param name
   * @param type
   * @param iscustomattribute
   */
  public AttributeBuiltIn(final String stringId, final String name) {
    super();
    this.stringId = stringId;
    this.name = name;
  }

  /**
   * @return the attributename
   */
  public String getAttributename() {
    return this.name;
  }

  /**
   * @param attributename the attributename to set
   */
  public void setAttributename(final String attributename) {
    this.name = attributename;
  }

  /**
   * @return the stringId
   */
  public String getStringId() {
    return this.stringId;
  }


  /**
   * @param stringId the stringId to set
   */
  public void setStringId(final String stringId) {
    this.stringId = stringId;
  }


}
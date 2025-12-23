/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author QYU1HC
 */
@Entity
@Table(name = "action")
public class Action {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "elmActionId", nullable = false)
  private String actionId;

  @Column(name = "parentId")
  private Integer parentId;

  @ManyToOne
  @JoinColumn(name = "permissionId", referencedColumnName = "id", insertable = false, updatable = false)
  @JsonBackReference
  private ELMPermissions permission;


  /**
   * @return the id
   */
  public Integer getId() {
    return this.id;
  }


  /**
   * @param id the id to set
   */
  public void setId(final Integer id) {
    this.id = id;
  }


  /**
   * @return the actionId
   */
  public String getActionId() {
    return this.actionId;
  }


  /**
   * @param actionId the actionId to set
   */
  public void setActionId(final String actionId) {
    this.actionId = actionId;
  }


  /**
   * @return the parentId
   */
  public Integer getParentId() {
    return this.parentId;
  }


  /**
   * @param parentId the parentId to set
   */
  public void setParentId(final Integer parentId) {
    this.parentId = parentId;
  }

  /**
   * @return the permission
   */
  public ELMPermissions getPermission() {
    return this.permission;
  }


  /**
   * @param permission the permission to set
   */
  public void setPermission(final ELMPermissions permission) {
    this.permission = permission;
  }

  // Getters and Setters

}

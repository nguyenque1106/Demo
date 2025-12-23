/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author QYU1HC
 */
@Entity
@Table(name = "permission")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ELMPermissions implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "ref_id", unique = true, nullable = true)
  private String ref_id;

  @Column(name = "isAttrPermission", nullable = true)
  private boolean isAttrPermission;

  @ManyToOne
  @JoinColumn(name = "parentId")
  private ELMPermissions parentId;

  @Column(name = "permissionGroup", nullable = true)
  private String permissionGroup;

  @Column(name = "isProjectConfiguration", nullable = false)
  private boolean isProjectConfiguration;

  @Transient
  private List<ELMPermissions> childPermissions = new ArrayList<>();

  /**
   *
   */
  public ELMPermissions() {}

  /**
   * @param name
   */
  public ELMPermissions(final String name) {
    this.name = name;
  }

  /**
   * @param name
   * @param isAttrPerm
   */
  public ELMPermissions(final String name, final boolean isAttrPerm) {
    this(name);
    this.isAttrPermission = isAttrPerm;
  }

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
   * @return the isAttrPermission
   */
  public boolean isAttrPermission() {
    return this.isAttrPermission;
  }

  /**
   * @param isAttrPermission the isAttrPermission to set
   */
  public void setAttrPermission(final boolean isAttrPermission) {
    this.isAttrPermission = isAttrPermission;
  }

  /**
   * @return the ref_id
   */
  public String getRef_id() {
    return this.ref_id;
  }


  /**
   * @param ref_id the ref_id to set
   */
  public void setRef_id(final String ref_id) {
    this.ref_id = ref_id;
  }

  /**
   * @return the parentId
   */
  public ELMPermissions getParentId() {
    return this.parentId;
  }


  /**
   * @param parentId the parentId to set
   */
  public void setParentId(final ELMPermissions parentId) {
    this.parentId = parentId;
  }


  /**
   * @return the permissionGroup
   */
  public String getPermissionGroup() {
    return this.permissionGroup;
  }


  /**
   * @param permissionGroup the permissionGroup to set
   */
  public void setPermissionGroup(final String permissionGroup) {
    this.permissionGroup = permissionGroup;
  }

  /**
   * @return the isProjectConfiguration
   */
  public boolean isProjectConfiguration() {
    return this.isProjectConfiguration;
  }


  /**
   * @param isProjectConfiguration the isProjectConfiguration to set
   */
  public void setProjectConfiguration(final boolean isProjectConfiguration) {
    this.isProjectConfiguration = isProjectConfiguration;
  }

  /**
   * @return the childPermissions
   */
  public List<ELMPermissions> getChildPermissions() {
    return this.childPermissions;
  }

  /**
   * @param childPermissions the childPermissions to set
   */
  public void setChildPermissions(final List<ELMPermissions> childPermissions) {
    this.childPermissions = childPermissions;
  }


}

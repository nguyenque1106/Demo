/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dto;

/**
 * DTO representing a permission with its optional role mapping information. Combines permission details with
 * role_perm_act_mapping data.
 *
 * @author QYU1HC
 */
public class PermissionWithMappingDTO {

  // Permission fields
  private int permissionId;
  private String permissionName;
  private String ref_id;
  private boolean attrPermission;
  private Integer parentId;
  private String permissionGroup;
  private boolean projectConfiguration;

  // Role mapping fields (nullable if no mapping exists)
  private Integer roleId;
  private Boolean permitted;
  private Integer requestPresent;

  /**
   * Default constructor.
   */
  public PermissionWithMappingDTO() {}

  /**
   * Full constructor with all fields.
   */
  public PermissionWithMappingDTO(final int permissionId, final String permissionName, final String ref_id,
      final boolean attrPermission, final Integer parentId, final String permissionGroup,
      final boolean projectConfiguration, final Integer roleId, final Boolean permitted, final Integer requestPresent) {
    this.permissionId = permissionId;
    this.permissionName = permissionName;
    this.ref_id = ref_id;
    this.attrPermission = attrPermission;
    this.parentId = parentId;
    this.permissionGroup = permissionGroup;
    this.projectConfiguration = projectConfiguration;
    this.roleId = roleId;
    this.permitted = permitted;
    this.requestPresent = requestPresent;
  }

  // Getters and Setters

  public int getPermissionId() {
    return this.permissionId;
  }

  public void setPermissionId(final int permissionId) {
    this.permissionId = permissionId;
  }

  public String getPermissionName() {
    return this.permissionName;
  }

  public void setPermissionName(final String permissionName) {
    this.permissionName = permissionName;
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

  public boolean isAttrPermission() {
    return this.attrPermission;
  }

  public void setAttrPermission(final boolean attrPermission) {
    this.attrPermission = attrPermission;
  }

  public Integer getParentId() {
    return this.parentId;
  }

  public void setParentId(final Integer parentId) {
    this.parentId = parentId;
  }

  public String getPermissionGroup() {
    return this.permissionGroup;
  }

  public void setPermissionGroup(final String permissionGroup) {
    this.permissionGroup = permissionGroup;
  }

  public boolean isProjectConfiguration() {
    return this.projectConfiguration;
  }

  public void setProjectConfiguration(final boolean projectConfiguration) {
    this.projectConfiguration = projectConfiguration;
  }

  public Integer getRoleId() {
    return this.roleId;
  }

  public void setRoleId(final Integer roleId) {
    this.roleId = roleId;
  }

  public Boolean isPermitted() {
    return this.permitted;
  }

  public void setPermitted(final Boolean permitted) {
    this.permitted = permitted;
  }

  public Integer getRequestPresent() {
    return this.requestPresent;
  }

  public void setRequestPresent(final Integer requestPresent) {
    this.requestPresent = requestPresent;
  }
}
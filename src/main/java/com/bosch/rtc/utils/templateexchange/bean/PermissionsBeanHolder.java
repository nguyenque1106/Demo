/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.bean;

import java.util.Set;

/**
 * This class is holding the Role Permission bean instances which is used in different use cases.
 * 
 * @author PPT4KOR
 */
public class PermissionsBeanHolder {


  private final Set<RolePermissionBean> permissionXMLContents;


  /**
   * Parameterised Constructor.
   *
   * @param rolePermXMLContents - the xml contents of the Permissions, must not be null
   */
  public PermissionsBeanHolder(final Set<RolePermissionBean> rolePermXMLContents) {
    this.permissionXMLContents = rolePermXMLContents;

  }


  /**
   * @return the permissionXMLContents
   */
  public Set<RolePermissionBean> getPermissionXMLContents() {
    return permissionXMLContents;
  }


}


/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;

import com.bosch.epc.datamodel.ELMPermissions;

/**
 * @author QYU1HC
 */
public interface ELMPermService {

  /**
   * @param PermissionId
   * @return
   */
  public boolean checkExist(int PermissionId);

  /**
   * Get list of Permissions
   * 
   * @param isFilterByAttrPerm true if the list of Attribute Permissions needed, false if the list of Permissions needed
   * @return List<Permission> List of permissions
   */
  public List<ELMPermissions> findAllByIsAttrPermission(boolean isFilterByAttrPerm);
}

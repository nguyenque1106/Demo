/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;

import com.bosch.epc.model.PermELMRoleResponse;

/**
 * @author QYU1HC
 */
public interface PermELMRoleServiceInterface {

  /**
   * @return all Perm & ELMRole
   */
  public List<PermELMRoleResponse> getAll();
}

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;

import com.bosch.epc.model.StagesRoleELMRoleResponse;

/**
 * @author QYU1HC
 */
public interface StagesRoleELMRoleServiceInterface {

  /**
   * @return list of StagesRole-ELMRole
   */
  public List<StagesRoleELMRoleResponse> findAll();
}

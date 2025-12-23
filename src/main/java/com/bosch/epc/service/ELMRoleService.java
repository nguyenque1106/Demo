/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bosch.epc.datamodel.ELMRole;

/**
 * @author QYU1HC
 */
@Service
public interface ELMRoleService {

  /**
   * @return list of roles
   */
  public List<ELMRole> getAllELMRoles();

  /**
   * This function to get all ELM Roles by projectAreaId
   *
   * @param projectAreaId
   * @return
   */
  public List<ELMRole> getELMRolesByProjectAreaId(final int projectAreaId);

  /**
   * @param elmRoles
   * @return
   */
  @Transactional
  public List<ELMRole> saveELMRoles(final List<ELMRole> elmRoles);

  public boolean checkExist(final Integer elmRoleId);
}

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bosch.epc.dao.ELMRoleRepository;
import com.bosch.epc.datamodel.ELMRole;

/**
 * @author QYU1HC
 */
@Service
public class ELMRoleServiceImpl  implements ELMRoleService {

  @Autowired
  private ELMRoleRepository elmRoleRepo;

  /**
   * @return list of roles
   */
  @Override
  public List<ELMRole> getAllELMRoles() {
    return this.elmRoleRepo.findAll();
  }

  /**
   * This function to get all ELM Roles by projectAreaId
   *
   * @param projectAreaId
   * @return
   */
  @Override
  public List<ELMRole> getELMRolesByProjectAreaId(final int projectAreaId) {
    return this.elmRoleRepo.findByProjectAreaId(projectAreaId);
  }

  /**
   * @param elmRoles
   * @return
   */
  @Transactional
  @Override
  public List<ELMRole> saveELMRoles(final List<ELMRole> elmRoles) {
    return this.elmRoleRepo.saveAll(elmRoles);
  }

  @Override
  public boolean checkExist(final Integer elmRoleId) {
    return this.elmRoleRepo.existsById(elmRoleId);
  }
}

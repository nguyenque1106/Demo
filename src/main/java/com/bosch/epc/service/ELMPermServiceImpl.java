/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.epc.dao.ELMPermRepo;
import com.bosch.epc.datamodel.ELMPermissions;

/**
 * @author QYU1HC
 */
@Service
public class ELMPermServiceImpl implements ELMPermService {

  @Autowired
  private ELMPermRepo repo;

  @Override
  public boolean checkExist(final int PermissionId) {
    return this.repo.existsById(PermissionId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ELMPermissions> findAllByIsAttrPermission(boolean isFilterByAttrPerm) {
    return this.repo.findAllByIsAttrPermission(isFilterByAttrPerm);
  }

}

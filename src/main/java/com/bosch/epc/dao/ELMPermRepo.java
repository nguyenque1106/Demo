/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bosch.epc.datamodel.ELMPermissions;

/**
 * @author QYU1HC
 */
@Repository
public interface ELMPermRepo extends JpaRepository<ELMPermissions, Integer> {

  /**
   * Get list permission which is attribute type.
   * 
   * @param isAttrPermission
   * @return
   */
  List<ELMPermissions> findAllByIsAttrPermission(boolean isAttrPermission);
}

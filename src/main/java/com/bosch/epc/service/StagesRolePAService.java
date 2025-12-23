/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.epc.dao.StagesRolePARepository;
import com.bosch.epc.datamodel.StagesRolePA;

/**
 * @author QYU1HC
 */
@Service
public class StagesRolePAService {

  @Autowired
  private StagesRolePARepository stagesRolePARepo;

  /**
   * @return lis of PA
   */
  public List<StagesRolePA> getAll() {
    return this.stagesRolePARepo.findAll();
  }

}

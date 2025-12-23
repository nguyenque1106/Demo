/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.epc.dao.StagesRoleELMRoleRepository;
import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.StagesRole;
import com.bosch.epc.datamodel.StagesRoleELMRoleMapping;
import com.bosch.epc.datamodel.StagesRoleELMRoleMappingId;
import com.bosch.epc.model.StagesRoleELMRoleResponse;

/**
 * @author QYU1HC
 */
@Service
public class StagesRoleELMRoleServiceImpl implements StagesRoleELMRoleServiceInterface {

  @Autowired
  private StagesRoleELMRoleRepository stagesRoleELMRoleRepository;

  @Override
  public List<StagesRoleELMRoleResponse> findAll() {
    List<StagesRoleELMRoleMapping> listMapping = this.stagesRoleELMRoleRepository.findAll();
    return listMapping.stream().map(val -> {
      StagesRole stagesRole = val.getStagesRole();
      ELMRole elmRole = val.getElmRole();
      int isRequestPresent = val.getIsRequestPresent();
      StagesRoleELMRoleResponse response = new StagesRoleELMRoleResponse();
      response.setId(new StagesRoleELMRoleMappingId(val.getId().getStagesId(), val.getId().getRoleId()));
      response.setStagesRole(stagesRole);
      response.setElmRole(elmRole);
      response.setIsRequestPresent(isRequestPresent);
      return response;
    }).collect(Collectors.toList());

  }
}

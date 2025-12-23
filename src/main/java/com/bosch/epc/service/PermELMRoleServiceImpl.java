/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.epc.dao.PermELMRoleRepository;
import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.PermELMRoleMapping;
import com.bosch.epc.datamodel.PermELMRoleMappingId;
import com.bosch.epc.datamodel.ELMPermissions;
import com.bosch.epc.model.PermELMRoleResponse;

/**
 * @author QYU1HC
 */
@Service
public class PermELMRoleServiceImpl implements PermELMRoleServiceInterface {

  @Autowired
  private PermELMRoleRepository repository;

  @Override
  public List<PermELMRoleResponse> getAll() {
    List<PermELMRoleMapping> listMapping = this.repository.findAll();
    return listMapping.stream().map(val -> {
      ELMPermissions permission = val.getPermission();
      ELMRole elmRole = val.getElmRole();
      PermELMRoleResponse response = new PermELMRoleResponse();
      response.setId(new PermELMRoleMappingId(val.getId().getElmRoleId(), val.getId().getPermissionId()));
      response.setPermission(permission);
      response.setElmRole(elmRole);
      response.setPermitted(val.isPermitted());
      response.setUnderProgress(val.isUnderProgress());
      return response;
    }).collect(Collectors.toList());

  }

}

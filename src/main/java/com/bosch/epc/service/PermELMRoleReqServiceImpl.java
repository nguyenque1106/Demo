/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.epc.dao.PermELMRoleReqRepository;
import com.bosch.epc.datamodel.PermELMRoleReqMapping;
import com.bosch.epc.datamodel.PermELMRoleReqMappingId;
import com.bosch.epc.exception.ResourceNotFoundException;
import com.bosch.epc.model.PermELMRoleReqResponse;

/**
 * @author QYU1HC
 */
@Service
public class PermELMRoleReqServiceImpl implements PermELMRoleReqServiceInterface {

  @Autowired
  private PermELMRoleReqRepository permELMRoleReqRepo;

  @Autowired
  private ELMRoleService elmRoleService;

  @Autowired
  private ELMPermService permService;

  @Autowired
  private RequestService requestService;

  @Override
  @Transactional
  public PermELMRoleReqResponse addNewPermELMRoleReqMapping(final PermELMRoleReqMappingId id) {
    validateMappingIds(id);
    int amountOfRecordSaved =
        this.permELMRoleReqRepo.save(id.getElmRoleId(), id.getPermissionId(), id.getRequestId(), true);
    if (amountOfRecordSaved <= 0) {
      throw new RuntimeException("Cannot save permission - elm role - request mapping with data: " + id.getElmRoleId() +
          " - " + id.getPermissionId() + " - " + id.getRequestId());
    }
    PermELMRoleReqMapping savedPermELMRoleReq = this.permELMRoleReqRepo.findById(id).get();
    PermELMRoleReqResponse response = new PermELMRoleReqResponse();
    response.setId(id);
    response.setRequest(savedPermELMRoleReq.getRequest());
    response.setPermission(savedPermELMRoleReq.getPermission());
    response.setElmRole(savedPermELMRoleReq.getElmRole());
    response.setPermitted(savedPermELMRoleReq.isPermitted());
    return response;
  }

  @Override
  public List<PermELMRoleReqResponse> findByRequestId(final int requestId) {
//    Integer intRequestId = Integer.parseInt(requestId);
    List<PermELMRoleReqMapping> results = this.permELMRoleReqRepo.findById_RequestId(requestId);
    if (results.isEmpty()) {
      throw new ResourceNotFoundException("Request not found with id: " + requestId);
    }
    return results.stream().map(res -> {
      PermELMRoleReqResponse response = new PermELMRoleReqResponse();
      response.setId(res.getId());
      response.setElmRole(res.getElmRole());
      response.setPermission(res.getPermission());
      response.setRequest(res.getRequest());
      response.setPermitted(res.isPermitted());
      return response;
    }).collect(Collectors.toList());
  }

  /**
   * @param id
   */
  private void validateMappingIds(final PermELMRoleReqMappingId id) {
    if (!this.requestService.checkExist(id.getRequestId())) {
      throw new ResourceNotFoundException("Request not found with id: " + id.getRequestId());
    }
    if (!this.permService.checkExist(id.getPermissionId())) {
      throw new ResourceNotFoundException("Permission not found with id: " + id.getPermissionId());
    }
    if (!this.elmRoleService.checkExist(id.getElmRoleId())) {
      throw new ResourceNotFoundException("ELM role not found with id: " + id.getElmRoleId());
    }
  }
}

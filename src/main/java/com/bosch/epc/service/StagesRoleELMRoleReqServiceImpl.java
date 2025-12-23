/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.epc.dao.StagesDaoImpl;
import com.bosch.epc.dao.StagesRoleELMRoleReqRepository;
import com.bosch.epc.datamodel.StagesRoleELMRoleReqMapping;
import com.bosch.epc.datamodel.StagesRoleELMRoleReqMappingId;
import com.bosch.epc.exception.ResourceNotFoundException;
import com.bosch.epc.model.StagesRoleELMRoleReqResponse;

/**
 * @author QYU1HC
 */
@Service
public class StagesRoleELMRoleReqServiceImpl implements StagesRoleELMRoleReqServiceInterface {

  @Autowired
  private StagesRoleELMRoleReqRepository stagesRoleELMRoleReqRepository;

  @Autowired
  private StagesDaoImpl stagesRoleService;

  @Autowired
  private ELMRoleService elmRoleService;

  @Autowired
  private RequestService requestService;

  @Override
  public List<StagesRoleELMRoleReqResponse> findByRequestId(final String requestId) {
    Integer intRequestId = Integer.parseInt(requestId);
    List<StagesRoleELMRoleReqMapping> mappingList =
        this.stagesRoleELMRoleReqRepository.findById_RequestId(intRequestId);
    if (mappingList.isEmpty()) {
      throw new ResourceNotFoundException("Request not found with id: " + requestId);
    }
    return mappingList.stream().map(mappingItem -> {
      StagesRoleELMRoleReqResponse dto = new StagesRoleELMRoleReqResponse();
      dto.setId(mappingItem.getId());
      dto.setStagesRole(mappingItem.getStagesRole());
      dto.setElmRole(mappingItem.getElmRole());
      return dto;
    }).collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public List<StagesRoleELMRoleReqResponse> addNewStagesRoleELMRoleReqMappings(
      final List<StagesRoleELMRoleReqMappingId> ids) {
    List<StagesRoleELMRoleReqResponse> responses = new ArrayList<>();

    for (StagesRoleELMRoleReqMappingId id : ids) {
      validateMappingIds(id);

      int amountOfRecordSaved =
          this.stagesRoleELMRoleReqRepository.save(id.getStagesRoleId(), id.getElmRoleId(), id.getRequestId());

      if (amountOfRecordSaved <= 0) {
        throw new RuntimeException("Cannot save mapping with data: " + id.getStagesRoleId() + " - " +
            id.getElmRoleId() + " - " + id.getRequestId());
      }

      StagesRoleELMRoleReqMapping savedMapping = this.stagesRoleELMRoleReqRepository.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("Failed to fetch saved mapping"));

      StagesRoleELMRoleReqResponse response = new StagesRoleELMRoleReqResponse();
      response.setId(id);
      response.setStagesRole(savedMapping.getStagesRole());
      response.setElmRole(savedMapping.getElmRole());

      responses.add(response);
    }

    return responses;
  }


  /**
   * @param id
   */
  private void validateMappingIds(final StagesRoleELMRoleReqMappingId id) {
    if (!this.requestService.checkExist(id.getRequestId())) {
      throw new ResourceNotFoundException("Request not found with id: " + id.getRequestId());
    }
    if (!this.stagesRoleService.checkExist(id.getStagesRoleId())) {
      throw new ResourceNotFoundException("Stages role not found with id: " + id.getStagesRoleId());
    }
    if (!this.elmRoleService.checkExist(id.getElmRoleId())) {
      throw new ResourceNotFoundException("ELM role not found with id: " + id.getElmRoleId());
    }
  }
}

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bosch.epc.dao.ProjectAreaRepository;
import com.bosch.epc.dao.RequestRepository;
import com.bosch.epc.dao.StagesRepository;
import com.bosch.epc.dao.StagesRolePARequestRepository;
import com.bosch.epc.datamodel.ProjectArea;
import com.bosch.epc.datamodel.Request;
import com.bosch.epc.datamodel.StagesRole;
import com.bosch.epc.datamodel.StagesRolePARequest;
import com.bosch.epc.model.StagesRolePARequestID;

/**
 * @author QYU1HC
 */
@Service
public class StagesRolePARequestService {

  @Autowired
  private StagesRolePARequestRepository stagesRolePARequestRepo;

  @Autowired
  private StagesRepository stagesRepo;

  @Autowired
  private RequestRepository requestRepo;

  @Autowired
  private ProjectAreaRepository projectAreaRepo;

  /**
   * @param requestId
   * @return
   */
  @Transactional
  public List<StagesRolePARequest> findByRequestId(final int requestId) {
    return this.stagesRolePARequestRepo.findByRequestId(requestId);
  }

  @Transactional
  public List<StagesRolePARequest> createStagesRolePARequests(final List<StagesRolePARequestID> ids) {
    List<StagesRolePARequest> createdMappings = new ArrayList<>();

    for (StagesRolePARequestID id : ids) {
      int stagesRoleId = id.getStagesRoleId();
      int projectAreaId = id.getProjectAreaId();
      int requestId = id.getRequestId();

      StagesRole stagesRole = this.stagesRepo.findById(stagesRoleId)
          .orElseThrow(() -> new RuntimeException("Stages Role not found: " + stagesRoleId));

      ProjectArea projectArea = this.projectAreaRepo.findById(projectAreaId)
          .orElseThrow(() -> new RuntimeException("Project Area not found: " + projectAreaId));

      Request request = this.requestRepo.findById(requestId)
          .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

      StagesRolePARequestID mappingKey = new StagesRolePARequestID();
      mappingKey.setStagesRoleId(stagesRoleId);
      mappingKey.setProjectAreaId(projectAreaId);
      mappingKey.setRequestId(requestId);

      StagesRolePARequest mapping = new StagesRolePARequest();
      mapping.setId(mappingKey);
      mapping.setStagesRole(stagesRole);
      mapping.setProjectArea(projectArea);
      mapping.setRequest(request);

      createdMappings.add(mapping);
    }

    return this.stagesRolePARequestRepo.saveAll(createdMappings);
  }


  /**
   * @param stagesRoleId
   * @param projectAreaId
   * @param requestId
   * @return
   */
  @Transactional
  public void deleteStagesRolePARequest(final int stagesRoleId, final int projectAreaId, final int requestId) {
    StagesRolePARequestID mappingKey = new StagesRolePARequestID();
    mappingKey.setStagesRoleId(stagesRoleId);
    mappingKey.setProjectAreaId(projectAreaId);
    mappingKey.setRequestId(requestId);

    this.stagesRolePARequestRepo.deleteById(mappingKey);
  }

  /**
   * @param stagesRoleId
   * @param projectAreaId
   * @param requestId
   * @param newStagesRolePARequestID
   */
  @Transactional
  public void updateStagesRolePARequest(final int stagesRoleId, final int projectAreaId, final int requestId,
      final StagesRolePARequestID newStagesRolePARequestID) {
    // Retrieve the new entities based on the new ids
    StagesRole newStagesRole = this.stagesRepo.findById(newStagesRolePARequestID.getStagesRoleId())
        .orElseThrow(() -> new RuntimeException("New Stages Role not found"));

    ProjectArea newProjectArea = this.projectAreaRepo.findById(newStagesRolePARequestID.getProjectAreaId())
        .orElseThrow(() -> new RuntimeException("New Project Area not found"));

    Request newRequest = this.requestRepo.findById(newStagesRolePARequestID.getRequestId())
        .orElseThrow(() -> new RuntimeException("New Request not found"));

    // Find the current mappingKey in table in DB and delete it
    StagesRolePARequestID mappingKey = new StagesRolePARequestID();
    mappingKey.setStagesRoleId(stagesRoleId);
    mappingKey.setProjectAreaId(projectAreaId);
    mappingKey.setRequestId(requestId);
    this.stagesRolePARequestRepo.deleteById(mappingKey);

    // Update the mapping with new entities
    StagesRolePARequestID newMappingKey = new StagesRolePARequestID();
    newMappingKey.setStagesRoleId(newStagesRolePARequestID.getStagesRoleId());
    newMappingKey.setProjectAreaId(newStagesRolePARequestID.getProjectAreaId());
    newMappingKey.setRequestId(newStagesRolePARequestID.getRequestId());

    StagesRolePARequest mapping = new StagesRolePARequest();
    mapping.setId(newMappingKey);
    mapping.setStagesRole(newStagesRole);
    mapping.setProjectArea(newProjectArea);
    mapping.setRequest(newRequest);

    this.stagesRolePARequestRepo.save(mapping); // Save the updated mapping
  }
}

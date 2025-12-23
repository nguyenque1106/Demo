/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import org.springframework.stereotype.Service;

import com.bosch.epc.dao.PARoleRequestRepository;
import com.bosch.epc.dao.ProjectAreaRepository;
import com.bosch.epc.datamodel.PARoleRequest;
import com.bosch.epc.datamodel.ProjectArea;

/**
 * @author VFE1COB
 *
 */

  @Service
  public class PARoleRequestService {
    private final PARoleRequestRepository parRoleRequestRepository;
    private final ProjectAreaRepository projectAreaRepository;

    /**
     * @param parRoleRequestRepository
     * @param projectAreaRepository
     */
    public PARoleRequestService(PARoleRequestRepository parRoleRequestRepository, ProjectAreaRepository projectAreaRepository) {
        this.parRoleRequestRepository = parRoleRequestRepository;
        this.projectAreaRepository = projectAreaRepository;
    }

    /**
     * @param projectAreaId if entry is available in project area table
     * @param name 
     * @return
     */
    public PARoleRequest createPARoleRequest(Integer projectAreaId, String name) {
        ProjectArea projectArea = projectAreaRepository.findById(projectAreaId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectArea not found"));

        PARoleRequest request = new PARoleRequest();
        request.setProjectArea(projectArea);
        request.setName(name);

        return parRoleRequestRepository.save(request);
    }
  }

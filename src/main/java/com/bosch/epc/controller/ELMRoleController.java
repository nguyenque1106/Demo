/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.exception.ResourceNotFoundException;
import com.bosch.epc.service.ELMRoleService;
import com.bosch.epc.service.ProjectAreaService;
import com.ibm.team.process.client.IProcessClientService;
import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author QYU1HC
 */
@RestController
@RequestMapping(value = "/project-area-roles")
@Tag(name = "Project Area Roles", description = "APIs for managing ELM roles in project areas")
public class ELMRoleController {

  private static final Logger logger = LoggerFactory.getLogger(ELMRoleController.class);

  @Autowired
  private ELMRoleService elmRoleService;

  @Autowired
  private ProjectAreaService paService;

  private List<IProjectArea> iProjectAreas = new ArrayList<>();

  /**
   * Get all ELM roles
   *
   * @return list of elm roles
   */
  @Operation(summary = "Get all ELM roles", description = "Retrieves a list of all ELM roles from the database")
  @GetMapping
  public List<ELMRole> getAllRoles() {
    return this.elmRoleService.getAllELMRoles();
  }

  /**
   * Get ELM roles by project area ID
   *
   * @param masterProjectAreaId Project area identifier
   * @return List of ELM roles for the specified project area
   */
  @Operation(summary = "Get roles by project area", description = "Retrieves a list of ELM roles associated with the specified project area")
  @GetMapping("/project-areas/{masterProjectAreaId}")
  public ResponseEntity<?> getRolesByProjectArea(@PathVariable final int masterProjectAreaId) {
    try {
      List<ELMRole> response = this.elmRoleService.getELMRolesByProjectAreaId(masterProjectAreaId);
      if (response.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }
      return ResponseEntity.ok(response);
    }
    catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  /**
   * the function to get project area
   *
   * @param projectAreaUUID
   * @throws TeamRepositoryException
   * @return iProjectArea
   */
  @SuppressWarnings("unchecked")
  public IProjectArea getProjectArea(final String projectAreaUUID, final ITeamRepository repo)
      throws TeamRepositoryException {
    if (this.iProjectAreas.size() == 0) {
      this.iProjectAreas = ((IProcessItemService) repo.getClientLibrary(IProcessItemService.class))
          .findAllProjectAreas(IProcessClientService.ALL_PROPERTIES, new NullProgressMonitor());
    }
    for (IProjectArea iProjectArea : this.iProjectAreas) {
      if (iProjectArea.getItemId().getUuidValue().contentEquals(projectAreaUUID) && !iProjectArea.isArchived()) {
        return iProjectArea;
      }
    }
    return null;
  }

}

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bosch.epc.constant.CommonConstant;
import com.bosch.epc.datamodel.ProjectArea;
import com.bosch.epc.service.ProjectAreaService;
import com.bosch.rtc.util.AlmServerConnection;
import com.bosch.rtc.util.UserUtils;
import com.ibm.team.process.client.IProcessClientService;
import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.TeamRepositoryException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author QYU1HC
 */
@RestController
@RequestMapping(value = "/project-areas")
@Tag(name = "Project Area", description = "APIs for managing project areas")
public class ProjectAreaController {

  private static final Logger logger = LoggerFactory.getLogger(ProjectAreaController.class);

  @Autowired
  private ProjectAreaService paService;

  /**
   * Get all project areas
   * 
   * @return list of project areas
   */
  @Operation(summary = "Get all project areas", description = "Retrieves a list of all project areas from the database")
  @GetMapping
  public Iterable<ProjectArea> getAllProjectAreas() {
    return this.paService.getAllPAs();
  }

  /**
   * Get project areas accessible by logged in user
   * 
   * @return list of accessible project areas
   */
  @Operation(summary = "Get accessible project areas", description = "Retrieves a list of project areas that are accessible by the currently logged in user")
  @GetMapping("/accessible")
  public Iterable<ProjectArea> getAccessibleProjectAreas() {

    try {
      String loggedInUser = UserUtils.getNTIDBySSO();

      if (loggedInUser.equals(CommonConstant.EMPTY_STR)) {
        logger.info("Anonymous user is not allowed.");
        return new ArrayList<>();
      }

      ITeamRepository repo = AlmServerConnection.getRepo();
      IProcessItemService processItemService = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
      List<IProjectArea> projectAreaList =
          processItemService.findAllProjectAreas(IProcessClientService.ALL_PROPERTIES, null);

      IContributor user = repo.contributorManager().fetchContributorByUserId(loggedInUser, null);

      List<String> accessiblePAUUIDs = new ArrayList<>();
      for (IProjectArea iProjectArea : projectAreaList) {
        if (iProjectArea.hasMember(user)) {
          logger.debug("User {} has access to Project area :{}", loggedInUser, iProjectArea.getName());
          accessiblePAUUIDs.add(iProjectArea.getItemId().getUuidValue());
        }
      }
      return this.paService.findByUuidIn(accessiblePAUUIDs);
    }
    catch (IOException | TeamRepositoryException e) {
      logger.error("ProjectAreaController getByLoggedInUser() : Unable to fecth the Project Areas of logged in user");
    }
    return null;
  }
}

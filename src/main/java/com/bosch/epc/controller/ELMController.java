/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bosch.epc.constant.StateGroupEnum;
import com.bosch.epc.constant.WITypeEnum;
import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.ProjectArea;
import com.bosch.epc.service.ELMRoleService;
import com.bosch.epc.service.ProjectAreaService;
import com.bosch.rtc.util.AlmServerConnection;
import com.bosch.rtc.util.PropertyUtils;
import com.ibm.team.process.client.IClientProcess;
import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.process.common.IRole;
import com.ibm.team.repository.client.ILoginHandler2;
import com.ibm.team.repository.client.ILoginInfo2;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.login.UsernameAndPasswordLoginInfo;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.model.IResolution;
import com.ibm.team.workitem.common.model.IState;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.workflow.IStateGroup;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;

/**
 * The ELMController class to have the endpoints on ELM server data.
 * 
 * @author QYU1HC
 */
@RestController
@RequestMapping(value = "/elm")
public class ELMController {

  private static final Logger logger = LoggerFactory.getLogger(ELMController.class);

  @Autowired
  private ELMRoleService elmRoleService;

  @Autowired
  private ProjectAreaService paService;


  /**
   * @return list of elm roles
   */
  @GetMapping("/getall")
  public List<ELMRole> getAllELMRoles() {
    return this.elmRoleService.getAllELMRoles();
  }

  /**
   * Method to fetch all state groups
   * 
   * @return list of state groups
   */
  @GetMapping("/stateGroups")
  public List<String> getAllRoleNames() {
    return Arrays.stream(StateGroupEnum.values()).map(Enum::name).collect(Collectors.toList());
  }

  /**
   * Retrieve States, Resolutions for WI Type from spec.xml
   * 
   * @param projectAreaUUID Project Area UUID
   * @return list of Condition objects
   */
  @GetMapping(value = "/fetchworkitemtypes/{projectAreaUUID}")
  public ResponseEntity<?> fetchworkitemtypesByPA(@PathVariable final String projectAreaUUID) {
    try {
      ITeamRepository repo = AlmServerConnection.getRepo();
      IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
      IProjectArea projectArea = paService.getIProjectAreaByUUID(projectAreaUUID, repo);
      if (projectArea == null) {
        throw new TeamRepositoryException("Project area UUID " + projectAreaUUID + " does not exist.");
      }

      // Find the Work Item Type
      List<IWorkItemType> workItemTypes = workItemClient.findWorkItemTypes(projectArea, new NullProgressMonitor());
      if (workItemTypes == null) {
        throw new TeamRepositoryException(
            "Work item types does not exist in project area " + projectArea.getName() + ".");
      }
      Map<String, String> workItemTypesMap = new LinkedHashMap<>();
      workItemTypesMap
          .putAll(Arrays.stream(WITypeEnum.values()).collect(Collectors.toMap(WITypeEnum::getId, WITypeEnum::getName)));
      for (IWorkItemType iWorkItemType : workItemTypes) {
        workItemTypesMap.put(iWorkItemType.getIdentifier(), iWorkItemType.getDisplayName());
      }

      logger.debug(" Work item Types Map : {}", workItemTypesMap);
      return ResponseEntity.ok(workItemTypesMap);

    }
    catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error while retrieving workflow details: " + ex.getMessage());
    }
  }

  /**
   * Retrieve States, Resolutions for WI Type from spec.xml
   * 
   * @param projectAreaUUID Project Area UUID
   * @param workItemTypeId  Work item Type Id
   * @return list of Condition objects
   */
  @GetMapping(value = "/fetchstategroupmap/{projectAreaUUID}/{workItemTypeId}")
  public ResponseEntity<?> fetchstategroupmap(@PathVariable final String projectAreaUUID,
      @PathVariable final String workItemTypeId) {
    try {
      ITeamRepository repo = AlmServerConnection.getRepo();
      IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
      IProjectArea projectArea = paService.getIProjectAreaByUUID(projectAreaUUID, repo);
      if (projectArea == null) {
        throw new TeamRepositoryException("Project area UUID : " + projectAreaUUID + " does not exist.");
      }

      if (Arrays.stream(WITypeEnum.values()).anyMatch(e -> e.getId().equalsIgnoreCase(workItemTypeId))) {
        return ResponseEntity.ok(Arrays.stream(StateGroupEnum.values())
            .collect(Collectors.toMap(Enum::name, e -> new ArrayList<>(), (a, b) -> a, LinkedHashMap::new)));
      }

      // Find the Work Item Type
      IWorkItemType workItemType =
          workItemClient.findWorkItemType(projectArea, workItemTypeId, new NullProgressMonitor());
      if (workItemType == null) {
        throw new TeamRepositoryException(
            "Work item type " + workItemTypeId + " does not exist in project area " + projectArea.getName() + ".");
      }

      // Get the Workflow Information for the Work Item Type
      IWorkflowInfo workflowInfo = workItemClient.getWorkflow(workItemTypeId, projectArea, new NullProgressMonitor());


      if (workflowInfo == null) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("No workflow defined for work item type: " + workItemTypeId);
      }

      // Iterate through states and actions
      Map<Map<String, String>, List<Map<String, String>>> stateGroupMap = new LinkedHashMap<>();
      

      Identifier<IState>[] states = workflowInfo.getAllStateIds();

      for (Identifier<IState> stateId : states) {

          String stateName = workflowInfo.getStateName(stateId);

          // Get resolution IDs for the state
          Identifier<IResolution>[] resolutionIds = workflowInfo.getStateResolutionIds(stateId);

          if (resolutionIds.length == 0) {

              // No resolutions → fetch state group with null
              IStateGroup group = workflowInfo.getStateGroup(stateId, null);
              String groupName = group.getName();

              // Key map
              Map<String, String> keyMap = new LinkedHashMap<>();
              keyMap.put(Arrays.stream(StateGroupEnum.values())
                  .filter(sg -> sg.getName().equalsIgnoreCase(groupName))
                  .map(StateGroupEnum::getId)
                  .findFirst()
                  .orElse(null), groupName);

              // Value entry map
              Map<String, String> valueItem = new LinkedHashMap<>();
              valueItem.put(stateId.getStringIdentifier(), stateName);

              // Add to main map
              stateGroupMap
                  .computeIfAbsent(keyMap, k -> new ArrayList<>())
                  .add(valueItem);
          }
          else {

              for (Identifier<IResolution> resId : resolutionIds) {

                  String resolutionName = workflowInfo.getResolutionName(resId);
                  IStateGroup group = workflowInfo.getStateGroup(stateId, resId);
                  String groupName = group.getName();

                  // Key map
                  Map<String, String> keyMap = new LinkedHashMap<>();
                  keyMap.put(Arrays.stream(StateGroupEnum.values())
                      .filter(sg -> sg.getName().equalsIgnoreCase(groupName))
                      .map(StateGroupEnum::getId)
                      .findFirst()
                      .orElse(null), groupName);

                  // Value entry map
                  Map<String, String> valueItem = new LinkedHashMap<>();
                  valueItem.put(stateId.getStringIdentifier(), stateName);
                  valueItem.put(resId.getStringIdentifier(), resolutionName);

                  // Add into LinkedHashMap (keeps order)
                  stateGroupMap
                      .computeIfAbsent(keyMap, k -> new ArrayList<>())
                      .add(valueItem);
              }
          }
      }
      
      logger.debug("State Group Map : {}", stateGroupMap);
      return ResponseEntity.ok(stateGroupMap);

    }
    catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error while retrieving workflow details: " + ex.getMessage());
    }
  }

  /**
   * Method to update ELM Role
   * 
   * @param request
   * @return
   * @throws ClassNotFoundException
   * @throws IOException
   */
  @PostMapping("/updateelmrole")
  public @ResponseBody Map<String, String> handleUpdateELMRole(@RequestBody final Map<String, String> request)
      throws ClassNotFoundException, IOException {
    try {
      if (!TeamPlatform.isStarted()) {
        TeamPlatform.startup();
      }
      ITeamRepository repo =
          TeamPlatform.getTeamRepositoryService().getTeamRepository(PropertyUtils.getPropValues("ALM_REPOSITORY_URL"));
      repo.setConnectionTimeout(900);
      repo.registerLoginHandler(new ILoginHandler2() {

        @Override
        public ILoginInfo2 challenge(final ITeamRepository repo) {
          try {
            return new UsernameAndPasswordLoginInfo(PropertyUtils.getPropValues("ALM_TEST_USER_NAME"),
                PropertyUtils.getPropValues("ALM_TEST_USER_PASSWORD"));
          }
          catch (IOException e) {
            logger.error(e.getMessage());
          }
          return null;
        }
      });
      repo.login(new NullProgressMonitor());
      IProcessItemService processItemService = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
      List<ProjectArea> projectAreas = this.paService.getAllPAs();

      // Create the ELM Roles
      List<ELMRole> elmRoles = new ArrayList<>();

      for (ProjectArea pa : projectAreas) {
        String paUUID = pa.getUuid();
        IProjectArea iProjectArea = paService.getIProjectAreaByUUID(paUUID, repo);
        if (iProjectArea == null) {
          continue;
        }
        IClientProcess clientProcess = processItemService.getClientProcess(iProjectArea, new NullProgressMonitor());

        IRole[] availableRoles = clientProcess.getRoles(iProjectArea, new NullProgressMonitor());
        for (IRole role : availableRoles) {
          ELMRole elmRole = new ELMRole();
          elmRole.setProjectArea(pa);
          elmRole.setName(role.getId());
          elmRole.setIdentifier(role.getId());
          elmRole.setCreatedBy(PropertyUtils.getPropValues("ALM_SERVICE_USER_NAME"));
          elmRole.setCreationDate(new Date(System.currentTimeMillis()));
          elmRoles.add(elmRole);
        }
      }

      this.elmRoleService.saveELMRoles(elmRoles);

    }
    catch (TeamRepositoryException e) {
      logger.error("Exception while connecting to ALM: " + e);
    }

    // Respond with a success message
    Map<String, String> response = new HashMap<>();
    response.put("status", "success");
    response.put("message", "ELM Roles handled successfully!");
    return response;
  }

}

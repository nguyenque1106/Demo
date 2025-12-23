/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bosch.epc.constant.CommonConstant;
import com.bosch.epc.constant.StateGroupEnum;
import com.bosch.epc.dao.AttrPermLockRepository;
import com.bosch.epc.datamodel.AttrPermCondition;
import com.bosch.epc.datamodel.AttrPermRole;
import com.bosch.epc.datamodel.AttrPermWorkflow;
import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.ProjectArea;
import com.bosch.epc.datamodel.Request;
import com.bosch.epc.exception.EpcException;
import com.bosch.epc.model.XMLCondition;
import com.bosch.epc.service.ELMAttrPermService;
import com.bosch.epc.service.ELMAttrPermServiceImpl;
import com.bosch.epc.service.ELMRoleService;
import com.bosch.epc.service.ProjectAreaService;
import com.bosch.epc.service.RequestService;
import com.bosch.rtc.util.AlmServerConnection;
import com.bosch.rtc.util.ELMUtils;
import com.bosch.rtc.util.PropertyUtils;
import com.bosch.rtc.util.TEUUtility;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;

import io.swagger.v3.oas.annotations.Operation;

/**
 * Rest controller class to manage the updates of ELM Project Areas to handle the Attribute level permisssions.
 *
 * @author PPT4KOR
 */
@RestController
@RequestMapping(value = "/attrperm")
public class ELMAttrPermController {

  private static final Logger logger = LoggerFactory.getLogger(ELMAttrPermController.class);

  @Autowired
  private ELMAttrPermService service;

  @Autowired
  private ProjectAreaService paService;

  @Autowired
  private RequestService requestService;

  @Autowired
  private ELMAttrPermServiceImpl attrPermService;

  @Autowired
  private AttrPermLockRepository attrPermLockRepo;

  @Autowired
  private ELMRoleService elmRoleService;

  /**
   * @return list of project areas
   */
  @GetMapping("/getall")
  public Iterable<AttrPermCondition> getAll() {
    return this.service.getAll();
  }

  /**
   * Getting Attribute has relationship to Request and ProjectArea
   *
   * @param projectAreaID   - project area id
   * @param workitemTypeID  work item type id
   * @param stateGroup      statGroup Id
   * @param stateId         State Id
   * @param resolutionId    Resolution Id
   * @param filteredRoleIds Role Ids
   * @return list of project areas
   */
  @Operation(summary = "Getting Attribute has relationship to Request and ProjectArea")
  @GetMapping("/default/filter")
  public ResponseEntity<?> filterData(@RequestParam(required = true) Integer projectAreaID,
      @RequestParam(required = true) String workitemTypeID, @RequestParam(required = false) String stateGroup,
      @RequestParam(required = false) String stateId, @RequestParam(required = false) String resolutionId,
      @RequestParam(required = false) List<Integer> filteredRoleIds) {

    // Validate: At least one input must be provided
    if (stateGroup == null && stateId == null && resolutionId == null && filteredRoleIds == null) {
      return ResponseEntity.badRequest().body(
          Map.of("error", "At least one filter must be provided: stateGroup, state, resolution or filteredUserRoles"));
    }

    try {
      logger.info("Getting Attribute Permission Roles Conditions by projectId[{}]. {}", projectAreaID,
          CommonConstant.MESS_START);
      List<AttrPermCondition> attrPermConditions;
      ITeamRepository repo = AlmServerConnection.getRepo();

      // get Project Area from project area Id
      ProjectArea selectedPA = this.paService.findById(projectAreaID).stream().findFirst()
          .orElseThrow(() -> new RuntimeException("Project Area not found"));

      IProjectArea projectArea = paService.getIProjectAreaByUUID(selectedPA.getUuid(), repo);

      String badReqMessage;
      if (projectArea == null) {
        badReqMessage = String.format("Cannot find projectarea with id: %d", projectAreaID);
        logger.error(badReqMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badReqMessage);
      }

      String stateGroupName = stateGroup != null ? Arrays.stream(StateGroupEnum.values())
          .filter(e -> e.getId().equalsIgnoreCase(stateGroup)).map(StateGroupEnum::getName).findFirst().get() : null;
      String workitemTypeName = ELMUtils.getWorkitemTypeName(workitemTypeID, repo, projectArea);
      String stateName = ELMUtils.getStateNameById(workitemTypeID, stateId, repo, projectArea);
      String resolutionName = stateName != null
          ? ELMUtils.getResolutionNameById(workitemTypeID, stateId, resolutionId, repo, projectArea) : null;
      List<String> elmRolesFiltered = getRoleNamesByIds(filteredRoleIds, selectedPA.getId());

      // find the specification path
      String specificationFilePath = PropertyUtils.getPropValues(PropertyUtils.TEU_TOOL_PATH_SPEC)
          .replace(PropertyUtils.PARAM_PROJECT_AREA_NAME, projectArea.getName());
      if (!new File(specificationFilePath).exists()) {
        // Download the PA - need verify which template need to be downloaded
        TEUUtility.downloadTemplate(projectArea.getName());
      }
      if (!new File(specificationFilePath).exists()) {
        badReqMessage = "There is no specification file to parse data. Re-check the downloading.";
        logger.error(badReqMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badReqMessage);
      }

      // get attribute matrix data and also check for empty data
      List<XMLCondition> xmlConditions = this.requestService.fetchPermissionsFromSpec(projectArea.getName());

      logger.debug("XMLCondition list is empty?{}", xmlConditions.isEmpty());
      if (xmlConditions.isEmpty()) {

        // create fresh copy of conditions
        attrPermConditions = this.attrPermService.createEmptyConditions(specificationFilePath, selectedPA);
      }
      else {
        // convert XMLCondition to AttrPermCondition and return the data
        attrPermConditions =
            this.attrPermService.createAttrPermConditions(specificationFilePath, selectedPA, xmlConditions);
      }

      // update the request data into the deails and disable the changes
      return ResponseEntity.ok(filterAttrConditions(attrPermConditions, stateGroupName, workitemTypeName, stateName,
          resolutionName, elmRolesFiltered));

    }
    catch (EpcException | IOException | TeamRepositoryException e) {
      e.printStackTrace();
      logger.error("Error:{}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    finally {
      logger.info("Getting Attribute Permission Roles Conditions by requestId[], and projectId[{}]. {}", projectAreaID,
          CommonConstant.MESS_END);
    }

  }

  /**
   * Method to filter the Attribute Conditions
   * 
   * @param attrPermConditions Attribute Conditions
   * @param stateGroupName     stateGroup Name
   * @param workitemTypeName   work item Type Name
   * @param stateName          state Name
   * @param resolutionName     resolution Name
   * @param elmRolesFiltered   ELM Role Names
   * @return Filtered Attribute Conditions
   */
  private List<AttrPermCondition> filterAttrConditions(List<AttrPermCondition> attrPermConditions,
      String stateGroupName, String workitemTypeName, String stateName, String resolutionName,
      List<String> elmRolesFiltered) {
    return attrPermConditions.stream().peek(c -> {
      if (c.getAttrPermWorkflows() == null)
        c.setAttrPermWorkflows(Collections.emptyList());
    }).map(condition -> {
      List<AttrPermWorkflow> filtered =
          condition.getAttrPermWorkflows().stream()
              .peek(
                  w -> logger
                      .debug(
                          "Witype() : "
                              + w.getWitype() + " status : " + w.getWistatus() + " Resolution : " + w
                                  .getWiresolution()))
              .filter(w -> workitemTypeName.equalsIgnoreCase(w.getWitype())
                  && (stateGroupName != null ? stateGroupName.equalsIgnoreCase(w.getWistatusgrp())
                      : (resolutionName == null ? stateName.equalsIgnoreCase(w.getWistatus())
                          : (stateName.equalsIgnoreCase(w.getWistatus())
                              && resolutionName.equalsIgnoreCase(w.getWiresolution())))))
              .map(w -> {
                if (w.getAttrPermRoles() == null) {
                  w.setAttrPermRoles(Collections.emptyList());
                }
                else {
                  List<AttrPermRole> filteredRoles = w.getAttrPermRoles().stream()
                      .peek(r -> logger.debug("Role Identifier : " + r.getPaRole().getIdentifier()))
                      .filter(r -> r.getPaRole() != null)
                      .filter(r -> elmRolesFiltered.contains(r.getPaRole().getName())).collect(Collectors.toList());
                  w.setAttrPermRoles(filteredRoles);
                }
                return w;
              }).collect(Collectors.toList());

      condition.setAttrPermWorkflows(filtered);
      return condition;
    }).collect(Collectors.toList());
  }

  /**
   * Method to get Role Names list by Role Ids
   * 
   * @param filteredRoleIds filtered RoleIds
   * @param projectAreaId   projectArea Id
   * @return List of Role Names
   */
  public List<String> getRoleNamesByIds(List<Integer> filteredRoleIds, int projectAreaId) {

    if (filteredRoleIds == null || filteredRoleIds.isEmpty()) {
      return elmRoleService.getELMRolesByProjectAreaId(projectAreaId).stream().map(ELMRole::getName)
          .collect(Collectors.toList());
    }

    return elmRoleService.getELMRolesByProjectAreaId(projectAreaId).stream()
        .filter(role -> filteredRoleIds.contains(role.getId())).map(ELMRole::getName).collect(Collectors.toList());
  }

  /**
   * Getting Attribute has relationship to Request and ProjectArea
   *
   * @param requestid       - request id
   * @param projectAreaID   - project area id
   * @param workitemTypeID  - work item Type ID
   * @param stateGroup      - stateGroup
   * @param stateId         - state Id
   * @param resolutionId    - resolution Id
   * @param filteredRoleIds - filtered RoleIds
   * @return list of attribute conditions
   */
  @Operation(summary = "Getting Attribute has relationship to Request and ProjectArea")
  @GetMapping("/request/{requestid}/filter")
  public ResponseEntity<?> filterRequestData(
      @PathVariable(name = "requestid", required = false) final Integer requestid,
      @RequestParam(required = false) Integer projectAreaID, @RequestParam(required = false) String workitemTypeID,
      @RequestParam(required = false) String stateGroup, @RequestParam(required = false) String stateId,
      @RequestParam(required = false) String resolutionId,
      @RequestParam(required = false) List<Integer> filteredRoleIds) {
    try {
      logger.info("Getting Attribute Permission Roles Conditions by requestId[{}], and projectId[{}]. {}", requestid,
          projectAreaID, CommonConstant.MESS_START);
      List<AttrPermCondition> attrPermConditions;
      ITeamRepository repo = AlmServerConnection.getRepo();

      // code for no changesets
      ProjectArea selectedPA = this.paService.findById(projectAreaID).stream().findFirst()
          .orElseThrow(() -> new RuntimeException("Project Area not found"));


      IProjectArea projectArea = paService.getIProjectAreaByUUID(selectedPA.getUuid(), repo);

      String badReqMessage;
      if (projectArea == null) {
        badReqMessage = String.format("Cannot find projectarea with id: %d", projectAreaID);
        logger.error(badReqMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badReqMessage);
      }
      String stateGroupName = stateGroup != null ? Arrays.stream(StateGroupEnum.values())
          .filter(e -> e.getId().equalsIgnoreCase(stateGroup)).map(StateGroupEnum::getName).findFirst().get() : null;
      String workitemTypeName = ELMUtils.getWorkitemTypeName(workitemTypeID, repo, projectArea);
      String stateName = ELMUtils.getStateNameById(workitemTypeID, stateId, repo, projectArea);
      String resolutionName = stateName != null
          ? ELMUtils.getResolutionNameById(workitemTypeID, stateId, resolutionId, repo, projectArea) : null;
      List<String> elmRolesFiltered = getRoleNamesByIds(filteredRoleIds, selectedPA.getId());
      Request request = getRequestById(requestid);
      // in case there hasn't any existed request - getRequestById method can be optimised after api change.
      if (request == null) {
        logger.debug("There is no request with id:{}", requestid);
        // find the specification path
        String specificationFilePath = PropertyUtils.getPropValues(PropertyUtils.TEU_TOOL_PATH_SPEC)
            .replace(PropertyUtils.PARAM_PROJECT_AREA_NAME, selectedPA.getName());
        if (!new File(specificationFilePath).exists()) {
          // Download the PA - need verify which template need to be downloaded
          TEUUtility.downloadTemplate(selectedPA.getName());
        }
        if (!new File(specificationFilePath).exists()) {
          badReqMessage = "There is no specification file to parse data. Re-check the downloading.";
          logger.error(badReqMessage);
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badReqMessage);
        }

        // get attribute matrix data and also check for empty data
        List<XMLCondition> xmlConditions = this.requestService.fetchPermissionsFromSpec(selectedPA.getName());

        logger.debug("XMLCondition list is empty?{}", xmlConditions.isEmpty());
        if (xmlConditions.isEmpty()) {

          // create fresh copy of conditions
          attrPermConditions = this.attrPermService.createEmptyConditions(specificationFilePath, selectedPA);
        }
        else {
          // convert XMLCondition to AttrPermCondition and return the data
          attrPermConditions =
              this.attrPermService.createAttrPermConditions(specificationFilePath, selectedPA, xmlConditions);
        }
      }
      else {
        logger.debug("Found out request and projectarea. Getting from database.");
        // Has role-permission in DB ?
        attrPermConditions = this.attrPermService.getListAttrPermCondFromDB(request, selectedPA);
      }

      // update the request data into the deails and disable the changes
      return ResponseEntity.ok(filterAttrConditions(attrPermConditions, stateGroupName, workitemTypeName, stateName,
          resolutionName, elmRolesFiltered));

    }
    catch (EpcException | IOException | TeamRepositoryException e) {
      logger.error("Error:{}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    finally {
      logger.info("Getting Attribute Permission Roles Conditions by requestId[{}], and projectId[{}]. {}", requestid,
          projectAreaID, CommonConstant.MESS_END);
    }

  }

  /**
   * Method to get locked data of Attribute conditions
   * 
   * @return List of Attribute conditions
   */
  @Operation(summary = "Getting Attribute has relationship to Request and ProjectArea")
  @GetMapping("/getlockinfo")
  public ResponseEntity<?> getLockInfo() {
    return ResponseEntity.ok(attrPermLockRepo.findAllOpenRequest());

  }


  /**
   * Mehtod to get Request Object by requestid
   * 
   * @param requestid request id
   * @return Request Object
   */
  private Request getRequestById(final Integer requestid) {
    if ((requestid == null) || (requestid.intValue() == 0)) {
      return null;
    }
    try {
      return this.requestService.findDataByRequestId(requestid);
    }
    catch (EntityNotFoundException e) {
      logger.warn("Request id {} does NOT exist.", requestid);
    }
    return null;
  }

}

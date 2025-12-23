/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bosch.epc.constant.ToolRoleEnum;
import com.bosch.epc.datamodel.Request;
import com.bosch.epc.model.Attribute;
import com.bosch.epc.model.WorkItemType;
import com.bosch.epc.model.XMLCondition;
import com.bosch.epc.service.RequestService;
import com.bosch.rtc.util.UserUtils;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author VFE1COB Main controller for handling epc requests, workon requests
 */
@RestController
@RequestMapping(value = "/request")
public class RequestController {

  private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

  @Autowired
  private RequestService requestService;

  /**
   * Get WorkON request status
   * 
   * @param workonid id of the workon request
   * @return status of the request
   */

  @GetMapping(value = "/workonstatus/{workonid}")
  public String getRequestbyId(@PathVariable(name = "workonid") String workonid) {

    try {
      return requestService.getRequestStatus(workonid);
    }
    catch (IOException e) {
      logger.debug("Error while invoking Workon application" + e);
      return "WorkonFailure";
    }

  }

  /**
   * create workon request
   * 
   * @param workonRequest WorkOnRequest with necessary details populated
   * @return
   */


  /**
   * @param changesetname name of the changeset
   * @param description   description of the changeset
   * @return response after updating in Request table
   */
  @PostMapping(value = "/create")
  @Operation(summary = "Create new blank changeset", description = "Crerate new request without changes")

  public ResponseEntity<Integer> createChangeset(@RequestBody Map<String, String> params) {
    Request request = requestService.createRequestEntry(params.get("changesetname"), params.get("description"));
    return ResponseEntity.ok(request.getId());
  }

  /**
   * @param request object from UI with all the changes
   * @return response after updating in Request table
   */

  @PostMapping(value = "/update", consumes = { MediaType.APPLICATION_JSON_VALUE, "application/json;charset=UTF-8" })
  @Operation(summary = "update request", description = "Update request with changes")
  public ResponseEntity<String> updateRequest(@RequestBody Request request) {
    requestService.updateRequestEntry(request);
    if (request.getAttrPermConditionMappings() != null && !(request.getAttrPermConditionMappings().isEmpty()))
      requestService.updatePermissionRole(request);

    return ResponseEntity.ok("Updated Request table successfully");
  }

  /**
   * @param requestId the submitted request id
   * @return response after updating in Request table
   */
  @PostMapping(value = "/create-workon")
  @Operation(summary = "update request", description = "Update request with changes")
  public ResponseEntity<?> createWorkON(@RequestBody Map<String, String> params) {
    int requestId = Integer.parseInt(params.get("requestId"));
    Request request = requestService.findDataByRequestId(requestId);
    if (request.getStagesRoleELMRoleReqMapping().isEmpty() && request.getRolePermActReqMappings().isEmpty()
        && request.getAttrPermConditionMappings().isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("status", 400, "error", "Invalid Request", "message",
          "The request is empty. Please add details before submitting."));
    }
    Request req = requestService.createWorkON(requestId, params.get("approver"));
    return ResponseEntity.ok(req.getWorkonid());
  }


  /**
   * Retrieve Attributes, workitem mapping from spec.xml
   * 
   * @return Map<String, Attribute> list of attributes along with theie details
   */
  @GetMapping(value = "/attributes")

  public @ResponseBody Map<String, Attribute> getAllAttributes() {
    return requestService.fetchAttributesfromSpec();
  }


  /**
   * Retrieve Conditions, Workflow properties like permissions and ALM role mapping from spec.xml
   * 
   * @return list of Condition objects
   */
  @GetMapping(value = "/permissions")

  public @ResponseBody Iterable<XMLCondition> getAllConditions() {
    return requestService.fetchPermissionsFromSpec();
  }

  /**
   * Retrieve WorkItemType from spec.xml
   * 
   * @return list of WorkItemType objects
   */
  @GetMapping(value = "/workitemtype")

  public @ResponseBody List<WorkItemType> getWITypes() {
    return requestService.fetchWITypeFromSpec();
  }


  /**
   * Retrieve existing Changesets from DB
   * 
   * @return list of Request objects
   */
  @GetMapping(value = "")
  @Operation(summary = "Retrieve All Requests", description = "To retrieve all request and their corrresponding changes")
  public @ResponseBody List<Request> getChangesets() {
    if (ToolRoleEnum.ADMIN.hasRole() || ToolRoleEnum.PROCESS_OWNER.hasRole()) {
      return requestService.findAll();
    }
    return requestService.findByCreatedBy(UserUtils.extractSimpleUsername());
  }

  /**
   * Retrieve particular Changesets from DB
   * 
   * @param requestId to retrieve particular Request
   * @return list of Request objects
   */
  @GetMapping(value = "/{requestId}")
  @Operation(summary = "Retrieve particular request", description = "To retrieve particular request and its corresponding changes based on request id")

  public @ResponseBody Request getChangesetDetails(@PathVariable final int requestId) {
    return requestService.findDataByRequestId(requestId);
  }
}
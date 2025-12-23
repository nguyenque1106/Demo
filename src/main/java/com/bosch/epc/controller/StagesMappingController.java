/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.controller;

import java.util.List;

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

import com.bosch.epc.datamodel.StagesRoleELMRoleReqMappingId;
import com.bosch.epc.datamodel.StagesRolePA;
import com.bosch.epc.datamodel.StagesRolePARequest;
import com.bosch.epc.exception.ResourceNotFoundException;
import com.bosch.epc.model.StagesRoleELMRoleReqResponse;
import com.bosch.epc.model.StagesRoleELMRoleResponse;
import com.bosch.epc.model.StagesRolePARequestID;
import com.bosch.epc.service.StagesRoleELMRoleReqServiceImpl;
import com.bosch.epc.service.StagesRoleELMRoleServiceImpl;
import com.bosch.epc.service.StagesRolePARequestService;
import com.bosch.epc.service.StagesRolePAService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author QYU1HC
 */
@RestController
@RequestMapping("/stages-mappings")
@Tag(name = "Stages Mappings", description = "APIs for managing stages role mappings")
public class StagesMappingController {

  @Autowired
  private StagesRoleELMRoleServiceImpl stagesRoleELMRoleServiceImpl;

  @Autowired
  private StagesRoleELMRoleReqServiceImpl stagesRoleELMRoleReqServiceImpl;

  @Autowired
  private StagesRolePAService stagesRolePAService;

  @Autowired
  private StagesRolePARequestService stagesRolePARequestService;

  /**
   * Get all stages role to project area mappings
   *
   * @return List of stages role to project area mappings
   */
  @Operation(summary = "Get all stages role PA mappings", description = "Retrieves a list of all stages role to project area mappings")
  @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successfully retrieved mappings") })
  @GetMapping("/project-areas")
  public @ResponseBody Iterable<StagesRolePA> getAllStagesRolePA() {
    return this.stagesRolePAService.getAll();
  }

  /**
   * Get stages role to project area mappings by request ID
   *
   * @param requestId Request identifier
   * @return List of stages role to project area request mappings
   */
  @Operation(summary = "Get stages role PA mappings by request", description = "Retrieves a list of stages role to project area mappings for the specified request")
  @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successfully retrieved mappings") })
  @GetMapping("/project-areas/request/{requestId}")
  public @ResponseBody Iterable<StagesRolePARequest> getAllStagesRolePAByRequestId(
      @Parameter(description = "ID of the request to get mappings for") @PathVariable final int requestId) {
    return this.stagesRolePARequestService.findByRequestId(requestId);
  }

  /**
   * Create new stages role to project area request mappings
   *
   * @param ids List of mapping request data
   * @return Created mappings
   */
  @Operation(summary = "Create multiple stages role PA mappings", description = "Creates new stages role to project area request mappings")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Successfully created mappings"),
      @ApiResponse(responseCode = "400", description = "Invalid request data"),
      @ApiResponse(responseCode = "404", description = "One or more entities not found") })
  @PostMapping("/project-areas")
  public ResponseEntity<?> createMappings(
      @Parameter(description = "List of mapping request data") @RequestBody final List<StagesRolePARequestID> ids) {

    try {
      List<StagesRolePARequest> createdMappings = this.stagesRolePARequestService.createStagesRolePARequests(ids);
      return ResponseEntity.ok(createdMappings);
    }
    catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create mappings: " + e.getMessage());
    }
  }


  /**
   * Get all stages role to ELM role mappings
   *
   * @return List of stages role to ELM role mappings
   */
  @Operation(summary = "Get all stages role ELM mappings", description = "Retrieves a list of all stages role to ELM role mappings")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Successfully retrieved mappings"),
      @ApiResponse(responseCode = "404", description = "No mappings found") })
  @GetMapping("/default")
  public ResponseEntity<?> getAllStagesRoleELMRole() {
    try {
      List<StagesRoleELMRoleResponse> response = this.stagesRoleELMRoleServiceImpl.findAll();
      return ResponseEntity.ok(response);
    }
    catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  /**
   * Get stages role to ELM role mappings by request ID
   *
   * @param requestId Request identifier
   * @return List of stages role to ELM role request mappings
   */
  @Operation(summary = "Get stages role ELM mappings by request", description = "Retrieves a list of stages role to ELM role mappings for the specified request")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Successfully retrieved mappings"),
      @ApiResponse(responseCode = "404", description = "Request not found or has no mappings") })
  @GetMapping("/request/{requestId}")
  public ResponseEntity<?> getMappingByRequestId(
      @Parameter(description = "ID of the request to get mappings for") @PathVariable final String requestId) {
    try {
      List<StagesRoleELMRoleReqResponse> response = this.stagesRoleELMRoleReqServiceImpl.findByRequestId(requestId);
      return ResponseEntity.ok(response);
    }
    catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  /**
   * Create new stages role to ELM role request mappings
   *
   * @param ids List of mapping identifiers
   * @return Created mappings
   */
  @Operation(summary = "Create multiple stages role to ELM role request mappings", description = "Creates new stages role to ELM role request mappings")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Successfully created mappings"),
      @ApiResponse(responseCode = "400", description = "Invalid mapping data") })
  @PostMapping("/elm-roles")
  public ResponseEntity<?> addStagesRoleELMRoleReqMappings(
      @Parameter(description = "List of mapping data") @RequestBody final List<StagesRoleELMRoleReqMappingId> ids) {
    try {
      List<StagesRoleELMRoleReqResponse> responses =
          this.stagesRoleELMRoleReqServiceImpl.addNewStagesRoleELMRoleReqMappings(ids);
      return ResponseEntity.ok(responses);
    }
    catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create mappings: " + e.getMessage());
    }
  }

}

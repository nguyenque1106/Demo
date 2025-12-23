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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bosch.epc.datamodel.ELMPermissions;
import com.bosch.epc.datamodel.RolePermReqtMapping;
import com.bosch.epc.dto.PermissionWithMappingDTO;
import com.bosch.epc.exception.ResourceNotFoundException;
import com.bosch.epc.service.ELMPermService;
import com.bosch.epc.service.RolePermMappingService;
import com.bosch.epc.service.RolePermReqtMappingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author QYU1HC
 */
@RestController
@RequestMapping(value = "/elmpermissions")
@Tag(name = "Permissions", description = "APIs for managing ELM permissions")
public class ELMPermController {

  private final RolePermMappingService rolePermActService;
  private final RolePermReqtMappingService rolePermActReqService;


  /**
   * PermissionServiceInterface
   */
  @Autowired
  private ELMPermService service;

  /**
   * @param rolePermActService
   * @param rolePermActReqService
   */
  public ELMPermController(final RolePermMappingService rolePermActService,
      final RolePermReqtMappingService rolePermActReqService) {
    super();
    this.rolePermActService = rolePermActService;
    this.rolePermActReqService = rolePermActReqService;
  }

  /**
   * Get all permissions with their mapping status for a specific role
   *
   * @param roleId Role identifier
   * @return List of permissions with their mapping status
   */
  @Operation(summary = "Get permissions with mapping status by role", description = "Retrieves all permissions with their mapping status for the specified role using LEFT JOIN")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Successfully retrieved permissions with mapping status"),
      @ApiResponse(responseCode = "404", description = "No permissions found") })
  @GetMapping("/default/{roleId}")
  public ResponseEntity<?> getPermissionsWithMappingByRoleId(
      @Parameter(description = "ID of the role to get permissions with mapping for") @PathVariable final int roleId) {
    try {
      List<PermissionWithMappingDTO> response = this.rolePermActService.getPermissionsWithMappingByRoleId(roleId);

      if (response.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No permissions found with permissionGroup not null");
      }

      return ResponseEntity.ok(response);
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error retrieving permissions: " + e.getMessage());
    }
  }

  /**
   * Get role permission action mappings by request ID
   *
   * @param requestId Request identifier
   * @return List of role permission action request mappings
   */
  @Operation(summary = "Get mappings by request", description = "Retrieves a list of role permission action mappings for the specified request")
  @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successfully retrieved mappings"),
      @ApiResponse(responseCode = "404", description = "No mappings found for request") })
  @GetMapping("/requests/{requestId}")
  public ResponseEntity<?> getMappingsByRequestId(
      @Parameter(description = "ID of the request to get mappings for") @PathVariable final int requestId) {
    try {
      List<RolePermReqtMapping> reqMappings = this.rolePermActReqService.getMappingsByRequestId(requestId);
      if (reqMappings.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(reqMappings);
      }
      return ResponseEntity.ok(reqMappings);
    }
    catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  /**
   * Get role permission request mappings by role ID and request ID
   *
   * @param roleId    Role identifier
   * @param requestId Request identifier
   * @return List of role permission request mappings for the specified role and request
   */
  @Operation(summary = "Get permission mappings by role and request", description = "Retrieves role permission request mappings for a specific role and request")
  @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successfully retrieved permission mappings"),
      @ApiResponse(responseCode = "404", description = "Role or request not found"),
      @ApiResponse(responseCode = "204", description = "No permission mappings found for the specified role and request") })
  @GetMapping("/request/{requestId}/{roleId}")
  public ResponseEntity<?> getMappingsByRoleAndRequest(
      @Parameter(description = "ID of the role to get permission mappings for") @PathVariable final int roleId,
      @Parameter(description = "ID of the request to get permission mappings for") @PathVariable final int requestId) {

    try {
      List<RolePermReqtMapping> mappings = this.rolePermActReqService.getMappingsByRoleAndRequest(roleId, requestId);

      if (mappings.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mappings);
      }

      return ResponseEntity.ok(mappings);

    }
    catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  /**
   * Get list of permission types for Attribute permission module
   *
   * @return List of permission types for Attribute permission module
   */
  @Operation(summary = "Get list of permission types for Attribute permission module", description = "Retrieves a list of permission types for Attribute permission module")
  @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successfully retrieved permissions"),
      @ApiResponse(responseCode = "404", description = "Attribute Permission not found") })
  @GetMapping("/attributepermissions")
  public ResponseEntity<?> retrievePermissionTypes() {
    try {
      List<ELMPermissions> response = this.service.findAllByIsAttrPermission(true);
      if (response.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }
      return ResponseEntity.ok(response);
    }
    catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
}

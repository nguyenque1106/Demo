/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bosch.epc.datamodel.RolePermMapping;
import com.bosch.epc.datamodel.RolePermReqtMapping;
import com.bosch.epc.datamodel.RolePermReqtMappingId;
import com.bosch.epc.exception.ResourceNotFoundException;
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
@RequestMapping("/role-permission-mappings")
@Tag(name = "Role Permission Mappings", description = "APIs for managing role permission action mappings")
public class RolePermMappingController {

  private final RolePermMappingService rolePermActService;
  private final RolePermReqtMappingService rolePermActReqService;

  /**
   * @param rolePermActService Role permission action service
   * @param rolePermActReqService Role permission action request service
   */
  public RolePermMappingController(final RolePermMappingService rolePermActService,
      final RolePermReqtMappingService rolePermActReqService) {
    this.rolePermActService = rolePermActService;
    this.rolePermActReqService = rolePermActReqService;
  }

  /**
   * Get all role permission action mappings
   *
   * @return List of role permission action mappings
   */
  @Operation(summary = "Get all role permission mappings", description = "Retrieves a list of all role permission action mappings")
  @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successfully retrieved mappings") })
  @GetMapping
  public ResponseEntity<List<RolePermMapping>> getAllMappings() {
    List<RolePermMapping> mappings = this.rolePermActService.getAllMappings();
    return ResponseEntity.ok(mappings);
  }

  /**
   * Update multiple role permission action request mappings
   *
   * @param ids List of mapping identifiers
   * @return List of updated mappings
   */
  @Operation(summary = "Update multiple role permission mappings", description = "Updates existing role permission action request mappings")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Successfully created mappings"),
      @ApiResponse(responseCode = "400", description = "Invalid request data"),
      @ApiResponse(responseCode = "404", description = "One or more entities not found") })
  @PostMapping
  public ResponseEntity<?> updateMappings(
      @Parameter(description = "List of mapping data to update") @RequestBody final List<RolePermReqtMappingId> ids) {

    try {
      List<RolePermReqtMapping> updatedMappings = this.rolePermActReqService.updateMappings(ids);
      return ResponseEntity.ok(updatedMappings);
    }
    catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create mappings: " + e.getMessage());
    }
  }

}

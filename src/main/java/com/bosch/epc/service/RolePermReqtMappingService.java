/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.epc.dao.ELMRoleRepository;
import com.bosch.epc.dao.ELMPermRepo;
import com.bosch.epc.dao.RequestRepository;
import com.bosch.epc.dao.RolePermReqtMappingRepository;
import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.ELMPermissions;
import com.bosch.epc.datamodel.Request;
import com.bosch.epc.datamodel.RolePermReqtMapping;
import com.bosch.epc.datamodel.RolePermReqtMappingId;

/**
 * Service class for managing role-permission-request mappings.
 *
 * @author QYU1HC
 */
@Service
public class RolePermReqtMappingService {

  private final RolePermReqtMappingRepository mappingRepository;
  private final RequestRepository requestRepository;
  private final ELMRoleRepository roleRepository;
  private final ELMPermRepo permissionRepository;

  /**
   * @param mappingRepository
   * @param requestRepository
   * @param roleRepository
   * @param permissionRepository
   */
  @Autowired
  public RolePermReqtMappingService(final RolePermReqtMappingRepository mappingRepository,
      final RequestRepository requestRepository, final ELMRoleRepository roleRepository,
      final ELMPermRepo permissionRepository) {
    this.mappingRepository = mappingRepository;
    this.requestRepository = requestRepository;
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
  }

  /**
   * Retrieves all role-permission-request mappings for a given request ID.
   *
   * @param requestId the ID of the request
   * @return list of mappings associated with the request
   */
  public List<RolePermReqtMapping> getMappingsByRequestId(final int requestId) {
    return this.mappingRepository.findByIdRequestId(requestId);
  }

  /**
   * Creates or updates role-permission-request mappings based on the provided IDs.
   *
   * @param mappingIds list of composite IDs for the mappings to create/update
   * @return list of saved mappings
   * @throws IllegalArgumentException if any referenced entity is not found
   */
  @Transactional
  public List<RolePermReqtMapping> updateMappings(final List<RolePermReqtMappingId> mappingIds) {
    List<RolePermReqtMapping> mappings =
        mappingIds.stream().map(this::createMapping).collect(Collectors.toList());

    return this.mappingRepository.saveAll(mappings);
  }

  /**
   * Retrieves all role-permission-request mappings for a specific role and request combination.
   *
   * @param roleId the ID of the role
   * @param requestId the ID of the request
   * @return list of mappings for the specified role and request
   */
  public List<RolePermReqtMapping> getMappingsByRoleAndRequest(final int roleId, final int requestId) {
    return this.mappingRepository.findByIdRoleIdAndIdRequestId(roleId, requestId);
  }

  /**
   * Creates a RolePermissionRequestMapping from the provided ID.
   *
   * @param id the composite ID containing role, permission, and request IDs
   * @return the created mapping
   * @throws IllegalArgumentException if any referenced entity is not found
   */
  private RolePermReqtMapping createMapping(final RolePermReqtMappingId id) {
    ELMRole role = this.roleRepository.findById(id.getRoleId())
        .orElseThrow(() -> new IllegalArgumentException("ELM role not found with ID: " + id.getRoleId()));

    ELMPermissions permission = this.permissionRepository.findById(id.getPermissionId())
        .orElseThrow(() -> new IllegalArgumentException("Permission not found with ID: " + id.getPermissionId()));

    Request request = this.requestRepository.findById(id.getRequestId())
        .orElseThrow(() -> new IllegalArgumentException("Request not found with ID: " + id.getRequestId()));

    RolePermReqtMapping mapping = new RolePermReqtMapping();
    mapping.setId(id);
    mapping.setRole(role);
    mapping.setPermission(permission);
    mapping.setRequest(request);

    return mapping;
  }
}
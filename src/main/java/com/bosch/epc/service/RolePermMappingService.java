/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bosch.epc.dao.RolePermMappingRepository;
import com.bosch.epc.datamodel.Request;
import com.bosch.epc.datamodel.RolePermMapping;
import com.bosch.epc.datamodel.RolePermReqtMapping;
import com.bosch.epc.dto.PermissionWithMappingDTO;

/**
 * @author QYU1HC
 */
@Service
public class RolePermMappingService {

  private final RolePermMappingRepository mappingRepository;

  /**
   * Full constructor with all fields.
   *
   * @param mappingRepository
   */
  public RolePermMappingService(final RolePermMappingRepository mappingRepository) {
    this.mappingRepository = mappingRepository;
  }

  /**
   * Function is used to get all mapping records
   *
   * @return
   */
  public List<RolePermMapping> getAllMappings() {
    return this.mappingRepository.findAll();
  }

  /**
   * Function is used to get all mapping records by RoleID
   *
   * @param roleId
   * @return
   */
  public List<RolePermMapping> getMappingsByRoleId(final int roleId) {
    return this.mappingRepository.findByIdRoleId(roleId);
  }

  /**
   * Function is used to get all mapping records by RoleID and return a PermissionWithMappingDTO object
   *
   * @param roleId
   * @return
   */
  public List<PermissionWithMappingDTO> getPermissionsWithMappingByRoleId(final int roleId) {
    // Using native query approach
    List<Object[]> results = this.mappingRepository.findPermissionsWithMappingByRoleIdNative(roleId);

    return results.stream().map(this::mapToDTO).collect(Collectors.toList());
  }

  private PermissionWithMappingDTO mapToDTO(final Object[] row) {
    PermissionWithMappingDTO dto = new PermissionWithMappingDTO();

    // Handle potential null values safely
    dto.setPermissionId(((Number) row[0]).intValue());
    dto.setPermissionName((String) row[1]);
    dto.setRef_id((String) row[2]);
    dto.setAttrPermission((Boolean) row[3]);
    dto.setParentId(row[4] != null ? ((Number) row[4]).intValue() : null); // parentId can be null
    dto.setPermissionGroup((String) row[5]);
    dto.setProjectConfiguration((Boolean) row[6]);
    dto.setRoleId(row[7] != null ? ((Number) row[7]).intValue() : null);
    dto.setPermitted(row[8] != null ? (Boolean) row[8] : null);
    dto.setRequestPresent(row[9] != null ? ((Number) row[9]).intValue() : null);

    return dto;
  }

  /**
   * Method to clear the locks of Role permission mapping table
   *
   * @param request Request
   */
  public void clearLocksByReq(final Request request) {
    List<RolePermReqtMapping> rolePermReqtMappings = request.getRolePermActReqMappings();
    List<RolePermMapping> rolePermMappings = this.mappingRepository.findByIsRequestPresent(request.getId());

    // Create lookup map for by col1+col2
    Map<String, RolePermReqtMapping> lookupMap = rolePermReqtMappings.stream()
        .collect(Collectors.toMap(e -> e.getRole().getId() + "|" + e.getPermission().getId(), e -> e));

    // Update list2 with data from list1
    for (RolePermMapping rolePermMapping : rolePermMappings) {
      String key = rolePermMapping.getRole().getId() + "|" + rolePermMapping.getPermission().getId();
      RolePermReqtMapping rolePermReqtMapping = lookupMap.get(key);

      if (rolePermReqtMapping != null) {
        rolePermMapping.setIsRequestPresent(0); // always set to zero
        rolePermMapping.setPermitted(rolePermReqtMapping.isPermitted()); // copy Permitted data from RolePermReqtMapping
      }
    }

    // save to repo
    this.mappingRepository.saveAll(rolePermMappings);
  }
}

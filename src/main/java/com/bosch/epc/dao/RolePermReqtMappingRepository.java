/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bosch.epc.datamodel.RolePermReqtMapping;
import com.bosch.epc.datamodel.RolePermReqtMappingId;

/**
 * Repository interface for managing role-permission-request mappings. Provides CRUD operations and custom query methods
 * for {@link RolePermReqtMapping} entities.
 *
 * @author QYU1HC
 */
@Repository
public interface RolePermReqtMappingRepository
    extends JpaRepository<RolePermReqtMapping, RolePermReqtMappingId> {

  /**
   * Finds all role-permission-request mappings for a specific request.
   *
   * @param requestId the ID of the request to search for
   * @return list of mappings associated with the given request ID
   */
  List<RolePermReqtMapping> findByIdRequestId(int requestId);

  /**
   * Finds all role-permission-request mappings for a specific role and request combination.
   *
   * @param roleId the ID of the role to search for
   * @param requestId the ID of the request to search for
   * @return list of mappings matching both the role ID and request ID
   */
  List<RolePermReqtMapping> findByIdRoleIdAndIdRequestId(int roleId, int requestId);

  /**
   * Using native query to upsert request mapping
   * 
   * @param roleId
   * @param permissionId
   * @param requestId
   * @param isPermitted
   */
  @Modifying
  @Query(value = "INSERT INTO role_perm_act_req_mapping (roleId, permissionId, requestId, isPermitted) " +
      "VALUES (:roleId, :permissionId, :requestId, :isPermitted) " +
      "ON DUPLICATE KEY UPDATE isPermitted = :isPermitted", nativeQuery = true)
  void upsertReqtMapping(@Param("roleId") int roleId, @Param("permissionId") int permissionId,
      @Param("requestId") int requestId, @Param("isPermitted") boolean isPermitted);
}
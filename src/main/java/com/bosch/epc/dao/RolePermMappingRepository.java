/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bosch.epc.datamodel.RolePermMapping;
import com.bosch.epc.datamodel.RolePermMappingId;

/**
 * @author QYU1HC
 */
public interface RolePermMappingRepository extends JpaRepository<RolePermMapping, RolePermMappingId> {

  /**
   * Function is used to find all mapping by RoleId
   *
   * @param roleId
   * @return
   */
  List<RolePermMapping> findByIdRoleId(int roleId);

  /**
   * Using native query with result mapping
   *
   * @param roleId
   * @return
   */
  @Query(value = "SELECT p.id as permissionId, p.name as permissionName, p.ref_id, " +
      "p.isAttrPermission, p.parentId, p.permissionGroup, p.isProjectConfiguration, " +
      "rpm.roleId, rpm.isPermitted, rpm.isRequestPresent " + "FROM permission p " +
      "LEFT JOIN role_perm_act_mapping rpm ON p.id = rpm.permissionId AND rpm.roleId = :roleId " +
      "WHERE p.permissionGroup IS NOT NULL", nativeQuery = true)
  List<Object[]> findPermissionsWithMappingByRoleIdNative(@Param("roleId") int roleId);

  /**
   * Fetch RolePermmappings by request id
   *
   * @param id request id
   * @return List of RolePermmappings
   */
  List<RolePermMapping> findByIsRequestPresent(int id);

  /**
   * Using native query to upsert mapping
   *
   * @param roleId
   * @param permissionId
   * @param isPermitted
   * @param isRequestPresent
   */
  @Modifying
  @Query(value = "INSERT INTO role_perm_act_mapping (roleId, permissionId, isRequestPresent) " +
      "VALUES (:roleId, :permissionId, :isRequestPresent) " +
      "ON DUPLICATE KEY UPDATE isRequestPresent = :isRequestPresent", nativeQuery = true)
  void upsertMapping(@Param("roleId") int roleId, @Param("permissionId") int permissionId,
      @Param("isRequestPresent") int isRequestPresent);

  /**
   * Using native query to check the Existing Request mapping
   *
   * @param roleId
   * @param permissionId
   * @param currentRequestId
   * @return numbers of row were updated
   */
  @Modifying
  @Query(value = "UPDATE role_perm_act_mapping " + "SET isRequestPresent = 0 " + "WHERE roleId = :roleId " +
      "AND permissionId = :permissionId " + "AND isRequestPresent != 0 " +
      "AND isRequestPresent = :currentRequestId", nativeQuery = true)
  int clearExistingRequest(@Param("roleId") int roleId, @Param("permissionId") int permissionId,
      @Param("currentRequestId") int currentRequestId);
}

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bosch.epc.datamodel.PermELMRoleMapping;
import com.bosch.epc.datamodel.PermELMRoleMappingId;

/**
 * @author QYU1HC
 */
@Repository
public interface PermELMRoleRepository extends JpaRepository<PermELMRoleMapping, PermELMRoleMappingId> {

  @Override
  @Query("SELECT map FROM PermELMRoleMapping map " + "JOIN FETCH map.permission " + "JOIN FETCH map.elmRole")
  List<PermELMRoleMapping> findAll();

  /**
   * @param id
   * @param id2
   * @return
   */
  @Query("SELECT p FROM PermELMRoleMapping p WHERE p.id.elmRoleId = :id AND p.id.permissionId = :id2")
  Optional<PermELMRoleMapping> findByElmIdPermissionId(@Param("id")int elmRoleId, @Param("id2")Integer permissionId);
}

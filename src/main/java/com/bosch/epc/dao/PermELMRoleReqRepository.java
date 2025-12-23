/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bosch.epc.datamodel.PermELMRoleReqMapping;
import com.bosch.epc.datamodel.PermELMRoleReqMappingId;

/**
 * @author QYU1HC
 */
@Repository
public interface PermELMRoleReqRepository extends JpaRepository<PermELMRoleReqMapping, PermELMRoleReqMappingId> {

  /**
   * @param elmRoleId
   * @param permissionId
   * @param requestId
   * @param isPermitted
   * @return
   */
  @Modifying
  @Query(nativeQuery = true, value = "INSERT INTO perm_role_req_mapping (elmRoleId, permissionId, requestId, isPermitted) VALUES(?1, ?2, ?3, ?4)")
  int save(int elmRoleId, int permissionId, int requestId, boolean isPermitted);

  /**
   * @param requestId
   * @return
   */
  @EntityGraph(attributePaths = { "elmRole", "permission", "request" })
  List<PermELMRoleReqMapping> findById_RequestId(int requestId);
  
 
  
}

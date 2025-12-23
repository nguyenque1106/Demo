/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bosch.epc.datamodel.AttrPermCondition;

/**
 * ProjectAreaRepository Class
 *
 * @author QYU1HC
 */
public interface ELMAttrPermRepo extends JpaRepository<AttrPermCondition, Integer> {

//  /**
//   * Find the Project Areas by UUID
//   *
//   * @param accessiblePAUUIDs List of UUIDs for search
//   * @param isArchived        isArchived
//   * @return List of ProjectAreas
//   */
//  List<ProjectArea> findByUuidInAndIsArchived(List<String> accessiblePAUUIDs, boolean isArchived);
  /**
   * @param requestid
   * @return
   */
  @Query(value = "select attrPermCond from AttrPermCondition attrPermCond where attrPermCond.request.id=?1")
  List<AttrPermCondition> findByRequestId(int requestid);

  /**
   * @param requestId
   * @param projectAreaId
   * @return
   */
  @EntityGraph(attributePaths = {"attrPermWorkflows"})
  @Query("SELECT c FROM AttrPermCondition c WHERE c.request.id = ?1 AND c.projectArea.id = ?2")
  List<AttrPermCondition> findWithWorkflows(int requestId, int projectAreaId);

}


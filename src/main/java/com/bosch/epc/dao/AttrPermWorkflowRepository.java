/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bosch.epc.datamodel.AttrPermWorkflow;

/**
 * @author VFE1COB
 *
 */
public interface AttrPermWorkflowRepository extends JpaRepository<AttrPermWorkflow, Integer> {
  @EntityGraph(attributePaths = {"attrPermRoles"})
  @Query("SELECT w FROM AttrPermWorkflow w WHERE w.id IN :workflowIds")
  List<AttrPermWorkflow> findWithRoles(@Param("workflowIds") List<Integer> workflowIds);

 
  
  @Query("SELECT w FROM AttrPermWorkflow w " +
      "WHERE w.witype = :witype AND w.wistatus = :wistatus " +
      "AND w.wistatusgrp = :wistatusgrp AND w.wiresolution = :wiresolution " +
      "AND w.attrPermCondition.id = :conditionId")
Optional<AttrPermWorkflow> findByUniqueKeys(
       @Param("witype") String witype,
       @Param("wistatus") String wistatus,
       @Param("wistatusgrp") String wistatusgrp,
       @Param("wiresolution") String wiresolution,
       @Param("conditionId") int conditionId
);
}


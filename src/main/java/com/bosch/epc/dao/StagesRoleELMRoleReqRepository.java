/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bosch.epc.datamodel.StagesRoleELMRoleReqMapping;
import com.bosch.epc.datamodel.StagesRoleELMRoleReqMappingId;

/**
 * @author QYU1HC
 */
@Repository
public interface StagesRoleELMRoleReqRepository
    extends JpaRepository<StagesRoleELMRoleReqMapping, StagesRoleELMRoleReqMappingId> {

  /**
   * @param requestId
   * @return
   */
  @EntityGraph(attributePaths = { "stagesRole", "elmRole", "request" })
  List<StagesRoleELMRoleReqMapping> findById_RequestId(Integer requestId);

  @Override
  Optional<StagesRoleELMRoleReqMapping> findById(StagesRoleELMRoleReqMappingId id);

  /**
   * @param stagesRoleId
   * @param elmRoleId
   * @param requestId
   * @return
   */
  @Modifying
  @Query(nativeQuery = true, value = "INSERT INTO stagesrole_role_req_mapping (stagesRoleId, elmRoleId, requestId) VALUES(?1, ?2, ?3)")
  int save(int stagesRoleId, int elmRoleId, int requestId);

  /**
   * @param i
   * @param j
   * @return
   */
  @Query("SELECT s FROM StagesRoleELMRoleReqMapping s WHERE s.id.stagesRoleId = :i AND s.id.elmRoleId = :j")
  Optional<StagesRoleELMRoleReqMapping> findByStagesRoleIdElmRoleId(@Param("i") int stagesRoleId,
      @Param("j") int elmRoleId);
  
  
  @Modifying
  @Query(nativeQuery = true, value ="UPDATE epc.stagesrole_role_req_mapping  SET requestId = :requestId WHERE ( stagesRoleId= :stagesRoleIdd AND elmRoleId= :elmRoleIdd)")
  int updateRequestById(@Param("requestId") int requestId, @Param("elmRoleIdd") int elmRoleIdd,@Param("stagesRoleIdd") int stagesRoleIdd);

}

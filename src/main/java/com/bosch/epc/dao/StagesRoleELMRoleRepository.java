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

import com.bosch.epc.datamodel.StagesRoleELMRoleMapping;
import com.bosch.epc.datamodel.StagesRoleELMRoleMappingId;

/**
 * @author QYU1HC
 */
@Repository
public interface StagesRoleELMRoleRepository
    extends JpaRepository<StagesRoleELMRoleMapping, StagesRoleELMRoleMappingId> {

  @Query("SELECT map FROM StagesRoleELMRoleMapping map " + "JOIN FETCH map.stagesRole " + "JOIN FETCH map.elmRole")
  List<StagesRoleELMRoleMapping> findAll();
  
  
  /**
   * @param stagesRoleId
   * @param elmRoleId
   * @param isRequestActive 
   * 
   * @return
   */
  @Modifying
  @Query(nativeQuery = true, value = "INSERT INTO epc.stagesrole_role_mapping(stagesRoleId, elmRoleId, isRequestPresent) VALUES(?1, ?2, ?3)")
  int save(int stagesRoleId, int elmRoleId, int isRequestPresent);
  
  
  /**
   * @param id
   * @param isRequestPresent
   * @return 
   * @return
   */
 
  @Modifying
  @Query(nativeQuery = true, value ="UPDATE epc.stagesrole_role_mapping  SET isRequestPresent = :isRequestPresent WHERE ( stagesRoleId= :stagesRoleIdd AND elmRoleId= :elmRoleIdd)")
  int updateRequestById(@Param("isRequestPresent") int isRequestPresent, @Param("elmRoleIdd") int elmRoleIdd,@Param("stagesRoleIdd") int stagesRoleIdd);

 }

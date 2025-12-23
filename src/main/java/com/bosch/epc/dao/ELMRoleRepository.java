/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.PARoleRequest;

/**
 *
 */
@Repository
public interface ELMRoleRepository extends JpaRepository<ELMRole, Integer> {

  /**
   * @param identifier
   * @param id
   * @return
   */
  Optional<PARoleRequest> findByIdentifierAndProjectAreaId(String identifier, int id);

  /**
   * Finds all ELMRole entities by project area ID.
   *
   * @param projectAreaId The ID of the project area.
   * @return A list of matching ELMRole entities.
   */
  List<ELMRole> findByProjectAreaId(int projectAreaId);
}

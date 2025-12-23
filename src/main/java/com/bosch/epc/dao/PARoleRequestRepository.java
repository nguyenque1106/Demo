/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bosch.epc.datamodel.PARoleRequest;

/**
 * @author VFE1COB
 */
@Repository
public interface PARoleRequestRepository extends JpaRepository<PARoleRequest, Integer> {

  /**
   * @param identifier
   * @param id
   * @return
   */
  Optional<PARoleRequest> findByIdentifierAndProjectAreaId(String identifier, int id);

}

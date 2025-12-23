/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bosch.epc.datamodel.StagesRolePARequest;
import com.bosch.epc.model.StagesRolePARequestID;

/**
 * @author QYU1HC
 */
@Repository
public interface StagesRolePARequestRepository extends JpaRepository<StagesRolePARequest, StagesRolePARequestID> {

  /**
   * @param requestId
   * @return list of StagesRole-PA-Request
   */
  List<StagesRolePARequest> findByRequestId(int requestId);
}

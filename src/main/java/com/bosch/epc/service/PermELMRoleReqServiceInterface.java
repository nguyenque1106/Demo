/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;

import com.bosch.epc.datamodel.PermELMRoleReqMappingId;
import com.bosch.epc.model.PermELMRoleReqResponse;

/**
 * @author QYU1HC
 */
public interface PermELMRoleReqServiceInterface {

  /**
   * @param id
   * @return
   */
  public PermELMRoleReqResponse addNewPermELMRoleReqMapping(PermELMRoleReqMappingId id);

  /**
   * @param requestId
   * @return
   */
  public List<PermELMRoleReqResponse> findByRequestId(int requestId);
}

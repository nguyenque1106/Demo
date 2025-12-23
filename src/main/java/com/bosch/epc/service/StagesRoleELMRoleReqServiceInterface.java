/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;

import com.bosch.epc.datamodel.StagesRoleELMRoleReqMappingId;
import com.bosch.epc.model.StagesRoleELMRoleReqResponse;

/**
 * @author QYU1HC
 */
public interface StagesRoleELMRoleReqServiceInterface {

  /**
   * @param requestId
   * @return
   */
  public List<StagesRoleELMRoleReqResponse> findByRequestId(String requestId);

  /**
   * @param id
   * @return
   */
  public List<StagesRoleELMRoleReqResponse> addNewStagesRoleELMRoleReqMappings(List<StagesRoleELMRoleReqMappingId> ids);
}

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;

//import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bosch.epc.datamodel.AttrPermCondition;
import com.bosch.epc.datamodel.ProjectArea;
import com.bosch.epc.datamodel.Request;
import com.bosch.epc.exception.EpcException;
import com.bosch.epc.model.XMLCondition;

/**
 * Interface class for ProjectArea Service.
 *
 * @author QYU1HC
 */
@Service
public interface ELMAttrPermService {

  /**
   * @return lis of PA
   */
  public List<AttrPermCondition> getAll();


  /**
   * Create list of AttrPermCondition with empty permissions
   *
   * @param specificationFilePath
   * @param selectedPA
   * @return
   */
  public List<AttrPermCondition> createEmptyConditions(String specificationFilePath, ProjectArea selectedPA)
      throws EpcException;

  /**
   * Create list of AttrPermCondition based on <conditions> under specification.xml file
   *
   * @param specificationFilePath
   * @param selectedPA
   * @param xmlConditions
   * @return
   */
  public List<AttrPermCondition> createAttrPermConditions(String specificationFilePath, ProjectArea selectedPA,
      List<XMLCondition> xmlConditions)
      throws EpcException;

  /**
   * Getting list of AttrPermCondition from Database by parameters
   *
   * @param request
   * @param selectedPA
   * @return
   */
//  @Cacheable("AttrPermInProgress")
  public List<AttrPermCondition> getListAttrPermCondFromDB(Request request, ProjectArea selectedPA);


  /**
   * Getting list of AttrPermCondition by mapping RequestBody(PermRoleRequest)
   *
   * @param permRoleRequestBody
   * @return
   */
  public AttrPermCondition getAttrPermConditionByRequestBody(AttrPermCondition permRoleRequestBody);
}

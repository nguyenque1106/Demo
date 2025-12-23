package com.bosch.epc.service;

import com.bosch.epc.datamodel.StagesRole;

/**
 * @author VFE1COB StageserviceInterface defines methods to retrieves the stages roles from DB
 *
 */
public interface StagesServiceInterface {

  /**
   * @return list of stagesrole
   */
  public Iterable<StagesRole> findAll();
}
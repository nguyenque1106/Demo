package com.bosch.epc.service;

import com.bosch.epc.datamodel.AttributeBuiltIn;

/**
 * Interface class for Attribute Service
 *
 * @author PPT4KOR
 */
public interface AttributeServiceInterface {

  /**
   * Find all.
   *
   * @return the iterable
   */
  public Iterable<AttributeBuiltIn> findAll();


}
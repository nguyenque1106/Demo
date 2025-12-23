package com.bosch.epc.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.epc.dao.AttributeDaoImpl;
import com.bosch.epc.datamodel.AttributeBuiltIn;


/**
 * Service class to manage the Attribute data in DB
 *
 * @author PPT4KOR
 */
@Service
@Transactional
public class AttributeServiceImpl implements AttributeServiceInterface {

  @Autowired
  private AttributeDaoImpl dao;

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<AttributeBuiltIn> findAll() {
    return this.dao.findAll();

  }

}
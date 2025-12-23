package com.bosch.epc.dao;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.epc.datamodel.AttributeBuiltIn;

/**
 * DAO class for ELM EWM Built-in Attributes
 * 
 * @author PPT4KOR
 */
@Service
@Transactional
public class AttributeDaoImpl {

  @Autowired
  private AttributeServiceInterfaceRepository repo;


  /**
   * Find all.
   *
   * @return the iterable
   */
  public Iterable<AttributeBuiltIn> findAll() {
    return repo.findAll();

  }


  /**
   * Find built in attrs.
   *
   * @return the iterable
   */
  public Iterable<AttributeBuiltIn> findBuiltInAttrs() {
    return repo.findAll();
  }

  /**
   * Find custom attrs.
   *
   * @return the iterable
   */
  public Iterable<AttributeBuiltIn> findCustomAttrs() {
    return null;// repo.findCustomAttrs();
  }


}
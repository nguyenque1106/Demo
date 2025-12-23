package com.bosch.epc.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.epc.dao.StagesDaoImpl;
import com.bosch.epc.datamodel.StagesRole;


/**
 * @author VFE1COB StageserviceImpl class retrieves the stages roles from DB and passes tot he front end
 *
 */
@Service
@Transactional
public class StagesServiceImpl implements StagesServiceInterface {

  @Autowired
  private StagesDaoImpl dao;


  public Iterable<StagesRole> findAll() {
    return dao.findAll();

  }


}
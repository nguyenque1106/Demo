package com.bosch.epc.dao;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.epc.datamodel.StagesRole;


/**
 * @author VFE1COB Used as a DAO for retrieving stages roles
 */
@Service
@Transactional
public class StagesDaoImpl {

  @Autowired
  private StagesRepository repo;


  /**
   * //Retrieve all stages role from DB
   *
   * @return list of stagesRoles
   */
  public Iterable<StagesRole> findAll() {
    return this.repo.findAll();

  }

  /**
   * @param stagesRoleId
   * @return true if the id is existed
   */
  public boolean checkExist(final Integer stagesRoleId) {
    return this.repo.existsById(stagesRoleId);
  }


}
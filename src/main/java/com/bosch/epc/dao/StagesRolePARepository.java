/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bosch.epc.datamodel.StagesRolePA;
import com.bosch.epc.model.StagesRolePAID;

/**
 * @author QYU1HC
 */
@Repository
public interface StagesRolePARepository extends JpaRepository<StagesRolePA, StagesRolePAID> {

}


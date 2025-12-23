/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bosch.epc.datamodel.StagesRole;

/**
 *
 */
@Repository
public interface StagesRepository extends JpaRepository<StagesRole, Integer> {

}

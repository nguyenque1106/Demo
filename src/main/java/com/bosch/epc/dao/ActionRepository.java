/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bosch.epc.datamodel.Action;

/**
 * @author QYU1HC
 */
public interface ActionRepository extends JpaRepository<Action, Integer> {

}

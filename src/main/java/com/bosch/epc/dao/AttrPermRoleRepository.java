/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bosch.epc.datamodel.AttrPermRole;


/**
 * @author GHT9HC
 */
public interface AttrPermRoleRepository extends JpaRepository<AttrPermRole, Integer> {

}

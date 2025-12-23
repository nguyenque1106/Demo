/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import org.springframework.data.repository.CrudRepository;

import com.bosch.epc.datamodel.WORequest;


/**
 * @author VFE1COB Repository to perform CRUD operations on WorkON requests
 */
public interface WorkONRepository extends CrudRepository<WORequest, Integer> {

}

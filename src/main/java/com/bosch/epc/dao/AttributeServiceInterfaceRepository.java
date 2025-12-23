package com.bosch.epc.dao;

import org.springframework.data.repository.CrudRepository;

import com.bosch.epc.datamodel.AttributeBuiltIn;

/**
 * Repository class for ELM EWM Built-in Attributes
 * 
 * @author PPT4KOR
 */

public interface AttributeServiceInterfaceRepository extends CrudRepository<AttributeBuiltIn, Integer> {


}
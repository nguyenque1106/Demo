/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bosch.epc.datamodel.Request;

/**
 * @author QYU1HC
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

  
  
  /**
   * @param requestId
   * @return the query response
   */
 
   @Query("SELECT r FROM Request r where r.id= :requestId")
  Request findRequestById(@Param("requestId") Integer requestId);

  /**
   * @param createdBy fetch request for loggedin User
   * @return Listof Requests
   */
   List<Request> findByCreatedByIgnoreCase(String createdBy);
  
  /**
   *  read all request from request table(status:pending approval)
   * @return list of requests
   */
  @Query("SELECT r FROM Request r WHERE r.status = :status")
  List<Request> findAllByStatus(@Param("status")String status);
  
  @Modifying
  @Query("UPDATE Request r SET r.status = :status WHERE r.id = :id")
  int updateStatusById(@Param("id") int id, @Param("status") String status);
}

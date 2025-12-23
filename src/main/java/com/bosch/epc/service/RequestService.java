/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.bosch.epc.datamodel.Request;
import com.bosch.epc.datamodel.WORequest;
import com.bosch.epc.model.Attribute;
import com.bosch.epc.model.WorkFlowDefinition;
import com.bosch.epc.model.WorkItemType;
import com.bosch.epc.model.XMLCondition;

/**
 * EPC request service interface which defines the methods to create workon request, get status on workon request, store
 * workon id on DB Store Attributes in DB, Retrieve Attributes in DB
 * 
 * @author QYU1HC
 */
public interface RequestService {

  /**
   * @param requestId
   * @return true of the request existed
   */
  public boolean checkExist(Integer requestId);

  /**
   * @param requestId
   * @return
   */
  public Request findDataByRequestId(Integer requestId);

  /**
   * Retrieves workon request from workon system
   *
   * @param id workon request id
   * @return string status of workon request
   * @throws IOException
   */
  public String getRequestStatus(String id) throws IOException;

  /**
   * @param woRequest model for workon request
   */
  public void addRequestDetailstoDB(WORequest woRequest);

  /**
   * Fetch attributes, workitem mapping from spec.xml
   *
   * @return Map<String, Attribute> attributeId as key and attribute object as value
   */
  public Map<String, Attribute> fetchAttributesfromSpec();

  /**
   * fetch permissions, role, conditions from spec.xml
   *
   * @return List<XMLCondition> List of Condiiton objects
   */
  public List<XMLCondition> fetchPermissionsFromSpec();

  /**
   * @author GHT9HC
   * @param projectAreaName
   * @return
   */
  public List<XMLCondition> fetchPermissionsFromSpec(String projectAreaName);


  /**
   * @return List<WorkItemType> List of all workitem types
   */
  public List<WorkItemType> fetchWITypeFromSpec();

  /**
   * @return List<WorkFlowDefinition> List of all state,resolution for a WI type
   */
  public List<WorkFlowDefinition> fetchStateResolutions();


  /**
   * @author GHT9HC Add new or update existed request.
   * @param request
   * @return
   */
  public boolean saveRequest(Request request);

  /**
   * @param requestId
   * @param userId
   * @return
   */
  String createWORequest(int requestId, String userId);

  /**
   * Fecth the list of requests by status
   * 
   * @param status Request status
   * @return list of requests
   */
  List<Request> findAllByStatus(final String status);

  /**
   * Update isRequestPresent column value
   *
   * @param request EPC request
   * @param i       update the isRequestPresent to 0
   * @return
   */
  void updateStagesELMMappingById(final Request request, final int i);

  /**
   * Update request status after workon approval
   *
   * @param i
   * @param status
   */
  void updateStatusById(final int i, final String status);

  /**
   * @return
   */
  List<Request> findAll();

  /**
   * Method to create the changeset
   * 
   * @param changeSetName changeSet Name
   * @param changeSetDesc changeSet Description
   * @return
   */
  Request createRequestEntry(String changeSetName, String changeSetDesc);

  /**
   * Method to update the Request
   * 
   * @param request Request object
   */
  Request updateRequestEntry(Request request);

  /**
   * Method to update the Permission Role
   * 
   * @param request Request object
   */
  ResponseEntity<String> updatePermissionRole(Request request);

  /**
   * Method to create WorkOn request
   * 
   * @param requestId
   * @param userId
   * @return Request object
   */
  Request createWorkON(int requestId, String userId);

  /**
   * @param loggedInUser
   * @return list of Request objects
   */
  List<Request> findByCreatedBy(String loggedInUser);
}

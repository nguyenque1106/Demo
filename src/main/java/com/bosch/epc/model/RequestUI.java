/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author VFE1COB
 *
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RequestUI {
 

    private int requestId;
    private String status;
    private List<RoleDefinition> almRoleDefinitions = new ArrayList<>();

    private String createdBy;
    private String modifiedBy;
    
    private Date createdDate;
    private List<Condition> epcConditions = new ArrayList<>();
    private Date modifiedDate;
   
    private String changesetName;
    /**
     * Empty Constructor for fixing Jackson deserialization issue
     */
    public RequestUI() {
      
    }
    /**
     * @return the requestId
     */
    public int getRequestId() {
      return requestId;
    }

    /**
     * @param requestId the requestId to set
     */
    public void setRequestId(int requestId) {
      this.requestId = requestId;
    }

    /**
     * @return the requestID
     */
    public int getRequestID() {
      return requestId;
    }

    /**
     * @param requestID the requestID to set
     */
    public void setRequestID(int requestID) {
      requestId = requestID;
    }

    /**
     * @return the status
     */
    public String getStatus() {
      return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
      this.status = status;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
      return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
    }

    
    /**
     * @return the changesetName
     */
    public String getChangesetName() {
      return changesetName;
    }

    /**
     * @param changesetName the changesetName to set
     */
    public void setChangesetName(String changesetName) {
      this.changesetName = changesetName;
    }

    /**
     * @return the almRoleDefinitions
     */
    public List<RoleDefinition> getAlmRoleDefinitions() {
      return almRoleDefinitions;
    }

    /**
     * @param almRoleDefinitions the almRoleDefinitions to set
     */
    public void setAlmRoleDefinitions(List<RoleDefinition> almRoleDefinitions) {
      this.almRoleDefinitions = almRoleDefinitions;
    }

    /**
     * @return the modifiedBy
     */
    public String getModifiedBy() {
      return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(String modifiedBy) {
      this.modifiedBy = modifiedBy;
    }

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
      return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
      this.createdDate = createdDate;
    }

    /**
     * @return the modifiedDate
     */
    public Date getModifiedDate() {
      return modifiedDate;
    }

    /**
     * @param modifiedDate the modifiedDate to set
     */
    public void setModifiedDate(Date modifiedDate) {
      this.modifiedDate = modifiedDate;
    }
    /**
     * @return the epcConditions
     */
    public List<Condition> getEpcConditions() {
      return epcConditions;
    }
    /**
     * @param epcConditions the epcConditions to set
     */
    public void setEpcConditions(List<Condition> epcConditions) {
      this.epcConditions = epcConditions;
    }

 

    /**
     * @return the epcConditions
     */
  /*  public List<Condition> getEpcConditions() {
      return epcConditions;
    }

    *//**
     * @param epcConditions the epcConditions to set
     *//*
    public void setEpcConditions(List<Condition> epcConditions) {
      this.epcConditions = epcConditions;
    }
*/
   

  }



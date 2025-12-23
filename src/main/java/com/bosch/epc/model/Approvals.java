/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * @author VFE1COB
 */
@Component
public class Approvals {

  private int approvalId;
  private String status;
  private int requestId;
  private String workOnLink;
  private Date createdDate;
  private Date modifiedDate;
  private String modifiedBy;

  /**
   * @return the approvalId
   */
  public int getApprovalId() {
    return approvalId;
  }

  /**
   * @param approvalId the approvalId to set
   */
  public void setApprovalId(int approvalId) {
    this.approvalId = approvalId;
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
   * @return the workOnLink
   */
  public String getWorkOnLink() {
    return workOnLink;
  }

  /**
   * @param workOnLink the workOnLink to set
   */
  public void setWorkOnLink(String workOnLink) {
    this.workOnLink = workOnLink;
  }


}

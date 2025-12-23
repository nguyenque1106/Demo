/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.constant;


/**
 * Enum class to represent the status for Requests
 * 
 * @author PPT4KOR
 */
public enum RequestStatus {

  /**
   * Draft Status : Request can be editted
   */
  DRAFT("Draft"),
  /**
   * Pending Approval Status : WorkOn Request is pending with approvals
   */
  PENDING_FOR_APPROVAL("Pending For Approval"),
  /**
   * Pending Approval Status : WorkOn Request is pending with approvals
   */
  APPROVED_SCEDULED("Approved and Scheduled"),
  /**
   * Processing Status :WorkOn Request is rejected or declined to process
   */
  REJECTED_DECLINED("Rejected or Declined"),
  /**
   * Revoked Status : Request is revoked so the WorkOn request will be cancelled
   */
  REVOKED("Revoked"),
  /**
   * Closed Status : Request is closed. The changes are pushed to ELM Server
   */
  CLOSED("Closed");

  private String value;

  private RequestStatus(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value; // This will return , # or +
  }
}

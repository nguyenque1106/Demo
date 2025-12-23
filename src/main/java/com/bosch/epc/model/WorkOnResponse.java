/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import java.util.List;

/**
 * @author VFE1COB
 */
public class WorkOnResponse {


  private List<Status> status;
  private String key;
  private String resolution;
  private List<ApprovalHistory> approvalHistory;
  private List<CustomField> customFields;
  private List<SystemField> systemFields;

  // Getters and setters

  /**
   * @return list of status
   */
  public List<Status> getStatus() {
    return status;
  }


  /**
   * @param status set status of request
   */
  public void setStatus(List<Status> status) {
    this.status = status;
  }

  /**
   * @return get the key from the response
   */
  public String getKey() {
    return key;
  }

  /**
   * @param requestKey set the key
   */
  public void setKey(String requestKey) {
    this.key = requestKey;
  }

  /**
   * @return get resolution of request
   */
  public String getResolution() {
    return resolution;
  }

  /**
   * @param resolution Set the resolution for the particular request
   */
  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  /**
   * @return list of approval history
   */
  public List<ApprovalHistory> getApprovalHistory() {
    return approvalHistory;
  }

  /**
   * @param approvalHistory list of apporval history to set
   */
  public void setApprovalHistory(List<ApprovalHistory> approvalHistory) {
    this.approvalHistory = approvalHistory;
  }

  /**
   * @return list of custom field objects
   */
  public List<CustomField> getCustomFields() {
    return customFields;
  }

  /**
   * @param customFields set customfields
   */
  public void setCustomFields(List<CustomField> customFields) {
    this.customFields = customFields;
  }

  /**
   * @return list of systemfields object
   */
  public List<SystemField> getSystemFields() {
    return systemFields;
  }

  /**
   * @param systemFields set list of systemfields object
   */
  public void setSystemFields(List<SystemField> systemFields) {
    this.systemFields = systemFields;
  }

  /**
   * @author VFE1COB Status class with localename and i8nValue
   */
  public static class Status {

    private String localeName;
    private String i8nValue;

    /**
     * @return localename
     */
    // Getters and setters
    public String getLocaleName() {
      return localeName;
    }

    /**
     * @param localeName string
     */
    public void setLocaleName(String localeName) {
      this.localeName = localeName;
    }

    /**
     * @return i8nValue status of the request
     */
    public String getI8nValue() {
      return i8nValue;
    }

    /**
     * @param i8nValue set the i8nValue
     */
    public void setI8nValue(String i8nValue) {
      this.i8nValue = i8nValue;
    }
  }

  /**
   * @author VFE1COB Approval history class with approverID,name,assigneeID,name,comments,actions and step
   */
  public static class ApprovalHistory {

    private String approverId;
    private String approverName;
    private String assigneeId;
    private String assigneeName;
    private String comments;
    private String approvalTime;
    private String actionTaken;
    private String stepName;

    // Getters and setters
    /**
     * @return approverId
     */
    public String getApproverId() {
      return approverId;
    }

    /**
     * @param approverId set the approverID
     */
    public void setApproverId(String approverId) {
      this.approverId = approverId;
    }

    /**
     * @return approverName
     */
    public String getApproverName() {
      return approverName;
    }

    /**
     * @param approverName set approverNAme
     */
    public void setApproverName(String approverName) {
      this.approverName = approverName;
    }

    /**
     * @return assigneeId
     */
    public String getAssigneeId() {
      return assigneeId;
    }

    /**
     * @param assigneeId set assigneeId
     */
    public void setAssigneeId(String assigneeId) {
      this.assigneeId = assigneeId;
    }

    /**
     * @return AssingeeName
     */
    public String getAssigneeName() {
      return assigneeName;
    }

    /**
     * @param assigneeName set Assigneename
     */
    public void setAssigneeName(String assigneeName) {
      this.assigneeName = assigneeName;
    }

    /**
     * @return comments of a particular request
     */
    public String getComments() {
      return comments;
    }

    /**
     * @param comments pass comments to set
     */
    public void setComments(String comments) {
      this.comments = comments;
    }

    /**
     * @return approvalTime
     */
    public String getApprovalTime() {
      return approvalTime;
    }

    /**
     * @param approvalTime set
     */
    public void setApprovalTime(String approvalTime) {
      this.approvalTime = approvalTime;
    }

    /**
     * @return ActionTaken
     */
    public String getActionTaken() {
      return actionTaken;
    }

    /**
     * @param actionTaken set approved or rejected
     */
    public void setActionTaken(String actionTaken) {
      this.actionTaken = actionTaken;
    }

    /**
     * @return stepname
     */
    public String getStepName() {
      return stepName;
    }

    /**
     * @param stepName addition
     */
    public void setStepName(String stepName) {
      this.stepName = stepName;
    }
  }

  /**
   * @author VFE1COB Class for customField
   */
  public static class CustomField {

    private String key;
    private String value;

    /**
     * @return get customKey
     */
    // Getters and setters
    public String getKey() {
      return key;
    }

    /**
     * @param key set customkey
     */
    public void setKey(String key) {
      this.key = key;
    }

    /**
     * @return retrieve vlaue
     */
    public String getValue() {
      return value;
    }

    /**
     * @param value addition
     */
    public void setValue(String value) {
      this.value = value;
    }
  }

  /**
   * @author VFE1COB class for systemFields
   */
  public static class SystemField {

    private String key;
    private String value;

    /**
     * @return get requestkey
     */
    // Getters and setters
    public String getKey() {
      return key;
    }

    /**
     * @param key requestkey set
     */
    public void setKey(String key) {
      this.key = key;
    }

    /**
     * @return value
     */
    public String getValue() {
      return value;
    }

    /**
     * @param value set
     */
    public void setValue(String value) {
      this.value = value;
    }
  }
}


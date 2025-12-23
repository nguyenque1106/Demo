/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author VFE1COB
 */
public class WorkOnRequest {

  private String summary;
  private String pkey;
  private String issuetype;
  private String applicant;
  private String priority;
  private Data data;


  /**
   * @author VFE1COB
   */
  public static class Data {

    @JsonProperty("rbga.field.sourceSystem")
    private String sourceSystem;
    @JsonProperty("rbga.field.termCheck")
    private String termCheck;
    @JsonProperty("rbga.field.description")
    private String description;
    @JsonProperty("rbga.field.comments")
    private String comments;
    @JsonProperty("rbga.field.approvalstep")
    private String approvalStep;
    @JsonProperty("rbga.field.externalLink")
    private String externalLink;
    @JsonProperty(value = "rbga.field.additionalFields", required = false)
    private List<AdditionalField> additionalFields;
    @JsonProperty(value = "rbga.field.attach", required = false)
    private List<Attach> attach;
    @JsonProperty("rbga.field.workflowType")
    private String workflowType;
    @JsonProperty("rbga.field.wf2")
    private String wf2;
    @JsonProperty("rbga.field.wf3")
    private String wf3;
    @JsonProperty("rbga.field.parallelWorkflowSel")
    private String parallelWorkflowSel;
    @JsonProperty("rbga.field.parallelWorkflowSel2")
    private String parallelWorkflowSel2;
    @JsonProperty("rbga.field.parallelWorkflowSel3")
    private String parallelWorkflowSel3;
    @JsonProperty("rbga.field.approver1")
    private ApproverGroup approver1;
    @JsonProperty("rbga.field.approver2")
    private ApproverGroup approver2;
    @JsonProperty("rbga.field.whenApproved")
    private ApproverGroup whenApproved;
    @JsonProperty("rbga.field.whenDeclined")
    private ApproverGroup whenDeclined;
    @JsonProperty("rbga.field.tempNew")
    private String tempNew;

    /**
     * @param string sourceSystem from where the request is created
     */
    public void setSourceSystem(String string) {
      this.sourceSystem = string;

    }

    /**
     * @param string TermCheck
     */
    public void setTermCheck(String string) {
      this.termCheck = string;

    }

    /**
     * @param string Description for the request
     */
    public void setDescription(String string) {
      this.description = string;

    }

    /**
     * @param string Comments
     */
    public void setComments(String string) {
      this.comments = string;

    }

    /**
     * @param string External link
     */
    public void setExternalLink(String string) {
      this.externalLink = string;

    }

    /**
     * @param asList Additional Fields
     */
    public void setAdditionalFields(List<AdditionalField> asList) {
      this.additionalFields = asList;

    }

    /**
     * @param asList Attachments List
     */
    public void setAttach(List<Attach> asList) {
      this.attach = asList;

    }

    /**
     * @param string Workflow type serial or parallel
     */
    public void setWorkflowType(String string) {
      this.workflowType = string;

    }

    /**
     * @param string second workflow type
     */
    public void setWf2(String string) {
      this.wf2 = string;

    }

    /**
     * @param string third workflow type
     */
    public void setWf3(String string) {
      this.wf3 = string;

    }

    /**
     * @param string set parallel
     */
    public void setParallelWorkflowSel(String string) {
      this.parallelWorkflowSel = string;

    }

    /**
     * @param string set parallel for workflow2
     */
    public void setParallelWorkflowSel2(String string) {
      this.parallelWorkflowSel2 = string;

    }

    /**
     * @param string set parallel for workflow3
     */
    public void setParallelWorkflowSel3(String string) {
      this.parallelWorkflowSel3 = string;

    }

    /**
     * @param approverGroup list of approvers
     */
    public void setApprover1(ApproverGroup approverGroup) {
      this.approver1 = approverGroup;

    }

    /**
     * @param approverGroup when approved email will be triggered for this group
     */
    public void setWhenApproved(ApproverGroup approverGroup) {
      this.whenApproved = approverGroup;

    }

    /**
     * @param approverGroup when declined email will be triggered for this group
     */
    public void setWhenDeclined(ApproverGroup approverGroup) {
      this.whenDeclined = approverGroup;

    }

    /**
     * @param string set approver step
     */
    public void setApproverStep(String string) {
      this.approvalStep = string;
    }

    /**
     * @param string set temp set as new request
     */
    public void setTempNew(String string) {
      this.tempNew = string;
    }

    /**
     * @param approverGroup2 group of approvers for next level
     */
    public void setApprover2(ApproverGroup approverGroup2) {

      this.approver2 = approverGroup2;
    }

    // Getters and setters
  }

  /**
   * @author VFE1COB Additional fields along with Data
   */
  public static class AdditionalField {

    private String fields;
    private String details;

    /**
     * @return the fields
     */
    public String getFields() {
      return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(String fields) {
      this.fields = fields;
    }

    /**
     * @return the details
     */
    public String getDetails() {
      return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(String details) {
      this.details = details;
    }

  }

  /**
   * @author VFE1COB Attachment class with fielname and file
   */
  public static class Attach {

    private String filename;
    private String file;

    /**
     * @return the filename
     */
    public String getFilename() {
      return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
      this.filename = filename;
    }

    /**
     * @return the file
     */
    public String getFile() {
      return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(String file) {
      this.file = file;
    }

    // Getters and setters
  }

  /**
   * @author VFE1COB Class for approver Group list of approvers set maxapprover set type set check duplicate
   */
  public static class ApproverGroup {

    private List<Approver> approvers;
    private String checkDuplicate;
    private String maxApprover;
    private String type;

    /**
     * @return the approvers
     */
    public List<Approver> getApprovers() {
      return approvers;
    }

    /**
     * @param approvers the approvers to set
     */
    public void setApprovers(List<Approver> approvers) {
      this.approvers = approvers;
    }

    /**
     * @return the checkDuplicate
     */
    public String getCheckDuplicate() {
      return checkDuplicate;
    }

    /**
     * @param checkDuplicate the checkDuplicate to set
     */
    public void setCheckDuplicate(String checkDuplicate) {
      this.checkDuplicate = checkDuplicate;
    }

    /**
     * @return the maxApprover
     */
    public String getMaxApprover() {
      return maxApprover;
    }

    /**
     * @param maxApprover the maxApprover to set
     */
    public void setMaxApprover(String maxApprover) {
      this.maxApprover = maxApprover;
    }

    /**
     * @return the type
     */
    public String getType() {
      return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
      this.type = type;
    }

    // Getters and setters
  }

  /**
   * @author VFE1COB Approver model attributes "addAfterEnabled": true, "deleteFlag": "Yes", "description": "", "fixed":
   *         false, "removable": true, "userid": "$userid", "ccList": ""
   */
  public static class Approver {

    private boolean addAfterEnabled;
    private String deleteFlag;
    private String description;
    private boolean fixed;
    private boolean removable;
    private String userid;
    private String ccList;

    /**
     * @return the addAfterEnabled
     */
    public boolean isAddAfterEnabled() {
      return addAfterEnabled;
    }

    /**
     * @param addAfterEnabled the addAfterEnabled to set
     */
    public void setAddAfterEnabled(boolean addAfterEnabled) {
      this.addAfterEnabled = addAfterEnabled;
    }

    /**
     * @return the deleteFlag
     */
    public String getDeleteFlag() {
      return deleteFlag;
    }

    /**
     * @param deleteFlag the deleteFlag to set
     */
    public void setDeleteFlag(String deleteFlag) {
      this.deleteFlag = deleteFlag;
    }

    /**
     * @return the description
     */
    public String getDescription() {
      return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
      this.description = description;
    }

    /**
     * @return the fixed
     */
    public boolean isFixed() {
      return fixed;
    }

    /**
     * @param fixed the fixed to set
     */
    public void setFixed(boolean fixed) {
      this.fixed = fixed;
    }

    /**
     * @return the removable
     */
    public boolean isRemovable() {
      return removable;
    }

    /**
     * @param removable the removable to set
     */
    public void setRemovable(boolean removable) {
      this.removable = removable;
    }

    /**
     * @return the userid
     */
    public String getUserid() {
      return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
      this.userid = userid;
    }

    /**
     * @return the ccList
     */
    public String getCcList() {
      return ccList;
    }

    /**
     * @param ccList the ccList to set
     */
    public void setCcList(String ccList) {
      this.ccList = ccList;
    }

    // Getters and setters
  }

  /**
   * @return the summary
   */
  public String getSummary() {
    return summary;
  }

  /**
   * @param summary the summary to set
   */
  public void setSummary(String summary) {
    this.summary = summary;
  }

  /**
   * @return the pkey
   */
  public String getPkey() {
    return pkey;
  }

  /**
   * @param pkey the pkey to set
   */
  public void setPkey(String pkey) {
    this.pkey = pkey;
  }

  /**
   * @return the issuetype
   */
  public String getIssuetype() {
    return issuetype;
  }

  /**
   * @param issuetype the issuetype to set
   */
  public void setIssuetype(String issuetype) {
    this.issuetype = issuetype;
  }

  /**
   * @return the applicant
   */
  public String getApplicant() {
    return applicant;
  }

  /**
   * @param applicant the applicant to set
   */
  public void setApplicant(String applicant) {
    this.applicant = applicant;
  }

  /**
   * @return the priority
   */
  public String getPriority() {
    return priority;
  }

  /**
   * @param priority the priority to set
   */
  public void setPriority(String priority) {
    this.priority = priority;
  }

  /**
   * @return the data
   */
  public Data getData() {
    return data;
  }

  /**
   * @param data the data to set
   */
  public void setData(Data data) {
    this.data = data;
  }


}
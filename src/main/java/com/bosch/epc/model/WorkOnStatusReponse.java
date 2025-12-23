/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;


import java.util.List;

/**
 * @author VFE1COB
 */
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author VFE1COB ResponseClass for retreiving workon response
 */
public class WorkOnStatusReponse {

  @JsonProperty("status")
  private List<Status> status;

  @JsonProperty("requestKey")
  private String requestKey;

  @JsonProperty("resolution")
  private String resolution;

  /**
   * @return list of status
   */
  // Getters and Setters
  public List<Status> getStatus() {
    return status;
  }

  /**
   * @param status set list of status
   */
  public void setStatus(List<Status> status) {
    this.status = status;
  }

  /**
   * @return get request key
   */
  public String getRequestKey() {
    return requestKey;
  }

  /**
   * @param requestKey set requestkey
   */
  public void setRequestKey(String requestKey) {
    this.requestKey = requestKey;
  }

  /**
   * @return get resolution of workon request
   */
  public String getResolution() {
    return resolution;
  }

  /**
   * @param resolution set reoslution of workon request
   */
  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  /**
   * @author VFE1COB
   */
  public static class Status {

    @JsonProperty("i8nValue")
    private String i8nValue;

    @JsonProperty("localeName")
    private String localeName;

    // Getters and Setters
    /**
     * @return status of request
     */
    public String getI8nValue() {
      return i8nValue;
    }

    /**
     * @param i8nValue set status
     */
    public void setI8nValue(String i8nValue) {
      this.i8nValue = i8nValue;
    }

    /**
     * @return localeName
     */
    public String getLocaleName() {
      return localeName;
    }

    /**
     * @param localeName set localename
     */
    public void setLocaleName(String localeName) {
      this.localeName = localeName;
    }
  }
}


/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.model;

import org.springframework.stereotype.Component;

/**
 * The class helps to store the details of ELM server details
 * 
 * @author ppt4kor
 */
@Component
public class ELMServerDtl {

  private String repositoryURL;
  private String projectAreaName;

  /**
   * @return the repositoryURL
   */
  public String getRepositoryURL() {
    return repositoryURL;
  }

  /**
   * @param repositoryURL the repositoryURL to set
   */
  public void setRepositoryURL(String repositoryURL) {
    this.repositoryURL = repositoryURL;
  }

  /**
   * @return the projectAreaName
   */
  public String getProjectAreaName() {
    return projectAreaName;
  }

  /**
   * @param projectAreaName the projectAreaName to set
   */
  public void setProjectAreaName(String projectAreaName) {
    this.projectAreaName = projectAreaName;
  }

}

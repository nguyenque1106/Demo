/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bosch.epc.constant.CommonConstant;
import com.bosch.epc.jobs.ELMDataSyncJob;
import com.bosch.epc.jobs.ProcessRequestJob;
import com.bosch.epc.jobs.WorkONStatusSyncJob;

import io.swagger.v3.oas.annotations.Hidden;


/**
 * Controller to Trigger and manage jobs on scheduler failure.
 * 
 * @author PPT4KOR
 */
@Hidden
@RestController
@RequestMapping("/jobs")
public class JobController {

  /**
   * Logger variable
   */
  private static final Logger logger = LoggerFactory.getLogger(JobController.class);

  private final ProcessRequestJob processRequestJob;

  private final ELMDataSyncJob elmDataSyncJob;

  private final WorkONStatusSyncJob workONStatusSyncJob;

  /**
   * Constructor
   * 
   * @param processRequestJob   {@link ProcessRequestJob}
   * @param elmDataSyncJob      {@link ELMDataSyncJob}
   * @param workONStatusSyncJob {@link WorkONStatusSyncJob}
   */
  public JobController(ProcessRequestJob processRequestJob, ELMDataSyncJob elmDataSyncJob,
      WorkONStatusSyncJob workONStatusSyncJob) {
    this.workONStatusSyncJob = workONStatusSyncJob;
    this.elmDataSyncJob = elmDataSyncJob;
    this.processRequestJob = processRequestJob;
  }

  /**
   * Manual rest call for Process Request Job
   * 
   * @return Success message
   */
  @GetMapping("/run-processRequest")
  public ResponseEntity<String> runProcessRequest() {
    logger.info("runProcessRequest {}", CommonConstant.MESS_START);
    processRequestJob.executeJob();
    logger.info("runProcessRequest {}", CommonConstant.MESS_END);
    return ResponseEntity.ok("ProcessRequest Job executed successfully");
  }

  /**
   * Manual rest call for ELM data Sync Job
   * 
   * @return Success message
   */
  @GetMapping("/run-elmdataSync")
  public ResponseEntity<String> runELMDataSync() {
    logger.info("runELMDataSync {}", CommonConstant.MESS_START);
    elmDataSyncJob.executeJob();
    logger.info("runELMDataSync {}", CommonConstant.MESS_END);
    return ResponseEntity.ok("ELMDataSync Job executed successfully");
  }

  /**
   * Manual rest call for WorkON Status Sync Job
   * 
   * @return Success message
   */
  @GetMapping("/run-workONStatusSync")
  public ResponseEntity<String> runWorkONStatusSync() {
    logger.info("runWorkONStatusSync {}", CommonConstant.MESS_START);
    workONStatusSyncJob.executeJob();
    logger.info("runWorkONStatusSync {}", CommonConstant.MESS_END);
    return ResponseEntity.ok("WorkONStatusSync Job executed successfully");
  }
}


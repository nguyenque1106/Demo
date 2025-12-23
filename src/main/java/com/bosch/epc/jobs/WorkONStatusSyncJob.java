
//  Copyright (c) Robert Bosch GmbH. All rights reserved.

package com.bosch.epc.jobs;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bosch.epc.constant.RequestStatus;
import com.bosch.epc.datamodel.Request;
import com.bosch.epc.service.RequestService;

/**
 * The Scheduler class to run the scheduled Tasks using cron expression
 * 
 * @author PPT4KOR
 **/
@Component
public class WorkONStatusSyncJob {

  private static final Logger logger = LoggerFactory.getLogger(WorkONStatusSyncJob.class);

  @Autowired
  private RequestService reqService;
  
  /**Scheduler Method triggered according to the cron expression. 
   * Method to update the workon status and update the request table accordingly
   * It also updates the master table once the request is apporved
   */
 @Scheduled(cron = "${cronExpToSyncWorkONStatus}")
  public void executeJob() {
  //read all request from request table(status:pending approval)
    List<Request> reqList=reqService.findAllByStatus(RequestStatus.PENDING_FOR_APPROVAL.toString());
    if(!(reqList.isEmpty()))
    {
      for(Request request:reqList)
      {
        //call requeststatus n loop for all req 
        String status;
        try {
          status = reqService.getRequestStatus(request.getWorkonid());
          if(RequestStatus.APPROVED_SCEDULED.toString().contains(status))
          {
            //update status of request from pending approval to approved
            reqService.updateStatusById(request.getId(),RequestStatus.APPROVED_SCEDULED.toString());
            }
          else if(RequestStatus.REJECTED_DECLINED.toString().contains(status))
          {
            reqService.updateStatusById(request.getId(),RequestStatus.REJECTED_DECLINED.toString());
          }
        }
        catch (IOException e) {
         logger.error("Error invoking workon applciation for retrieving status");
        }
       
        
      }
    }
    else {
      logger.info("No pending request for approval");
    }
  }

 
}

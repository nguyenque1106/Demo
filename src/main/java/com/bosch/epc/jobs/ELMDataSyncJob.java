
//  Copyright (c) Robert Bosch GmbH. All rights reserved.

package com.bosch.epc.jobs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bosch.epc.datamodel.ProjectArea;
import com.bosch.epc.service.ProjectAreaService;
import com.bosch.rtc.util.AlmServerConnection;
import com.bosch.rtc.util.PropertyUtils;
import com.ibm.team.process.client.IProcessClientService;
import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;

/**
 * The ELMDataSyncJob class to sync the Project Areas between tool and ELM server
 * 
 * @author PPT4KOR
 **/
@Component
public class ELMDataSyncJob {

  private static final Logger logger = LoggerFactory.getLogger(ELMDataSyncJob.class);

  @Autowired
  private ProjectAreaService paService;

  /**
   * Scheduler Method triggered according to the cron expression. 
   * ELM Project Areas will be in sync with EPC DB
   */
  @SuppressWarnings("unchecked")
  @Scheduled(cron = "${cronExpToSyncELMData}")
  public void executeJob() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
    String strDate = dateFormat.format(new Date());
    logger.info("Cron job Scheduler: Job running at - " + strDate);

    try {
      ITeamRepository repo = AlmServerConnection.getRepo();

      IProcessItemService processItemService = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);

      // Fetch the Project Areas from ELM
      List<IProjectArea> eLMPrjAreaList =
          processItemService.findAllProjectAreas(IProcessClientService.ALL_PROPERTIES, null);

      // Fetch the Projetc Areas from DB
      List<ProjectArea> toolPrjAreaList = paService.getAllPAs();

      List<ProjectArea> pAsToUpdate = new ArrayList<ProjectArea>();

      // Update the Project Area list. MasterProjectArea information will be updated later as one master PA can be
      // derived to multiple Children
      for (IProjectArea elmProjectArea : eLMPrjAreaList) {

        boolean isProjAreaFound = false;
        for (ProjectArea projectArea : toolPrjAreaList) {

          // check if the ELM Project Area available in DB and update incase of changes
          if (projectArea.getUuid().equalsIgnoreCase(elmProjectArea.getItemId().getUuidValue())) {
            isProjAreaFound = true;
            // compare the data
            if (!(projectArea.getName().equalsIgnoreCase(elmProjectArea.getName())
                && projectArea.getIsArchived().equals(elmProjectArea.isArchived()))) {
              projectArea.setName(elmProjectArea.getName());
              projectArea.setIsArchived(elmProjectArea.isArchived());
              projectArea.setModifiedBy(PropertyUtils.getPropValues("ALM_SERVICE_USER_NAME"));
              projectArea.setModificationDate(new java.sql.Date(System.currentTimeMillis()));
              pAsToUpdate.add(projectArea);
            }
          }
        }

        // Save the Project Area if the ELM Project Area is not avilable in DB
        if (!isProjAreaFound) {
          // Add to DB
          ProjectArea newMProjectArea = new ProjectArea();
          newMProjectArea.setUuid(elmProjectArea.getItemId().getUuidValue());
          newMProjectArea.setName(elmProjectArea.getName());
          newMProjectArea.setIsArchived(elmProjectArea.isArchived());
          newMProjectArea.setCreatedBy(PropertyUtils.getPropValues("ALM_SERVICE_USER_NAME"));
          newMProjectArea.setCreationDate(new java.sql.Date(System.currentTimeMillis()));
          pAsToUpdate.add(newMProjectArea);
        }

      }
      // update the data to DB
      paService.savePAs(pAsToUpdate);

      // Fetch the latest Project Area list from DB
      toolPrjAreaList = paService.getAllPAs();

      // Create a map with UUID to update the Master Project Area information
      Map<String, ProjectArea> map =
          toolPrjAreaList.stream().collect(Collectors.toMap(ProjectArea::getUuid, Function.identity()));

      pAsToUpdate = new ArrayList<ProjectArea>();
      for (IProjectArea elmProjectArea : eLMPrjAreaList) {

        // Get the Master Project Area of ELM Project Area
        IProjectAreaHandle elmParentPA =
            ((com.ibm.team.process.internal.common.ProjectArea) elmProjectArea).getProcessProvider();

        for (ProjectArea projectArea : toolPrjAreaList) {

          if (projectArea.getUuid().equalsIgnoreCase(elmProjectArea.getItemId().getUuidValue())) {

            logger.debug("PA " + projectArea.getName() + " ELM PA : " + elmProjectArea.getName() + " ELM Master PA : "
                + (elmParentPA == null ? "ELM Parent PA is null"
                    : map.get(elmParentPA.getItemId().getUuidValue()).getName()));

            if (null == elmParentPA && null != projectArea.getMasterPA()) {
              // update Project Area
              projectArea.setMasterPA(null);
              projectArea.setModifiedBy(PropertyUtils.getPropValues("ALM_SERVICE_USER_NAME"));
              projectArea.setModificationDate(new java.sql.Date(System.currentTimeMillis()));
              pAsToUpdate.add(projectArea);
            }
            else if ((null != elmParentPA && null == projectArea.getMasterPA()) || (null != elmParentPA
                && map.get(elmParentPA.getItemId().getUuidValue()).getId() != projectArea.getMasterPA().getId())) {
              // update Project Area
              projectArea.setMasterPA(map.get(elmParentPA.getItemId().getUuidValue()));
              projectArea.setModifiedBy(PropertyUtils.getPropValues("ALM_SERVICE_USER_NAME"));
              projectArea.setModificationDate(new java.sql.Date(System.currentTimeMillis()));
              pAsToUpdate.add(projectArea);
            }
          }
        }
      }
      // update the data to DB
      paService.savePAs(pAsToUpdate);
    }
    catch (TeamRepositoryException | IOException e) {
      logger.error("Scheduler syncELMData().. Unable to sync the ELM data with EPC DB");
    }

    String endDate = dateFormat.format(new Date());
    logger.info("Cron job Scheduler: Job running at - " + endDate);
  }
}

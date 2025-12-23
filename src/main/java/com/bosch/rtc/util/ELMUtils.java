/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.util;

import java.util.Arrays;
import java.util.Optional;

import org.eclipse.core.runtime.NullProgressMonitor;

import com.bosch.epc.constant.WITypeEnum;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.model.IResolution;
import com.ibm.team.workitem.common.model.IState;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;

/**
 * Utility class providing helper methods to retrieve workflow-related information from IBM Engineering Lifecycle
 * Management (ELM / RTC).
 * 
 * @author PPT4KOR
 */
public final class ELMUtils {

  private ELMUtils() {
  }

  /**
   * Mehtod to get the work item type name from work item Id
   * 
   * @param workItemTypeId work Item Type Id
   * @param repo           ITeamRepository
   * @param projectArea    projectArea
   * @return an {@link Optional} containing the resolved work item type name if it exists; otherwise
   *         {@link Optional#empty()}
   * @throws TeamRepositoryException TeamRepositoryException
   */
  public static String getWorkitemTypeName(String workItemTypeId, ITeamRepository repo, IProjectArea projectArea)
      throws TeamRepositoryException {

    // validate workItemTypeId
    if (workItemTypeId == null || workItemTypeId.isBlank()) {
      return null;
    }

    // Check if the workItemTypeId is default
    Optional<String> defaultWorkItemType = findDefaultWorkItemType(workItemTypeId);

    if (!defaultWorkItemType.isPresent()) {
      IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
      IWorkItemType workItemType =
          workItemClient.findWorkItemType(projectArea, workItemTypeId, new NullProgressMonitor());
      if (workItemType == null) {
        throw new TeamRepositoryException(
            "Work item type " + workItemTypeId + " does not exist in project area " + projectArea.getName() + ".");
      }
      return workItemType.getDisplayName();
    }
    return defaultWorkItemType.get();

  }

  /**
   * Validates whether the given default work item type is given in the input.
   * <p>
   * If the work item type is default, its identifier is returned wrapped in an {@link Optional}. If not exist an empty
   * {@code Optional} is returned.
   * 
   * @param workItemTypeId work Item Type Id
   * @return default Work item Type Name
   */
  private static Optional<String> findDefaultWorkItemType(String workItemTypeId) {
    return Arrays.stream(WITypeEnum.values()).filter(e -> e.getId().equalsIgnoreCase(workItemTypeId))
        .map(WITypeEnum::getName).findFirst();
  }

  /**
   * Mehtod to get the state name from state Id
   * 
   * @param workItemTypeId work Item Type Id
   * @param stateId        state Id
   * @param repo           ITeamRepository
   * @param projectArea    projectArea
   * @return Work item Type Name
   * @throws TeamRepositoryException TeamRepositoryException
   */
  public static String getStateNameById(String workItemTypeId, String stateId, ITeamRepository repo,
      IProjectArea projectArea) throws TeamRepositoryException {

    // validate workItemTypeId
    if (stateId == null || stateId.isBlank()) {
      return null;
    }

    // Check if the workItemTypeId is default
    Optional<String> defaultWorkItemType = findDefaultWorkItemType(workItemTypeId);

    if (!defaultWorkItemType.isPresent()) {
      // Get the Workflow Information for the Work Item Type
      IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
      IWorkflowInfo workflowInfo = workItemClient.getWorkflow(workItemTypeId, projectArea, new NullProgressMonitor());


      if (workflowInfo == null) {
        throw new TeamRepositoryException(
            "Work item type " + workItemTypeId + " does not exist in project area " + projectArea.getName() + ".");
      }

      Identifier<IState>[] states = workflowInfo.getAllStateIds();

      for (Identifier<IState> stateIdByWrkflw : states) {
        if (stateIdByWrkflw.getStringIdentifier().equalsIgnoreCase(stateId)) {
          // 4. Get State Name
          return workflowInfo.getStateName(stateIdByWrkflw);
        }
      }
    }

    return defaultWorkItemType.get();

  }

  /**
   * Mehtod to get the Resolution name from Resolution Id
   * 
   * @param workItemTypeId work Item Type Id
   * @param stateId        state Id
   * @param resolutionId   Resolution Id
   * @param repo           ITeamRepository
   * @param projectArea    projectArea
   * @return Work item Type Name
   * @throws TeamRepositoryException TeamRepositoryException
   */
  public static String getResolutionNameById(String workItemTypeId, String stateId, String resolutionId,
      ITeamRepository repo, IProjectArea projectArea) throws TeamRepositoryException {

    if (stateId == null || stateId.isBlank()) {
      return null;
    }

    // Get the Workflow Information for the Work Item Type
    IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
    IWorkflowInfo workflowInfo = workItemClient.getWorkflow(workItemTypeId, projectArea, new NullProgressMonitor());


    if (workflowInfo == null) {
      throw new TeamRepositoryException(
          "Work item type " + workItemTypeId + " does not exist in project area " + projectArea.getName() + ".");
    }
    Identifier<IState>[] states = workflowInfo.getAllStateIds();

    for (Identifier<IState> stateIdByWrkflw : states) {
      if (stateIdByWrkflw.getStringIdentifier().equalsIgnoreCase(stateId)) {
        // Get resolution IDs for the state
        Identifier<IResolution>[] resolutionIds = workflowInfo.getStateResolutionIds(stateIdByWrkflw);

        if (resolutionIds.length != 0) {
          for (Identifier<IResolution> resIdByWrkflw : resolutionIds) {

            if (resIdByWrkflw.getStringIdentifier().equalsIgnoreCase(resolutionId)) {
              // 4. Get Resolution Name
              return workflowInfo.getResolutionName(resIdByWrkflw);
            }
          }
        }
      }
    }
    return null;
  }

}

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bosch.epc.datamodel.ProjectArea;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;

/**
 * Interface class for ProjectArea Service.
 *
 * @author QYU1HC
 */
@Service
public interface ProjectAreaService {

  /**
   * @return lis of PA
   */
  public List<ProjectArea> getAllPAs();

  /**
   * @return lis of PA
   */
  public Optional<ProjectArea> findById(Integer projectAreaId);

  /**
   * @param elmRoles
   * @return
   */
  public List<ProjectArea> savePAs(final List<ProjectArea> projectAreas);

  /**
   * Find Project Areas by UUID
   *
   * @param accessiblePAUUIDs accessiblePAUUIDs
   * @return List of Project Areas
   */
  public List<ProjectArea> findByUuidIn(List<String> accessiblePAUUIDs);

  /**
   * Find IProjectArea by Project Area UUID
   * 
   * @param projectAreaUUID project Area UUID
   * @param repo            repository
   * @return IProjectArea object
   * @throws TeamRepositoryException TeamRepositoryException
   */
  public IProjectArea getIProjectAreaByUUID(String projectAreaUUID, ITeamRepository repo)
      throws TeamRepositoryException;

}

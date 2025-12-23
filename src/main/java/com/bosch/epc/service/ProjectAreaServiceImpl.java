/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.util.List;
import java.util.Optional;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bosch.epc.dao.ProjectAreaRepository;
import com.bosch.epc.datamodel.ProjectArea;
import com.ibm.team.process.client.IProcessClientService;
import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;

/**
 * Service class to manage the Project Area data in DB
 *
 * @author QYU1HC
 */
@Service
public class ProjectAreaServiceImpl implements ProjectAreaService {

  @Autowired
  private ProjectAreaRepository paRepo;

  /**
   * @return lis of PA
   */
  @Override
  public List<ProjectArea> getAllPAs() {
    return this.paRepo.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<ProjectArea> findById(final Integer projectAreaId) {
    return this.paRepo.findById(projectAreaId);
  }

  /**
   * @param elmRoles
   * @return
   */
  @Transactional
  @Override
  public List<ProjectArea> savePAs(final List<ProjectArea> projectAreas) {
    return this.paRepo.saveAll(projectAreas);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectArea> findByUuidIn(final List<String> accessiblePAUUIDs) {
    return this.paRepo.findByUuidIn(accessiblePAUUIDs);

  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public IProjectArea getIProjectAreaByUUID(final String projectAreaUUID, final ITeamRepository repo)
      throws TeamRepositoryException {
    List<IProjectArea> iProjectAreas = ((IProcessItemService) repo.getClientLibrary(IProcessItemService.class))
          .findAllProjectAreas(IProcessClientService.ALL_PROPERTIES, new NullProgressMonitor());
    for (IProjectArea iProjectArea : iProjectAreas) {
      if (iProjectArea.getItemId().getUuidValue().contentEquals(projectAreaUUID) && !iProjectArea.isArchived()) {
        return iProjectArea;
      }
    }
    return null;
  }

}

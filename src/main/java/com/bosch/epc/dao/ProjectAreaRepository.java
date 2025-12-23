/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bosch.epc.datamodel.ProjectArea;

/**
 * ProjectAreaRepository Class
 * 
 * @author QYU1HC
 */
public interface ProjectAreaRepository extends JpaRepository<ProjectArea, Integer> {

  /**
   * Find the Project Areas by UUID
   * 
   * @param accessiblePAs List of UUIDs for search
   * @return List of ProjectAreas
   */
  List<ProjectArea> findByUuidIn(List<String> accessiblePAUUIDs);
}


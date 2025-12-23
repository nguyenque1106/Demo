/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.unittest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bosch.epc.service.ProjectAreaService;

/**
 * Test class for EhCache
 * 
 * @author PPT4KOR
 */
@SpringBootTest
@Transactional
public class EhcacheTest {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private ProjectAreaService paService;

  /**
   * Measure time taken WITHOUT cache paService.getAllPAs();
   */
  @Test
  public void testCache() {

    entityManager.unwrap(Session.class).getSessionFactory().getCache().evictAllRegions();

    long start = System.currentTimeMillis();

    for (int i = 0; i < 5; i++) {
      entityManager.clear();
      paService.getAllPAs();
    }

    long end = System.currentTimeMillis();
    long timeTakenWithoutCache = end - start;

    start = System.currentTimeMillis();

    paService.getAllPAs(); // DB hit

    for (int i = 0; i < 4; i++) {
      entityManager.clear();
      paService.getAllPAs(); // Cache hit
    }

    end = System.currentTimeMillis();
    long timeTakenWithCache = end - start;
    assertAll(
        () -> assertThat(timeTakenWithoutCache)
            .as("Expected cache to be faster, but Time taken without Cache=%d ns Time taken with Cache=%d ns",
                timeTakenWithoutCache, timeTakenWithCache)
            .isGreaterThan(timeTakenWithCache),
        () -> assertThat(true).as("Success: With cache, time reduced! Time taken without Cache=" + timeTakenWithoutCache
            + " ns, Time taken with Cache=" + timeTakenWithCache + " ns").isTrue());
  }

}

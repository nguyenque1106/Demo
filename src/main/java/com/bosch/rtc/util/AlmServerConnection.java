package com.bosch.rtc.util;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.ibm.team.repository.client.ILoginHandler2;
import com.ibm.team.repository.client.ILoginInfo2;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.login.UsernameAndPasswordLoginInfo;
import com.ibm.team.repository.common.TeamRepositoryException;

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */

/**
 * AlmServerConnection class to get the repository connection of ELM
 * 
 * @author ppt4kor
 */
public class AlmServerConnection {

  /** The logger. */
  private static final Logger logger = LogManager.getLogger(AlmServerConnection.class.getName());

  /** The repo. */
  private static ITeamRepository repo = null;


  /**
   * Method to get the repository connection of ELM
   * 
   * @return the repo
   * @throws IOException             IOException
   * @throws TeamRepositoryException TeamRepositoryException
   */
  public static ITeamRepository getRepo() throws TeamRepositoryException, IOException {
    if (repo == null) {
      connectToServer();
    }
    return repo;
  }


  /**
   * @param repo the repo to set
   */
  public static void setRepo(ITeamRepository repo) {
    AlmServerConnection.repo = repo;
  }

  /**
   * Constructor
   */
  private AlmServerConnection() {
  }

  /**
   * Function to connect to the ELM server and get the repository connection
   * 
   * @return repository
   * @throws TeamRepositoryException : TeamRepositoryException
   * @throws IOException             IOException
   */
  public static ITeamRepository connectToServer() throws TeamRepositoryException, IOException {

    logger.debug("AlmServerConnection connectToServer() Started..");

    if (!TeamPlatform.isStarted()) {
      TeamPlatform.startup();
    }

    repo = TeamPlatform.getTeamRepositoryService().getTeamRepository(PropertyUtils.getPropValues("ALM_REPOSITORY_URL"));
    repo.registerLoginHandler(new ILoginHandler2() {

      @Override
      public ILoginInfo2 challenge(final ITeamRepository repository) {
        try {
          return new UsernameAndPasswordLoginInfo(PropertyUtils.getPropValues("ALM_TEST_USER_NAME"),
              PropertyUtils.getPropValues("ALM_TEST_USER_PASSWORD"));
        }
        catch (IOException e) {
          e.printStackTrace();
          return null;
        }
      }
    });
    repo.login(new NullProgressMonitor());
    logger.debug("AlmServerConnection connectToServer() Finished..");
    return repo;
  }


}

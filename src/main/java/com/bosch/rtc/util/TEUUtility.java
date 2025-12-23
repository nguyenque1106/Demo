/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilitly class for TEU Tool. The class will run the TEU tool for the given Project Area details
 *
 * @author ppt4kor
 */
public class TEUUtility {

  /**
   * Logger variable
   */
  private static final Logger logger = LoggerFactory.getLogger(TEUUtility.class);

  private static final String teu_Merge_Target = "path.mergeTarget";

  /**
   * Download the Project Area Template by the given Project Area name.
   *
   * @param projectAreaName the project area name
   * @return true, if successful
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static boolean downloadTemplate(final String projectAreaName) throws IOException {
    logger.info("Downloding template from TEU Tool started..");
    try {
      ProcessBuilder builder = new ProcessBuilder(PropertyUtils.getPropValues("TEU_CMD_ARGS_1"),
          PropertyUtils.getPropValues("TEU_CMD_ARGS_2"),
          PropertyUtils.getPropValues("TEU_CMD_ARGS_3").replace("?ProjAreaName", projectAreaName));
      builder.redirectErrorStream(true);
      Process p = builder.start();
      BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      while (true) {
        line = r.readLine();
        if (line == null) {
          break;
        }
        // Code to check the errors
        if (line.contains("Error message")) {
          logger.error(line);
          return false;
        }
        if (line.contains("TEMPLATE EXCHANGE TOOL - ENDED")) {
          break;
        }
        logger.info(line);
      }
      logger.info("Downloding template from TEU Tool completed..");
      return true;
    }
    catch (Exception e) {
      throw new IOException(e);
    }
  }

  /**
   * Upload the Project Area Template for the given Project Area name.
   *
   * @param projectAreaName Project Area Name
   * @return true, if successful
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static boolean uploadTemplate(final String projectAreaName) throws IOException {
    logger.info("Uploading of template from TEU Tool started..");
    try {

      // code to update the properties file for mergeTarget key
      Properties props = new Properties();
      try (FileInputStream in = new FileInputStream(PropertyUtils.getPropValues("TEU_TOOL_PROPERTIES_PATH"))) {
        props.load(in);
      }

      try (FileOutputStream out = new FileOutputStream(PropertyUtils.getPropValues("TEU_TOOL_PROPERTIES_PATH"))) {
        props.setProperty(teu_Merge_Target,
            PropertyUtils.getPropValues("TEU_TOOL_Templates_PATH").replace("?ProjAreaName", projectAreaName));
        props.store(out, null);
      }

      ProcessBuilder builder1 = new ProcessBuilder(PropertyUtils.getPropValues("TEU_CMD_ARGS_1"),
          PropertyUtils.getPropValues("TEU_CMD_ARGS_2"),
          PropertyUtils.getPropValues("TEU_CMD_ARGS_4").replace("?ProjAreaName", projectAreaName));
      builder1.redirectErrorStream(true);
      Process p1 = builder1.start();
      BufferedReader r1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
      while (true) {
        String line = r1.readLine();
        if (line == null) {
          break;
        }
        // Code to check the errors
        if (line.contains("Could not perform the assemble task")) {
          logger.error(line);
          return false;
        }
        if (line.contains("TEMPLATE EXCHANGE TOOL - ENDED")) {
          break;
        }
        logger.info(line);
      }
      logger.info("Uploading of template from TEU Tool completed..");
      return true;
    }
    catch (Exception e) {
      throw new IOException(e);
    }
  }

  /**
   * @param file
   * @return
   */
  public static boolean validFile(final File file) {
    boolean fileExist = true;
    if (!file.exists()) {
      boolean folderExist = true;
      fileExist = false;
      try {
        if (!file.getParentFile().exists()) {
          folderExist = file.getParentFile().mkdirs();
        }
        if (folderExist) {
          fileExist = file.createNewFile();
        }
      }
      catch (IOException e) {
        logger.error("Can not create file:{}", file.getAbsolutePath());
      }
      catch (SecurityException e) {
        logger.error("The folder has no write permission. Folder path:{}", file.getParent());
      }
    }
    return fileExist;
  }
}

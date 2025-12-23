package com.bosch.rtc.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;


/**
 * PropertyUtils Class to read the Property file
 *
 * @author PPT4KOR
 */
public class PropertyUtils {

  public static final String TEU_TOOL_TEMPLATES_PATH = "TEU_TOOL_Templates_PATH";
  public static final String TEU_TOOL_PATH_SPEC = "TEU_TOOL_PATH_SPEC";
  public static final String PARAM_PROJECT_AREA_NAME = "?ProjAreaName";

  /**
   * {@link Constructor}
   */
  private PropertyUtils() {
    // Do nothing
  }

  /**
   * @param propertyName Property Name
   * @return value of Property Name
   * @throws IOException Exception found while reading the file
   */
  @SuppressWarnings("null")
  public static String getPropValues(final String propertyName) throws IOException {

    String propFileName = null;
    InputStream inputStream = null;
    String result = null;
    try {
      Properties prop = new Properties();
      propFileName = "./application.properties";

      inputStream = PropertyUtils.class.getClassLoader().getResourceAsStream(propFileName);
      if (inputStream != null) {
        prop.load(inputStream);
      }
      else {
        throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
      }

      result = prop.getProperty(propertyName);


    }
    catch (Exception e) {
      System.out.println("Exception: " + e);
    }
    finally {
      inputStream.close();
    }
    return result;
  }
}


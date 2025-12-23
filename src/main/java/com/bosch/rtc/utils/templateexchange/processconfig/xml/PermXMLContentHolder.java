/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.processconfig.xml;

/**
 * This class is holding the operation xml contents which is used to merge in the process config file.
 * 
 * @author PPT4KOR
 */
public class PermXMLContentHolder {

  private final String opertionXMLContents;

  /**
   * Constructor
   * 
   * @param opertionXMLContents opertion XML Contents
   */
  public PermXMLContentHolder(final String opertionXMLContents) {
    this.opertionXMLContents = opertionXMLContents;

  }

  /**
   * @return the opertionXMLContents
   */
  public String getOpertionXMLContents() {
    return opertionXMLContents;
  }

}

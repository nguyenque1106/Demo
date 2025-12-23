/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.processconfig.xml;

/**
 * @author VFE1COB
 *
 */
public class RoleXMLContentHolder {
  final private String roleXMLContents;
 
  /**
   * @param roleXMLContents
   */
  public RoleXMLContentHolder(final String roleXMLContents) {
    this.roleXMLContents = roleXMLContents;

  }

  /**
   * @return the roleXMLContents
   */
  public String getRoleXMLContents() {
    return roleXMLContents;
  }

}

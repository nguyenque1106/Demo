/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.constant;


/**
 * @author GHT9HC
 */
public enum RoleEnum {
                      EVERYONE("Everyone", "default");

  private String roleName;
  private String identifier;

  private RoleEnum(final String roleName, final String identifier) {
    this.roleName = roleName;
    this.identifier = identifier;
  }

  /**
   * @return the roleName
   */
  public String getRoleName() {
    return this.roleName;
  }


  /**
   * @return the identifier
   */
  public String getIdentifier() {
    return this.identifier;
  }

}

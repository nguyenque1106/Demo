/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.constant;


/**
 * Enum class for default Work item type
 * 
 * @author GHT9HC
 */
public enum WITypeEnum {

                        /**
                         * GENERAL_WI_TYPES
                         */
                        GENERAL_WI_TYPES("General for all WI Types", "default");

  private String name;
  private String id;

  private WITypeEnum(final String typeName, final String typeId) {
    this.name = typeName;
    this.id = typeId;
  }

  /**
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }
}

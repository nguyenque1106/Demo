/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.constant;


/**
 * Enum class for State Groups
 * 
 * @author GHT9HC
 */
public enum StateGroupEnum {

                            /**
                             * Open
                             */
                            OPEN("Open", "open"),
                            /**
                             * In Progress
                             */
                            IN_PROGRESS("In Progress", "inprogress"),
                            /**
                             * Closed
                             */
                            CLOSED("Closed", "closed");

  private String name;
  private String id;

  private StateGroupEnum(final String typeName, final String typeId) {
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

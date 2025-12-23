/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.util;


/**
 * @author VFE1COB
 *
 */

public enum Operation {


                       /**
                        * Add operation : Add a new mapping
                        */
                       ADD("Add"),
                       /**
                        * Remove operation : Remove the exisiting mapping
                        */
                       REMOVE("Remove");


  private String value;

  private Operation(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value; // This will return , # or +
  }
}



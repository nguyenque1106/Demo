/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.util;


/**
 * @author VFE1COB
 *
 */
public class DatabaseOperationException extends RuntimeException {
  public DatabaseOperationException(String message, Throwable cause) {
      super(message, cause);
  }
}

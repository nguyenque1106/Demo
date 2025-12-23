/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.exception;


/**
 * @author QYU1HC
 */
public class ResourceNotFoundException extends RuntimeException {

  /**
   * @param message
   */
  public ResourceNotFoundException(final String message) {
    super(message);
  }
}

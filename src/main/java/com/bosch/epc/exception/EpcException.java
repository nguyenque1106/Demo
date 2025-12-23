/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.exception;


/**
 * Custom exception class for the EPC Tool related exceptions.
 * 
 * @author PPT4KOR
 */
public class EpcException extends Exception {

  private static final long serialVersionUID = -2624604826105822476L;

  /**
   * Parameterised constructor with the cause which is the reason for the exception
   *
   * @param cause cause for the exception
   */
  public EpcException(final Throwable cause) {
    super(cause);
  }

  /**
   * Parameterised constructor with the customMessage which is the reason for the exception
   *
   * @param customMessage which is the reason for the exception
   */
  public EpcException(final String customMessage) {
    super(customMessage);
  }

  /**
   * Parameterised constructor with the customMessage which is the reason for the exception
   *
   * @param customMessage which is the reason for the exception
   * @param cause         for the exception
   */
  public EpcException(final String customMessage, final Throwable cause) {
    super(customMessage, cause);
  }

}

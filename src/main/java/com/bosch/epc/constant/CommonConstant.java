/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.constant;


/**
 * Constant class is used for whole project.
 *
 * @author GHT9HC
 */
public final class CommonConstant {

  private CommonConstant() {}

  /**
   *
   */
  public static final String EMPTY_STR = "";
  /**
   *
   */
  public static final String SPACE_STR = " ";
  /**
   *
   */
  public static final String BACKWARD_SLASHES_STR = "\\";
  /**
  *
  */
 public static final String DOUBLE_BACKWARD_SLASHES_STR = "\\\\";
  /**
   *
   */
  public static final String NEWLINE = "\n";

  /**
   *
   */
  public static final String HYPHEN_STR = "-";
  /**
   *
   */
  public static final String STRING_STR = "string";
  /**
   *
   */
  public static final String MESS_START = HYPHEN_STR + SPACE_STR + "Start";

  /**
   *
   */
  public static final String MESS_END = HYPHEN_STR + SPACE_STR + "End";
  /**
   *
   */
  public static final String DOMAINS = "EMEA,DE,APAC,BOSCH,US,BR,CZ,NL,IT,QS";
  /**
   *
   */
  public static final String LDAP_URL = "ldap://rb-gc-12.de.bosch.com:3268";
  /**
   *
   */
  public static final String CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

  /**
   *
   */
  public static final int INT_ZERO = 0;
  
}

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.util;

import java.util.StringTokenizer;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bosch.common.ldap.model.enums.Domain;
import com.bosch.epc.constant.CommonConstant;

import waffle.servlet.WindowsPrincipal;

/**
 * User util class
 *
 * @author GHT9HC
 */
public final class UserUtils {

  private UserUtils() {
  }

  /**
   * Retrieves the username of the currently authenticated user from the SecurityContext.
   * 
   * @return The username as a String
   */
  public static String extractSimpleUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String fullUserName = authentication.getName();
    if (fullUserName == null) {
      return null;
    }

    // Split the string using the backslash as a delimiter
    // We must escape the backslash twice: once for the Java string literal, once for the regex engine
    String[] parts = fullUserName.split(CommonConstant.DOUBLE_BACKWARD_SLASHES_STR);

    // Return the last part of the array
    if (parts.length > 0) {
      return parts[parts.length - 1];
    }
    return fullUserName;
  }
  
  public static String extractSimpleUsername(String fullUserName) {
    if (fullUserName == null) {
      return null;
    }

    // Split the string using the backslash as a delimiter
    // We must escape the backslash twice: once for the Java string literal, once for the regex engine
    String[] parts = fullUserName.split(CommonConstant.DOUBLE_BACKWARD_SLASHES_STR);

    // Return the last part of the array
    if (parts.length > 0) {
      return parts[parts.length - 1];
    }
    return fullUserName;
  }

  /**
   * getNTIDBySSO - NTID
   *
   * @return
   */
  public static String getNTIDBySSO() {
    String loggedInUser = CommonConstant.EMPTY_STR;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getPrincipal() instanceof WindowsPrincipal) {// sso windows authentication
      WindowsPrincipal principal = (WindowsPrincipal) authentication.getPrincipal();
      loggedInUser = getNTIDByDN(principal.getName());
    }
    return loggedInUser;
  }

  /**
   * getDomainNTIDBySSO - APAC\NTID
   *
   * @return
   */
  public static String getDomainNTIDBySSO() {
    String loggedInUser = CommonConstant.EMPTY_STR;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getPrincipal() instanceof WindowsPrincipal) {// sso windows authentication
      WindowsPrincipal principal = (WindowsPrincipal) authentication.getPrincipal();
      loggedInUser = principal.getName();
    }
    return loggedInUser;
  }

  /**
   * @param domainNtid
   * @return
   */
  public static String getNTIDByDN(final String domainNtid) {
    StringTokenizer token = new StringTokenizer(domainNtid, CommonConstant.BACKWARD_SLASHES_STR);
    String[] tokenEle = new String[2];
    int i = 0;
    while (token.hasMoreTokens()) {
      tokenEle[i] = token.nextToken();
      i++;
    }
    return tokenEle[1];
  }

  /**
   * @param domainNtid
   * @return
   */
  public static Domain getDomainByDN(final String domainNtid) {
    Domain domain;
    StringTokenizer token = new StringTokenizer(domainNtid, CommonConstant.BACKWARD_SLASHES_STR);
    String[] tokenEle = new String[2];
    int i = 0;
    while (token.hasMoreTokens()) {
      tokenEle[i] = token.nextToken();
      i++;
    }
    String domainString = tokenEle[0];

    // List of domain which reflect under ldap.jar [DE, APAC, EMEA, BR, US, CZ, NL, IT, QS]
    switch (domainString) {
      case "DE":
        domain = Domain.DE;
        break;
      case "APAC":
        domain = Domain.APAC;
        break;
      case "EMEA":
        domain = Domain.EMEA;
        break;
      case "BR":
        domain = Domain.BR;
        break;
      case "US":
        domain = Domain.US;
        break;
      case "CZ":
        domain = Domain.CZ;
        break;
      case "NL":
        domain = Domain.NL;
        break;
      case "IT":
        domain = Domain.IT;
        break;
      case "QS":
        domain = Domain.QS;
        break;
      default:
        domain = null;
        break;
    }

    return domain;
  }
}

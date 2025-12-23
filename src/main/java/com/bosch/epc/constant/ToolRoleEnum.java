/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.constant;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author PPT4KOR
 */
public enum ToolRoleEnum {

                          /**
                           * Administrator
                           */
                          ADMIN("DE\\IDM2BCD_EPC_PS-TC_ADMINISTRATOR_PS"),
                          /**
                           * Process Owner
                           */
                          PROCESS_OWNER("DE\\IDM2BCD_EPC_PS-TC_PROCESSOWNERS_PS"),
                          /**
                           * Process Delegate
                           */
                          PROCESS_DELEGATE("DE\\IDM2BCD_EPC_PS-TC_PROCESSDELEGATES_PS"),
                          /**
                           * Process Advisor
                           */
                          PROCESS_ADVISOR("DE\\IDM2BCD_EPC_PS-TC_PROCESSADVISORS_PS");

  private String roleId;
  private static final String ROLE_PREFIX = "ROLE_";

  private ToolRoleEnum(final String roleId) {
    this.roleId = roleId;
  }

  /**
   * @return the roleId
   */
  public String getRoleId() {
    return ROLE_PREFIX + roleId;
  }

  /**
   * Checks whether the currently authenticated user has the required role.
   * <p>
   * This method typically evaluates the authorities assigned to the authenticated principal and determines if a
   * specific role is present.
   * </p>
   *
   * @return {@code true} if the user possesses the required role; {@code false} otherwise.
   */
  public boolean hasRole() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    if (authorities == null) {
      return false;
    }

    // Check if any of the user's granted authorities match this enum's authority string
    return authorities.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(this.getRoleId()));
  }

}

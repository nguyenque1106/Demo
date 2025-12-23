/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosch.epc.datamodel.ELMPermissions;
import com.bosch.epc.datamodel.RolePermMapping;
import com.bosch.epc.datamodel.RolePermReqtMapping;
import com.bosch.rtc.utils.templateexchange.bean.ActionBean;

/**
 * Class to convert the Permissions to Actionbeans
 *
 * @author PPT4KOR
 */
public class PermissionToActionConverter {

  private static final Logger logger = LoggerFactory.getLogger(PermissionToActionConverter.class);

  /**
   * RolePermReqtMappings
   */
  protected static List<RolePermReqtMapping> rolePermReqtMappings;

  /**
   * RolePermMappings
   */
  protected static List<RolePermMapping> rolePermMappings;


  /**
   * Convert list of Permission to list of Action with hierarchy
   *
   * @param permissions List of Permissions to convert to ActionBean
   * @param rolePermReqtMap List of Role Permissions available in Request
   * @param rolePermMap List of Role Permissions
   * @return List of ActionBeans
   */
  public static Set<ActionBean> convertPermissionsToActions(final List<ELMPermissions> permissions,
      final List<RolePermReqtMapping> rolePermReqtMap, final List<RolePermMapping> rolePermMap) {
    if ((permissions == null) || permissions.isEmpty()) {
      return new HashSet<ActionBean>();
    }
    rolePermReqtMappings = rolePermReqtMap;
    rolePermMappings = rolePermMap;

    return permissions.stream().map(PermissionToActionConverter::convertPermissionToAction).filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }


  /**
   * Recursively convert Permission to Action with all children
   */
  private static ActionBean convertPermissionToAction(final ELMPermissions permission) {
    if (permission == null) {
      return null;
    }
    logger.info("Processing Permission Id {}", permission.getRef_id());

    if ((permission.getChildPermissions() == null) || permission.getChildPermissions().isEmpty()) {
      List<RolePermReqtMapping> rolePermsReqs = getRolePermsReqWithPermission(permission);
      if (rolePermsReqs.isEmpty()) {
        List<RolePermMapping> rolePerms = getRolePermsWithPermission(permission);
        if (!rolePerms.isEmpty()) {
          RolePermMapping rolePerm = rolePerms.get(0);
          if (!rolePerm.isPermitted()) {
            // if not permitted then we will ignore the action
            return null;
          }
        }
      }
      else {
        RolePermReqtMapping rolePermsReq = rolePermsReqs.get(0);
        if (!rolePermsReq.isPermitted()) {
          // if not permitted then we will ignore the action
          return null;
        }
      }
    }

    ActionBean action = new ActionBean();
    action.setId(permission.getRef_id());

    // Recursively convert sub-permissions to sub-actions
    List<ActionBean> subActions = new ArrayList<>();
    if (permission.getChildPermissions() != null) {
      for (ELMPermissions subPermission : permission.getChildPermissions()) {
        ActionBean subAction = convertPermissionToAction(subPermission);
        if (subAction != null) {
          subActions.add(subAction);
        }
      }
    }
    action.setChildActions(new HashSet<>(subActions));

    return action;
  }

  /**
   * Check if permission exists by permission object
   *
   * @param permission permission
   * @return List of RolePermReqtMapping
   */
  public static List<RolePermReqtMapping> getRolePermsReqWithPermission(final ELMPermissions permission) {
    if ((rolePermReqtMappings == null) || (permission == null)) {
      return new ArrayList<>();
    }

    return rolePermReqtMappings.stream().filter(
        rolePerm -> (rolePerm.getPermission() != null) && permission.getId().equals(rolePerm.getPermission().getId()))
        .collect(Collectors.toList());
  }

  /**
   * Check if permission exists by permission object
   *
   * @param permission permission
   * @return List of RolePermMapping
   */
  public static List<RolePermMapping> getRolePermsWithPermission(final ELMPermissions permission) {
    if ((rolePermMappings == null) || (permission == null)) {
      return new ArrayList<>();
    }

    return rolePermMappings.stream().filter(
        rolePerm -> (rolePerm.getPermission() != null) && permission.getId().equals(rolePerm.getPermission().getId()))
        .collect(Collectors.toList());
  }


}

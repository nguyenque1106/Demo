/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bosch.epc.constant.CommonConstant;
import com.bosch.epc.constant.RoleEnum;
import com.bosch.epc.datamodel.AttrPermRole;
import com.bosch.epc.datamodel.ELMPermissions;
import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.ProjectArea;
import com.bosch.epc.model.ALMRole;

/**
 * @author GHT9HC This class is used for Entity/model purposes
 */
public final class EntityUtils {

  private EntityUtils() {}

  /**
   * Create an empty permission of list roles
   *
   * @param elmRoles - pa role
   * @return List<AttrPermRole>
   */
  public static List<AttrPermRole> createEmptyAttrPermRole(final List<ELMRole> elmRoles) {
    List<AttrPermRole> listResult = new ArrayList<>();
    elmRoles.stream().forEachOrdered(r -> listResult.add(emptyAttrPermRole(r)));
    return listResult;
  }


  /**
   * @param everyone
   * @return
   */
  private static AttrPermRole emptyAttrPermRole(final ELMRole elmRole) {
    // Hide the id of elmrole before return
    elmRole.setId(0);
    // map default to Everyone
    if (elmRole.getIdentifier().equals(RoleEnum.EVERYONE.getIdentifier())) {
      elmRole.setName(RoleEnum.EVERYONE.getRoleName());
    }
    return new AttrPermRole(elmRole, new ELMPermissions(CommonConstant.EMPTY_STR));
  }

  /**
   * Create AttrPermRole object by mapping to the role in DB
   *
   * @param almRole
   * @param elmRoles
   * @param projectArea
   * @return
   */
  public static AttrPermRole createAttrPermRoleForAttribute(final ALMRole almRole, final List<ELMRole> elmRoles,
      final ProjectArea projectArea) {
    ELMRole elmRole = findElmRole(elmRoles, almRole, projectArea);
    return new AttrPermRole(elmRole, new ELMPermissions(almRole.getPermission(), true));
  }

  /**
   * @param elmRoles
   * @param almRole
   * @return
   */
  private static ELMRole findElmRole(final List<ELMRole> elmRoles, final ALMRole almRole, final ProjectArea projectArea) {
    Optional<ELMRole> existedRole = elmRoles.stream().filter(r -> equalsIgnoreCaseSpace(r, almRole)).findFirst();
    if (existedRole.isPresent()) {
     return existedRole.get();
    }
    return new ELMRole(almRole.getId(), projectArea, CommonConstant.EMPTY_STR);
  }

  /**
   * @param r
   * @param almRole
   * @return
   */
  private static boolean equalsIgnoreCaseSpace(final ELMRole r, final ALMRole almRole) {
    if (almRole.getId().equals(RoleEnum.EVERYONE.getRoleName())) {
      return r.getName().equals(RoleEnum.EVERYONE.getIdentifier());
    }
    return r.getName().toLowerCase().replace(CommonConstant.SPACE_STR, CommonConstant.EMPTY_STR)
        .equals(almRole.getId().toLowerCase().replace(CommonConstant.SPACE_STR, CommonConstant.EMPTY_STR));
  }

  /**
   * Get the Element from Node which is a xml parsing result
   *
   * @param node
   * @return
   */
  public static Element getElementFromNode(final Node node) {
    if ((node != null) && (node.getNodeType() == Node.ELEMENT_NODE)) {
      return (Element) node;
    }
    return null;
  }

}

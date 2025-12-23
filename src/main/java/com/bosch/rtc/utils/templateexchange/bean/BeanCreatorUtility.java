/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bosch.rtc.util.AttributeBehaviorConstants;

/**
 * This class is used to create the condition/precondition related beans with the provided values.
 */
public class BeanCreatorUtility {

  private BeanCreatorUtility() {
    // Do Nothing
  }


  /**
   * This method is used to return the newly created {@link ConditionBean} instance.
   *
   * @param conditionId - the condition id, must not be null
   * @param conditionName - the condition name, must not be null
   * @param workItemType - the workitem type value of the condition , must not be null
   * @param status - the status of the condition, must not be null
   * @param resolution - the resolution value of condition, must not be null
   * @param roles - the roles associated with the condition, must not be null
   * @return {@link ConditionBean} instance.
   */
  public static ConditionBean getCreatedConditionBean(final String conditionId, final String conditionName,
      final String workItemType, final String status, final String resolution, final List<RoleBean> roles) {
    ConditionBean conditionBean = new ConditionBean();
    conditionBean.setId(conditionId);
    conditionBean.setName(conditionName);
    conditionBean.setProviderId(AttributeBehaviorConstants.CONDITION_PROVIDER_ID);
    WorkflowPropertyBean workflowPropertyBean = getCreatedWorkflowPropertyBean(workItemType, status, resolution, roles);
    conditionBean.getWorkflowPropertyBeans().add(workflowPropertyBean);
    return conditionBean;
  }

  /**
   * This method is used to return the newly created {@link WorkflowPropertyBean} instance.
   *
   * @param workItemType - the workitem type value of the condition , must not be null
   * @param status - the status of the condition, must not be null
   * @param resolution - the resolution value of condition, must not be null
   * @param roles - the roles associated with the condition, must not be null
   * @return {@link WorkflowPropertyBean} instance.
   */
  public static WorkflowPropertyBean getCreatedWorkflowPropertyBean(final String workItemType, final String status,
      final String resolution, final List<RoleBean> roles) {
    WorkflowPropertyBean workflowPropertyBean = new WorkflowPropertyBean();
    String statusGroup = resolution == null ? status : null;
    workflowPropertyBean.setWorkItemType(workItemType);
    workflowPropertyBean.setStatusGroup(statusGroup);
    if (resolution != null) {
      workflowPropertyBean.setResolution(resolution);
      workflowPropertyBean.setStatus(status);
    }
    workflowPropertyBean.setRoles(roles);
    return workflowPropertyBean;
  }

  /**
   * This method is used to return the created {@link RoleBean} instance.
   *
   * @param roleId - the role id/name of rolebean, must not be null
   * @param rolePermission - the role permission of rolebean, must not be null
   * @return {@link RoleBean} instance.
   */
  public static RoleBean getCreatedRoleBean(final String roleId, final String rolePermission) {
    return null;
//    return getCreatedRoleBean(roleId, rolePermission, null);
  }

  /**
   * This method is used to return the created {@link RoleBean} instance.
   *
   * @param roleId - the role id/name of rolebean, must not be null
   * @param rolePermission - the role permission of rolebean, must not be null
   * @param specialConditionBean - the {@link SpecialConditionBean} instance, must not be null
   * @return {@link RoleBean} instance.
   */
//  public static RoleBean getCreatedRoleBean(final String roleId, final String rolePermission,
//      final SpecialConditionBean specialConditionBean) {
//    RoleBean role = new RoleBean();
//    role.setId(roleId);
//    role.setPermission(rolePermission);
//
//    if (specialConditionBean != null) {
//      role.setConditionType(specialConditionBean.getConditionType());
//      String conditionScript = specialConditionBean.getConditionScript();
//      conditionScript = conditionScript.replace(System.getProperty("line.separator"), " ");
//      // encode string to base64
//      byte[] encodedConditionScript = Base64.getEncoder().encode(conditionScript.getBytes());
//
//      role.setConditionScript(new String(encodedConditionScript));
//    }
//    return role;
//  }

  /**
   * This method is used to return the created {@link PreConditionBean} instance.
   *
   * @param id - the pre condition id, must not be null
   * @param name - the pre condition name, must not be null
   * @param description - the pre condition description, must not be null
   * @param attributeId - the attribute id , must not be null
   * @param customizedAttributeId - the customized attribute id , must not be null
   * @param isRequiredAttribute - boolean value TRUE for required attribute, FALSE for readonly attributes
   * @return {@link PreConditionBean} instance.
   */
  public static PreConditionBean getCreatedPreConditionBean(final String id, final String name,
      final String description, final String attributeId, final String customizedAttributeId,
      final boolean isRequiredAttribute) {
    PreConditionBean preConditionBean = new PreConditionBean();
    preConditionBean.setId(id);
    preConditionBean.setName(name);
    preConditionBean.setDescription(description);
    AttributeBean attributeBean = getAttributeBean(attributeId, customizedAttributeId);
    Set<AttributeBean> attributeBeans = new HashSet<>();
    attributeBeans.add(attributeBean);
    if (isRequiredAttribute) {
      preConditionBean.setRequiredAttributes(attributeBeans);
    }
    else {
      preConditionBean.setReadOnlyAttributes(attributeBeans);
    }
    return preConditionBean;
  }

  /**
   * This method is used to return the created {@link AttributeBean} instance.
   *
   * @param attributeId - the attribute id , must not be null
   * @param customizedAttributeId - the customized attribute id , must not be null
   * @return {@link AttributeBean} instance
   */
  public static AttributeBean getAttributeBean(final String attributeId, final String customizedAttributeId) {
    AttributeBean attributeBean = new AttributeBean();
    attributeBean.setAttributeId(attributeId);
    attributeBean.setRuleId(customizedAttributeId);
    return attributeBean;
  }


  /**
   * This method is used to return the created {@link SpecialConditionBean} instance.
   *
   * @param id - special condition id, must not be null
   * @param description - special condition description, must not be null
   * @param conditionScript - special condition script, must not be null
   * @param conditionType - special condition type must not be null
   * @return {@link SpecialConditionBean} instance.
   */
//  public static SpecialConditionBean getCreatedSpecialConditionBean(final String id, final String description,
//      final String conditionScript, final String conditionType) {
//    SpecialConditionBean specialConditionBean = new SpecialConditionBean();
//    specialConditionBean.setId(id);
//    specialConditionBean.setDescription(description);
//    specialConditionBean.setConditionScript(conditionScript);
//    specialConditionBean.setConditionType(conditionType);
//    return specialConditionBean;
//  }


  /**
   * This method is used to return the created {@link CustomConditionBean} instance.
   *
   * @param id - id of the custom condition, must not be null
   * @param description - description of the condition, must not be null
   * @param conditionScriptName - condition script name, must not be null
   * @param conditionType - condition type, must not be null
   * @return {@link CustomConditionBean} instance.
   */
//  public static CustomConditionBean getCreatedCustomConditionBean(final String id, final String description,
//      final String conditionScriptName, final String conditionType) {
//    CustomConditionBean customConditionBean = new CustomConditionBean();
//    customConditionBean.setId(id);
//    customConditionBean.setDescription(description);
//    customConditionBean.setConditionScriptName(conditionScriptName);
//    customConditionBean.setConditionType(conditionType);
//    return customConditionBean;
//  }
}

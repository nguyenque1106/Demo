/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.util;


/**
 * This class represents the constants file which is used for the AttributeBehavior excel parsing.
 *
 * @author pla7kor
 */
public class AttributeBehaviorConstants {

  /**
   * Message represents the General workitem type string in the attribute behavior excel file.
   */
  public static final String GENERAL_WORKITEM_TYPE = "General for all WI Types";
  /**
   * Message represents the string value which is used to skip for reading the role permission.
   */
  public static final String SKIP_ROLE_PERMISSION_VALUE = "i";
  /**
   * Message represents the condition provider id.
   */
  public static final String CONDITION_PROVIDER_ID = "com.bosch.rtc.ecl.workitem.attributebehavior.condition";
  /**
   * Message represents the precondition required attributes id.
   */
  public static final String PRECONDITION_REQUIRED_ATTRIBUTES_ID = "com.ibm.team.workitem.advisor.requiredAttributes";
  /**
   * Message represents the precondition read only attributes id.
   */
  public static final String PRECONDITION_READ_ONLY_ATTRIBUTES_ID = "com.ibm.team.workitem.advisor.readOnlyAttributes";
  /**
   * Message represents the precondition required attributes name.
   */
  public static final String PRECONDITION_REQUIRED_ATTRIBUTES_NAME = "Required Attributes For Condition";
  /**
   * Message represents the precondition read only attributes name.
   */
  public static final String PRECONDITION_READ_ONLY_ATTRIBUTES_NAME = "Read-Only Attributes For Condition";
  /**
   * Message represents the precondition required attributes description.
   */
  public static final String PRECONDITION_REQUIRED_ATTRIBUTES_DESCRIPTION =
      "Verifies that a work item that matches a condition can only be saved if the selected attributes are different from the default value";
  /**
   * Message represents the precondition read only attributes description.
   */
  public static final String PRECONDITION_READ_ONLY_ATTRIBUTES_DESCRIPTION =
      "Verifies that a work item that matches a condition can only be saved if the selected attributes are unchanged";
  /**
   * Message represents the required condition prefix content.
   */
  public static final String REQUIRED_CONDITION_PREFIX = "RequiredCondition_";
  /**
   * Message represents the read only condition prefix content.
   */
  public static final String READONLY_CONDITION_PREFIX = "ReadOnlyCondition_";
  /**
   * Message represents the customized required condition attribute prefix content.
   */
  public static final String CUSTOMIZED_REQUIRED_ATTRIBUTE_ID_PREFIX =
      "com.ibm.team.workitem.valueproviders.CONDITION.required";
  /**
   * Message represents the customized read only condition attribute prefix content.
   */
  public static final String CUSTOMIZED_READ_ONLY_ATTRIBUTE_ID_PREFIX =
      "com.ibm.team.workitem.valueproviders.CONDITION.readOnly";
  /**
   * Message represents the missing attribute id in the given excel file and the updation in specification xml file.
   */
  public static final String MESSAGE_MISSING_ATTRIBUTE_ID =
      "In Attribute Behavior excel file the \"%s\" attribute id is missing. It has been updated in condition/precondition section of the process config (specification) xml file...!!!";

  private AttributeBehaviorConstants() {
    // Do Nothing
  }

}

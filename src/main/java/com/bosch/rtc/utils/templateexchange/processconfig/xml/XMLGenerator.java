/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.processconfig.xml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.bosch.epc.exception.ProcessConfigException;
import com.bosch.rtc.util.AttributeBehaviorConstants;
import com.bosch.rtc.utils.templateexchange.bean.AttributeBean;
import com.bosch.rtc.utils.templateexchange.bean.BeanCreatorUtility;
import com.bosch.rtc.utils.templateexchange.bean.ConditionBean;
import com.bosch.rtc.utils.templateexchange.bean.ConditionBeanHolder;
import com.bosch.rtc.utils.templateexchange.bean.PermissionsBeanHolder;
import com.bosch.rtc.utils.templateexchange.bean.PreConditionBean;
import com.bosch.rtc.utils.templateexchange.bean.RoleBean;
import com.bosch.rtc.utils.templateexchange.bean.RoleDefinitionBean;
import com.bosch.rtc.utils.templateexchange.bean.RoleDefinitionBeanHolder;
import com.bosch.rtc.utils.templateexchange.bean.RolePermissionBean;
import com.bosch.rtc.utils.templateexchange.bean.WorkflowPropertyBean;

/**
 * This class is used to generate the xml contents from the bean classes.
 *
 * @author pla7kor
 */
public class XMLGenerator {

  private static final String MESSAGE_UNABLE_TO_PARSE_XML_CONTENTS =
      "Unable to parse the \"%s\" bean xml contents. Please check...!!!";
  private static final String MESSAGE_INVALID_ATTRIBUTE_DEFINITION_NODE =
      "Error while parsing the Attribute Definition Node. Either parent Node is not found or parent Id is missing. Please check the process config file...!!!";
//  private static final LoggerStorage loggerStorage =
  // TemplateExchangeLoggerStorage.createWithLogger(TemplateExchangeLoggerStorage.LOGGER_TASKS);


  private XMLGenerator() {
    // Do Nothing
  }


  /**
   * This method is used to extract the contents from the bean classes and generated the xml contents.
   *
   * @param conditionBeanHolder   - the bean holder classes represents the beans , must not be null.
   * @param specificationFilePath - the specification xml file path, must not be null
   * @return ConditionXMLContentHolder instance
   * @throws ProcessConfigException - if any issue occurs while extracting the contents from the bean classes.
   */
  public static ConditionXMLContentHolder generateXMLContents(final ConditionBeanHolder conditionBeanHolder,
      final String specificationFilePath) throws ProcessConfigException {

    Set<ConditionBean> conditionBeans = conditionBeanHolder.getConditionBeans();
    Set<PreConditionBean> preConditionBeans = conditionBeanHolder.getPreConditionBeans();
    Set<String> internalAttributeIds = conditionBeanHolder.getInternalAttributeIds();

    Set<String> availableAttributeIds = getAvailableAttributeIds(conditionBeans);
    List<RoleBean> avaiableRoles = getRolesForGeneralWITypes(conditionBeans);
    List<String> statusGroupsForGeneralWITypes = getStatusGroupsForGeneralWITypes(conditionBeans);
    Set<String> missingAttributeIds =
        getMissingAttributeIds(specificationFilePath, availableAttributeIds, internalAttributeIds);

    // Here adding 5 to the size of available attribute ids to get the proper column id number
    int startingIdNumber = availableAttributeIds.size() + internalAttributeIds.size() + 5;
    Set<ConditionBean> missingConditionBeans =
        getMissingConditionBeans(missingAttributeIds, startingIdNumber, avaiableRoles, statusGroupsForGeneralWITypes);
    preConditionBeans = updateMissingPreConditionBeans(missingAttributeIds, startingIdNumber, preConditionBeans);

    // Adding the missing beans
    conditionBeans.addAll(missingConditionBeans);

    String conditionXMLContents = getConditionXMLContents(conditionBeans);
    List<String> preConditionXMLContents = new ArrayList<>();
    for (PreConditionBean preConditionBean : preConditionBeans) {
      preConditionXMLContents.add(getPreConditionXMLContents(preConditionBean));
    }

    return new ConditionXMLContentHolder(conditionXMLContents, preConditionXMLContents);
  }


  /**
   * This method is used to return the list of status group used for general workitem types.
   */
  private static List<String> getStatusGroupsForGeneralWITypes(final Set<ConditionBean> conditionBeans) {
    List<String> statusGroups = new ArrayList<>();
    ConditionBean conditionBean = conditionBeans.iterator().next();
    Set<WorkflowPropertyBean> workflowPropertyBeans = conditionBean.getWorkflowPropertyBeans();
    for (WorkflowPropertyBean workflowPropertyBean : workflowPropertyBeans) {
      if (workflowPropertyBean.getWorkItemType().equals(AttributeBehaviorConstants.GENERAL_WORKITEM_TYPE)) {
        statusGroups.add(workflowPropertyBean.getStatusGroup());
      }
    }
    return statusGroups;
  }

  /**
   * This method is used to get the roles for the general workitem types with read only permissions.
   */
  private static List<RoleBean> getRolesForGeneralWITypes(final Set<ConditionBean> conditionBeans) {
    List<RoleBean> avaiableRoles = getAvailableRoles(conditionBeans);
    List<RoleBean> newRoles = new ArrayList<>();
    for (RoleBean roleBean : avaiableRoles) {
      // Creating a new role bean because it should not modify the existing rolebean object.
      RoleBean newRoleBean = getRoleBeanForGeneralWIType(roleBean);
      newRoles.add(newRoleBean);
    }
    return newRoles;
  }

  /**
   * This method is used to return the new role bean for general workitem type.
   */
  private static RoleBean getRoleBeanForGeneralWIType(final RoleBean roleBean) {
    RoleBean newRoleBean = new RoleBean();
    newRoleBean.setId(roleBean.getId());
    newRoleBean.setPermission("r");
    return newRoleBean;
  }


  /**
   * This method is used to get the condition xml contents.
   */
  private static String getConditionXMLContents(final Set<ConditionBean> conditionBeans) throws ProcessConfigException {
    Conditions conditions = new Conditions();
    conditions.setConditionBeans(conditionBeans);
    OutputStream outputStream = getOutputStream();
    try {
      Marshaller jaxbMarshaller = getJAXBMarshaller(Conditions.class);
      jaxbMarshaller.marshal(conditions, outputStream);
    }
    catch (JAXBException e) {
      throw new ProcessConfigException(String.format(MESSAGE_UNABLE_TO_PARSE_XML_CONTENTS, "condition"));
    }
    return outputStream.toString();
  }

  /**
   * This method is used to get the precondition xml contents.
   */
  private static String getPreConditionXMLContents(final PreConditionBean preConditionBean)
      throws ProcessConfigException {
    OutputStream outputStream = getOutputStream();
    try {
      Marshaller jaxbMarshaller = getJAXBMarshaller(PreConditionBean.class);
      jaxbMarshaller.marshal(preConditionBean, outputStream);
    }
    catch (JAXBException e) {
      throw new ProcessConfigException(String.format(MESSAGE_UNABLE_TO_PARSE_XML_CONTENTS, "pre-condition"));
    }
    return outputStream.toString();
  }


  /**
   * This class is used for holding the condition beans and mainly used to marshal the objects to the desired xml tag
   * contents.
   */
  @XmlRootElement(name = "conditions")
  @XmlAccessorType(XmlAccessType.FIELD)
  private static class Conditions {

    @XmlElement(name = "condition", required = true)
    Set<ConditionBean> conditionBeans;

    /**
     * @param conditionBeans the conditionBeans to set
     */
    public void setConditionBeans(final Set<ConditionBean> conditionBeans) {
      this.conditionBeans = conditionBeans;
    }
  }

  /**
   * This method is used to get the JAXB marshaller instead with the provided class.
   */
  private static <T extends Object> Marshaller getJAXBMarshaller(final Class<T> clazz)
      throws JAXBException, PropertyException {
    JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    return jaxbMarshaller;
  }

  /**
   * This method is used to get the customized output stream which is used to write the xml contents.
   */
  private static OutputStream getOutputStream() {
    OutputStream outputStream = new OutputStream() {

      private final StringBuffer stringBuffer = new StringBuffer();

      @Override
      public void write(final int b) {
        this.stringBuffer.append((char) b);
      }

      @Override
      public String toString() {
        return this.stringBuffer.toString();
      }
    };
    return outputStream;
  }


  /**
   * This method is used to get the missing condition attribute beans.
   */
  private static Set<ConditionBean> getMissingConditionBeans(final Set<String> missingAttributeIds,
      final int startingIdNumber, final List<RoleBean> supportedRoles,
      final List<String> statusGroupsForGeneralWITypes) {
    Set<ConditionBean> newConditionBeans = new HashSet<>();
    int numberOfAttributes = startingIdNumber;
    for (String attributeId : missingAttributeIds) {
      String requiredConditionId =
          AttributeBehaviorConstants.CUSTOMIZED_REQUIRED_ATTRIBUTE_ID_PREFIX + numberOfAttributes;
      String requiredConditionName = AttributeBehaviorConstants.REQUIRED_CONDITION_PREFIX + attributeId;
      ConditionBean newRequiredConditionBean = getCreatedConditionBeanForGeneralWITypes(requiredConditionId,
          requiredConditionName, supportedRoles, statusGroupsForGeneralWITypes);

      String readOnlyConditionId =
          AttributeBehaviorConstants.CUSTOMIZED_READ_ONLY_ATTRIBUTE_ID_PREFIX + numberOfAttributes++;
      String readOnlyConditionName = AttributeBehaviorConstants.READONLY_CONDITION_PREFIX + attributeId;
      ConditionBean newReadOnlyConditionBean = getCreatedConditionBeanForGeneralWITypes(readOnlyConditionId,
          readOnlyConditionName, supportedRoles, statusGroupsForGeneralWITypes);

      newConditionBeans.add(newRequiredConditionBean);
      newConditionBeans.add(newReadOnlyConditionBean);
    }

    return newConditionBeans;
  }

  /**
   * This method is used to update the missing pre condition attribute beans.
   */
  private static Set<PreConditionBean> updateMissingPreConditionBeans(final Set<String> missingAttributeIds,
      final int startingIdNumber, final Set<PreConditionBean> preConditionBeans) {
    Set<AttributeBean> requiredAttributeBeans = new HashSet<>();
    Set<AttributeBean> readOnlyAttributeBeans = new HashSet<>();
    int numberOfAttributes = startingIdNumber;

    for (String attributeId : missingAttributeIds) {
      String customizedRequiredAttributeId =
          AttributeBehaviorConstants.CUSTOMIZED_REQUIRED_ATTRIBUTE_ID_PREFIX + numberOfAttributes;
      AttributeBean requiredAttributeBean =
          BeanCreatorUtility.getAttributeBean(attributeId, customizedRequiredAttributeId);
      requiredAttributeBeans.add(requiredAttributeBean);

      String customizedReadOnlyAttributeId =
          AttributeBehaviorConstants.CUSTOMIZED_READ_ONLY_ATTRIBUTE_ID_PREFIX + numberOfAttributes++;
      AttributeBean readOnlyAttributeBean =
          BeanCreatorUtility.getAttributeBean(attributeId, customizedReadOnlyAttributeId);
      readOnlyAttributeBeans.add(readOnlyAttributeBean);
    }

    for (PreConditionBean preConditionBean : preConditionBeans) {
      if (preConditionBean.getId().equals(AttributeBehaviorConstants.PRECONDITION_REQUIRED_ATTRIBUTES_ID)) {
        preConditionBean.getRequiredAttributes().addAll(requiredAttributeBeans);
      }
      else {
        preConditionBean.getReadOnlyAttributes().addAll(readOnlyAttributeBeans);
      }
    }

    return preConditionBeans;
  }

  /**
   * This method is used to get the missing attribute ids.
   */
  private static Set<String> getMissingAttributeIds(final String specificationFilePath,
      final Set<String> availableAttributeIds, final Set<String> internalAttributeIds) throws ProcessConfigException {
    Set<String> allAttributeIds = new HashSet<>();
    Set<String> missingAttributeIds = new HashSet<>();
    try {
      Document document = getDocumentObjectForFileContent(specificationFilePath);
      document.getDocumentElement().normalize();
      XPath xPath = XPathFactory.newInstance().newXPath();
      String expForAttributeDefinitionsTag =
          "//process-specification//project-configuration//data//configuration-data[@id='com.ibm.team.workitem.configuration.workItemTypes']//attributeDefinitions//attributeDefinition";
      NodeList nodeListOfAttributeDefinitions =
          (NodeList) xPath.compile(expForAttributeDefinitionsTag).evaluate(document, XPathConstants.NODESET);

      for (int nodeIndex = 0; nodeIndex < nodeListOfAttributeDefinitions.getLength(); nodeIndex++) {
        Node attributeDefinitionNode = nodeListOfAttributeDefinitions.item(nodeIndex);
        String attributeId = attributeDefinitionNode.getAttributes().getNamedItem("id").getTextContent();
        allAttributeIds.add(attributeId);
      }

      // If the attribute id is internal then it's skipped.
      for (String attributeId : allAttributeIds) {
        if (!internalAttributeIds.contains(attributeId) && !availableAttributeIds.contains(attributeId)) {
          missingAttributeIds.add(attributeId);
//          loggerStorage.getLogger()
//              .warn(String.format(AttributeBehaviorConstants.MESSAGE_MISSING_ATTRIBUTE_ID, attributeId));
        }
      }
    }
    catch (Exception e) {
      throw new ProcessConfigException(MESSAGE_INVALID_ATTRIBUTE_DEFINITION_NODE);
    }

    return missingAttributeIds;
  }

  /**
   * Method that reads the file content into {@link Document} object.
   */
  private static Document getDocumentObjectForFileContent(final String specificationFilePath)
      throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    return docBuilder.parse(new File(specificationFilePath));
  }


  /**
   * This method is used to iterate and find atleast one of the existing role ids from the condition beans.
   */
  private static List<RoleBean> getAvailableRoles(final Set<ConditionBean> conditionBeans) {
    ConditionBean conditionBean = conditionBeans.iterator().next();
    Set<WorkflowPropertyBean> workflowPropertyBeans = conditionBean.getWorkflowPropertyBeans();
    for (WorkflowPropertyBean workflowPropertyBean : workflowPropertyBeans) {
      if (workflowPropertyBean.getWorkItemType().equals(AttributeBehaviorConstants.GENERAL_WORKITEM_TYPE)) {
        return workflowPropertyBean.getRoles();
      }
    }
    return Collections.emptyList();
  }

  /**
   * This method is used to return the newly created ConditionBean instance.
   */
  private static ConditionBean getCreatedConditionBeanForGeneralWITypes(final String conditionId,
      final String conditionName, final List<RoleBean> supportedRoles,
      final List<String> statusGroupsForGeneralWITypes) {
    ConditionBean conditionBean = new ConditionBean();
    conditionBean.setId(conditionId);
    conditionBean.setName(conditionName);
    conditionBean.setProviderId(AttributeBehaviorConstants.CONDITION_PROVIDER_ID);

    Set<WorkflowPropertyBean> workflowPropertyBeans =
        getWorkflowPropertyBeansForGeneralWITypes(supportedRoles, statusGroupsForGeneralWITypes);
    conditionBean.setWorkflowPropertyBeans(workflowPropertyBeans);

    return conditionBean;
  }

  /**
   * This method is used to return the newly created WorkflowPropertyBeans for the general workitem types.
   */
  private static Set<WorkflowPropertyBean> getWorkflowPropertyBeansForGeneralWITypes(
      final List<RoleBean> supportedRoles, final List<String> statusGroupsForGeneralWITypes) {
    Set<WorkflowPropertyBean> workflowPropertyBeans = new HashSet<>();
    for (String statusGroup : statusGroupsForGeneralWITypes) {
      WorkflowPropertyBean workflowPropertyBean = BeanCreatorUtility.getCreatedWorkflowPropertyBean(
          AttributeBehaviorConstants.GENERAL_WORKITEM_TYPE, statusGroup, null, supportedRoles);
      workflowPropertyBeans.add(workflowPropertyBean);
    }
    return workflowPropertyBeans;
  }

  /**
   * This method is used to get the available attribute ids from the condition beans.
   */
  private static Set<String> getAvailableAttributeIds(final Set<ConditionBean> conditionBeans) {
    Set<String> attributeIds = new HashSet<>();
    for (ConditionBean conditionBean : conditionBeans) {
      String name = conditionBean.getName();
      if (name.contains(AttributeBehaviorConstants.REQUIRED_CONDITION_PREFIX)) {
        String attributeId = name.replace(AttributeBehaviorConstants.REQUIRED_CONDITION_PREFIX, "");
        attributeIds.add(attributeId);
      }
    }

    return attributeIds;
  }

  /**
   * @param roleDefinitionBeanHolder
   * @param specificationFilePath
   * @return RoleXMLContentHolder object
   * @throws ProcessConfigException
   */
  public static RoleXMLContentHolder generateXMLRoles(final RoleDefinitionBeanHolder roleDefinitionBeanHolder,
      final String specificationFilePath) throws ProcessConfigException {

    Set<RoleDefinitionBean> roleDefinitionBeans = roleDefinitionBeanHolder.getRoleXMLContents();

    String roleXMLContents = getRoleXMLContents(roleDefinitionBeans);
    return new RoleXMLContentHolder(roleXMLContents);
  }

  /**
   * Method to generate Permission XML contents
   * 
   * @param permissionsBeanHolder permissionsBeanHolder
   * @return PermissionXMLContentHolder object
   * @throws ProcessConfigException ProcessConfigException
   */
  public static PermXMLContentHolder generateXMLPermissions(final PermissionsBeanHolder permissionsBeanHolder)
      throws ProcessConfigException {

    Set<RolePermissionBean> rolePermissionBeans = permissionsBeanHolder.getPermissionXMLContents();

    String roleXMLContents = getPermXMLContents(rolePermissionBeans);
    return new PermXMLContentHolder(roleXMLContents);
  }

  /** This method to marshall the newly created xml content to RoleDefinitions class **/
  private static String getPermXMLContents(final Set<RolePermissionBean> rolePermissionBeans)
      throws ProcessConfigException {
    PermDefinitions permDefinitions = new PermDefinitions();
    permDefinitions.setRolePermissionBeans(rolePermissionBeans);
    OutputStream outputStream = getOutputStream();
    try {
      Marshaller jaxbMarshaller = getJAXBMarshaller(PermDefinitions.class);
      jaxbMarshaller.marshal(permDefinitions, outputStream);
    }
    catch (JAXBException e) {
      throw new ProcessConfigException(String.format(MESSAGE_UNABLE_TO_PARSE_XML_CONTENTS, "ELM Permission Group"));
    }
    return outputStream.toString();
  }

  /** This method to marshall the newly created xml content to RoleDefinitions class **/
  private static String getRoleXMLContents(final Set<RoleDefinitionBean> roleDefinitionBeans)
      throws ProcessConfigException {
    RoleDefinitions roleDefinitions = new RoleDefinitions();
    roleDefinitions.setRoleDefinitionBeans(roleDefinitionBeans);
    OutputStream outputStream = getOutputStream();
    try {
      Marshaller jaxbMarshaller = getJAXBMarshaller(RoleDefinitions.class);
      jaxbMarshaller.marshal(roleDefinitions, outputStream);
    }
    catch (JAXBException e) {

      throw new ProcessConfigException(String.format(MESSAGE_UNABLE_TO_PARSE_XML_CONTENTS, "condition"));
    }
    return outputStream.toString();
  }

  /** RoleDefinitions class to hold the collection of RoleDefinition beans **/
  @XmlRootElement(name = "role-definitions")
  @XmlAccessorType(XmlAccessType.FIELD)
  private static class RoleDefinitions {

    @XmlElement(name = "role-definition", required = true)
    Set<RoleDefinitionBean> roleBeans;

    /**
     * @param roleDefinitionBeans the roleDefinitionBeans to set
     */
    public void setRoleDefinitionBeans(final Set<RoleDefinitionBean> roleDefinitionBeans) {
      this.roleBeans = roleDefinitionBeans;
    }
  }

  /** PermDefinitions class to hold the collection of RolePermissionBean beans **/
  @XmlRootElement(name = "permissions")
  @XmlAccessorType(XmlAccessType.FIELD)
  private static class PermDefinitions {

    @XmlElement(name = "role", required = true)
    Set<RolePermissionBean> rolePermissionBeans;

    /**
     * @param rolePermissionBeans the rolePermissionBeans to set
     */
    public void setRolePermissionBeans(final Set<RolePermissionBean> rolePermissionBeans) {
      this.rolePermissionBeans = rolePermissionBeans;
    }
  }

}

/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.rtc.utils.templateexchange.processconfig.xml;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.ProjectArea;
import com.bosch.epc.datamodel.Request;
import com.bosch.epc.exception.ProcessConfigException;

/**
 * This class is used to merge the generated conditons and pre-conditions contents to the process config file.
 */
public class XMLMerger {

  /**
   * Logger variable
   */
  private static final Logger logger = LoggerFactory.getLogger(XMLMerger.class);

  private static final String MESSAGE_ERROR_IN_PARSING_PROCESS_CONFIG =
      "Error while parsing the processConfig file...!!!";
  private static final String MESSAGE_INVALID_NODE =
      "Error while parsing the \"%s\" Attribute Node. Either parent Node is not found or parent Id is missing. Please check the process config file...!!!";
  private static final String MESSAGE_INVALID_CONDITION_PRECONDITON_NODE =
      "Error while removing conditions/preConditions node. Please check the processConfigFile...!!!";

  private XMLMerger() {
    // Do Nothing
  }

  /**
   * This method is used to merge the condition and precondition attribute to the processConfig xml file.
   *
   * @param specificationFilePath     - path of processConfig file, , must not be null
   * @param conditionXMLContentHolder - the xml content holder instance containing the required values, must not be null
   * @throws ProcessConfigException - if any issues occurs while parsing the config file and the given xml contents.
   */
  public static void mergeXMLContentsToProcessConfig(final String specificationFilePath,
      final ConditionXMLContentHolder conditionXMLContentHolder) throws ProcessConfigException {
    try {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

      // get the specification.xml file
      Document document = docBuilder.parse(new File(specificationFilePath));
      document.setXmlStandalone(true);
      document.getDocumentElement().normalize();

      XPath xPath = XPathFactory.newInstance().newXPath();
      String conditionXMLContents = conditionXMLContentHolder.getConditionXMLContents();
      List<String> preConditionXMLContentsList = conditionXMLContentHolder.getPreConditionXMLContents();

      removeConditionAndPreConditionTagIfPresent(document, xPath);

      parseConditionAttributes(docBuilder, document, xPath, conditionXMLContents);

      for (String preConditionXMLContents : preConditionXMLContentsList) {
        parsePreconditionAttribute(docBuilder, document, xPath, preConditionXMLContents);
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      StreamResult result = new StreamResult(new File(specificationFilePath));
      DOMSource source = new DOMSource(document);
      transformer.transform(source, result);
    }
    catch (Exception e) {
      throw new ProcessConfigException(MESSAGE_ERROR_IN_PARSING_PROCESS_CONFIG, e);
    }
  }

  /**
   * This method is used to parse the processConfig file and delete condition and precondition data if exists from
   * earlier transform task.
   */
  private static void removeConditionAndPreConditionTagIfPresent(final Document doc, final XPath xPath)
      throws ProcessConfigException {
    try {

      // delete conditions tag if exists.
      String expressionForConditionsTag =
          "//process-specification//project-configuration//data//configuration-data[@id='com.ibm.team.workitem.configuration.providers']//conditions";
      NodeList nodeListOfConditionAttributes =
          (NodeList) xPath.compile(expressionForConditionsTag).evaluate(doc, XPathConstants.NODESET);

      if ((nodeListOfConditionAttributes != null) && (nodeListOfConditionAttributes.getLength() > 0)) {
        Node itemOfRequiredAttribute = nodeListOfConditionAttributes.item(0);
        Node parentNode = itemOfRequiredAttribute.getParentNode();
        parentNode.removeChild(itemOfRequiredAttribute);
      }

      // delete the required attribute data if exists.
      String expressionForRequiredAttribute =
          "//process-specification//team-configuration//behavior//role//operation[@id='com.ibm.team.workitem.operation.workItemSave']//precondition[@id='com.ibm.team.workitem.advisor.requiredAttributes']";
      NodeList nodeListOfRequiredAttributes =
          (NodeList) xPath.compile(expressionForRequiredAttribute).evaluate(doc, XPathConstants.NODESET);

      if (nodeListOfRequiredAttributes != null) {
        int length = nodeListOfRequiredAttributes.getLength();
        for (int i = 0; i < length; i++) {
          Node itemOfRequiredAttribute = nodeListOfRequiredAttributes.item(i);
          Node preconditions = itemOfRequiredAttribute.getParentNode();
          preconditions.removeChild(itemOfRequiredAttribute);
        }
      }

      // delete the readonly attribute data if exists.
      String expressionForReadOnlyAttribute =
          "//process-specification//team-configuration//behavior//role//operation[@id='com.ibm.team.workitem.operation.workItemSave']//precondition[@id='com.ibm.team.workitem.advisor.readOnlyAttributes']";
      NodeList nodeListOfReadOnlyAttribute =
          (NodeList) xPath.compile(expressionForReadOnlyAttribute).evaluate(doc, XPathConstants.NODESET);

      if (nodeListOfRequiredAttributes != null) {
        int length = nodeListOfRequiredAttributes.getLength();
        for (int i = 0; i < length; i++) {
          Node itemOfReadOnlyAttribute = nodeListOfReadOnlyAttribute.item(i);
          Node preconditions = itemOfReadOnlyAttribute.getParentNode();
          preconditions.removeChild(itemOfReadOnlyAttribute);
        }
      }
    }
    catch (Exception e) {
      throw new ProcessConfigException(MESSAGE_INVALID_CONDITION_PRECONDITON_NODE, e);
    }
  }

  /**
   * This method is used to parse the processConfig file and add the condition attributes.
   */
  private static void parseConditionAttributes(final DocumentBuilder docBuilder, final Document document,
      final XPath xPath, final String conditionXMLContents) throws ProcessConfigException {
    try {
      // read the configuration-data providers of the processConfig file.
      String expressionForProviders =
          "//process-specification//project-configuration//data//configuration-data[@id='com.ibm.team.workitem.configuration.providers']";
      NodeList nodeListForProviders =
          (NodeList) xPath.compile(expressionForProviders).evaluate(document, XPathConstants.NODESET);
      if ((nodeListForProviders != null) && (nodeListForProviders.getLength() > 0)) {
        Node itemOfProviders = nodeListForProviders.item(0);
        Document conditionsDoc = docBuilder.parse(new ByteArrayInputStream(conditionXMLContents.getBytes()));

        // append the child
        Node conditionsNode = document.importNode(conditionsDoc.getDocumentElement(), true);
        itemOfProviders.appendChild(conditionsNode);
      }
    }
    catch (Exception e) {
      throw new ProcessConfigException(String.format(MESSAGE_INVALID_NODE, "condition", e));
    }
  }

  /**
   * This method is used to parse the processConfig file and merge the preconditions attribute.
   */
  private static void parsePreconditionAttribute(final DocumentBuilder docBuilder, final Document document,
      final XPath xPath, final String preConditionXMLContents) throws ProcessConfigException {
    try {
      // read the Operation workItemSave data.
      String expressionForPreconditions =
          "//process-specification//team-configuration//behavior//role//operation[@id='com.ibm.team.workitem.operation.workItemSave']//preconditions";
      NodeList nodeListOfPreconditions =
          (NodeList) xPath.compile(expressionForPreconditions).evaluate(document, XPathConstants.NODESET);

      if (nodeListOfPreconditions != null) {

        int length = nodeListOfPreconditions.getLength();
        for (int i = 0; i < length; i++) {
          Node itemOfPreconditions = nodeListOfPreconditions.item(i);
          Document preconditionsDoc = docBuilder.parse(new ByteArrayInputStream(preConditionXMLContents.getBytes()));

          // append the child
          Node preconditionsNode = document.importNode(preconditionsDoc.getDocumentElement(), true);
          itemOfPreconditions.appendChild(preconditionsNode);
        }
      }
    }
    catch (Exception e) {
      throw new ProcessConfigException(String.format(MESSAGE_INVALID_NODE, e, "pre-condition"));
    }
  }

  /**
   * @param specificationFilePath specificiation.xml file path
   * @param roleXMLContentHolder  created/modified roles xml content from UI
   * @param request               Actual request from UI
   * @param projectArea
   * @throws ProcessConfigException Error while parsing the processConfig file
   */

  public static void mergeRolesToProcessConfig(final String specificationFilePath,
      final RoleXMLContentHolder roleXMLContentHolder, Request request, ProjectArea projectArea)
      throws ProcessConfigException {
    try {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

      // get the specification.xml file
      Document document = docBuilder.parse(new File(specificationFilePath));
      document.setXmlStandalone(true);
      document.getDocumentElement().normalize();

      XPath xPath = XPathFactory.newInstance().newXPath();
      String rolesXMLContents = roleXMLContentHolder.getRoleXMLContents();
      NodeList roleDefinitionsList = document.getElementsByTagName("role-definitions");

      if (roleDefinitionsList.getLength() == 0) {
        // <role-definitions> does not exist, create it
        Element roleDefinitions = document.createElement("role-definitions");
        document.getDocumentElement().appendChild(roleDefinitions);
      }
      parseRolesAttributes(docBuilder, document, xPath, rolesXMLContents, request, projectArea);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      StreamResult result = new StreamResult(new File(specificationFilePath));
      DOMSource source = new DOMSource(document);
      transformer.transform(source, result);

    }
    catch (Exception e) {
      throw new ProcessConfigException(MESSAGE_ERROR_IN_PARSING_PROCESS_CONFIG, e);
    }

  }


  private static void parseRolesAttributes(final DocumentBuilder docBuilder, final Document document, final XPath xPath,
      final String rolesXMLContents, Request request, ProjectArea projectArea) throws ProcessConfigException {
    try {
      // read the configuration-data providers of the processConfig file.
      String expressionForProviders = "//process-specification//role-definitions";
      NodeList nodeListForProviders =
          (NodeList) xPath.compile(expressionForProviders).evaluate(document, XPathConstants.NODESET);
      if ((nodeListForProviders != null) && (nodeListForProviders.getLength() > 0)) {
        Node itemOfProviders = nodeListForProviders.item(0);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document rolesdocument = builder.parse(new InputSource(new StringReader(rolesXMLContents)));

        // Set up XPath to query <role-definition> elements
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile("//role-definition"); // XPath query to select all <role-definition>

        // Evaluate the expression and retrieve nodes
        NodeList nodeList = (NodeList) expr.evaluate(rolesdocument, XPathConstants.NODESET);

        // Iterate over the <role-definition> elements and retrieve their attributes
        for (int i = 0; i < nodeList.getLength(); i++) {

          Element roleDefinition = (Element) nodeList.item(i);
          // Set up XPath to query <role-id> attribute
          String expression = "//role-definition[@role-id='" + roleDefinition.getAttribute("role-id") + "']";

          Node newRoleNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);
          if (newRoleNode != null && newRoleNode.hasAttributes()) {
            // if roleid is available already, we replace with new node hence we solve the update of role name,
            // cardinality and description
            logger.debug("Ignore creating new role since its already available");
            itemOfProviders.removeChild(newRoleNode);
            Node rolesNode = document.importNode(nodeList.item(i), true);
            itemOfProviders.appendChild(rolesNode);
          }
          else {
            // Add new roles only if it is not available already

            Node rolesNode = document.importNode(nodeList.item(i), true);
            itemOfProviders.appendChild(rolesNode);
            for (int var = 0; var < projectArea.getElmRoles().size(); var++) {
              ELMRole roleDefinitionObj = projectArea.getElmRoles().get(var);
              String modifiedID = roleDefinitionObj.getIdentifier();
              String expressionModifiedID = "//role-definition[@role-id='" + modifiedID + "']";

              Node modifiedRoleNode = (Node) xpath.evaluate(expressionModifiedID, document, XPathConstants.NODE);
              if (modifiedRoleNode != null && modifiedRoleNode.hasAttributes()
                  && roleDefinitionObj.getIdentifier().equalsIgnoreCase(roleDefinition.getAttribute("role-id"))) {
                itemOfProviders.removeChild(modifiedRoleNode);
              }
            }
          }


        }
      }
    }
    catch (Exception e) {
      throw new ProcessConfigException(String.format(MESSAGE_INVALID_NODE, "condition", e));
    }

  }

  /**
   * This method is used to merge the Project operation and Team operations to the processConfig xml file.
   * 
   * @param specificationFilePath      specification File Path
   * @param permissionXMLContentHolder permission XML Content Holder
   * @throws ProcessConfigException - if any issues occurs while parsing the config file and the given xml contents.
   */
  public static void mergeXMLContentsToProcessConfig(String specificationFilePath,
      final PermXMLContentHolder permissionXMLContentHolder) throws ProcessConfigException {
    try {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

      // get the specification.xml file
      Document document = docBuilder.parse(new File(specificationFilePath));
      document.setXmlStandalone(true);
      document.getDocumentElement().normalize();

      XPath xPath = XPathFactory.newInstance().newXPath();

      // get the XML contents
      String xmlContents = permissionXMLContentHolder.getOpertionXMLContents();

      // Parse permissions
      parsePermissions(docBuilder, document, xPath, xmlContents);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      StreamResult result = new StreamResult(new File(specificationFilePath));
      DOMSource source = new DOMSource(document);
      transformer.transform(source, result);

    }
    catch (Exception e) {
      throw new ProcessConfigException(MESSAGE_ERROR_IN_PARSING_PROCESS_CONFIG, e);
    }
  }

  /**
   * This method is used to parse the processConfig file and merge the preconditions attribute.
   */
  private static void parsePermissions(final DocumentBuilder docBuilder, final Document document, final XPath xPath,
      final String permsXMLContents) throws ProcessConfigException {
    try {

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document permDocument = builder.parse(new InputSource(new StringReader(permsXMLContents)));

      // Set up XPath to query <role-definition> elements
      XPathFactory xPathfactory = XPathFactory.newInstance();
      XPath xpath = xPathfactory.newXPath();
      XPathExpression expr = xpath.compile("//role"); // XPath query to select all <role-definition>

      // Evaluate the expression and retrieve nodes
      NodeList nodeList = (NodeList) expr.evaluate(permDocument, XPathConstants.NODESET);

      // Iterate over the <role-definition> elements and retrieve their attributes
      for (int i = 0; i < nodeList.getLength(); i++) {

        Element roleDefinition = (Element) nodeList.item(i);

        // Set up XPath to query <role id> attribute
        String expression = "//role[@id='" + roleDefinition.getAttribute("id") + "']";

        Node newRoleNode = (Node) xpath.evaluate(expression, permDocument, XPathConstants.NODE);
        if (newRoleNode != null && newRoleNode.hasAttributes()) {

          expr = xpath.compile("//operation"); // XPath query to select all <role-definition>

          // Evaluate the expression and retrieve nodes
          nodeList = (NodeList) expr.evaluate(permDocument, XPathConstants.NODESET);

          // Iterate over the <role-definition> elements and retrieve their attributes
          for (int p = 0; p < nodeList.getLength(); p++) {

            Element permission = (Element) nodeList.item(p);

            // Set up XPath to query <role id> and <operation id> attribute
            expression = "//role[@id='" + roleDefinition.getAttribute("id") + "']//operation[@id='"
                + permission.getAttribute("id") + "']";
            newRoleNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);

            // read the configuration-data providers of the processConfig file.
            String expForTOProviders = "//process-specification//team-configuration//permissions//role[@id='"
                + roleDefinition.getAttribute("id") + "']";
            NodeList nodeListForTOProviders =
                (NodeList) xPath.compile(expForTOProviders).evaluate(document, XPathConstants.NODESET);

            if ((nodeListForTOProviders != null) && (nodeListForTOProviders.getLength() > 0)) {
              Node itemOfProviders = nodeListForTOProviders.item(0);
              if (null != newRoleNode) {
                itemOfProviders.removeChild(newRoleNode);
              }
              Node rolesNode = document.importNode(nodeList.item(p), true);
              itemOfProviders.appendChild(rolesNode);
            }
          }

          // try for project opertion
          expr = xpath.compile("//project-operation"); // XPath query to select all <project-operation>

          // Evaluate the expression and retrieve nodes
          nodeList = (NodeList) expr.evaluate(permDocument, XPathConstants.NODESET);

          // Iterate over the <role-definition> elements and retrieve their attributes
          for (int p = 0; p < nodeList.getLength(); p++) {

            Element permission = (Element) nodeList.item(p);

            // Set up XPath to query <role id> <project-operation id> attribute
            expression = "//role[@id='" + roleDefinition.getAttribute("id") + "']//project-operation[@id='"
                + permission.getAttribute("id") + "']";
            newRoleNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);

            // read the configuration-data providers of the processConfig file.
            String expForTOProviders = "//process-specification//project-configuration//permissions//role[@id='"
                + roleDefinition.getAttribute("id") + "']";
            NodeList nodeListForTOProviders =
                (NodeList) xPath.compile(expForTOProviders).evaluate(document, XPathConstants.NODESET);

            if ((nodeListForTOProviders != null) && (nodeListForTOProviders.getLength() > 0)) {
              Node itemOfProviders = nodeListForTOProviders.item(0);
              if (null != newRoleNode) {
                itemOfProviders.removeChild(newRoleNode);
              }
              Node rolesNode = document.importNode(nodeList.item(p), true);
              itemOfProviders.appendChild(rolesNode);
            }
          }

        }
      }
    }
    catch (Exception e) {
      throw new ProcessConfigException(String.format(MESSAGE_INVALID_NODE, "Operation", e));
    }

  }

}

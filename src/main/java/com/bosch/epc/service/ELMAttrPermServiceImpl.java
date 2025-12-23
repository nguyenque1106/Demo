/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bosch.epc.constant.CommonConstant;
import com.bosch.epc.constant.StateGroupEnum;
import com.bosch.epc.constant.WITypeEnum;
import com.bosch.epc.constant.XMLConstant;
import com.bosch.epc.dao.AttrPermLockRepository;
import com.bosch.epc.dao.AttrPermRoleRepository;
import com.bosch.epc.dao.AttrPermWorkflowRepository;
import com.bosch.epc.dao.ELMAttrPermRepo;
import com.bosch.epc.dao.ELMPermRepo;
import com.bosch.epc.dao.ELMRoleRepository;
import com.bosch.epc.dao.RequestRepository;
import com.bosch.epc.datamodel.AttrPermCondition;
import com.bosch.epc.datamodel.AttrPermLock;
import com.bosch.epc.datamodel.AttrPermRole;
import com.bosch.epc.datamodel.AttrPermWorkflow;
import com.bosch.epc.datamodel.ELMPermissions;
import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.ProjectArea;
import com.bosch.epc.datamodel.Request;
import com.bosch.epc.exception.EpcException;
import com.bosch.epc.model.Attribute;
import com.bosch.epc.model.Role;
import com.bosch.epc.model.WorkFlowProperties;
import com.bosch.epc.model.XMLCondition;
import com.bosch.rtc.util.AttributeUtils;
import com.bosch.rtc.util.EntityUtils;
import com.bosch.rtc.util.TEUUtility;

/**
 * Service class to manage the Project Area data in DB
 *
 * @author QYU1HC
 */
@Service
public class ELMAttrPermServiceImpl implements ELMAttrPermService {

  private static Logger logger = LoggerFactory.getLogger(ELMAttrPermServiceImpl.class);

  @Autowired
  private ELMAttrPermRepo attrPermCondRepo;

  @Autowired
  private AttrPermWorkflowRepository attrPermWorkflowRepo;

  @Autowired
  private ELMPermRepo permRepo;

  @Autowired
  private AttrPermRoleRepository attrPermRoleRepo;

  @Autowired
  private ELMRoleService elmRoleService;

  @Autowired
  private ELMRoleRepository elmRoleRepo;


  @Autowired
  private RequestService requestService;

  @Autowired
  private RequestRepository reqRepo;

  @Autowired
  private AttrPermLockRepository attrPermLockRepo;

  /**
   * @return lis of PA
   */
  @Override
  public List<AttrPermCondition> getAll() {
    return this.attrPermCondRepo.findAll();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<AttrPermCondition> createEmptyConditions(final String specificationFilePath, final ProjectArea selectedPA)
      throws EpcException {
    logger.info("Creating empty condition list.{}", CommonConstant.MESS_START);
    List<AttrPermCondition> conditions = new ArrayList<>();
    StringBuilder errorMess = new StringBuilder();

    Document document = parseDocumentFromFile(specificationFilePath, errorMess);

    // get attributes list
    List<Attribute> attributes = fetchAttributesfromSpec(document, errorMess);
    // TODO: types can be archived.take care
    Map<String, Map<String, List<String>>> workItemTypes = fetchWITypeFromSpec(document);
    // order the general WI type to the top of map.
    Map<String, Map<String, List<String>>> workItemTypesOrder = reOrderMap(workItemTypes);

    conditions = buildAttrConditions(attributes, workItemTypesOrder, selectedPA, new ArrayList<>());
    logger.info("Creating empty condition list.{} with size:{}", CommonConstant.MESS_END, conditions.size());
    if (conditions.isEmpty()) {
      throw new EpcException(errorMess.toString());
    }
    return conditions;
  }

  /**
   * @param specificationFilePath
   * @param errorMess
   * @return
   */
  private Document parseDocumentFromFile(final String specificationFilePath, final StringBuilder errorMess)
      throws EpcException {
    File file = new File(specificationFilePath);
    if (!TEUUtility.validFile(file)) {
      errorMess.append(String.format("%s file is not valid.\n", specificationFilePath));
      logger.error(errorMess.toString());
      throw new EpcException(errorMess.toString());
    }

    Document document = AttributeUtils.parseDocumentByFile(file);
    if (document == null) {
      errorMess.append(String.format("Cannot parse document from file:%s \n", file.getAbsolutePath()));
      logger.error(errorMess.toString());
      throw new EpcException(errorMess.toString());
    }
    return document;
  }


  /**
   * @param workItemTypes
   * @return
   */
  private Map<String, Map<String, List<String>>> reOrderMap(
      final Map<String, Map<String, List<String>>> workItemTypes) {
    logger.debug("Re-order the GENERAL_WI_TYPES.");
    Map<String, Map<String, List<String>>> linkedMap = new LinkedHashMap<>();
    Map<String, List<String>> stageneralWIStateMap;
    if (!workItemTypes.containsKey(WITypeEnum.GENERAL_WI_TYPES.getName())) {
      List<String> emptyList = new ArrayList<>();
      stageneralWIStateMap = Map.of(StateGroupEnum.OPEN.getName(), emptyList, StateGroupEnum.IN_PROGRESS.getName(),
          emptyList, StateGroupEnum.CLOSED.getName(), emptyList);
    }
    else {
      stageneralWIStateMap = workItemTypes.get(WITypeEnum.GENERAL_WI_TYPES.getName());
      workItemTypes.remove(WITypeEnum.GENERAL_WI_TYPES.getName());
    }
    linkedMap.put(WITypeEnum.GENERAL_WI_TYPES.getName(), stageneralWIStateMap);
    linkedMap.putAll(workItemTypes);
    return linkedMap;
  }

  /**
   * Build the return json value with following arguments
   *
   * @param attributes
   * @param workItemTypes
   * @param xmlConditions
   * @return
   */
  private List<AttrPermCondition> buildAttrConditions(final List<Attribute> attributes,
      final Map<String, Map<String, List<String>>> workItemTypes, final ProjectArea projectArea,
      final List<XMLCondition> xmlConditions) {
    logger.debug("Building list of AttrPermCondition.{}", CommonConstant.MESS_START);
    List<AttrPermCondition> conditions = new ArrayList<>();

    List<ELMRole> elmRoles = this.elmRoleService.getELMRolesByProjectAreaId(projectArea.getId());
    logger.debug("Size of elmRoles .{}", elmRoles.size());

    for (Attribute attribute : attributes) {
      AttrPermCondition attrPermCondition = new AttrPermCondition();
      attrPermCondition.setProjectArea(projectArea);
      attrPermCondition.setAttrid(attribute.getiD());
      attrPermCondition.setAttrname(attribute.getName());

      ArrayList<AttrPermWorkflow> attrPermWorkflows = new ArrayList<>();
      for (Map.Entry<String, Map<String, List<String>>> typeEntry : workItemTypes.entrySet()) {
        for (Map.Entry<String, List<String>> stateEntry : typeEntry.getValue().entrySet()) {
          // resolution list is empty
          if (stateEntry.getValue().isEmpty()) {
            attrPermWorkflows.add(createAttrPermWorkflow(attribute.getiD(), typeEntry.getKey(), stateEntry.getKey(),
                CommonConstant.EMPTY_STR, xmlConditions, projectArea, elmRoles));
          }
          else {
            addAttrPermWorkflowToResolution(typeEntry.getKey(), stateEntry, attribute.getiD(), xmlConditions,
                projectArea, elmRoles, attrPermWorkflows);
          }
        }
      }

      attrPermCondition.setAttrPermWorkflows(attrPermWorkflows);
      conditions.add(attrPermCondition);
    }
    logger.debug("Building list of AttrPermCondition.{}", CommonConstant.MESS_START);
    return conditions;
  }

  /**
   * @param key
   * @param stateEntry
   * @param id
   * @param xmlConditions
   * @param projectArea
   * @param elmRoles
   */
  private void addAttrPermWorkflowToResolution(final String type, final Entry<String, List<String>> stateEntry,
      final String attributeId, final List<XMLCondition> xmlConditions, final ProjectArea projectArea,
      final List<ELMRole> elmRoles, final ArrayList<AttrPermWorkflow> attrPermWorkflows) {
    for (String resolution : stateEntry.getValue()) {
      // add for each resolution
      attrPermWorkflows.add(createAttrPermWorkflow(attributeId, type, stateEntry.getKey(), resolution, xmlConditions,
          projectArea, elmRoles));
    }
  }

  /**
   * @param attributeId
   * @param workItemType
   * @param state
   * @param xmlConditions
   * @param elmRoles
   * @return
   */
  private AttrPermWorkflow createAttrPermWorkflow(final String attributeId, final String workItemType,
      final String state, final String resolution, final List<XMLCondition> xmlConditions,
      final ProjectArea projectArea, final List<ELMRole> elmRoles) {
    AttrPermWorkflow attrPermWorkflow = new AttrPermWorkflow();
    try {
    attrPermWorkflow.setWitype(workItemType);
    attrPermWorkflow.setWistatusgrp(state);
    attrPermWorkflow.setWistatus(state);
    attrPermWorkflow.setWiresolution(resolution);

    // state = Open, In progress, Closed
    List<AttrPermRole> roles =
        getPermRolesByStateType(attributeId, state, workItemType, xmlConditions, projectArea, elmRoles);
    if (roles.isEmpty()) {
      roles = EntityUtils.createEmptyAttrPermRole(elmRoles);
    }

    attrPermWorkflow.setAttrPermRoles(roles);
    roles.stream().forEach(r -> r.setAttrPermWorkflow(attrPermWorkflow)); // depend on current entity design.
    return attrPermWorkflow;
  }
    catch(Exception e) {
      logger.error("Permission state not available");
    }
    return attrPermWorkflow;

  }
  /**
   * Fetch details from specification.xml
   *
   * @return Map<String, Attribute> Attribute Map with their id as key
   */
  private List<Attribute> fetchAttributesfromSpec(final Document document, final StringBuilder errorMessage)
      throws EpcException {
    logger.debug("Fetching the attributes from spec file.");
    Map<String, Attribute> attMap = new HashMap<>();
    List<Attribute> attributes = new ArrayList<>();
    try {
      // Get the list of customAttributes elements

      NodeList attributeDefinitionList = document.getElementsByTagName(XMLConstant.TAG_ATTRIBUTE_DEFINITION);
      errorMessage.append(String.format("List of %s node has size:%d", XMLConstant.TAG_ATTRIBUTE_DEFINITION,
          attributeDefinitionList.getLength())).append(CommonConstant.NEWLINE);
      logger.debug("List of {} node has size:{}", XMLConstant.TAG_ATTRIBUTE_DEFINITION,
          attributeDefinitionList.getLength());

      // Iterate through attributeDefinition nodes
      for (int i = 0; i < attributeDefinitionList.getLength(); i++) {
        Element attributeDefinitionElement = (Element) attributeDefinitionList.item(i);
        Attribute reqattribute = new Attribute();
        // Retrieve id, name, and type attributes
        String id = attributeDefinitionElement.getAttribute(XMLConstant.ID);
        reqattribute.setiD(id);
        reqattribute.setName(attributeDefinitionElement.getAttribute(XMLConstant.NAME));
        reqattribute.setType(attributeDefinitionElement.getAttribute(XMLConstant.TYPE));
        reqattribute.setCustomAttribute(false);

        attMap.put(id, reqattribute);
//        attributes.add(reqattribute);

      }
      NodeList customAttributesList = document.getElementsByTagName(XMLConstant.TAG_CUSTOM_ATTRIBUTE_DEFINITION);
      errorMessage.append(String.format("List of %s node has size:%d", XMLConstant.TAG_CUSTOM_ATTRIBUTE_DEFINITION,
          customAttributesList.getLength())).append(CommonConstant.NEWLINE);
      logger.debug("List of {} node has size:{}", XMLConstant.TAG_CUSTOM_ATTRIBUTE_DEFINITION,
          customAttributesList.getLength());
      // Loop through each customAttributes element
      for (int i = 0; i < customAttributesList.getLength(); i++) {
        Node customAttributesNode = customAttributesList.item(i);

        if (customAttributesNode.getNodeType() == Node.ELEMENT_NODE) {
          Element customAttributesElement = (Element) customAttributesNode;

          String category = customAttributesElement.getAttribute(XMLConstant.CATEGORY);

          NodeList customAttributeList = customAttributesElement.getElementsByTagName("customAttribute");

          // Loop through each customAttribute element
          for (int j = 0; j < customAttributeList.getLength(); j++) {
            Node customAttributeNode = customAttributeList.item(j);

            if (customAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
              Element customAttributeElement = (Element) customAttributeNode;
              Attribute attribute = new Attribute();
              // Get the id and name attributes
              String id = customAttributeElement.getAttribute(XMLConstant.ID);
              attribute.setiD(id);
              attribute.setName(customAttributeElement.getAttribute(XMLConstant.NAME));
              attribute.setType(customAttributeElement.getAttribute(XMLConstant.TYPE));
              attribute.setCustomAttribute(true);
              attribute.setWorkItem(category);
              attMap.put(id, attribute);
//              attributes.add(attribute);
            }
          }
        }

      }

      attributes = attMap.values().stream().collect(Collectors.toList());
    }
    catch (Exception e) {
      errorMessage.append(String.format("Issue in fetching data from specification.xml %s", e.getMessage()))
          .append(CommonConstant.NEWLINE);
      throw new EpcException(errorMessage.toString());
    }
    return attributes;
  }

  private Map<String, Map<String, List<String>>> fetchWITypeFromSpec(final Document document) {
    Map<String, Map<String, List<String>>> typesMap = new HashMap<>();
    try {
      logger.debug("Fetching WI Types from spec file.");
      Element element;
      // Get the bindings between workitem type and workflow
      NodeList typeCatBindingNodes = document.getElementsByTagName(XMLConstant.TAG_WI_CATEGORY_BINDING);
      Map<String, String> workItemCategoryBinding = new HashMap<>();
      for (int i = 0; i < typeCatBindingNodes.getLength(); i++) {
        if ((element = getElementFromNode(typeCatBindingNodes.item(i))) != null) {
          workItemCategoryBinding.put(element.getAttribute(XMLConstant.WI_CATEGORY_ID),
              element.getAttribute(XMLConstant.WORKFLOW_ID));
        }
      }


      NodeList workflowDefinitions = document.getElementsByTagName(XMLConstant.TAG_WORKFLOW_DEFINITION);
      Map<String, List<String>> actionListResolutionMap;
      Map<String, String> resolutionIdNameMap;
      // 3. Output of this map can be [wfId, StateMap<Open,[resolutionlist]>]
      Map<String, Map<String, List<String>>> wfIdStateMappingMap = new HashMap<>();
      // workitemtype, map<state, List<resolution>
      for (int i = 0; i < workflowDefinitions.getLength(); i++) {
        String wfId = getWorkflowDefId(workflowDefinitions.item(i));
        if ((wfId != null) && ((element = getWorkflowElement(workflowDefinitions.item(i))) != null)) {// workflow
          // [ResolutionId,Name]
          resolutionIdNameMap = getResolutionIdNameMap(element.getElementsByTagName(XMLConstant.TAG_RESOLUTION));
          // [Action, List<ResolutionName>]
          actionListResolutionMap =
              getActionListResolutionMap(element.getElementsByTagName(XMLConstant.TAG_ACTION), resolutionIdNameMap); // element

          // Get all <state> elements inside the workflow
          NodeList states = element.getElementsByTagName(XMLConstant.TAG_STATE);
          Map<String, List<String>> stateResMap = new HashMap<>();
          Element stateElement;
          for (int j = 0; j < states.getLength(); j++) {
            if ((stateElement = getElementFromNode(states.item(j))) != null) {
              // GHT9HC - Mapping the state with resolution list is empty as currently
              stateResMap.put(stateElement.getAttribute(XMLConstant.NAME),
                  getResolutionState(stateElement, actionListResolutionMap));
            }
          }
          wfIdStateMappingMap.put(wfId, stateResMap); // Map<workflowID, StateMap<Open,[solutionlist]>
        }
      }

      // Get all <type> nodes
      NodeList typeNodes = document.getElementsByTagName(XMLConstant.TYPE);

      for (int i = 0; i < typeNodes.getLength(); i++) {
        Element typeElement = EntityUtils.getElementFromNode(typeNodes.item(i));
        String workflowCat = typeElement.getAttribute(XMLConstant.CATEGORY);
        String workflowId = workItemCategoryBinding.get(workflowCat);
        Map<String, List<String>> stateMap = wfIdStateMappingMap.get(workflowId);
        // {TC Change Request, Map{New=[],Close=[],...} }
        typesMap.put(typeElement.getAttribute(XMLConstant.NAME), stateMap);
      }
    }
    catch (

    Exception e) {
      logger.error("Failed while executing fetchWITypeFromSpec from spec.xml. Error:{}", e.getMessage());
    }
    return typesMap;
  }


  /**
   * @param stateElement
   * @param actionListResolutionMap
   * @return
   */
  private List<String> getResolutionState(final Element stateElement,
      final Map<String, List<String>> actionListResolutionMap) {
    List<String> resolutionNames = new LinkedList<>();
    Element actionElement;
    List<String> listRes;
    for (int i = 0; i < stateElement.getChildNodes().getLength(); i++) {
      if (((actionElement = EntityUtils.getElementFromNode(stateElement.getChildNodes().item(i))) != null)
          && !(listRes = getValidResolutions(actionElement, actionListResolutionMap)).isEmpty()) {
        resolutionNames.addAll(listRes);
      }
    }

    return resolutionNames;
  }

  /**
   * @param actionElement
   * @param actionListResolutionMap
   * @return
   */
  private List<String> getValidResolutions(final Element actionElement,
      final Map<String, List<String>> actionListResolutionMap) {
    List<String> res = new ArrayList<>();
    if (actionListResolutionMap.containsKey(actionElement.getAttribute(XMLConstant.ID))) {
      res = actionListResolutionMap.get(actionElement.getAttribute(XMLConstant.ID));
    }
    return res;
  }

  /**
   * @param element
   * @return
   */
  private Map<String, String> getResolutionIdNameMap(final NodeList resolutionNodeList) {
    Map<String, String> resIdNameMap = new LinkedHashMap<>();
    Element resolutionElement;
    for (int i = 0; i < resolutionNodeList.getLength(); i++) {
      if (((resolutionElement = EntityUtils.getElementFromNode(resolutionNodeList.item(i))) != null)
          && hasPrimaryResolutionElement(resolutionElement)) {
        resIdNameMap.put(resolutionElement.getAttribute(XMLConstant.ID),
            resolutionElement.getAttribute(XMLConstant.NAME));
      }
    }
    return resIdNameMap;
  }

  /**
   * This filter is used to avoid the children resolution under action tag
   *
   * @param resolutionElement
   * @return
   */
  private boolean hasPrimaryResolutionElement(final Element resolutionElement) {
    return resolutionElement.hasAttribute(XMLConstant.ID) && resolutionElement.hasAttribute(XMLConstant.NAME)
        && resolutionElement.hasAttribute(XMLConstant.GROUP);
  }

  /**
   * Output: Mapping between {ActionId,List of ResolutionName}
   *
   * @param element
   * @param resolutionIdNameMap
   * @return
   */
  private Map<String, List<String>> getActionListResolutionMap(final NodeList actionNodeList,
      final Map<String, String> resolutionIdNameMap) {
    Map<String, List<String>> actionResolutionNameMap = new HashMap<>();
    Element actionElement;
    for (int i = 0; i < actionNodeList.getLength(); i++) {
      if (((actionElement = EntityUtils.getElementFromNode(actionNodeList.item(i))) != null)
          && hasPrimaryActionElement(actionElement)
          && (actionElement.getElementsByTagName(XMLConstant.TAG_RESOLUTION).getLength() > 0)) {
        actionResolutionNameMap.put(actionElement.getAttribute(XMLConstant.ID),
            getListResolutionName(actionElement.getElementsByTagName(XMLConstant.TAG_RESOLUTION), resolutionIdNameMap));
      }
    }
    return actionResolutionNameMap;
  }

  /**
   * Get Resolution ID under action tag and Add Resolution Name to result
   *
   * @param resUnderActionNodeList - resolution tags which are children of action tag
   * @param resolutionIdNameMap
   * @return
   */
  private List<String> getListResolutionName(final NodeList resUnderActionNodeList,
      final Map<String, String> resolutionIdNameMap) {
    List<String> resNameList = new LinkedList<>();
    Element resolutionEle;
    for (int i = 0; i < resUnderActionNodeList.getLength(); i++) {
      if (((resolutionEle = EntityUtils.getElementFromNode(resUnderActionNodeList.item(i))) != null)
          && resolutionEle.hasAttribute(XMLConstant.ID)
          && resolutionIdNameMap.containsKey(resolutionEle.getAttribute(XMLConstant.ID))) {
        resNameList.add(resolutionIdNameMap.get(resolutionEle.getAttribute(XMLConstant.ID)));
      }
    }
    return resNameList;
  }

  /**
   * @param actionElement
   * @return
   */
  private boolean hasPrimaryActionElement(final Element actionElement) {
    return actionElement.hasAttribute(XMLConstant.ID) && actionElement.hasAttribute(XMLConstant.NAME)
        && actionElement.hasAttribute(XMLConstant.TAG_STATE);
  }

  /**
   * @param item
   * @return
   */
  private String getWorkflowDefId(final Node item) {
    Element e = EntityUtils.getElementFromNode(item);
    if ((e != null) && e.hasAttribute(XMLConstant.ID)) {
      return e.getAttribute(XMLConstant.ID);
    }
    return null;
  }

  /**
   * @param item
   * @return
   */
  private Element getWorkflowElement(final Node workflowDefNode) {
    Element e;
    // get the 1st workflow tag inside workflowDefinition
    for (int i = 0; i < workflowDefNode.getChildNodes().getLength(); i++) {
      if (((e = getElementFromNode(workflowDefNode.getChildNodes().item(i))) != null)
          && e.getNodeName().equals(XMLConstant.TAG_WORKFLOW)) {
        return e;
      }
    }
    return null;
  }


  /**
   * @param item
   * @return
   */
  private Element getElementFromNode(final Node node) {
    if ((node != null) && (node.getNodeType() == Node.ELEMENT_NODE)) {
      return (Element) node;
    }
    return null;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<AttrPermCondition> createAttrPermConditions(final String specificationFilePath,
      final ProjectArea selectedPA, final List<XMLCondition> xmlConditions) throws EpcException {
    List<AttrPermCondition> conditions = new ArrayList<>();
    StringBuilder errorMess = new StringBuilder();

    Document document = parseDocumentFromFile(specificationFilePath, errorMess);
    try {
      // get attributes list
      List<Attribute> attributes = fetchAttributesfromSpec(document, errorMess);
      // [wfId, StateMap<Open,[resolutionlist]>]
      // wfId = <type name="Track Build Item"> = <workflowProperties status="In Progress" workItemType="Track Build
      // Item">
      Map<String, Map<String, List<String>>> workItemTypes = fetchWITypeFromSpec(document);
      // order the general WI type to the top of map.
      Map<String, Map<String, List<String>>> workItemTypesOrder = reOrderMap(workItemTypes);

      // mapping to result list
      conditions = buildAttrConditions(attributes, workItemTypesOrder, selectedPA, xmlConditions);
    }
    catch (Exception e) {
      throw new EpcException("Error:" + e.getMessage());
    }
    return conditions;
  }

  /**
   * @param attributeId
   * @param state
   * @param workItemType
   * @param xmlConditions
   * @param projectArea TODO
   * @return
   */
  private List<AttrPermRole> getPermRolesByStateType(final String attributeId, final String state,
      final String workItemType, final List<XMLCondition> xmlConditions, final ProjectArea projectArea,
      final List<ELMRole> elmRoles) {
    List<AttrPermRole> attributePermRoles = new ArrayList<>();
    for (XMLCondition con : xmlConditions) {
      // don't care about the attribute of condition
      // Getting the workItemType, status as filter options
      if (con.getName().equals(attributeId)) {
        attributePermRoles = con.getWorkflowProperties().stream()
            .filter(property -> equalsWITypeAndStatus(workItemType, state, property))
            .flatMap(wfp -> wfp.getRoles().stream()).map(almRole ->
            // create AttrPermRole
            EntityUtils.createAttrPermRoleForAttribute(almRole, elmRoles, projectArea)).collect(Collectors.toList());
        // break by finding list of AttrPermRole
        if (!attributePermRoles.isEmpty()) {
          break;
        }
      }
    }
    return attributePermRoles;
  }

  /**
   * @param workItemType
   * @param state
   * @param property
   * @return
   */
  private boolean equalsWITypeAndStatus(final String workItemType, final String state,
      final WorkFlowProperties property) {
    return (property.getWorkItemType() != null) && property.getWorkItemType().equals(workItemType)
        && (((property.getStatus() != null) && property.getStatus().equals(state))
            || ((property.getStatusGroup() != null) && property.getStatusGroup().equals(state)));
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<AttrPermCondition> getListAttrPermCondFromDB(final Request request, final ProjectArea selectedPA) {
    return this.getConditionWorkflowRoles(request.getId(), selectedPA.getId());
  }

  public List<AttrPermCondition> getConditionWorkflowRoles(int requestId, int projectAreaId) {
    // Step 1: fetch conditions + workflows
    List<AttrPermCondition> conditions = attrPermCondRepo.findWithWorkflows(requestId, projectAreaId);

    // Collect workflow IDs
    List<Integer> workflowIds = conditions.stream().flatMap(c -> c.getAttrPermWorkflows().stream())
        .map(AttrPermWorkflow::getId).collect(Collectors.toList());

    if (!workflowIds.isEmpty()) {
      // Step 2: load workflows + roles
      attrPermWorkflowRepo.findWithRoles(workflowIds);
      // this initializes roles inside each workflow
    }

    return conditions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AttrPermCondition getAttrPermConditionByRequestBody(final AttrPermCondition attrPermCondition) {

    List<AttrPermCondition> conditions = this.getConditionWorkflowRoles(attrPermCondition.getRequest().getId(),
        attrPermCondition.getProjectArea().getId());

    for (AttrPermCondition cond : conditions) {
      if (cond.getAttrname().equals(attrPermCondition.getAttrname())) {
        return cond; // return the first match
      }
    }

    return null; // nothing found
  }


  /**
   * @param attrPermConditionEntity
   * @param workflows
   * @param triggerTime
   * @return
   */
  @Transactional
  public boolean upsertPermRoleByEntity(final AttrPermCondition attrPermConditionEntity,
      final List<AttrPermWorkflow> workflows, final Date triggerTime) {
    logger.info("Upserting Permission/Role {}", CommonConstant.MESS_START);
    try {
      Map<String, ELMPermissions> mapPermission = getMapPermission();

      for (AttrPermWorkflow wf : workflows) {
        Optional<AttrPermWorkflow> existingWfOpt = attrPermConditionEntity.getAttrPermWorkflows().stream()
            .filter(existingWf -> existingWf.getWistatus().equalsIgnoreCase(wf.getWistatus())
                && existingWf.getWitype().equalsIgnoreCase(wf.getWitype()))
            .findFirst();

        AttrPermWorkflow targetWf = existingWfOpt.orElseGet(() -> {
          AttrPermWorkflow nw = new AttrPermWorkflow();
          nw.setWiresolution(wf.getWiresolution());
          nw.setWistatus(wf.getWistatus());
          nw.setWistatusgrp(wf.getWistatusgrp());
          nw.setWitype(wf.getWitype());
          nw.setAttrPermCondition(attrPermConditionEntity);
          attrPermConditionEntity.addWorkflow(nw);
          return nw;
        });

        if (targetWf.getAttrPermRoles() == null) {
          targetWf.setAttrPermRoles(new ArrayList<>());
        }

        List<AttrPermRole> existingRoles = new ArrayList<>(targetWf.getAttrPermRoles());

        for (AttrPermRole role : wf.getAttrPermRoles()) {
          Optional<AttrPermRole> existingRoleOpt =
              existingRoles.stream().filter(r -> r.getPaRole().getId() == role.getPaRole().getId()).findFirst();

          ELMRole managedRole = this.elmRoleRepo.findById(role.getPaRole().getId())
              .orElseThrow(() -> new IllegalArgumentException("Invalid role ID: " + role.getPaRole().getId()));

          ELMPermissions newPerm = null;
          if (role.getPermission() != null) {
            if (role.getPermission().getRef_id() != null) {
              newPerm = mapPermission.get(role.getPermission().getRef_id());
            if (role.getPermission().getRef_id() != null) {
              newPerm = mapPermission.get(role.getPermission().getRef_id());
            }
            else if (role.getPermission().getId() != 0) {
              newPerm = this.permRepo.findById(role.getPermission().getId()).orElseThrow(
                  () -> new IllegalArgumentException("Invalid permission ID: " + role.getPermission().getId()));
            }
          }

          if (existingRoleOpt.isPresent()) {
            AttrPermRole existingRole = existingRoleOpt.get();
            existingRole.setPaRole(managedRole);
            if ((newPerm != null) && !newPerm.equals(existingRole.getPermission())) {
              existingRole.setPermission(newPerm);
            }
          }
          else {
            if (newPerm != null) {
              AttrPermRole newRole = new AttrPermRole();
              newRole.setPaRole(managedRole);
              newRole.setPermission(newPerm);
              newRole.setAttrPermWorkflow(targetWf);
              targetWf.addAttrPermRole(newRole);
            }
          }
          boolean exists =
              this.attrPermLockRepo.existsForRequest(attrPermConditionEntity.getAttrname(), managedRole.getId(),
                  targetWf.getWitype(), targetWf.getWistatus(), targetWf.getWiresolution(), targetWf.getWistatusgrp());

          if (!exists) {
            AttrPermLock lock = new AttrPermLock();
            lock.setAttrStringId(attrPermConditionEntity.getAttrname());
            lock.setPaRoleId(managedRole.getId());
            lock.setWiType(targetWf.getWitype());
            lock.setWiStatus(targetWf.getWistatus());
            lock.setWiResolution(targetWf.getWiresolution());
            lock.setWiStatusGroup(targetWf.getWistatusgrp());
            lock.setIsRequestPresent(attrPermConditionEntity.getRequest().getId());

            this.attrPermLockRepo.save(lock);
          }
        }
      }

      // ✅ Save condition, cascades to workflows + roles
      this.attrPermCondRepo.save(attrPermConditionEntity);


      logger.info("Upserting Permission/Role {}", CommonConstant.MESS_END);
      return true;
      } }catch (Exception e) {
      logger.error("Error while upserting:", e);
      throw e;
    }
    return false;
  
     
  }


  /**
   * Get database Permission object from user input (r, w,...)
   *
   * @param workflows
   * @param permission
   * @param mapPermission
   * @return
   */
  private ELMPermissions getPermision(final String newPermission, final ELMPermissions permission,
      final Map<String, ELMPermissions> mapPermission) {
    ELMPermissions updatedPermission = permission;
    if (!permission.getRef_id().equals(newPermission)) {
      updatedPermission = mapPermission.get(newPermission);
      if (newPermission == null) {
        updatedPermission = permission;
      }
    }

    return updatedPermission;
  }

  /**
   * @return
   */
  private Map<String, ELMPermissions> getMapPermission() {
    List<ELMPermissions> permissions = this.permRepo.findAllByIsAttrPermission(true);

    Map<String, ELMPermissions> mapPermission = new HashMap<>();

    for (ELMPermissions perm : permissions) {
      // Use both ID and elmPermissionId as keys
      mapPermission.put(String.valueOf(perm.getId()), perm);
      mapPermission.put(perm.getRef_id(), perm);
    }

    return mapPermission;
  }


  /**
   * @param r
   * @param attrPermRoles
   * @return
   */
  private int getMappingRole(final String role, final List<AttrPermRole> attrPermRoles) {
    int index = -1;
    for (int i = 0; i < attrPermRoles.size(); i++) {
      String roleName = attrPermRoles.get(i).getPaRole().getName();
      if (roleName.equals(role)) {
        index = i;
        break; // stop at the first match
      }
    }
    return index;
  }


  /**
   * @return
   */
  private int getAttributeWorkflow(final List<AttrPermWorkflow> attrPermWorkflows, final AttrPermWorkflow wf) {
    for (int i = 0; i < attrPermWorkflows.size(); i++) {
      if (isMatchWorkflow(attrPermWorkflows.get(i), getMappingWorkflow(wf))) {
        return i;
      }
    }
    return -1;
  }

  /**
   * @param attrPermRole
   * @param roles
   * @return
   */
  private ELMPermissions findRolePerm(final AttrPermRole attrPermRole, final List<Role> roles) {
    return roles.stream()
        // Find by role name to file Permission which match with role,permissioin in roles
        .filter(r -> r.getName().equals(attrPermRole.getPaRole().getName()))
        // set value to Permission entity and return
        .map(r -> {
          attrPermRole.getPermission().setName(r.getPermission());
          return attrPermRole.getPermission();
        }).findFirst().orElse(null);
  }

  private boolean isMatchWorkflow(final AttrPermWorkflow attrPermWf, final Map<String, String> mapping) {
    boolean allMatched = true;

    for (Map.Entry<String, String> entry : mapping.entrySet()) {
      if (!isMatch(attrPermWf, entry.getKey(), entry.getValue())) {
        allMatched = false;
        break; // no need to check further
      }
    }
    return allMatched;
  }

  private static <T> boolean isMatch(final T object, final String fieldName, final Object expectedValue) {
    try {
      Field field = object.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      Object actualValue = field.get(object);
      return expectedValue.equals(actualValue);
    }
    catch (NoSuchFieldException | IllegalAccessException e) {
      // Handle the case where the field doesn't exist or is not accessible
      logger.error("Error accessing field: {} - {}", fieldName, e.getMessage());
      return false;
    }
  }

  /**
   * @param workflow
   * @return
   */
  private Map<String, String> getMappingWorkflow(final AttrPermWorkflow workflow) {
    Map<String, String> mapping = new HashMap<>();
    checkAddToMap("witype", workflow.getWitype(), mapping);
    checkAddToMap("wistatusgrp", workflow.getWistatusgrp(), mapping);
    checkAddToMap("wiresolution", workflow.getWiresolution(), mapping);
    checkAddToMap("wistatus", workflow.getWistatus(), mapping);

    return mapping;
  }

  /**
   * @param string
   * @param workitemType
   * @param mapping
   */
  private void checkAddToMap(final String key, final String value, final Map<String, String> mapping) {
    if ((value != null) && !value.isBlank() && !value.equals(CommonConstant.STRING_STR)) {
      mapping.put(key, value);
    }
  }

}
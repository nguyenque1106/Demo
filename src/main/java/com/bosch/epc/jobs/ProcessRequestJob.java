
//  Copyright (c) Robert Bosch GmbH. All rights reserved.

package com.bosch.epc.jobs;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bosch.epc.constant.CommonConstant;
import com.bosch.epc.constant.RequestStatus;
import com.bosch.epc.constant.RoleEnum;
import com.bosch.epc.datamodel.AttrPermCondition;
import com.bosch.epc.datamodel.AttrPermRole;
import com.bosch.epc.datamodel.AttrPermWorkflow;
import com.bosch.epc.datamodel.AttributeBuiltIn;
import com.bosch.epc.datamodel.ELMPermissions;
import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.ProjectArea;
import com.bosch.epc.datamodel.Request;
import com.bosch.epc.datamodel.RolePermMapping;
import com.bosch.epc.datamodel.RolePermReqtMapping;
import com.bosch.epc.model.WorkFlowProperties;
import com.bosch.epc.model.XMLCondition;
import com.bosch.epc.service.AttributeServiceImpl;
import com.bosch.epc.service.ELMPermService;
import com.bosch.epc.service.RequestService;
import com.bosch.epc.service.RolePermMappingService;
import com.bosch.rtc.util.AttributeBehaviorConstants;
import com.bosch.rtc.util.PermissionToActionConverter;
import com.bosch.rtc.util.PropertyUtils;
import com.bosch.rtc.util.TEUUtility;
import com.bosch.rtc.utils.templateexchange.bean.ActionBean;
import com.bosch.rtc.utils.templateexchange.bean.AttributeBean;
import com.bosch.rtc.utils.templateexchange.bean.ConditionBean;
import com.bosch.rtc.utils.templateexchange.bean.ConditionBeanHolder;
import com.bosch.rtc.utils.templateexchange.bean.PermissionsBeanHolder;
import com.bosch.rtc.utils.templateexchange.bean.PreConditionBean;
import com.bosch.rtc.utils.templateexchange.bean.ProjectOperationBean;
import com.bosch.rtc.utils.templateexchange.bean.RoleBean;
import com.bosch.rtc.utils.templateexchange.bean.RoleDefinitionBean;
import com.bosch.rtc.utils.templateexchange.bean.RoleDefinitionBeanHolder;
import com.bosch.rtc.utils.templateexchange.bean.RolePermissionBean;
import com.bosch.rtc.utils.templateexchange.bean.TeamOperationBean;
import com.bosch.rtc.utils.templateexchange.bean.WorkflowPropertyBean;
import com.bosch.rtc.utils.templateexchange.processconfig.xml.ConditionXMLContentHolder;
import com.bosch.rtc.utils.templateexchange.processconfig.xml.PermXMLContentHolder;
import com.bosch.rtc.utils.templateexchange.processconfig.xml.RoleXMLContentHolder;
import com.bosch.rtc.utils.templateexchange.processconfig.xml.XMLGenerator;
import com.bosch.rtc.utils.templateexchange.processconfig.xml.XMLMerger;

/**
 * The ProcessRequestJob class to process the approved requests of tool
 *
 * @author PPT4KOR
 **/
@Component
public class ProcessRequestJob {

  private static final Logger logger = LoggerFactory.getLogger(ProcessRequestJob.class);

  /**
   * EPC Request Service Service
   */
  @Autowired
  private RequestService requestService;

  /**
   * Attribute Service
   */
  @Autowired
  private AttributeServiceImpl service;

  /**
   * RolePermMappingService Service
   */
  @Autowired
  private RolePermMappingService rolePermActService;

  /**
   * RolePermMappingService Service
   */
  @Autowired
  private ELMPermService permissionServiceInterface;

  /**
   * Constant variable for Condition Type replace
   */
  private static final String CONDITION_TYPE_REPLACE_VAR = "?conditionType";

  /**
   * Constant variable for Condition Type
   */
  private static final String CONDITION_VAR = "com.ibm.team.workitem.valueproviders.CONDITION.?conditionType";

  /**
   * Constant variable for Condition Type Read Only
   */
  private static final String CONDITIONTYPE_READ_ONLY = "readOnly";

  /**
   * Constant variable for Condition Type Required
   */
  private static final String CONDITIONTYPE_REQUIRED = "required";

  /**
   * Set of Read only attribute beans
   */
  private Set<AttributeBean> rOattributeBeans;

  /**
   * Set of Required attribute beans
   */
  private Set<AttributeBean> rqOattributeBeans;


  /**
   * Scheduler Method triggered according to the cron expression. Update the WorkOn status for submitted requests Fetch
   * the WorkOn approved requests and update the specification XML file according to the changes Update the ELM Server
   * with new specification XML file Close the request
   */
  @Scheduled(cron = "${cronExpToProcessRequest}")
  public void executeJob() {

    logger.info("processRequests {}", CommonConstant.MESS_START);

    long startTime = System.currentTimeMillis();
    logger.info("Cron job Scheduler: Job running at - {}", startTime);

    List<Request> reqList = this.requestService.findAllByStatus(RequestStatus.APPROVED_SCEDULED.toString());
    if (!(reqList.isEmpty())) {
      for (Request request : reqList) {

        logger.info("Processing Request: {}", request.getId());

        // update the isRequestPResent to 0 in master table
        this.requestService.updateStagesELMMappingById(request, 0);

        // Check for attribute conditions and update if necessary
        List<AttrPermCondition> attrPermConditions = request.getAttrPermConditionMappings();

        List<RolePermReqtMapping> rolePermReqtMappings = request.getRolePermActReqMappings();

        if (!attrPermConditions.isEmpty() || !rolePermReqtMappings.isEmpty()) {
          if (!processRequest(request)) {
            logger.info("processRequest returned false, stopping further execution.");
            continue;
          }
        }
        else {
          logger.info("No people found, skipping processing.");
        }

        // update master table and clear the isRequestpresent column
        if (!rolePermReqtMappings.isEmpty()) {
          this.rolePermActService.clearLocksByReq(request);
        }

        this.requestService.updateStatusById(request.getId(), RequestStatus.CLOSED.toString());
      }
    }
    else {
      logger.info("No pending request for approval");
    }

    long endTime = System.currentTimeMillis();
    Duration duration = Duration.ofMillis(endTime - startTime);
    logger.info("Task completed in {} minute(s) {} second(s)", duration.toMinutes(),
        duration.minusMinutes(duration.toMinutes()).getSeconds());
  }


  // Composite key from WorkFlowProperties
  private static String signatureFromA(final WorkFlowProperties workFlowProperties) {
    return workFlowProperties.getWorkItemType() + "|" + workFlowProperties.getStatus() + "|" +
        workFlowProperties.getResolution() + "|" + workFlowProperties.getStatusGroup();
  }

  // Composite key from AttrPermWorkflow
  private static String signatureFromB(final AttrPermWorkflow attrPermWorkflow) {
    return attrPermWorkflow.getWitype() + "|" + attrPermWorkflow.getWistatus() + "|" +
        attrPermWorkflow.getWiresolution() + "|" + attrPermWorkflow.getWistatusgrp();
  }

  /**
   * Method to update attribute the permissions to the ELM Project Area.
   *
   * @param request ELM Attribute Permission request
   * @return true : Changes are uploaded to ELM server false : Failed to upload the changes to ELM server
   */
  public boolean processRequest(final Request request) {

    logger.info("processRequest {}", CommonConstant.MESS_START);

    Map<ProjectArea, RequestProcessData> mergedMap = new HashMap<>();

    if (null != request.getAttrPermConditionMappings()) {
      // create map with projectarea, conditions
      Map<ProjectArea, List<AttrPermCondition>> attrPermConditionMap = request.getAttrPermConditionMappings().stream()
          .collect(Collectors.groupingBy(attrMap -> attrMap.getProjectArea()));

      // Add AttrPermCondition
      attrPermConditionMap.forEach((projectArea,
          persons) -> mergedMap.computeIfAbsent(projectArea, k -> new RequestProcessData()).attrPermConditions
              .addAll(persons));
    }

    if (null != request.getRolePermActReqMappings()) {
      Map<ProjectArea, List<RolePermReqtMapping>> rolePermReqtMap = request.getRolePermActReqMappings().stream()
          .collect(Collectors.groupingBy(roleMap -> roleMap.getRole().getProjectArea()));

      // Add RolePermReqtMapping
      rolePermReqtMap.forEach((projectArea,
          employees) -> mergedMap.computeIfAbsent(projectArea, k -> new RequestProcessData()).rolePermReqMappings
              .addAll(employees));
    }

    Instant instantStart = Instant.now();

    for (ProjectArea projectArea : mergedMap.keySet()) {

      logger.info("Processing for PA : {} ", projectArea.getName());

      // Download the ELM PA template
      try {
        if (!TEUUtility.downloadTemplate(projectArea.getName())) {
          logger.error("Unable to download the template from the tool for \"{}\" Project Area...!!!",
              projectArea.getName());
          return false;
        }
      }
      catch (IOException e) {
        logger.error("TEU path is not configured properly.");
        logger.error("Unable to download the template from the tool for \"{}\" Project Area...!!!",
            projectArea.getName());
        return false;
      }

      try {

        // Push the Attribute permission changes to specification XML
        if (!mergedMap.get(projectArea).attrPermConditions.isEmpty()) {
          // find the specification path
          String specificationFilePath =
              PropertyUtils.getPropValues("TEU_TOOL_PATH_SPEC").replace("?ProjAreaName", projectArea.getName());

          ConditionXMLContentHolder conditionXMLContentHolder = XMLGenerator.generateXMLContents(
              getConditionBeanholder(mergedMap.get(projectArea).attrPermConditions, projectArea),
              specificationFilePath);

          Set<RoleDefinitionBean> roleDefinitionBeans = new HashSet<>();
          for (int i = 0; i < projectArea.getElmRoles().size(); i++) {
            ELMRole roleDefinition = projectArea.getElmRoles().get(i);
            RoleDefinitionBean roleDefinitionBean = new RoleDefinitionBean();
            roleDefinitionBean.setName(roleDefinition.getName());
            roleDefinitionBean.setRoleID(roleDefinition.getIdentifier());
            roleDefinitionBeans.add(roleDefinitionBean);
          }
          RoleDefinitionBeanHolder roleBeanHolder = new RoleDefinitionBeanHolder(roleDefinitionBeans);

          RoleXMLContentHolder roleXMLContentHolder =
              XMLGenerator.generateXMLRoles(roleBeanHolder, specificationFilePath);

          XMLMerger.mergeXMLContentsToProcessConfig(specificationFilePath, conditionXMLContentHolder);
          XMLMerger.mergeRolesToProcessConfig(specificationFilePath, roleXMLContentHolder, request, projectArea);

          logger.info("Attribute Permission changes are transferred to specification.xml file for Project Area {}",
              projectArea);
        }
        else {
          logger.info("Attribute Permission changes are not found for Project Area {}", projectArea);
        }

        // Push the ELM Role permissions changes to specification XML
        if (!mergedMap.get(projectArea).rolePermReqMappings.isEmpty()) {
          // find the specification path
          String specificationFilePath =
              PropertyUtils.getPropValues("TEU_TOOL_PATH_SPEC").replace("?ProjAreaName", projectArea.getName());

          PermXMLContentHolder permissionXMLContentHolder =
              XMLGenerator.generateXMLPermissions(generateRolePermBeanHolder(mergedMap, projectArea));

          logger.info(permissionXMLContentHolder.getOpertionXMLContents());
          XMLMerger.mergeXMLContentsToProcessConfig(specificationFilePath, permissionXMLContentHolder);

          logger.info("ELM Permission Group changes are transferred to specification.xml file for Project Area {}",
              projectArea);
        }
        else {
          logger.info("ELM Permission Group changes are not found for Project Area {}", projectArea);
        }

      }
      catch (Exception e) {
        logger.error(
            String.format("Failed to update the Template changes for EPC Request with ID: \"%s\"", request.getId()));
        return false;
      }

    }

    for (ProjectArea projectArea : mergedMap.keySet()) {
      // Upload the ELM PA template
      try {
        if (!TEUUtility.uploadTemplate(projectArea.getName())) {
          logger.error("Unable to upload the template from the tool for \"{}\" Project Area...!!!",
              projectArea.getName());
          return false;
        }
      }
      catch (IOException e) {
        logger.error(String.format("Unable to upload the template from the tool for \"%s\" Project Area...!!!",
            projectArea.getName()));
        return false;
      }
    }

    Instant instantEnd = Instant.now();
    logger.info("Elapsed Time: {}", Duration.between(instantStart, instantEnd).toSeconds());

    logger.info("processRequest {}", CommonConstant.MESS_END);
    return true;
  }

  /**
   * Method to create a Permissions Tree
   *
   * @param flatPermissions List of flat permisssions
   * @return Permission Tree
   */
  public static List<ELMPermissions> buildTree(final List<ELMPermissions> flatPermissions) {
    // Create a map for quick lookup
    Map<Integer, ELMPermissions> permissionMap =
        flatPermissions.stream().collect(Collectors.toMap(ELMPermissions::getId, p -> p));

    // Initialize child lists
    permissionMap.values().forEach(p -> p.setChildPermissions(new ArrayList<>()));

    // Build parent-child relationships
    List<ELMPermissions> rootPermissions = new ArrayList<>();
    for (ELMPermissions permission : flatPermissions) {
      if (permission.getParentId() != null) {
        ELMPermissions parent = permissionMap.get(permission.getParentId().getId());
        if (parent != null) {
          parent.getChildPermissions().add(permission);
        }
      }
      else {
        rootPermissions.add(permission);
      }
    }

    return rootPermissions;
  }

  /**
   * Find the main parent (root permission) for any given permission ID
   *
   * @param tree Permission Tree
   * @param targetPermissionId Target Permission id to find
   * @return permission if found else null
   */
  public static ELMPermissions findMainParent(final List<ELMPermissions> tree, final Integer targetPermissionId) {
    // First, find the permission in the tree
    ELMPermissions targetPermission = findPermissionInTree(tree, targetPermissionId);

    if (targetPermission == null) {
      return null; // Permission not found
    }

    // Traverse up the parent chain until we find the root
    return findRootPermission(targetPermission);
  }

  /**
   * Recursively find a permission in the entire tree
   */
  private static ELMPermissions findPermissionInTree(final List<ELMPermissions> tree, final Integer targetId) {
    for (ELMPermissions node : tree) {
      if (node.getId().equals(targetId)) {
        return node;
      }
      ELMPermissions found = findPermissionInTree(node.getChildPermissions(), targetId);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  /**
   * Method to find the Root Permission
   *
   * @param permission permission
   * @return Root Permission
   */
  private static ELMPermissions findRootPermission(final ELMPermissions permission) {
    ELMPermissions current = permission;

    // Keep going up until we find a permission with no parent
    while (current.getParentId() != null) {
      current = current.getParentId();
    }

    return current;
  }

  /**
   * Extract permission IDs from RolePermReqtMapping list
   *
   * @param roleMappings List the Role Permissions in Request
   * @return list of permission ids
   */
  public static List<Integer> getPermissionIds(final List<RolePermReqtMapping> roleMappings) {
    if ((roleMappings == null) || roleMappings.isEmpty()) {
      return new ArrayList<>();
    }

    return roleMappings.stream().map(RolePermReqtMapping::getPermission).filter(Objects::nonNull).map(ELMPermissions::getId)
        .collect(Collectors.toList());
  }

  /**
   * Mehtod to Generate the RolePermBeanHolder
   *
   * @param mergedMap mergedMap
   * @param projectArea projectArea
   * @return PermissionsBeanHolder PermissionsBeanHolder
   */
  private PermissionsBeanHolder generateRolePermBeanHolder(final Map<ProjectArea, RequestProcessData> mergedMap,
      final ProjectArea projectArea) {

    // Filter the mappings by role
    Map<ELMRole, List<RolePermReqtMapping>> rolePermReqPOsMap = mergedMap.get(projectArea).rolePermReqMappings.stream()
        .collect(Collectors.groupingBy(roleMap -> roleMap.getRole()));

    // Fetch all permissions and build a tree for multi level permission update
    List<ELMPermissions> mainPermList = buildTree(this.permissionServiceInterface.findAllByIsAttrPermission(false));

    Set<RolePermissionBean> rolePermissionBeans = new HashSet<>();
    for (Entry<ELMRole, List<RolePermReqtMapping>> map : rolePermReqPOsMap.entrySet()) {

      List<RolePermReqtMapping> rolePermReqtMappings = map.getValue();

      // get the list of permissions by role
      List<RolePermMapping> rolePermMappings = this.rolePermActService.getMappingsByRoleId(map.getKey().getId());

      // get the list of permissions to update
      List<Integer> permIdsToUpdate = getPermissionIds(rolePermReqtMappings);

      Set<ELMPermissions> mainPermToUpdate = new HashSet<>();
      for (Integer permissionId : permIdsToUpdate) {
        ELMPermissions mainParent = findMainParent(mainPermList, permissionId); // list of main permission to update
        mainPermToUpdate.add(mainParent);
      }

      RolePermissionBean roleDefinitionBean = new RolePermissionBean();
      roleDefinitionBean.setRoleID(map.getKey().getIdentifier());
      Set<ProjectOperationBean> projectOperationBeans = new HashSet<>();
      Set<TeamOperationBean> teamOperationBeans = new HashSet<>();
      for (ELMPermissions mainPerm : mainPermToUpdate) {

        // get the actions list
        Set<ActionBean> actions = PermissionToActionConverter
            .convertPermissionsToActions(mainPerm.getChildPermissions(), rolePermReqtMappings, rolePermMappings);

        if (mainPerm.isProjectConfiguration()) {
          ProjectOperationBean projectOperationBean = new ProjectOperationBean();
          projectOperationBean.setId(mainPerm.getRef_id());
          projectOperationBean.setActions(actions);
          projectOperationBeans.add(projectOperationBean);
        }
        else {
          TeamOperationBean teamOperationBean = new TeamOperationBean();
          teamOperationBean.setId(mainPerm.getRef_id());
          teamOperationBean.setActions(actions);
          teamOperationBeans.add(teamOperationBean);
        }
      }
      // Filter by parent permission
      roleDefinitionBean.setTeamOperationBeans(teamOperationBeans);
      roleDefinitionBean.setProjectOperationBeans(projectOperationBeans);
      rolePermissionBeans.add(roleDefinitionBean);
    }

    return new PermissionsBeanHolder(rolePermissionBeans);
  }

  /**
   * Method to create Condition bean holder
   *
   * @param processData
   * @param projectArea
   * @return
   * @throws IOException
   */
  private ConditionBeanHolder getConditionBeanholder(final List<AttrPermCondition> attrPermConditions,
      final ProjectArea projectArea)
      throws IOException {

    // get built in attributes defined in DB
    Iterable<AttributeBuiltIn> builtInAttributes = this.service.findAll();
    Iterator<AttributeBuiltIn> iterator = builtInAttributes.iterator();
    Set<String> internalAttributeIds = new HashSet<>();
    while (iterator.hasNext()) {
      internalAttributeIds.add(iterator.next().getStringId());
    }

    // get attribute matrix data and also check for empty data
    List<XMLCondition> xmlConditions = this.requestService.fetchPermissionsFromSpec(projectArea.getName());

    logger.debug("XMLCondition list is empty?{}", xmlConditions.isEmpty());

    this.rOattributeBeans = new HashSet<>();
    this.rqOattributeBeans = new HashSet<>();
    Collection<ConditionBean> conditionBeans = new ArrayList<>();

    // If the Attribute Matrix not integrated to Project Area then create conditions from Attribute conditions
    if (xmlConditions.isEmpty()) {
      conditionBeans = createCondsByAttrPerm(builtInAttributes, attrPermConditions);
    }
    else {

      // Filter conditions with builtin attributes and remove duplication added for readonly and required tags
      List<XMLCondition> xmlConditionsList = new ArrayList<>(xmlConditions.stream().filter(item -> {
        String suffix = item.getName().substring(item.getName().indexOf("_") + 1);
        return !internalAttributeIds.contains(suffix);
      }).collect(Collectors.toMap(item -> item.getName().substring(item.getName().indexOf("_") + 1), // key = suffix
          item -> item, // value = object
          (existing, replacement) -> existing)) // keep first one
          .values());

      // Create a map on Attribute conditions by attribute Id
      Map<String, List<AttrPermCondition>> attrPermCondByAttr =
          attrPermConditions.stream().collect(Collectors.groupingBy(AttrPermCondition::getAttrid));

      for (int i = 0; i < xmlConditionsList.size(); i++) {

        String conditionId = CONDITION_VAR + (StreamSupport.stream(builtInAttributes.spliterator(), false).count() + i);
        XMLCondition xmlCondition = xmlConditionsList.get(i);
        String attributeName = xmlCondition.getName();

        ConditionBean bean = generateROattrBeans(this.rOattributeBeans, conditionId, attributeName);

        Set<WorkflowPropertyBean> workflowPropertyBeans = new HashSet<>();

        // check if the Attribute update is available
        if (null != attrPermCondByAttr.get(attributeName)) {

          // Multiple Attribute Permission conditions found for a single attribute is not allowed
          if (attrPermCondByAttr.get(attributeName).size() > 1) {
            throw new IllegalStateException(
                "Multiple Attribute Permission conditions found for a single attribute in the request. Please check the input.");
          }

          // Build maps with composite key as signature
          Map<String, WorkFlowProperties> xmlConditionMap = xmlCondition.getWorkflowProperties().stream()
              .collect(Collectors.toMap(workflowProp -> signatureFromA(workflowProp), Function.identity()));

          Map<String, AttrPermWorkflow> attrPermCondByAttrMap =
              attrPermCondByAttr.get(attributeName).get(0).getAttrPermWorkflows().stream()
                  .collect(Collectors.toMap(attrPermWorkflow -> signatureFromB(attrPermWorkflow), Function.identity()));

          // Common (both WorkFlowProperties and AttrPermWorkflow)
          List<Pair<WorkFlowProperties, AttrPermWorkflow>> common =
              xmlConditionMap.keySet().stream().filter(attrPermCondByAttrMap::containsKey)
                  .map(key -> new Pair<>(xmlConditionMap.get(key), attrPermCondByAttrMap.get(key)))
                  .collect(Collectors.toList());

          // Merge the data
          workflowPropertyBeans.addAll(generateWrkflwPropBeans(attrPermConditions, xmlCondition, common));

          // Attributes available only in WorkFlowProperties
          List<WorkFlowProperties> onlyInXML =
              xmlConditionMap.keySet().stream().filter(k -> !attrPermCondByAttrMap.containsKey(k))
                  .map(xmlConditionMap::get).collect(Collectors.toList());
          workflowPropertyBeans.addAll(createWrkFlowBeansforXML(onlyInXML));

          // Attributes available only in AttrPermWorkflow
          List<AttrPermWorkflow> onlyInAttrPerm =
              attrPermCondByAttrMap.keySet().stream().filter(k -> !xmlConditionMap.containsKey(k))
                  .map(attrPermCondByAttrMap::get).collect(Collectors.toList());
          workflowPropertyBeans.addAll(createWrkFlowBeansforAttrPerm(onlyInAttrPerm));

        }
        else {
          workflowPropertyBeans.addAll(createWrkFlowBeansforXML(xmlCondition.getWorkflowProperties()));
        }

        bean.setWorkflowPropertyBeans(workflowPropertyBeans);
        conditionBeans.add(bean);

        bean = generateRQAttrBeans(this.rqOattributeBeans, conditionId, attributeName);
        bean.setWorkflowPropertyBeans(workflowPropertyBeans);
        conditionBeans.add(bean);
      }

    }

    Collection<PreConditionBean> preConditionBeans = new HashSet<>();
    PreConditionBean preConditionBean = new PreConditionBean();
    preConditionBean.setDescription(PropertyUtils.getPropValues("PRECON_READONLY_DESC"));
    preConditionBean.setName(PropertyUtils.getPropValues("PRECON_READONLY_NAME"));
    preConditionBean.setId(PropertyUtils.getPropValues("PRECON_READONLY_ID"));
    preConditionBean.setReadOnlyAttributes(this.rOattributeBeans);
    preConditionBean.setRequiredAttributes(null);

    preConditionBeans.add(preConditionBean);

    preConditionBean = new PreConditionBean();
    preConditionBean.setDescription(PropertyUtils.getPropValues("PRECON_REQ_DESC"));
    preConditionBean.setName(PropertyUtils.getPropValues("PRECON_REQ_NAME"));
    preConditionBean.setId(PropertyUtils.getPropValues("PRECON_REQ_ID"));
    preConditionBean.setReadOnlyAttributes(null);
    preConditionBean.setRequiredAttributes(this.rqOattributeBeans);

    preConditionBeans.add(preConditionBean);

    return new ConditionBeanHolder(conditionBeans, preConditionBeans, internalAttributeIds);
  }


  /**
   * Method to create a condition for Attribute Permission Conditions
   *
   * @param builtInAttributes List of Built in Attributes
   * @param attrPermConditions List of Attribute Permission Conditions
   * @return List of Condition Beans
   */
  private Collection<ConditionBean> createCondsByAttrPerm(final Iterable<AttributeBuiltIn> builtInAttributes,
      final List<AttrPermCondition> attrPermConditions) {
    ConditionBean bean;
    Collection<ConditionBean> conditionBeans = new ArrayList<>();
    for (int i = 0; i < attrPermConditions.size(); i++) {
      String conditionId = CONDITION_VAR + (StreamSupport.stream(builtInAttributes.spliterator(), false).count() + i);

      AttrPermCondition attrPermCondition = attrPermConditions.get(i);
      String attributeName = attrPermCondition.getAttrid();

      List<AttrPermWorkflow> onlyInAttrPerm = attrPermCondition.getAttrPermWorkflows();
      Set<WorkflowPropertyBean> workflowPropertyBeans = createWrkFlowBeansforAttrPerm(onlyInAttrPerm);

      bean = generateROattrBeans(this.rOattributeBeans, conditionId, attributeName);
      bean.setWorkflowPropertyBeans(workflowPropertyBeans);
      conditionBeans.add(bean);

      bean = generateRQAttrBeans(this.rqOattributeBeans, conditionId, attributeName);
      bean.setWorkflowPropertyBeans(workflowPropertyBeans);
      conditionBeans.add(bean);
    }
    return conditionBeans;
  }


  /**
   * Method to create a List of workflow property beans by matchin exisiting properties with requested properties change
   *
   * @param attrPermConditions List of attribute conditions
   * @param xmlCondition xmlCondition for merge
   * @param common common conditions matched
   * @return List of WorkflowPropertyBeans
   */
  private Set<WorkflowPropertyBean> generateWrkflwPropBeans(final List<AttrPermCondition> attrPermConditions,
      final XMLCondition xmlCondition, final List<Pair<WorkFlowProperties, AttrPermWorkflow>> common) {

    Set<WorkflowPropertyBean> workflowPropertyBeans = new HashSet<>();

    for (Pair<WorkFlowProperties, AttrPermWorkflow> pair : common) {
      WorkFlowProperties workFlowProperties = pair.first;
      WorkflowPropertyBean workflowPropertyBean = new WorkflowPropertyBean();
      workflowPropertyBean.setStatus(workFlowProperties.getStatus());
      workflowPropertyBean.setResolution(workFlowProperties.getResolution());
      workflowPropertyBean.setStatusGroup(workFlowProperties.getStatusGroup());
      workflowPropertyBean.setWorkItemType(workFlowProperties.getWorkItemType());

      List<RoleBean> roles = new ArrayList<>();
      for (int k = 0; k < workFlowProperties.getRoles().size(); k++) {
        RoleBean roleBean = new RoleBean();

        Optional<AttrPermRole> result =
            findMatching(attrPermConditions, xmlCondition.getName(), workFlowProperties.getWorkItemType(),
                workFlowProperties.getStatus(), workFlowProperties.getResolution(), workFlowProperties.getStatusGroup(),
                workFlowProperties.getRoles().get(k).getId().equals(RoleEnum.EVERYONE.getRoleName())
                    ? RoleEnum.EVERYONE.getIdentifier() : workFlowProperties.getRoles().get(k).getId());

        result.ifPresentOrElse(v -> logger.debug("Matching Attriute permission condition found: {}", result),
            () -> logger.debug("Matching Attriute permission condition NOT found"));

        if (result.isPresent() && !result.get().getPermission().getName().isBlank()) {
          roleBean.setId(workFlowProperties.getRoles().get(k).getId());
          roleBean.setPermission(result.isEmpty() ? workFlowProperties.getRoles().get(k).getPermission()
              : result.get().getPermission().getRef_id());
          roles.add(roleBean);
        }

      }
      if (!roles.isEmpty()) {
        workflowPropertyBean.setRoles(roles);
        workflowPropertyBeans.add(workflowPropertyBean);
      }
      else {
        logger.info("The workflow properties are removed for Type: {} Status: {} Resolution:{} StateGroup:{}",
            workFlowProperties.getWorkItemType(), workFlowProperties.getStatus(), workFlowProperties.getResolution(),
            workFlowProperties.getStatusGroup());
      }
    }
    return workflowPropertyBeans;
  }


  /**
   * Method to create condition bean for Required conditions
   *
   * @param rqOAttrBeans List of Required conditions
   * @param conditionId Condition Id
   * @param attributeName Attribute Name
   * @return
   */
  private ConditionBean generateRQAttrBeans(final Set<AttributeBean> rqOAttrBeans, final String conditionId,
      final String attributeName) {
    ConditionBean bean = new ConditionBean();
    bean.setId(conditionId.replace(CONDITION_TYPE_REPLACE_VAR, CONDITIONTYPE_REQUIRED));
    bean.setName(AttributeBehaviorConstants.REQUIRED_CONDITION_PREFIX + attributeName);
    bean.setProviderId(AttributeBehaviorConstants.CONDITION_PROVIDER_ID);

    AttributeBean attributeBean = new AttributeBean();
    attributeBean.setAttributeId(attributeName);
    attributeBean.setRuleId(conditionId.replace(CONDITION_TYPE_REPLACE_VAR, CONDITIONTYPE_REQUIRED));
    rqOAttrBeans.add(attributeBean);
    return bean;
  }


  /**
   * Method to create condition bean for ReadOnly conditions
   *
   * @param rOAttrBeans List of ReadOnly conditions
   * @param conditionId Condition Id
   * @param attributeName Attribute Name
   * @return
   */
  private ConditionBean generateROattrBeans(final Set<AttributeBean> rOAttrBeans, final String conditionId,
      final String attributeName) {
    ConditionBean bean = new ConditionBean();
    bean.setId(conditionId.replace(CONDITION_TYPE_REPLACE_VAR, CONDITIONTYPE_READ_ONLY));
    bean.setName(AttributeBehaviorConstants.READONLY_CONDITION_PREFIX + attributeName);
    bean.setProviderId(AttributeBehaviorConstants.CONDITION_PROVIDER_ID);

    AttributeBean attributeBean = new AttributeBean();
    attributeBean.setAttributeId(attributeName);
    attributeBean.setRuleId(conditionId.replace(CONDITION_TYPE_REPLACE_VAR, CONDITIONTYPE_READ_ONLY));
    rOAttrBeans.add(attributeBean);
    return bean;
  }


  /**
   * Method to create a List of workflow property beans for Attribute Permission condition workflows
   *
   * @param onlyInAttrPerm List of Attribute Permission conditions
   * @return
   */
  private Set<WorkflowPropertyBean> createWrkFlowBeansforAttrPerm(final List<AttrPermWorkflow> onlyInAttrPerm) {
    Set<WorkflowPropertyBean> workflowPropertyBeans = new HashSet<>();
    for (int j = 0; j < onlyInAttrPerm.size(); j++) {
      AttrPermWorkflow attrPermWorkflow = onlyInAttrPerm.get(j);
      WorkflowPropertyBean workflowPropertyBean = new WorkflowPropertyBean();
      workflowPropertyBean.setStatus(attrPermWorkflow.getWistatus());
      workflowPropertyBean.setResolution(attrPermWorkflow.getWiresolution());
      workflowPropertyBean.setStatusGroup(attrPermWorkflow.getWistatusgrp());
      workflowPropertyBean.setWorkItemType(attrPermWorkflow.getWitype());

      List<RoleBean> roles = new ArrayList<>();
      for (int k = 0; k < attrPermWorkflow.getAttrPermRoles().size(); k++) {
        RoleBean roleBean = new RoleBean();

        roleBean.setId(attrPermWorkflow.getAttrPermRoles().get(k).getPaRole().getIdentifier());
        roleBean.setPermission(attrPermWorkflow.getAttrPermRoles().get(k).getPermission().getRef_id());
        roles.add(roleBean);

      }
      workflowPropertyBean.setRoles(roles);
      workflowPropertyBeans.add(workflowPropertyBean);
    }
    return workflowPropertyBeans;
  }


  /**
   * Method to create a List of workflow property beans for specification condition workflows
   *
   * @param workflowPropertyBeans List of specification conditions
   * @return
   */
  private Set<WorkflowPropertyBean> createWrkFlowBeansforXML(final List<WorkFlowProperties> onlyInXML) {
    Set<WorkflowPropertyBean> workflowPropertyBeans = new HashSet<>();
    for (int j = 0; j < onlyInXML.size(); j++) {
      WorkFlowProperties workFlowProperties = onlyInXML.get(j);
      WorkflowPropertyBean workflowPropertyBean = new WorkflowPropertyBean();
      workflowPropertyBean.setStatus(workFlowProperties.getStatus());
      workflowPropertyBean.setResolution(workFlowProperties.getResolution());
      workflowPropertyBean.setStatusGroup(workFlowProperties.getStatusGroup());
      workflowPropertyBean.setWorkItemType(workFlowProperties.getWorkItemType());

      List<RoleBean> roles = new ArrayList<>();
      for (int k = 0; k < workFlowProperties.getRoles().size(); k++) {
        RoleBean roleBean = new RoleBean();

        roleBean.setId(workFlowProperties.getRoles().get(k).getId());
        roleBean.setPermission(workFlowProperties.getRoles().get(k).getPermission());
        roles.add(roleBean);
      }
      workflowPropertyBean.setRoles(roles);
      workflowPropertyBeans.add(workflowPropertyBean);
    }
    return workflowPropertyBeans;
  }


  /**
   * Method to find the matching data in attrPermConditions
   *
   * @param attrPermConditions attrPermConditions
   * @param attrName Attribute Name
   * @param wiType Work item type
   * @param wiStatus Work item status
   * @param wiresolution Work item resolution
   * @param wiStateGroup Work item state group
   * @param roleName role name
   * @return matched AttrPermRole object
   */
  public static Optional<AttrPermRole> findMatching(final List<AttrPermCondition> attrPermConditions,
      final String attrName, final String wiType, final String wiStatus, final String wiresolution,
      final String wiStateGroup, final String roleName) {

    logger.debug(
        "findMatching() search text Attribute Name {} - Work item type {} - Work item status {} - Work item resolution {} - Work item state group {} - role name {}",
        attrName, wiType, wiStatus, wiresolution, wiStateGroup, roleName);

    return attrPermConditions.stream().peek(p -> logger.debug("Checking Attribute Id {}: ", p.getAttrid()))
        .filter(p -> attrName.equals(p.getAttrid())) // match Attribute
        .flatMap(p -> p.getAttrPermWorkflows().stream())
        .peek(a -> logger.debug("Checking Workitem : {}", a.getWitype()))
        .filter(a -> wiType.equals(a.getWitype()) && Objects.equals(wiStatus, a.getWistatus()) &&
            Objects.equals(wiresolution, a.getWiresolution()) && Objects.equals(wiStateGroup, a.getWistatusgrp())) // match
        .flatMap(a -> a.getAttrPermRoles().stream())
        .peek(l -> logger.debug("Checking Role Name : {}", l.getPaRole().getName()))
        .filter(role -> roleName.equals(role.getPaRole().getName())) // match role name
        .findFirst();
  }

  /**
   * Helper class to hold pair of matching objects
   *
   * @author PPT4KOR
   * @param <ObjA> Object A
   * @param <ObjB> Object B
   */
  public static class Pair<ObjA, ObjB> {

    /**
     * First Object A
     */
    public final ObjA first;
    /**
     * Second Object B
     */
    public final ObjB second;

    /**
     * Constructor
     *
     * @param first firstObj
     * @param second secondObj
     */
    public Pair(final ObjA first, final ObjB second) {
      this.first = first;
      this.second = second;
    }
  }

  /**
   * Helper class to hold Mapping objects
   */
  class RequestProcessData {

    /**
     * Attribute Permisssion Conditions
     */
    List<AttrPermCondition> attrPermConditions = new ArrayList<>();

    /**
     * Role Permisssion Request Mappings
     */
    List<RolePermReqtMapping> rolePermReqMappings = new ArrayList<>();
  }

}

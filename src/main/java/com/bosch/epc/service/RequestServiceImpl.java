/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.bosch.epc.service;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bosch.epc.constant.CommonConstant;
import com.bosch.epc.constant.JsonConstant;
import com.bosch.epc.constant.RequestStatus;
import com.bosch.epc.dao.ELMPermRepo;
import com.bosch.epc.dao.ELMRoleRepository;
import com.bosch.epc.dao.ProjectAreaRepository;
import com.bosch.epc.dao.RequestDAO;
import com.bosch.epc.dao.RequestRepository;
import com.bosch.epc.dao.RolePermMappingRepository;
import com.bosch.epc.dao.RolePermReqtMappingRepository;
import com.bosch.epc.dao.StagesRepository;
import com.bosch.epc.dao.StagesRoleELMRoleRepository;
import com.bosch.epc.dao.StagesRoleELMRoleReqRepository;
import com.bosch.epc.dao.StagesRolePARepository;
import com.bosch.epc.datamodel.AttrPermCondition;
import com.bosch.epc.datamodel.ELMPermissions;
import com.bosch.epc.datamodel.ELMRole;
import com.bosch.epc.datamodel.PARoleRequest;
import com.bosch.epc.datamodel.PermELMRoleReqMapping;
import com.bosch.epc.datamodel.ProjectArea;
import com.bosch.epc.datamodel.Request;
import com.bosch.epc.datamodel.RolePermMapping;
import com.bosch.epc.datamodel.RolePermMappingId;
import com.bosch.epc.datamodel.RolePermReqtMapping;
import com.bosch.epc.datamodel.StagesRole;
import com.bosch.epc.datamodel.StagesRoleELMRoleMapping;
import com.bosch.epc.datamodel.StagesRoleELMRoleMappingId;
import com.bosch.epc.datamodel.StagesRoleELMRoleReqMapping;
import com.bosch.epc.datamodel.StagesRoleELMRoleReqMappingId;
import com.bosch.epc.datamodel.StagesRolePA;
import com.bosch.epc.datamodel.StagesRolePARequest;
import com.bosch.epc.datamodel.WORequest;
import com.bosch.epc.model.Attribute;
import com.bosch.epc.model.Conditions;
import com.bosch.epc.model.Resolution;
import com.bosch.epc.model.StagesRolePAID;
import com.bosch.epc.model.State;
import com.bosch.epc.model.WorkFlowDefinition;
import com.bosch.epc.model.WorkItemType;
import com.bosch.epc.model.WorkOnRequest;
import com.bosch.epc.model.XMLCondition;
import com.bosch.rtc.util.DuplicateEntryException;
import com.bosch.rtc.util.Operation;
import com.bosch.rtc.util.PropertyUtils;
import com.bosch.rtc.util.UserUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author QYU1HC
 */
@Service
public class RequestServiceImpl implements RequestService {

  private static Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private RequestDAO dao;

  @Autowired
  private RequestRepository requestRepo;
  @Autowired
  private ELMRoleRepository paRoleRepo;
  @Autowired
  private StagesRoleELMRoleReqRepository stagesELMRoleReqRepo;
  @Autowired
  private StagesRoleELMRoleRepository stagesELMRoleRepo;
  @Autowired
  private StagesRolePARepository stagesPARepo;
  @Autowired
  private final StagesRepository stagesRepo;

  @Autowired
  private ELMPermRepo permRepo;

  @Autowired
  private RolePermMappingRepository rolePermRepo;

  @Autowired
  private RolePermReqtMappingRepository rolePermReqRepo;

  @Autowired
  private ProjectAreaRepository projectAreaRepo;

  @Autowired
  private ELMAttrPermServiceImpl attrPermService;


  public RequestServiceImpl(final StagesRepository stagesRepo) {
    this.stagesRepo = stagesRepo;
  }

  @Autowired
  private ELMRoleRepository elmRoleRepo;

  @Override
  public boolean checkExist(final Integer requestId) {
    return this.requestRepository.existsById(requestId);
  }

  @Override
  public Request findDataByRequestId(final Integer requestId) {
    Request request = requestRepository.findRequestById(requestId);
    if (request == null) {
      throw new EntityNotFoundException("Request not found for ID: " + requestId);
    }
    return request;
  }

//Read the value from property file
  @Value("${TEU_TOOL_PATH_SPEC}")
  private String specFile;
  @Value("${Success}")
  private String success;
  @Value("${defaultChangesetURL}")
  private String defaultChangesetURL;

  /**
   * {@inheritDoc}
   *
   * @return status of the workon request
   * @throws IOException
   */
  @Override
  public String getRequestStatus(final String id) throws IOException {
    return this.dao.getRequestStatus(id);
  }


  /**
   * {@inheritDoc}
   *
   * @param request workonrequest to create request
   */
  @Override
  public String createWORequest(final int requestId, final String userId) {
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      WorkOnRequest woRequest =
          objectMapper.readValue(ResourceUtils.getFile("classpath:workonRequest.json"), WorkOnRequest.class);


      if (woRequest != null) {

        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.setSerializationInclusion(Include.NON_NULL);

        String jsonString = jsonMapper.writeValueAsString(woRequest);
        ObjectNode jsonNode = (ObjectNode) jsonMapper.readTree(jsonString);

        // Replace the value of a specific key

        if (jsonNode.has("data")) {
          ObjectNode dataNode = (ObjectNode) jsonNode.get("data");

          if (dataNode.has("rbga.field.description")) {
            dataNode.put("rbga.field.description", this.defaultChangesetURL + requestId);
          }
          Iterator<Map.Entry<String, JsonNode>> fields = dataNode.fields();
          while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            // Check if this is one of the approver fields
            if (value.has(JsonConstant.APPROVER)) {
              ArrayNode approversArray = (ArrayNode) value.get(JsonConstant.APPROVER);
              for (JsonNode approverNode : approversArray) {
                if ((approverNode instanceof ObjectNode) && approverNode.has(JsonConstant.USERID)) {
                  ((ObjectNode) approverNode).put(JsonConstant.USERID, userId); // your new userId
                }
              }
            }
          }

        }

        String updatedJsonString = jsonMapper.writeValueAsString(jsonNode);
        logger.info(jsonString);
        WORequest wo = this.dao.createEPCRequest(updatedJsonString);
        return wo.getWorkonId();
      }
      logger.error("Given request is empty or null");
    }

    catch (IOException e) {
      return ("IO EXception" + e.getLocalizedMessage());
    }
    catch (Exception e) {
      logger.error("Exception in Creating WorkON request" + e);

    }
    return null;

  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void addRequestDetailstoDB(final WORequest woRequest) {
    this.dao.add(woRequest);

  }

  /**
   * Fetch details from specification.xml
   *
   * @return Map<String, Attribute> Attribute Map with their id as key
   */
  @Override
  public Map<String, Attribute> fetchAttributesfromSpec() {
    Map<String, Attribute> attMap = new HashMap<>();
    Set<String> idSet = new HashSet<>();
    try {
      // Load and parse the XML file
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(this.specFile);

      document.getDocumentElement().normalize();

      // Get the list of customAttributes elements

      NodeList attributeDefinitionList = document.getElementsByTagName("attributeDefinition");


      // Iterate through attributeDefinition nodes
      for (int i = 0; i < attributeDefinitionList.getLength(); i++) {
        Element attributeDefinitionElement = (Element) attributeDefinitionList.item(i);
        Attribute reqattribute = new Attribute();
        // Retrieve id, name, and type attributes
        String id = attributeDefinitionElement.getAttribute("id");
        reqattribute.setiD(id);
        reqattribute.setName(attributeDefinitionElement.getAttribute("name"));
        reqattribute.setType(attributeDefinitionElement.getAttribute("type"));
        reqattribute.setCustomAttribute(false);

        attMap.put(id, reqattribute);

        idSet.add(id);
      }
      NodeList customAttributesList = document.getElementsByTagName("customAttributes");

      // Loop through each customAttributes element
      for (int i = 0; i < customAttributesList.getLength(); i++) {
        Node customAttributesNode = customAttributesList.item(i);

        if (customAttributesNode.getNodeType() == Node.ELEMENT_NODE) {
          Element customAttributesElement = (Element) customAttributesNode;

          String category = customAttributesElement.getAttribute("category");

          NodeList customAttributeList = customAttributesElement.getElementsByTagName("customAttribute");

          // Loop through each customAttribute element
          for (int j = 0; j < customAttributeList.getLength(); j++) {
            Node customAttributeNode = customAttributeList.item(j);

            if (customAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
              Element customAttributeElement = (Element) customAttributeNode;
              Attribute attribute = new Attribute();
              // Get the id and name attributes
              String id = customAttributeElement.getAttribute("id");
              attribute.setiD(id);
              attribute.setName(customAttributeElement.getAttribute("name"));
              attribute.setType(customAttributeElement.getAttribute("type"));
              attribute.setCustomAttribute(true);
              attribute.setWorkItem(category);
              attMap.put(id, attribute);
              idSet.add(id);

            }
          }
        }

      }
      List<String> uniqueIdList = new ArrayList<>(attMap.keySet());

      for (String uniqueId : attMap.keySet()) {
        logger.info(attMap.get(uniqueId).getiD());
        logger.info(attMap.get(uniqueId).getName());
        logger.info(attMap.get(uniqueId).getType());
        logger.info(attMap.get(uniqueId).getWorkItem());
      }

    }
    catch (Exception e) {
      logger.error("Issue in fetching data from specification.xml %s", e);

    }
    return attMap;
  }


  /**
   * method to read conditions and workflow properties from specification.xml
   *
   * @return List<Condition> objects
   */
  @Override
  public List<XMLCondition> fetchPermissionsFromSpec() {
    List<XMLCondition> conditionList = new ArrayList<>();
    try {
      // Parse the XML file into a DOM
      File file = new File(this.specFile);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(file);

      // Use XPath to locate the <conditions> element
      XPathFactory xpathFactory = XPathFactory.newInstance();
      XPath xpath = xpathFactory.newXPath();
      Node conditionsNode = (Node) xpath.evaluate("//conditions", doc, XPathConstants.NODE);

      if (conditionsNode != null) {
        // Create a JAXB context for the Conditions class
        JAXBContext jaxbContext = JAXBContext.newInstance(Conditions.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // Unmarshal the found <conditions> node
        Conditions conditions = (Conditions) unmarshaller.unmarshal(conditionsNode);

        // Process the conditions
        if (conditions != null) {
          conditionList = conditions.getConditionList();
          for (XMLCondition element : conditionList) {
            element.setName(element.getName().split("_")[1]);
            element.setType(element.getName().split("_")[0]);
          }
        }
      }
      else {
        logger.error("No <conditions> element found.");
      }
    }
    catch (Exception e) {
      logger.error("Failed from fetching data from spec.xml");
    }
    return conditionList;


  }

  @Override
  public List<WorkItemType> fetchWITypeFromSpec() {
    List<WorkItemType> typeList = new ArrayList<>();
    try {
      // Path to the XML file
      File xmlFile = new File(this.specFile);

      // Create a DocumentBuilderFactory and DocumentBuilder to parse the XML
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();

      // Parse the XML file
      Document document = builder.parse(xmlFile);
      document.getDocumentElement().normalize();

      // Get all <type> nodes
      NodeList typeNodes = document.getElementsByTagName("type");

      // List to store the attributes or contents of <type> tags


      // Iterate through the <type> nodes
      for (int i = 0; i < typeNodes.getLength(); i++) {
        Node node = typeNodes.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element typeElement = (Element) node;

          // Extract the 'id' attribute from each <type> tag (or any other attribute)
          String id = typeElement.getAttribute("id");
          WorkItemType wiType = new WorkItemType();
          wiType.setCategory(typeElement.getAttribute("category"));
          wiType.setName(typeElement.getAttribute("name"));
          wiType.setId(typeElement.getAttribute("id"));
          // Add the extracted attribute to the list
          typeList.add(wiType);
        }
      }


    }
    catch (Exception e) {
      logger.error("Failed from fetching data from spec.xml");
    }
    return typeList;


  }


  @Override
  public List<WorkFlowDefinition> fetchStateResolutions() {
    List<WorkFlowDefinition> workflowList = new ArrayList<>();
    try {
      // Path to your XML file
      File xmlFile = new File(this.specFile);

      // Set up the document parser
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(xmlFile);
      document.getDocumentElement().normalize();

      NodeList workflowDefinitions = document.getElementsByTagName("workflowDefinition");
      for (int i = 0; i < workflowDefinitions.getLength(); i++) {
        WorkFlowDefinition wfDefinition = new WorkFlowDefinition();
        Node wfNode = workflowDefinitions.item(i);
        if (wfNode.getNodeType() == Node.ELEMENT_NODE) {
          Element wfElement = (Element) wfNode;

          // Check if this is the workflowDefinition you're looking for
          String wfId = wfElement.getAttribute("id");
          wfDefinition.setId(wfId);
          // Get all <state> elements inside the workflow
          NodeList states = wfElement.getElementsByTagName("state");
          List<State> stateInfoList = new ArrayList<>();

          for (int j = 0; j < states.getLength(); j++) {
            Node stateNode = states.item(j);
            if (stateNode.getNodeType() == Node.ELEMENT_NODE) {
              Element stateElement = (Element) stateNode;
              State state = new State();
              // Extract state attributes
              state.setId(stateElement.getAttribute("id"));
              state.setName(stateElement.getAttribute("name"));
              state.setGroup(stateElement.getAttribute("group"));
              state.setShowResolution(stateElement.getAttribute("showResolution"));
              stateInfoList.add(state);
            }
          }
          wfDefinition.setStateList(stateInfoList);

          // Get all <resolution> elements inside the workflow
          NodeList resolutions = wfElement.getElementsByTagName("resolution");
          List<Resolution> resolutionInfoList = new ArrayList<>();

          for (int k = 0; k < resolutions.getLength(); k++) {
            Node resolutionNode = resolutions.item(k);
            Resolution resolution = new Resolution();
            if (resolutionNode.getNodeType() == Node.ELEMENT_NODE) {
              Element resolutionElement = (Element) resolutionNode;

              // Extract resolution attributes
              resolution.setId(resolutionElement.getAttribute("id"));
              resolution.setName(resolutionElement.getAttribute("name"));
              resolution.setGroup(resolutionElement.getAttribute("group"));

              resolutionInfoList.add(resolution);
            }
          }
          wfDefinition.setResolutionList(resolutionInfoList);
        }
        workflowList.add(wfDefinition);
      }


    }
    catch (Exception e) {
      logger.error("Failed from fetching data from spec.xml");
    }
    return workflowList;

  }


  /**
   * {@inheritDoc}
   */
  @Transactional
  public Request createRequestEntry(final String changeset_name, final String description) {
    Request request = new Request();
    request.setChangeset_name(changeset_name);
    request.setCreatedBy(UserUtils.getNTIDBySSO());
    request.setCreationDate(new Date(System.currentTimeMillis()));
    request.setStatus(RequestStatus.DRAFT.toString());
    request.setDescription(description);
    return this.requestRepo.save(request);
  }

  /**
   * @param requestId
   * @return
   */
  @Transactional
  public Request createWorkON(final int requestId, final String userId) {
    Request request = this.requestRepo.findRequestById(requestId);
    String workonid = createWORequest(requestId, userId);
    if (workonid != null) {
      request.setWorkonid(workonid);
    }
    request.setStatus(RequestStatus.PENDING_FOR_APPROVAL.toString());

    return this.requestRepo.save(request);

  }

  /**
   * @param request
   * @return
   */
  @Transactional
  public Request updateRequestEntry(final Request request) {

    List<PermELMRoleReqMapping> mappings = new ArrayList<>();

    // Add StagesRoleELMRoleReqMappings
    List<StagesRoleELMRoleReqMapping> stagesELMRoleMappings = new ArrayList<>();
    if (request.getStagesRoleELMRoleReqMapping() != null) {
      for (StagesRoleELMRoleReqMapping mappingDto : request.getStagesRoleELMRoleReqMapping()) {
        ELMRole elmRole = this.elmRoleRepo.findById(mappingDto.getElmRole().getId()).orElseThrow(
            () -> new EntityNotFoundException("ELMRole not found with id: " + mappingDto.getElmRole().getId()));

        StagesRole stagesRole = this.stagesRepo.findById(mappingDto.getStagesRole().getId()).orElseThrow(
            () -> new EntityNotFoundException("StagesRole not found with id: " + mappingDto.getStagesRole().getId()));
        StagesRoleELMRoleMappingId stagesELMRoleMappingId = new StagesRoleELMRoleMappingId();
        stagesELMRoleMappingId.setRoleId(mappingDto.getElmRole().getId());
        stagesELMRoleMappingId.setStagesId(mappingDto.getStagesRole().getId());
        Optional<StagesRoleELMRoleMapping> existingStagesELMRoleMapping =
            this.stagesELMRoleRepo.findById(stagesELMRoleMappingId);

        // for adding new mapping
        if (Operation.ADD.toString().equalsIgnoreCase(mappingDto.getOperation())
            && !(existingStagesELMRoleMapping.isPresent())) {
          StagesRoleELMRoleReqMapping mapping = new StagesRoleELMRoleReqMapping();
          mapping.setElmRole(elmRole);
          mapping.setStagesRole(stagesRole);
          mapping.setId(mappingDto.getId());
          mapping.setRequest(request);
          mapping.setOperation(mappingDto.getOperation());

          stagesELMRoleMappings.add(mapping);
          this.stagesELMRoleRepo.save(mappingDto.getStagesRole().getId(), mappingDto.getElmRole().getId(),
              request.getId());

        }
        // for removing mapping
        else if (Operation.REMOVE.toString().equalsIgnoreCase(mappingDto.getOperation())
            && existingStagesELMRoleMapping.isPresent()) {
          StagesRoleELMRoleReqMapping mapping = new StagesRoleELMRoleReqMapping();
          mapping.setElmRole(elmRole);
          mapping.setStagesRole(stagesRole);
          mapping.setId(mappingDto.getId());
          mapping.setRequest(request);
          mapping.setOperation(mappingDto.getOperation());
          stagesELMRoleMappings.add(mapping);

          this.stagesELMRoleRepo.updateRequestById(request.getId(), mappingDto.getElmRole().getId(),
              mappingDto.getStagesRole().getId());


        }
      }
      // for Undo
      List<StagesRoleELMRoleReqMapping> mappingList = this.stagesELMRoleReqRepo.findById_RequestId(request.getId());
      if (request.getStagesRoleELMRoleReqMapping().size() != mappingList.size()) {
        Set<String> currentMappingKeySet = new HashSet<>();
        List<StagesRoleELMRoleReqMapping> undoMappings = new ArrayList<>();
        List<StagesRoleELMRoleReqMapping> missingMappings = new ArrayList<>();
        for (StagesRoleELMRoleReqMapping currentMapping : request.getStagesRoleELMRoleReqMapping()) {
          currentMappingKeySet.add(currentMapping.getElmRole().getId() + "_" + currentMapping.getStagesRole().getId());

        }
        for (StagesRoleELMRoleReqMapping existingMapping : mappingList) {
          String key = existingMapping.getElmRole().getId() + "_" + existingMapping.getStagesRole().getId();
          if (!currentMappingKeySet.contains(key)) {
            missingMappings.add(existingMapping);
          }
        }
        for (StagesRoleELMRoleReqMapping missingMapping : missingMappings) {
          StagesRoleELMRoleMappingId stagesRoleELMRoleMappingId = new StagesRoleELMRoleMappingId();
          StagesRoleELMRoleReqMappingId stagesRoleELMRoleReqMappingId = new StagesRoleELMRoleReqMappingId();
          stagesRoleELMRoleMappingId.setRoleId(missingMapping.getElmRole().getId());
          stagesRoleELMRoleMappingId.setStagesId(missingMapping.getStagesRole().getId());
          stagesRoleELMRoleReqMappingId.setElmRoleId(missingMapping.getElmRole().getId());
          stagesRoleELMRoleReqMappingId.setStagesRoleId(missingMapping.getStagesRole().getId());
          stagesRoleELMRoleReqMappingId.setRequestId(request.getId());
          // remove for both tables

          if (Operation.ADD.toString().equalsIgnoreCase(missingMapping.getOperation())) {
            this.stagesELMRoleRepo.deleteById(stagesRoleELMRoleMappingId);
            this.stagesELMRoleReqRepo.deleteById(stagesRoleELMRoleReqMappingId);
          }
          else if (Operation.REMOVE.toString().equalsIgnoreCase(missingMapping.getOperation())) {
            this.stagesELMRoleRepo.updateRequestById(0, stagesRoleELMRoleMappingId.getRoleId(),
                stagesRoleELMRoleMappingId.getStagesId());
            this.stagesELMRoleReqRepo.deleteById(stagesRoleELMRoleReqMappingId);
          }
        }

      }
      request.setStagesRoleELMRoleReqMapping(stagesELMRoleMappings);
    }


    // Add StagesRolePARoleReqMappings
    List<StagesRolePARequest> stagesPAMappings = new ArrayList<>();
    if (request.getStagesRolePAReqMapping() != null) {
      for (StagesRolePARequest mappingDto : request.getStagesRolePAReqMapping()) {
        ProjectArea projectArea = this.projectAreaRepo.findById(mappingDto.getProjectArea().getId())
            .orElseThrow(() -> new RuntimeException("Project Area not found: " + mappingDto.getProjectArea().getId()));

        StagesRole stagesRole = this.stagesRepo.findById(mappingDto.getStagesRole().getId()).orElseThrow(
            () -> new EntityNotFoundException("StagesRole not found with id: " + mappingDto.getStagesRole().getId()));

        StagesRolePAID stagesPAMappingId = new StagesRolePAID();
        stagesPAMappingId.setStagesRoleId(mappingDto.getStagesRole().getId());
        stagesPAMappingId.setProjectAreaId(mappingDto.getProjectArea().getId());

        Optional<StagesRolePA> existingStagesPAReq = this.stagesPARepo.findById(stagesPAMappingId);

        if (existingStagesPAReq.isPresent()) {
          throw new DuplicateEntryException("Duplicate entry found for stagesPAId: "
              + mappingDto.getStagesRole().getId() + " and projectAreaId: " + mappingDto.getProjectArea().getId());
        }

        StagesRolePARequest mapping = new StagesRolePARequest();
        mapping.setProjectArea(projectArea);
        mapping.setStagesRole(stagesRole);
        mapping.setId(mappingDto.getId());
        mapping.setRequest(request);
        mapping.setOperation(mappingDto.getOperation());

        stagesPAMappings.add(mapping);
      }

      request.setStagesRolePAReqMapping(stagesPAMappings);
    }
    // Add RolePermission mapping
    List<RolePermReqtMapping> rolePermActReqMappings = new ArrayList<>();
    if (request.getRolePermActReqMappings() != null) {
      for (RolePermReqtMapping mappingDto : request.getRolePermActReqMappings()) {
        ELMRole elmRole = this.elmRoleRepo.findById(mappingDto.getRole().getId()).orElseThrow(
            () -> new EntityNotFoundException("ELMRole not found with id: " + mappingDto.getRole().getId()));

        ELMPermissions permission = this.permRepo.findById(mappingDto.getPermission().getId()).orElseThrow(
            () -> new EntityNotFoundException("Permission not found with id: " + mappingDto.getPermission().getId()));

        // Adding this to avoid PropertyAccessException(ActionId)
        RolePermMappingId rolePermActMapId = new RolePermMappingId();
        rolePermActMapId.setPermissionId(mappingDto.getPermission().getId());
        rolePermActMapId.setRoleId(mappingDto.getRole().getId());
        Optional<RolePermMapping> existingRolePermMapping = this.rolePermRepo.findById(rolePermActMapId);


        if (existingRolePermMapping.isPresent()) {
          updateRolePermMapping(mappingDto);
        }


        RolePermReqtMapping mapping = new RolePermReqtMapping(elmRole, permission, request, mappingDto.isPermitted());
        rolePermActReqMappings.add(mapping);
      }

      request.setRolePermActReqMappings(rolePermActReqMappings);
    }
    // Add PARoleRequests
    if (request.getPaRoleReqs() != null) {
      for (PARoleRequest paRoleReq : request.getPaRoleReqs()) {
        // Set the parent reference before saving
        paRoleReq.setRequest(request);
        // Check if the entry already exists based on identifier and projectAreaId
        Optional<PARoleRequest> existingPaRoleReq = this.paRoleRepo
            .findByIdentifierAndProjectAreaId(paRoleReq.getIdentifier(), paRoleReq.getProjectArea().getId());

        if (existingPaRoleReq.isPresent()) {
          throw new DuplicateEntryException("Duplicate entry found for identifier: " + paRoleReq.getIdentifier()
              + " and projectAreaId: " + paRoleReq.getProjectArea().getId());

        }
      }
    }
    Request reqDB =
        this.requestRepo.findById(request.getId()).orElseThrow(() -> new RuntimeException("Request not found"));
    reqDB.setModifiedDate(new Date(System.currentTimeMillis()));
    reqDB.setModifiedBy(UserUtils.getNTIDBySSO());
    reqDB.setPaRoleReqs(request.getPaRoleReqs());
    reqDB.setStagesRoleELMRoleReqMapping(request.getStagesRoleELMRoleReqMapping());
    reqDB.setStagesRolePAReqMapping(request.getStagesRolePAReqMapping());
    reqDB.setRolePermActReqMappings(request.getRolePermActReqMappings());
    return this.requestRepo.save(reqDB);
  }

  /**
   * @param mappingDto
   */
  private void updateRolePermMapping(final RolePermReqtMapping mappingDto) {
    int roleId = mappingDto.getRole().getId();
    int permissionId = mappingDto.getPermission().getId();
    int requestId = mappingDto.getRequest().getId();
    boolean isPermitted = mappingDto.isPermitted();

    // Step 0: Clear any existing requestId for this role-permission combination
    int updatedRows = this.rolePermRepo.clearExistingRequest(roleId, permissionId, requestId);

    if (updatedRows == 0) {
      // Step 1: Upsert role_perm_act_mapping
      this.rolePermRepo.upsertMapping(roleId, permissionId, requestId);
    }

    // Step 2: Upsert role_perm_act_req_mapping
    this.rolePermReqRepo.upsertReqtMapping(roleId, permissionId, requestId, isPermitted);
  }


  /**
   * 
   * {@inheritDoc}
   */
  public ResponseEntity<String> updatePermissionRole(final Request request) {
    logger.info("Updating PermissionRole {}", CommonConstant.MESS_START);

    for (AttrPermCondition mapping : request.getAttrPermConditionMappings()) {
      try {
        Date triggerTime = new Date(System.currentTimeMillis());

        // Try to fetch existing condition from DB
        AttrPermCondition existingCondition = this.attrPermService.getAttrPermConditionByRequestBody(mapping);

        // If not found, just use the mapping directly (service will handle insert)
        AttrPermCondition conditionToProcess = (existingCondition != null) ? existingCondition : mapping;

        // ✅ Make sure request is always set (important for req_id)
        conditionToProcess.setRequest(request);

        boolean success = this.attrPermService.upsertPermRoleByEntity(conditionToProcess,
            mapping.getAttrPermWorkflows(), triggerTime);

        if (!success) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The update request didn't succeed.");
        }
      }
      finally {
        logger.info("Updating PermissionRole {}", CommonConstant.MESS_END);
      }
    }

    return ResponseEntity.status(HttpStatus.OK).body("Updating permission of role is successful.");

  }


  /**
   * @param userName
   * @return
   */
  public List<Request> findByCreatedBy(String userName) {
    return this.requestRepo.findByCreatedByIgnoreCase(userName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Request> findAllByStatus(final String status) {
    return this.requestRepo.findAllByStatus(status);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Request> findAll() {
    return this.requestRepo.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void updateStatusById(final int i, final String status) {
    this.requestRepo.updateStatusById(i, status);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void updateStagesELMMappingById(final Request request, final int i) {
    List<StagesRoleELMRoleReqMapping> mappings = this.stagesELMRoleReqRepo.findById_RequestId(request.getId());
    if ((mappings != null) && !mappings.isEmpty()) {
      for (StagesRoleELMRoleReqMapping mappingDto : mappings) {
        StagesRoleELMRoleMappingId stagesELMRoleMappingId = new StagesRoleELMRoleMappingId();
        stagesELMRoleMappingId.setRoleId(mappingDto.getElmRole().getId());
        stagesELMRoleMappingId.setStagesId(mappingDto.getStagesRole().getId());
        Optional<StagesRoleELMRoleMapping> existingStagesELMRoleReq =
            this.stagesELMRoleRepo.findById(stagesELMRoleMappingId);
        if (existingStagesELMRoleReq.isPresent()) {
          this.stagesELMRoleRepo.updateRequestById(i, mappingDto.getElmRole().getId(),
              mappingDto.getStagesRole().getId());
          if (Operation.REMOVE.toString().equalsIgnoreCase(mappingDto.getOperation())) {
            this.stagesELMRoleRepo.deleteById(stagesELMRoleMappingId);
          }
        }


      }
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<XMLCondition> fetchPermissionsFromSpec(final String projectAreaName) {
    this.specFile = this.specFile.replace(PropertyUtils.PARAM_PROJECT_AREA_NAME, projectAreaName);
    return fetchPermissionsFromSpec();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean saveRequest(final Request request) {
    logger.info("Saving request entity.{}", request);
    if (request != null) {
      this.requestRepo.save(request);
      logger.debug("Saving request entity is done.");
      return true;
    }
    else {
      logger.error("Mappings is not available");
      return false;
    }
  }


}

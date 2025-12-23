package com.bosch.epc.unittest;

import org.junit.Before;
import org.mockito.Mock;

import com.bosch.epc.model.ELMServerDtl;
import com.bosch.epc.service.AttributeServiceImpl;
import com.bosch.epc.service.PARoleRequestService;

public class TEUControllerTest {

  @Mock
  private AttributeServiceImpl attributeService;

  @Mock
  private PARoleRequestService paRoleRequestService;

//  @InjectMocks
//  private TEUController teuController;

  @Before
  public void setUp() throws Exception {
//    MockitoAnnotations.openMocks(this);
  }

//    @Test
//    public void processRequest_shouldUpdateRoles() throws Exception {
//        // Arrange
//        Request request = getEPCRequest();
//        ELMServerDtl elmServerDtl = getELMServerDtl();
//
//        ArgumentCaptor<List<PARoleRequest>> argumentCaptor = ArgumentCaptor.forClass(List.class);
//
//        // Prepare the list of PARoleRequest objects expected to be passed
//        List<PARoleRequest> expectedPaRoleRequestList = new ArrayList<>();
//        PARoleRequest roleRequest1 = new PARoleRequest();
//        roleRequest1.setName("Role1");
//        expectedPaRoleRequestList.add(roleRequest1);
//
//        PARoleRequest roleRequest2 = new PARoleRequest();
//        roleRequest2.setName("Role2");
//        expectedPaRoleRequestList.add(roleRequest2);
//
//
//        // Mocking paRoleRequestService behavior
//        doNothing().when(paRoleRequestService).updateRole(expectedPaRoleRequestList);
//
//        // Act
//        boolean isTemplateUpdated = teuController.processRequest(request, elmServerDtl);
//
//        // Assert
//        assertTrue("Template changes should be updated", isTemplateUpdated);
//        verify(paRoleRequestService, times(1)).updateRole(argumentCaptor.capture());
//    }

//    private Request getEPCRequest() {
//        Request epcRequest = new Request();
//        epcRequest.setRequestID(100);
//        epcRequest.setStatus("Submitted");
//        List<Condition> conditions = new ArrayList<Condition>();
//        List<RoleDefinition> roleDefinitions = new ArrayList<RoleDefinition>();
//        int count = 7;
//
//        // first workflow record
//        Workflow epcTaskWorkflow = new Workflow();
//        epcTaskWorkflow.setId(count + 1);
//        epcTaskWorkflow.setStatus("New");
//        epcTaskWorkflow.setResolution("");
//        epcTaskWorkflow.setStatusGroup(null);
//        epcTaskWorkflow.setWorkitemType("Tool Problem");
//
//
//        RoleDefinition roleDefinition=new RoleDefinition();
//        roleDefinition.setCardinality("many");
//        roleDefinition.setDescription("Responsibilities of Tool User:&#10;&#10;    uses one or more software tool solution&#10;    responsible to use software tool with valid license&#10;    responsible to uninstall/remove software tool if not necessary (local installations only)&#10;    can request for updates/upgrades of version or functionality from RO Tool Responsible (Tool Owner)&#10;    can report bugs/misbehavior of software tool to RO Tool Responsible (Tool Owner)");
//        roleDefinition.setName("Tool Users");
//        roleDefinition.setRoleID("Tool manager");
//        roleDefinition.setModifiedID("tool development project manager");
//        roleDefinitions.add(roleDefinition);
//        roleDefinition=new RoleDefinition();
//        roleDefinition.setCardinality("many");
//        roleDefinition.setDescription("Responsibilities of Test Developer:&#10;&#10;    defines test cases for software tool&#10;    updates/extends test cases of software tool in case of functional changes");
//        roleDefinition.setName("Tester");
//        roleDefinition.setRoleID("Tester");
//        roleDefinitions.add(roleDefinition);
//
//        List<Role> epcTaskRoles = new ArrayList<>();
//
//        Role epcRole = new Role();
//        epcRole.setId(1);
//        epcRole.setName("Tool Coordinatorr");
//        epcRole.setPermission("w");
//        epcTaskRoles.add(epcRole);
//
//        epcRole = new Role();
//        epcRole.setId(2);
//        epcRole.setName("Tool CCB");
//        epcRole.setPermission("r");
//        epcTaskRoles.add(epcRole);
//
//        epcRole = new Role();
//        epcRole.setId(3);
//        epcRole.setName("Tool Development Project Manager");
//        epcRole.setPermission("m+r");
//        epcTaskRoles.add(epcRole);
//
//        epcRole = new Role();
//        epcRole.setId(4);
//        epcRole.setName("Tool Developer");
//        epcRole.setPermission("m");
//        epcTaskRoles.add(epcRole);
//
//        epcRole = new Role();
//        epcRole.setId(5);
//        epcRole.setName("Tool Test Developer");
//        epcRole.setPermission("w");
//        epcTaskRoles.add(epcRole);
//
//        epcRole = new Role();
//        epcRole.setId(1);
//        epcRole.setName("Tool Tester");
//        epcRole.setPermission("w");
//        epcTaskRoles.add(epcRole);
//
//        epcRole = new Role();
//        epcRole.setId(2);
//        epcRole.setName("Tool Responsible");
//        epcRole.setPermission("r");
//        epcTaskRoles.add(epcRole);
//
//        epcRole = new Role();
//        epcRole.setId(3);
//        epcRole.setName("Tool User");
//        epcRole.setPermission("m+r");
//        epcTaskRoles.add(epcRole);
//
//        epcRole = new Role();
//        epcRole.setId(4);
//        epcRole.setName("Tool Owner");
//        epcRole.setPermission("w");
//        epcTaskRoles.add(epcRole);
//
//        epcRole = new Role();
//        epcRole.setId(5);
//        epcRole.setName("Tool Classification Coordinator");
//        epcRole.setPermission("w");
//        epcTaskRoles.add(epcRole);
//
//        epcTaskWorkflow.setRoles(epcTaskRoles);
//        epcRequest.setAlmRoleDefinitions(roleDefinitions);
//        // Add other test data if needed
//        return epcRequest;
//    }

  private ELMServerDtl getELMServerDtl() {
    ELMServerDtl elmServerDtl = new ELMServerDtl();
    elmServerDtl.setProjectAreaName("ToolDevelopment SC 1.0");
    elmServerDtl.setRepositoryURL("https://rb-alm-03-q.de.bosch.com/ccm/");
    return elmServerDtl;
  }
}

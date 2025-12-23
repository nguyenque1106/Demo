package com.bosch.epc.unittest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.bosch.epc.dao.PARoleRequestRepository;
import com.bosch.epc.datamodel.PARoleRequest;
import com.bosch.epc.service.PARoleRequestService;

class PARoleRequestServiceTest {

  @Mock
  private PARoleRequestRepository paRoleRequestRepository;

  @InjectMocks
  private PARoleRequestService paRoleRequestService;

  @BeforeEach
  void setUp() {
//    MockitoAnnotations.openMocks(this);
  }

//    @Test
//    void updateRole_shouldSaveAllRoles() {
//        // Arrange
//        PARoleRequest role1 = new PARoleRequest();
//        role1.setId(0);
//        role1.setIdentifier("ghgh");
//        role1.setName("Role1");
//        role1.setProjectAreaId(76);
//        role1.setRequestId(9);
//        PARoleRequest role2 = new PARoleRequest();
//        role2.setName("Role2");
//        role2.setId(2);
//        role2.setIdentifier("gyyyyyyyyyy");
//        role2.setName("Role2");
//        role2.setProjectAreaId(768);
//        role2.setRequestId(7);
//
//        List<PARoleRequest> roles = Arrays.asList(role1, role2);
//
//        // Act
//        paRoleRequestService.updateRole(roles);
//
//        // Assert
//        verify(paRoleRequestRepository, times(1)).saveAll(roles);
//    }

  @Test
  void updateRole_shouldHandleEmptyList() {
    // Arrange
    List<PARoleRequest> roles = Arrays.asList();

    // Act
//        paRoleRequestService.updateRole(roles);

    // Assert
    verify(this.paRoleRequestRepository, times(1)).saveAll(roles);
  }
}

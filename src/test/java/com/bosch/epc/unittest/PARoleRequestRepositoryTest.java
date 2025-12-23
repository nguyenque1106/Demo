package com.bosch.epc.unittest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.bosch.epc.dao.PARoleRequestRepository;
import com.bosch.epc.datamodel.PARoleRequest;
import com.bosch.epc.service.PARoleRequestService;


@Transactional
class PARoleRequestRepositoryTest {

  @Mock
  private PARoleRequestRepository paRoleRequestRepository;

  @InjectMocks
  private PARoleRequestService paRoleRequestService;

  @BeforeEach
  void setUp() {
//    MockitoAnnotations.openMocks(this);
  }

//    @Test
//    @Rollback(false)
//    void whenSavingRoleRequest_thenDataIsPersisted() {
//        // Arrange
//        PARoleRequest role1 = new PARoleRequest();
//        role1.setId(0);
//        role1.setIdentifier("ghgh");
//        role1.setName("Test Role");
//        role1.setProjectAreaId(76);
//        role1.setRequestId(9);
//        PARoleRequest role2 = new PARoleRequest();
//        role2.setName("Role2");
//        role2.setId(2);
//        role2.setIdentifier("gyyyyyyyyyy");
//        role2.setName("Role2");
//        role2.setProjectAreaId(768);
//        role2.setRequestId(7);
//        List<PARoleRequest> roles = Arrays.asList(role1, role2);
//
//        List<PARoleRequest> savedRole = paRoleRequestRepository.saveAll(roles);
//
//        // Assert
//        assertThat(savedRole).isNotNull();
//
//    }

  @Test
  void whenSavingRoleRequestList_thenDataIsPersisted() {
    // Arrange
    PARoleRequest role1 = new PARoleRequest();
    role1.setName("Role1");
    PARoleRequest role2 = new PARoleRequest();
    role2.setName("Role2");

    when(this.paRoleRequestRepository.saveAll(Arrays.asList(role1, role2))).thenReturn(Arrays.asList(role1, role2));

    // Act
    List<PARoleRequest> savedRoles = this.paRoleRequestRepository.saveAll(Arrays.asList(role1, role2));

    // Assert
    assertThat(savedRoles).hasSize(2);
    assertThat(savedRoles.get(0).getName()).isEqualTo("Role1");
    assertThat(savedRoles.get(1).getName()).isEqualTo("Role2");
  }
}

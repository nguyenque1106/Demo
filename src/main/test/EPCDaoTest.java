import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.HttpURLConnection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.bosch.epc.dao.EPCDao;
import com.bosch.epc.datamodel.WORequest;

/**
 * @author VFE1COB Test class to test create workon request, retrieve workon request status 
 *
 */
public class EPCDaoTest {

   

    @InjectMocks
    private EPCDao epcDao;

    
    private HttpURLConnection connection;

    
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        epcDao.setKeyId("82eef6eb-5cb1-4ea3-a685-bc750be13748");
        epcDao.setHost("ews-esz-emea.api.bosch.com");
        epcDao.setContentType("application/json");
        epcDao.setCreateRequestURL("https://ews-esz-emea.api.bosch.com/workflow/workon02emea/q/v1/createrequest/create");
        epcDao.setStatusURL("https://ews-esz-emea.api.bosch.com/workflow/workon02emea/q/v1/status/");
   
    }
   
    @Test
    public void testGetRequestStatus() throws Exception {
        String id = "RBGA-235420";
       // String responseJson = "{\"requestKey\": \"RBGA-231815\", \"resolution\": \"Completed\", \"status\": [{\"localeName\": \"en_UK\", \"i8nValue\": \"Completed\"}]}";

       //
        String response = epcDao.getRequestStatus(id);
        assertEquals("Approved", response);
 }

    @Test
    public void testCreateEPCRequest() throws Exception {
        String jsonString ="{\"summary\":\"model name\",\"pkey\":\"RBGA\",\"issuetype\":\"rbga.issuetype.default\",\"applicant\":\"vfe1cob\",\"priority\":\"default\",\"data\":{\"rbga.field.sourceSystem\":\"EPC\",\"rbga.field.termCheck\":\"yes\",\"rbga.field.description\":\"EPC\",\"rbga.field.comments\":\"EPC\",\"rbga.field.approvalstep\":\"Two Step Approval\",\"rbga.field.workflowType\":\"Serial\",\"rbga.field.wf2\":\"Parallel\",\"rbga.field.wf3\":\"Serial\",\"rbga.field.parallelWorkflowSel\":\"All the Approvers has to approve\",\"rbga.field.parallelWorkflowSel2\":\"Only one Approver has to approve\",\"rbga.field.parallelWorkflowSel3\":\"All the Approvers has to approve\",\"rbga.field.approver1\":{\"approvers\":[{\"addAfterEnabled\":true,\"deleteFlag\":\"Yes\",\"description\":\"sds\",\"fixed\":false,\"removable\":true,\"userid\":\"vfe1cob\",\"ccList\":\"\"}],\"checkDuplicate\":\"false\",\"maxApprover\":\"20\",\"type\":\"1\"},\"rbga.field.approver2\":{\"approvers\":[{\"addAfterEnabled\":true,\"deleteFlag\":\"Yes\",\"description\":\"sds\",\"fixed\":false,\"removable\":true,\"userid\":\"slc5kor\",\"ccList\":\"\"}],\"checkDuplicate\":\"false\",\"maxApprover\":\"20\",\"type\":\"2\"},\"rbga.field.tempNew\":\"New Request\"}}";
        
        String responseJson = "{\"key\": \"12345\"}";
        WORequest response = epcDao.createEPCRequest(jsonString);
        assertNotNull(response);
  }
    
    @Test
    public void testSingleApprovalCreateEPCRequest() throws Exception {
        String jsonString ="{\"summary\":\"modelname\",\"pkey\":\"RBGA\",\"issuetype\":\"rbga.issuetype.default\",\"applicant\":\"vfe1cob\",\"priority\":\"default\",\"data\":{\"rbga.field.sourceSystem\":\"EPC\",\"rbga.field.description\":\"EPC\",\"rbga.field.comments\":\"EPC\",\"rbga.field.approvalstep\":\"TwoStepApproval\",\"rbga.field.tempNew\":\"NewRequest\",\"rbga.field.workflowType\":\"Serial\",\"rbga.field.parallelWorkflowSel\":\"AlltheApprovershastoapprove\",\"rbga.field.wf2\":\"Parallel\",\"rbga.field.parallelWorkflowSel2\":\"OnlyoneApproverhastoapprove\",\"rbga.field.wf3\":\"Serial\",\"rbga.field.parallelWorkflowSel3\":\"AlltheApprovershastoapprove\",\"rbga.field.approver1\":{\"approvers\":[{\"addAfterEnabled\":true,\"deleteFlag\":\"Yes\",\"description\":\"\",\"fixed\":false,\"removable\":true,\"userid\":\"vfe1cob\",\"ccList\":\"\"}],\"checkDuplicate\":\"false\",\"maxApprover\":\"20\",\"type\":\"1\"},\"rbga.field.approver2\":{\"approvers\":[{\"addAfterEnabled\":true,\"deleteFlag\":\"Yes\",\"description\":\"\",\"fixed\":false,\"removable\":true,\"userid\":\"drm8cob\",\"ccList\":\"\"},{\"addAfterEnabled\":true,\"deleteFlag\":\"Yes\",\"description\":\"\",\"fixed\":false,\"removable\":true,\"userid\":\"drm8cob\",\"ccList\":\"\"},{\"addAfterEnabled\":true,\"deleteFlag\":\"Yes\",\"description\":\"\",\"fixed\":false,\"removable\":true,\"userid\":\"drm8cob\",\"ccList\":\"\"}],\"checkDuplicate\":\"false\",\"maxApprover\":\"20\",\"type\":\"2\"}}}";
        
        String responseJson = "{\"key\": \"12345\"}";


        WORequest response = epcDao.createEPCRequest(jsonString);
        assertNotNull(response);
   }

}

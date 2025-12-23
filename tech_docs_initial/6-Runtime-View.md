# 6. Runtime View

**General Purpose:** Describe dynamic behavior using sequences or use cases.

## 6.1 Use Case: Create Role Configuration

**Purpose:** Administrator creates a new role with permissions and attribute controls.

### Sequence Diagram

```mermaid
sequenceDiagram
    actor Admin as ELM Administrator
    participant UI as Swagger UI
    participant RoleCtrl as ELMRoleController
    participant RoleSvc as ELMRoleService
    participant PermSvc as PermissionService
    participant RoleRepo as ELMRoleRepository
    participant MappingRepo as RolePermMappingRepository
    participant DB as MySQL Database
    
    Admin->>UI: Access /api/roles/createRole
    UI->>Admin: Request authentication
    Admin->>UI: Provide credentials
    UI->>RoleCtrl: POST /api/roles/createRole<br/>{roleName, description, permissions}
    
    RoleCtrl->>RoleCtrl: Validate request body
    RoleCtrl->>RoleSvc: createRole(roleRequest)
    
    RoleSvc->>RoleSvc: Validate role name uniqueness
    RoleSvc->>RoleSvc: Check user permissions
    
    RoleSvc->>RoleRepo: findByRoleName(name)
    RoleRepo->>DB: SELECT * FROM elm_role WHERE role_name=?
    DB-->>RoleRepo: Result (empty)
    RoleRepo-->>RoleSvc: Role not exists
    
    RoleSvc->>RoleSvc: Create ELMRole entity
    RoleSvc->>RoleRepo: save(elmRole)
    RoleRepo->>DB: INSERT INTO elm_role...
    DB-->>RoleRepo: Role ID: 123
    RoleRepo-->>RoleSvc: Saved role with ID
    
    loop For each permission
        RoleSvc->>PermSvc: validatePermission(permissionId)
        PermSvc-->>RoleSvc: Valid
        RoleSvc->>RoleSvc: Create mapping
        RoleSvc->>MappingRepo: save(rolePermMapping)
        MappingRepo->>DB: INSERT INTO role_perm_mapping...
        DB-->>MappingRepo: Mapping created
    end
    
    RoleSvc-->>RoleCtrl: Role created successfully
    RoleCtrl-->>UI: HTTP 201 Created<br/>{id: 123, roleName: "Developer"}
    UI-->>Admin: Success message
```

### Runtime Behavior

1. **Authentication Phase**
   - User credentials validated against LDAP
   - Session token created
   - User authorities loaded (ADMIN, USER, etc.)

2. **Validation Phase**
   - Request body validated (null checks, format)
   - Role name uniqueness checked
   - Permission IDs validated
   - User authorization verified

3. **Persistence Phase**
   - Transaction started (@Transactional)
   - Role entity saved to database
   - Role-permission mappings created
   - Transaction committed

4. **Response Phase**
   - Success response generated
   - HTTP 201 Created returned
   - Response includes created role details

## 6.2 Use Case: Submit Configuration Request for Approval

**Purpose:** User submits a configuration change request that goes through WorkON approval workflow.

### Sequence Diagram

```mermaid
sequenceDiagram
    actor User as Project Manager
    participant ReqCtrl as RequestController
    participant ReqSvc as RequestService
    participant WorkONClient as WorkON Client
    participant ReqRepo as RequestRepository
    participant DB as Database
    
    User->>ReqCtrl: POST /api/request/createRequest<br/>{type, description, changes}
    ReqCtrl->>ReqSvc: createRequest(requestData)
    
    ReqSvc->>ReqSvc: Validate request data
    ReqSvc->>ReqSvc: Create Request entity (status=DRAFT)
    ReqSvc->>ReqRepo: save(request)
    ReqRepo->>DB: INSERT INTO request...
    DB-->>ReqRepo: Request ID: 456
    ReqRepo-->>ReqSvc: Saved request
    
    ReqSvc-->>ReqCtrl: Request created (DRAFT)
    ReqCtrl-->>User: HTTP 201 Created<br/>{id: 456, status: "DRAFT"}
    
    Note over User: User reviews and submits
    
    User->>ReqCtrl: POST /api/request/submitForApproval/{id}
    ReqCtrl->>ReqSvc: submitForApproval(456)
    
    ReqSvc->>ReqRepo: findById(456)
    ReqRepo->>DB: SELECT * FROM request WHERE id=456
    DB-->>ReqRepo: Request data
    ReqRepo-->>ReqSvc: Request entity
    
    ReqSvc->>ReqSvc: Validate status (must be DRAFT)
    ReqSvc->>ReqSvc: Prepare WorkON request
    
    ReqSvc->>WorkONClient: submitApprovalRequest(workOnRequest)
    WorkONClient->>WorkONClient: Build HTTP request
    WorkONClient-->>ReqSvc: WorkON ID: "WO-2023-1234"
    
    ReqSvc->>ReqSvc: Update request status to PENDING
    ReqSvc->>ReqSvc: Save WorkON ID
    ReqSvc->>ReqRepo: update(request)
    ReqRepo->>DB: UPDATE request SET status='PENDING', workon_id='WO-2023-1234'
    DB-->>ReqRepo: Updated
    
    ReqSvc-->>ReqCtrl: Submitted successfully
    ReqCtrl-->>User: HTTP 200 OK<br/>{status: "PENDING", workOnId: "WO-2023-1234"}
```

### Runtime Behavior

1. **Draft Creation**
   - Request created with status DRAFT
   - Changes stored in database
   - User can edit before submitting

2. **Submission**
   - Request validated
   - WorkON approval request created
   - Request status changed to PENDING
   - WorkON ID stored for tracking

3. **Approval Tracking**
   - Background job polls WorkON status
   - Status updated automatically
   - User notified of approval/rejection

## 6.3 Use Case: Process Approved Request (Background Job)

**Purpose:** Scheduled job processes approved requests and deploys configurations to ELM.

### Sequence Diagram

```mermaid
sequenceDiagram
    participant Job as ProcessRequestJob<br/>@Scheduled
    participant ReqSvc as RequestService
    participant XMLSvc as XMLGenerationService
    participant XMLGen as XMLGenerator
    participant XMLMerge as XMLMerger
    participant TEU as TEUUtility
    participant ELM as ELM Server
    participant ReqRepo as RequestRepository
    participant DB as Database
    
    Note over Job: Scheduled execution<br/>Every 15 minutes
    
    Job->>ReqSvc: processApprovedRequests()
    ReqSvc->>ReqRepo: findByStatus("APPROVED")
    ReqRepo->>DB: SELECT * FROM request WHERE status='APPROVED'
    DB-->>ReqRepo: List of approved requests
    ReqRepo-->>ReqSvc: [Request 456, Request 789]
    
    loop For each approved request
        ReqSvc->>XMLSvc: generateConfiguration(request)
        
        Note over XMLSvc: Generate Role XML
        XMLSvc->>XMLGen: generateRoleDefinitions(roles)
        XMLGen->>XMLGen: Map to RoleBean
        XMLGen->>XMLGen: Marshal to XML
        XMLGen-->>XMLSvc: roles.xml
        
        Note over XMLSvc: Generate Permission XML
        XMLSvc->>XMLGen: generatePermissions(permissions)
        XMLGen->>XMLGen: Map to PermissionBean
        XMLGen->>XMLGen: Marshal to XML
        XMLGen-->>XMLSvc: permissions.xml
        
        Note over XMLSvc: Generate Condition XML
        XMLSvc->>XMLGen: generateConditions(attrPerms)
        XMLGen->>XMLGen: Map to ConditionBean
        XMLGen->>XMLGen: Marshal to XML
        XMLGen-->>XMLSvc: conditions.xml
        
        Note over XMLSvc: Merge with template
        XMLSvc->>XMLMerge: mergeWithTemplate(xmlFiles)
        XMLMerge->>XMLMerge: Load template
        XMLMerge->>XMLMerge: Merge configurations
        XMLMerge->>XMLMerge: Validate merged XML
        XMLMerge-->>XMLSvc: merged-config.xml
        
        XMLSvc-->>ReqSvc: Configuration files ready
        
        Note over ReqSvc: Deploy to ELM
        ReqSvc->>TEU: uploadConfiguration(config)
        TEU->>ELM: HTTP POST /ccm/process/upload
        ELM->>ELM: Validate XML
        ELM->>ELM: Apply configuration
        ELM-->>TEU: Deployment successful
        TEU-->>ReqSvc: Deployment confirmed
        
        ReqSvc->>ReqSvc: Update request status to COMPLETED
        ReqSvc->>ReqRepo: update(request)
        ReqRepo->>DB: UPDATE request SET status='COMPLETED', completed_date=NOW()
        DB-->>ReqRepo: Updated
        
        Note over ReqSvc: Send notification
        ReqSvc->>ReqSvc: notifyUser(request.requestor)
    end
    
    ReqSvc-->>Job: Processing completed
```

### Runtime Behavior

1. **Job Trigger**
   - Spring scheduler triggers at configured interval
   - Job acquires lock to prevent concurrent execution **(AI-Generated Placeholder)**

2. **Request Discovery**
   - Query database for approved requests
   - Sort by priority and creation date

3. **XML Generation**
   - Fetch related data (roles, permissions, attributes)
   - Map to JAXB beans
   - Marshal to XML files
   - Validate XML structure

4. **Template Merging**
   - Load base process template from ELM
   - Merge new configurations
   - Validate merged result
   - Ensure no conflicts

5. **Deployment**
   - Upload to ELM server via TEU
   - ELM validates and applies
   - Confirmation received

6. **Status Update**
   - Mark request as COMPLETED
   - Record completion timestamp
   - Update WorkON system
   - Notify requestor

7. **Error Handling**
   - If any step fails, mark as FAILED
   - Log error details
   - Notify administrator
   - Request can be retried manually

## 6.4 Use Case: Synchronize Project Areas from ELM

**Purpose:** Scheduled job keeps project area data in sync with ELM server.

### Sequence Diagram

```mermaid
sequenceDiagram
    participant Job as ELMDataSyncJob<br/>@Scheduled
    participant SyncSvc as SyncService
    participant ELMClient as AlmServerConnection
    participant ELM as ELM Server
    participant PARepo as ProjectAreaRepository
    participant Cache as Ehcache
    participant DB as Database
    
    Note over Job: Scheduled execution<br/>Daily at 2 AM
    
    Job->>SyncSvc: syncProjectAreas()
    SyncSvc->>ELMClient: connect(elmUrl, credentials)
    ELMClient->>ELM: Authenticate
    ELM-->>ELMClient: Authentication token
    
    SyncSvc->>ELMClient: fetchProjectAreas()
    ELMClient->>ELM: GET /ccm/oslc/projectareas
    ELM-->>ELMClient: JSON array of project areas
    ELMClient-->>SyncSvc: List<ProjectAreaDTO>
    
    SyncSvc->>PARepo: findAll()
    PARepo->>DB: SELECT * FROM project_area
    DB-->>PARepo: Existing project areas
    PARepo-->>SyncSvc: Current project areas
    
    SyncSvc->>SyncSvc: Compare and identify changes<br/>- New projects<br/>- Updated projects<br/>- Deleted projects
    
    loop For each new project
        SyncSvc->>SyncSvc: Create ProjectArea entity
        SyncSvc->>PARepo: save(projectArea)
        PARepo->>DB: INSERT INTO project_area...
        DB-->>PARepo: Saved
    end
    
    loop For each existing project
        alt Project metadata changed
            SyncSvc->>PARepo: update(projectArea)
            PARepo->>DB: UPDATE project_area...
            DB-->>PARepo: Updated
        end
    end
    
    loop For each deleted project
        SyncSvc->>PARepo: markInactive(projectArea)
        PARepo->>DB: UPDATE project_area SET active=FALSE
        DB-->>PARepo: Marked inactive
    end
    
    Note over SyncSvc: Fetch detailed metadata
    
    loop For each project area
        SyncSvc->>ELMClient: fetchWorkItemTypes(projectArea)
        ELMClient->>ELM: GET /ccm/oslc/types/{projectArea}
        ELM-->>ELMClient: Work item types
        
        SyncSvc->>ELMClient: fetchWorkflowStates(projectArea)
        ELMClient->>ELM: GET /ccm/process/workflows/{projectArea}
        ELM-->>ELMClient: Workflow states
        
        SyncSvc->>ELMClient: fetchAttributes(projectArea)
        ELMClient->>ELM: GET /ccm/oslc/attributes/{projectArea}
        ELM-->>ELMClient: Attribute definitions
        
        SyncSvc->>DB: Store metadata
    end
    
    Note over SyncSvc: Clear caches
    SyncSvc->>Cache: evictAll("projectAreas")
    Cache->>Cache: Clear project area cache
    SyncSvc->>Cache: evictAll("workItemTypes")
    Cache->>Cache: Clear work item type cache
    
    SyncSvc-->>Job: Sync completed successfully
    Job->>Job: Log sync summary<br/>- Projects added: 2<br/>- Projects updated: 5<br/>- Projects removed: 0
```

### Runtime Behavior

1. **Connection Phase**
   - Authenticate to ELM server
   - Obtain session token
   - Handle connection failures with retry

2. **Data Fetch Phase**
   - Fetch project area list
   - Fetch work item types
   - Fetch workflow definitions
   - Fetch attribute definitions

3. **Comparison Phase**
   - Compare with local database
   - Identify new, updated, deleted projects
   - Calculate delta changes

4. **Update Phase**
   - Insert new projects
   - Update changed projects
   - Mark deleted projects as inactive (soft delete)
   - Store metadata

5. **Cache Management**
   - Evict cached project area data
   - Force cache refresh on next access

6. **Completion**
   - Log sync summary
   - Record sync timestamp
   - Report errors if any

## 6.5 Use Case: Bulk Save Attribute Permissions

**Purpose:** Administrator saves multiple attribute permissions in a single operation for efficiency.

### Sequence Diagram

```mermaid
sequenceDiagram
    actor Admin
    participant AttrCtrl as ELMAttrPermController
    participant AttrSvc as ELMAttrPermService
    participant AttrRepo as ELMAttrPermRepo
    participant RoleRepo as AttrPermRoleRepository
    participant WFRepo as AttrPermWorkflowRepository
    participant DB as Database
    
    Admin->>AttrCtrl: POST /api/attrperm/bulkSaveAttrPermConditions<br/>{projectAreaName, permissions: [...]}
    
    AttrCtrl->>AttrCtrl: Validate request
    AttrCtrl->>AttrSvc: bulkSaveAttrPermConditions(bulkRequest)
    
    AttrSvc->>AttrSvc: Start transaction
    
    loop For each permission in list
        AttrSvc->>AttrSvc: Validate attribute exists
        AttrSvc->>AttrSvc: Validate work item type
        AttrSvc->>AttrSvc: Validate role IDs
        AttrSvc->>AttrSvc: Validate workflow state IDs
        
        alt Validation passes
            AttrSvc->>AttrSvc: Create AttrPermCondition
            AttrSvc->>AttrRepo: save(attrPermCondition)
            AttrRepo->>DB: INSERT INTO attr_perm_condition...
            DB-->>AttrRepo: Condition ID: X
            
            loop For each role ID
                AttrSvc->>RoleRepo: save(attrPermRole)
                RoleRepo->>DB: INSERT INTO attr_perm_role...
            end
            
            loop For each workflow state ID
                AttrSvc->>WFRepo: save(attrPermWorkflow)
                WFRepo->>DB: INSERT INTO attr_perm_workflow...
            end
            
            AttrSvc->>AttrSvc: Add to success list
        else Validation fails
            AttrSvc->>AttrSvc: Add to failure list
            AttrSvc->>AttrSvc: Continue with next
        end
    end
    
    AttrSvc->>AttrSvc: Commit transaction
    AttrSvc->>AttrSvc: Prepare bulk result
    AttrSvc-->>AttrCtrl: {successCount: 8, failedCount: 2, errors: [...]}
    
    alt All succeeded
        AttrCtrl-->>Admin: HTTP 201 Created
    else Partial success
        AttrCtrl-->>Admin: HTTP 207 Multi-Status
    else All failed
        AttrCtrl-->>Admin: HTTP 400 Bad Request
    end
```

### Runtime Behavior

1. **Bulk Request Reception**
   - Receive array of attribute permissions
   - Validate overall request structure

2. **Transaction Management**
   - Start single database transaction
   - Process all items within transaction
   - Rollback if critical error **(AI-Generated Placeholder)**

3. **Individual Processing**
   - Validate each permission independently
   - Continue processing even if some fail
   - Track success/failure counts

4. **Batch Persistence**
   - Use JPA batch operations for efficiency
   - Reduce database round trips
   - Improve performance for large batches

5. **Result Aggregation**
   - Collect successful IDs
   - Collect error messages
   - Return comprehensive result

6. **Response Strategy**
   - 201 Created: All succeeded
   - 207 Multi-Status: Partial success
   - 400 Bad Request: All failed

## 6.6 Runtime Constraints

| Constraint | Description | Impact |
|------------|-------------|--------|
| **Transaction Timeout** | 30 seconds per transaction **(AI-Generated Placeholder)** | Long-running operations may need splitting |
| **Session Timeout** | 30 minutes inactivity **(AI-Generated Placeholder)** | User must re-authenticate |
| **Job Concurrency** | Jobs run sequentially, not concurrently | Prevents data conflicts |
| **Database Connection Pool** | 10 connections max **(AI-Generated Placeholder)** | Limits concurrent operations |
| **XML File Size** | Max 10MB per configuration **(AI-Generated Placeholder)** | Very large projects may need optimization |
| **Bulk Operation Size** | Max 100 items per bulk request **(AI-Generated Placeholder)** | Prevents memory issues |

## 6.7 Performance Considerations

| Scenario | Optimization | Expected Performance |
|----------|--------------|---------------------|
| **Frequent Project Area Queries** | Ehcache with 1-hour TTL **(AI-Generated Placeholder)** | < 50ms response time |
| **Bulk Attribute Save** | JPA batch processing | < 10 seconds for 100 items **(AI-Generated Placeholder)** |
| **Request Processing** | Background job, non-blocking | No user wait time |
| **ELM Sync** | Scheduled off-peak hours | No business hour impact |
| **Database Queries** | Indexed foreign keys | < 100ms for most queries **(AI-Generated Placeholder)** |

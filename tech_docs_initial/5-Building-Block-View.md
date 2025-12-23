# 5. Building Block View

**General Purpose:** Decompose the system into modules, components, or services.

## 5.1 Whitebox Main Component

**General Purpose:** Detailed internal structure of the main EPC application.

### Level 0: System Context

```mermaid
graph TB
    subgraph "EPC System"
        EPC[ELM Permission<br/>Compass]
    end
    
    USERS[Users] --> EPC
    EPC --> ELM[ELM Server]
    EPC --> WORKON[WorkON]
    EPC --> LDAP[LDAP]
    EPC --> DB[(MySQL)]
    
    style EPC fill:#4CAF50,color:#fff
```

### Level 1: Main Application Structure

```mermaid
graph TB
    subgraph "EPC Application"
        WEB[Web Layer<br/>Controllers]
        BIZ[Business Layer<br/>Services]
        DATA[Data Access Layer<br/>Repositories]
        DOMAIN[Domain Layer<br/>Entities]
        JOBS[Job Layer<br/>Scheduled Tasks]
        INTEG[Integration Layer<br/>External Clients]
        CONFIG[Configuration<br/>Spring Config]
    end
    
    WEB --> BIZ
    BIZ --> DATA
    BIZ --> INTEG
    BIZ --> JOBS
    DATA --> DOMAIN
    CONFIG -.configures.-> WEB
    CONFIG -.configures.-> BIZ
    CONFIG -.configures.-> DATA
    
    style BIZ fill:#4CAF50,color:#fff
    style WEB fill:#2196F3,color:#fff
    style DATA fill:#FF9800,color:#fff
```

### Component Responsibilities

| Component | Responsibility | Key Classes |
|-----------|---------------|-------------|
| **Web Layer** | • Handle HTTP requests<br>• Validate input<br>• Map requests to responses<br>• Exception handling | `*Controller` classes<br>`CsrfController`<br>`GlobalExceptionHandler` |
| **Business Layer** | • Business logic<br>• Transaction management<br>• Validation rules<br>• Orchestration | `*Service` and `*ServiceImpl` classes |
| **Data Access Layer** | • Database operations<br>• Query execution<br>• Data mapping | `*Repository` interfaces<br>`*DaoImpl` classes |
| **Domain Layer** | • Domain entities<br>• JPA mappings<br>• Entity relationships | `ELMRole`, `ELMPermissions`, `Request`, etc. |
| **Job Layer** | • Scheduled tasks<br>• Background processing<br>• Batch operations | `ELMDataSyncJob`<br>`ProcessRequestJob`<br>`WorkONStatusSyncJob` |
| **Integration Layer** | • External system integration<br>• API clients<br>• XML processing | `AlmServerConnection`<br>`TEUUtility`<br>`XMLGenerator`, `XMLMerger` |
| **Configuration** | • Spring configuration<br>• Security setup<br>• Bean definitions | `SecurityConfig`<br>`EPCApplication` (main class) |

## 5.2 Building Blocks - Level 2

### 5.2.1 Web Layer (Controller Module)

**General Purpose:** Handle REST API requests and responses.

```mermaid
graph TB
    subgraph "Controller Layer"
        ROLE_CTRL[ELMRoleController]
        PERM_CTRL[ELMPermController]
        ATTR_CTRL[ELMAttrPermController]
        REQ_CTRL[RequestController]
        PA_CTRL[ProjectAreaController]
        STAGE_CTRL[StagesMappingController]
        USER_CTRL[UserController]
        JOB_CTRL[JobController]
        CSRF_CTRL[CsrfController]
    end
    
    CLIENT[REST Client] --> ROLE_CTRL
    CLIENT --> PERM_CTRL
    CLIENT --> ATTR_CTRL
    CLIENT --> REQ_CTRL
    CLIENT --> PA_CTRL
    
    style ROLE_CTRL fill:#2196F3,color:#fff
    style ATTR_CTRL fill:#2196F3,color:#fff
    style REQ_CTRL fill:#2196F3,color:#fff
```

#### Controller Components **(Project-Sourced)**

| Controller | Endpoints | Responsibility |
|------------|-----------|----------------|
| **ELMRoleController** | `/api/roles/*` | • Get all roles<br>• Create new role<br>• Update role<br>• Delete role<br>• Get roles by project area |
| **ELMPermController** | `/api/permissions/*` | • Get all permissions<br>• Create permission<br>• Update permission<br>• Delete permission<br>• Get permissions by category |
| **ELMAttrPermController** | `/api/attrperm/*` | • Get attribute permissions<br>• Save attribute condition<br>• Update attribute condition<br>• Delete attribute condition<br>• Bulk save<br>• Get attributes by work item type |
| **RequestController** | `/api/request/*` | • Create request<br>• Get request status<br>• Submit for approval<br>• Get request history<br>• Cancel request |
| **ProjectAreaController** | `/api/projectarea/*` | • Get all project areas<br>• Get project area details<br>• Sync project areas<br>• Check user access |
| **RolePermMappingController** | `/api/roleperm/*` | • Map role to permissions<br>• Get mappings<br>• Update mappings<br>• Delete mappings |
| **StagesMappingController** | `/api/stages/*` | • Map stages to roles<br>• Get stage mappings<br>• Update stage assignments |
| **UserController** | `/api/user/*` | • Get current user<br>• Get user roles<br>• LDAP user search |
| **JobController** | `/api/jobs/*` | • Trigger manual job execution<br>• Get job status<br>• View job history |
| **CsrfController** | `/api/csrf` | • Get CSRF token for clients |

### 5.2.2 Business Layer (Service Module)

**General Purpose:** Implement business logic and orchestration.

```mermaid
graph TB
    subgraph "Service Layer"
        ROLE_SVC[ELMRoleService]
        PERM_SVC[ELMPermService]
        ATTR_SVC[ELMAttrPermService]
        REQ_SVC[RequestService]
        PA_SVC[ProjectAreaService]
        MAPPING_SVC[RolePermMappingService]
        STAGE_SVC[StagesRoleService]
        SYNC_SVC[SyncService]
        XML_SVC[XMLGenerationService]
    end
    
    CONTROLLERS[Controllers] --> ROLE_SVC
    CONTROLLERS --> PERM_SVC
    CONTROLLERS --> ATTR_SVC
    CONTROLLERS --> REQ_SVC
    
    REQ_SVC --> XML_SVC
    REQ_SVC --> SYNC_SVC
    
    style ROLE_SVC fill:#4CAF50,color:#fff
    style REQ_SVC fill:#4CAF50,color:#fff
    style XML_SVC fill:#4CAF50,color:#fff
```

#### Service Components **(Project-Sourced)**

| Service | Responsibility | Key Methods |
|---------|---------------|-------------|
| **ELMRoleService** | Role management business logic | • createRole()<br>• updateRole()<br>• deleteRole()<br>• getRolesByProjectArea()<br>• validateRoleName() |
| **ELMPermService** | Permission management | • createPermission()<br>• updatePermission()<br>• getPermissionsByCategory()<br>• validatePermission() |
| **ELMAttrPermService** | Attribute permission logic | • saveAttrPermCondition()<br>• updateAttrPermCondition()<br>• bulkSaveConditions()<br>• getAttrPermsByProjectArea()<br>• validateAttributeExists() |
| **RequestService** | Request lifecycle management | • createRequest()<br>• submitForApproval()<br>• processRequest()<br>• updateStatus()<br>• generateConfiguration() |
| **ProjectAreaService** | Project area operations | • syncProjectAreas()<br>• getProjectAreaDetails()<br>• checkUserAccess()<br>• updateProjectAreaMetadata() |
| **RolePermMappingService** | Role-permission mapping | • mapRoleToPermissions()<br>• updateMappings()<br>• getMappingsByRole()<br>• validateMapping() |
| **StagesRoleService** | Stage-based role mapping | • mapStageToRoles()<br>• getStageAssignments()<br>• validateStageTransition() |
| **SyncService** | Data synchronization | • syncFromELM()<br>• syncWorkONStatus()<br>• reconcileData() |
| **XMLGenerationService** | XML configuration generation | • generateRoleXML()<br>• generatePermissionXML()<br>• generateConditionXML()<br>• mergeWithTemplate()<br>• validateXML() |

#### Service Layer Interactions

```mermaid
sequenceDiagram
    participant Ctrl as Controller
    participant ReqSvc as RequestService
    participant XMLSvc as XMLGenerationService
    participant WorkON as WorkON Client
    participant ELM as ELM Client
    participant Repo as Repository
    
    Ctrl->>ReqSvc: createRequest(data)
    ReqSvc->>Repo: save(request)
    Repo-->>ReqSvc: saved request
    ReqSvc->>WorkON: submitApproval()
    WorkON-->>ReqSvc: approval ID
    ReqSvc-->>Ctrl: request created
    
    Note over ReqSvc: Scheduled job checks status
    
    ReqSvc->>WorkON: checkStatus()
    WorkON-->>ReqSvc: APPROVED
    ReqSvc->>XMLSvc: generateXML(request)
    XMLSvc-->>ReqSvc: XML files
    ReqSvc->>ELM: deployConfiguration()
    ELM-->>ReqSvc: deployment success
    ReqSvc->>Repo: updateStatus(COMPLETED)
```

### 5.2.3 Data Access Layer (Repository Module)

**General Purpose:** Abstract database operations and provide query methods.

```mermaid
graph TB
    subgraph "Repository Layer"
        ROLE_REPO[ELMRoleRepository]
        PERM_REPO[ELMPermRepo]
        ATTR_REPO[ELMAttrPermRepo]
        REQ_REPO[RequestRepository]
        PA_REPO[ProjectAreaRepository]
        MAPPING_REPO[RolePermMappingRepository]
        STAGE_REPO[StagesRepository]
        WORKON_REPO[WorkONRepository]
    end
    
    subgraph "Custom DAOs"
        ATTR_DAO[AttributeDaoImpl]
        STAGE_DAO[StagesDaoImpl]
        REQ_DAO[RequestDAO]
    end
    
    SERVICE[Service Layer] --> ROLE_REPO
    SERVICE --> ATTR_REPO
    SERVICE --> REQ_REPO
    SERVICE --> ATTR_DAO
    
    style ROLE_REPO fill:#FF9800,color:#fff
    style ATTR_REPO fill:#FF9800,color:#fff
```

#### Repository Components **(Project-Sourced)**

| Repository | Entity | Key Methods |
|------------|--------|-------------|
| **ELMRoleRepository** | ELMRole | • findByProjectAreaName()<br>• findByRoleName()<br>• findByActiveTrue() |
| **ELMPermRepo** | ELMPermissions | • findByCategory()<br>• findByOperationName() |
| **ELMAttrPermRepo** | AttrPermCondition | • findByProjectAreaId()<br>• findByAttributeName()<br>• findByWorkItemType() |
| **RequestRepository** | Request | • findByStatus()<br>• findByRequestor()<br>• findByCreatedDateBetween()<br>• findByWorkOnRequestId() |
| **ProjectAreaRepository** | ProjectArea | • findByName()<br>• findByActiveTrue()<br>• findByElmUrl() |
| **RolePermMappingRepository** | RolePermMapping | • findByRoleId()<br>• findByPermissionId()<br>• deleteByRoleId() |
| **StagesRepository** | Stages | • findByStageName()<br>• findByProjectAreaId() |
| **WorkONRepository** | WorkONRequest | • findByRequestId()<br>• findByStatus() |

#### Custom DAO Implementations

| DAO | Purpose | Methods |
|-----|---------|---------|
| **AttributeDaoImpl** | Complex attribute queries | • getAttributesByWorkItemType()<br>• getAttributesWithPermissions()<br>• searchAttributes() |
| **StagesDaoImpl** | Stage hierarchy queries | • getStageHierarchy()<br>• getStagesByLevel()<br>• validateStageTransition() |
| **RequestDAO** | Complex request queries | • getRequestsWithDetails()<br>• getRequestHistory()<br>• getApprovalChain() |

### 5.2.4 Domain Layer (Entity Module)

**General Purpose:** Define data model and entity relationships.

```mermaid
erDiagram
    ProjectArea ||--o{ ELMRole : contains
    ProjectArea ||--o{ Request : "has requests"
    ProjectArea ||--o{ AttrPermCondition : configures
    
    ELMRole ||--o{ RolePermMapping : "has mappings"
    ELMPermissions ||--o{ RolePermMapping : "mapped to"
    
    AttrPermCondition ||--o{ AttrPermRole : "applies to"
    AttrPermCondition ||--o{ AttrPermWorkflow : "for workflows"
    
    ELMRole ||--o{ AttrPermRole : references
    
    Request ||--o{ PARoleRequest : includes
    Request ||--o{ PermELMRoleReq : includes
    Request ||--o{ RolePermReqtMapping : includes
    
    Stages ||--o{ StagesRolePARequest : "stage assignment"
    
    ProjectArea {
        Long id PK
        String name
        String elmUrl
        Boolean active
        Timestamp syncDate
    }
    
    ELMRole {
        Long id PK
        String roleName
        String description
        Long projectAreaId FK
        Boolean isBuiltIn
    }
    
    ELMPermissions {
        Long id PK
        String permissionName
        String category
        String operationType
    }
    
    RolePermMapping {
        Long id PK
        Long roleId FK
        Long permissionId FK
    }
    
    AttrPermCondition {
        Long id PK
        String attributeName
        String workItemType
        Boolean readPerm
        Boolean writePerm
        Boolean requiredPerm
        Long projectAreaId FK
    }
    
    Request {
        Long id PK
        String requestType
        String status
        String requestor
        Timestamp createdDate
        String workOnRequestId
    }
```

#### Key Entities **(Project-Sourced)**

| Entity | Description | Key Attributes |
|--------|-------------|----------------|
| **ProjectArea** | ELM project area | • name<br>• elmUrl<br>• active<br>• syncDate |
| **ELMRole** | Role definition | • roleName<br>• description<br>• projectAreaId<br>• isBuiltIn |
| **ELMPermissions** | Permission definition | • permissionName<br>• category (TEAM_OPERATION, PROJECT_OPERATION)<br>• operationType |
| **RolePermMapping** | Role-permission association | • roleId<br>• permissionId |
| **AttrPermCondition** | Attribute permission rule | • attributeName<br>• workItemType<br>• readPerm, writePerm, requiredPerm<br>• projectAreaId |
| **AttrPermRole** | Roles for attribute condition | • attrPermConditionId<br>• roleId |
| **AttrPermWorkflow** | Workflow states for condition | • attrPermConditionId<br>• workflowStateId |
| **Request** | Configuration change request | • requestType<br>• status<br>• requestor<br>• workOnRequestId |
| **Stages** | Project lifecycle stages | • stageName (Concept, Development, Testing, Production)<br>• stageOrder |

### 5.2.5 Job Layer (Scheduled Tasks Module)

**General Purpose:** Execute background and scheduled tasks.

```mermaid
graph TB
    subgraph "Scheduled Jobs"
        SYNC_JOB[ELMDataSyncJob<br/>@Scheduled]
        PROC_JOB[ProcessRequestJob<br/>@Scheduled]
        STATUS_JOB[WorkONStatusSyncJob<br/>@Scheduled]
    end
    
    subgraph "Services"
        SYNC_SVC[SyncService]
        REQ_SVC[RequestService]
        XML_SVC[XMLGenerationService]
    end
    
    SYNC_JOB --> SYNC_SVC
    PROC_JOB --> REQ_SVC
    PROC_JOB --> XML_SVC
    STATUS_JOB --> REQ_SVC
    
    style SYNC_JOB fill:#9C27B0,color:#fff
    style PROC_JOB fill:#9C27B0,color:#fff
```

#### Job Components **(Project-Sourced)**

| Job | Schedule | Purpose | Actions |
|-----|----------|---------|---------|
| **ELMDataSyncJob** | Configurable cron<br/>(e.g., daily 2 AM) | Sync project areas from ELM | • Fetch project areas from ELM<br>• Update local database<br>• Fetch work item types<br>• Fetch workflow definitions<br>• Update metadata |
| **ProcessRequestJob** | Every 15 minutes **(AI-Generated Placeholder)** | Process approved requests | • Find requests with status=APPROVED<br>• Generate XML configurations<br>• Validate XML<br>• Deploy to ELM<br>• Update status to COMPLETED/FAILED |
| **WorkONStatusSyncJob** | Every 15 minutes **(AI-Generated Placeholder)** | Sync WorkON request status | • Find requests with status=PENDING<br>• Query WorkON API for status<br>• Update local request status<br>• Trigger processing if approved |

### 5.2.6 Integration Layer (External System Clients)

**General Purpose:** Integrate with external systems.

```mermaid
graph TB
    subgraph "Integration Components"
        ELM_CLIENT[AlmServerConnection]
        TEU[TEUUtility]
        XML_GEN[XMLGenerator]
        XML_MERGE[XMLMerger]
        XML_HOLDERS[XML Content Holders]
    end
    
    subgraph "Bean Models"
        ROLE_BEAN[RoleDefinitionBean]
        PERM_BEAN[RolePermissionBean]
        COND_BEAN[ConditionBean]
        ATTR_BEAN[AttributeBean]
        WF_BEAN[WorkflowPropertyBean]
    end
    
    SERVICE[Service Layer] --> ELM_CLIENT
    SERVICE --> TEU
    TEU --> XML_GEN
    TEU --> XML_MERGE
    
    XML_GEN --> ROLE_BEAN
    XML_GEN --> PERM_BEAN
    XML_GEN --> COND_BEAN
    
    style ELM_CLIENT fill:#00BCD4,color:#fff
    style TEU fill:#00BCD4,color:#fff
```

#### Integration Components **(Project-Sourced from com.bosch.rtc package)**

| Component | Purpose | Key Methods |
|-----------|---------|-------------|
| **AlmServerConnection** | Connect to ELM server | • connect(url, username, password)<br>• fetchProjectAreas()<br>• fetchWorkItemTypes()<br>• getWorkflowStates() |
| **TEUUtility** | Template Exchange operations | • uploadConfiguration(xml)<br>• downloadTemplate()<br>• validateConfiguration() |
| **XMLGenerator** | Generate XML from data | • generateRoleDefinitions(roles)<br>• generatePermissions(permissions)<br>• generateConditions(conditions) |
| **XMLMerger** | Merge XML files | • mergeRoles(baseXml, newXml)<br>• mergePermissions(baseXml, newXml)<br>• validateMergedXml() |
| **RoleXMLContentHolder** | Hold role XML content | • addRole(roleBean)<br>• toXML() |
| **PermXMLContentHolder** | Hold permission XML content | • addPermission(permBean)<br>• toXML() |
| **ConditionXMLContentHolder** | Hold condition XML content | • addCondition(condBean)<br>• toXML() |

#### XML Bean Classes **(Project-Sourced)**

JAXB-annotated beans for XML marshalling:

- **RoleDefinitionBean**: Role structure
- **RoleBean**: Individual role
- **RolePermissionBean**: Permission structure
- **TeamOperationBean**: Team operation permissions
- **ProjectOperationBean**: Project operation permissions
- **ConditionBean**: Attribute condition structure
- **AttributeBean**: Attribute properties
- **WorkflowPropertyBean**: Workflow state properties
- **ActionBean**: Permission actions (Read, Write, Execute)

### 5.2.7 Configuration Module

**General Purpose:** Spring configuration and application setup.

```mermaid
graph TB
    subgraph "Configuration"
        MAIN[EPCApplication<br/>@SpringBootApplication]
        SEC[SecurityConfig<br/>@Configuration]
        CACHE[Cache Configuration<br/>ehcache.xml]
        PROPS[application.properties]
    end
    
    MAIN --> SEC
    MAIN --> CACHE
    MAIN --> PROPS
    
    style MAIN fill:#673AB7,color:#fff
    style SEC fill:#673AB7,color:#fff
```

#### Configuration Components **(Project-Sourced)**

| Component | Purpose | Configuration |
|-----------|---------|---------------|
| **EPCApplication** | Main entry point | • @SpringBootApplication<br>• @EnableScheduling<br>• @EnableCaching<br>• Component scan configuration |
| **SecurityConfig** | Security configuration | • LDAP authentication<br>• CSRF protection<br>• Role-based authorization<br>• Session management |
| **application.properties** | Application properties | • Database connection<br>• Server port<br>• Logging levels<br>• ELM URLs |
| **ehcache.xml** | Cache configuration | • Cache regions<br>• TTL settings<br>• Eviction policies |

## Component Dependencies

```mermaid
graph LR
    WEB[Web Layer] --> BIZ[Business Layer]
    BIZ --> DATA[Data Layer]
    BIZ --> INTEG[Integration Layer]
    BIZ --> JOBS[Job Layer]
    DATA --> DOMAIN[Domain Layer]
    INTEG --> DOMAIN
    
    CONFIG[Configuration] -.configures.-> WEB
    CONFIG -.configures.-> BIZ
    CONFIG -.configures.-> DATA
    
    style BIZ fill:#4CAF50,color:#fff
```

**Dependency Rules:**
- **Web** depends on **Business** (not Data directly)
- **Business** depends on **Data** and **Integration**
- **Data** depends on **Domain**
- **Jobs** depend on **Business** (not Data directly)
- **No circular dependencies**
- **Domain** has minimal dependencies (only JPA annotations)

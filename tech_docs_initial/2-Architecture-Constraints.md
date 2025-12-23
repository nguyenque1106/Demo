# 2. Architecture Constraints

**General Purpose:** Limitations, mandates, or predefined decisions that shape the architecture.

## 2.1 Technical Constraints

**General Purpose:** Technology stack, platforms, and technical decisions that must be followed.

| Constraint | Description | Rationale / Impact |
|------------|-------------|-------------------|
| **TC-1: Java Platform** | Java 9 **(Project-Sourced from pom.xml)** | • Bosch standard Java version for enterprise applications<br>• Provides required features for XML processing and JPA<br>• Impact: Limited to Java 9 language features |
| **TC-2: Spring Boot Framework** | Spring Boot 2.2.8.RELEASE **(Project-Sourced from pom.xml)** | • Bosch-approved framework for enterprise applications<br>• Provides dependency injection, security, REST support<br>• Impact: Architecture follows Spring conventions |
| **TC-3: Database Technology** | MySQL 5.x/8.x **(Project-Sourced from application.properties)** | • Existing Bosch database infrastructure<br>• Database server: bp0vm025.emea.bosch.com:3306 **(Project-Sourced)**<br>• Impact: SQL dialect limitations, no NoSQL features |
| **TC-4: Build Tool** | Maven 3.x **(Project-Sourced from pom.xml)** | • Bosch standard for Java builds<br>• Supports dependency management and plugins<br>• Impact: Project structure follows Maven conventions |
| **TC-5: Packaging Format** | WAR (Web Application Archive) **(Project-Sourced from pom.xml)** | • Deployment to existing application servers<br>• Impact: Requires external servlet container |
| **TC-6: XML Processing** | JAXB for XML marshalling/unmarshalling **(Project-Sourced)** | • Standard Java API for XML binding<br>• Required for ELM configuration format<br>• Impact: Bean classes must be JAXB-annotated |
| **TC-7: Caching Technology** | Ehcache **(Project-Sourced from ehcache.xml)** | • Lightweight, embedded caching solution<br>• Second-level cache for Hibernate<br>• Impact: Cache configuration via ehcache.xml |
| **TC-8: ELM Integration** | REST API and Template Exchange Utility **(Project-Sourced)** | • ELM server provides REST APIs for data retrieval<br>• TEU (Template Exchange Utility) for configuration deployment<br>• Impact: Must conform to ELM API contracts and XML schemas |
| **TC-9: Authentication** | LDAP integration mandatory **(Project-Sourced)** | • Centralized Bosch LDAP directory<br>• No local user database allowed<br>• Impact: Spring Security LDAP configuration required |
| **TC-10: API Documentation** | Swagger/OpenAPI 3.x **(Project-Sourced from pom.xml)** | • Standard for REST API documentation<br>• Required for API consumers<br>• Impact: Controllers must be annotated for Swagger |

### Development Environment Constraints

| Constraint | Details |
|------------|---------|
| **IDE** | Eclipse **(Project-Sourced from .project file)** |
| **Version Control** | Git (assumed from workspace structure) |
| **Code Repository** | Internal Bosch repository (inferred) |
| **JDK Version** | Java 9 compliance required |

## 2.2 Organizational Constraints

**General Purpose:** Team structure, processes, and organizational policies.

| Constraint | Description | Impact |
|------------|-------------|--------|
| **OC-1: Bosch IT Standards** | Must comply with Bosch IT security and development standards | • Code review requirements<br>• Security scanning mandatory<br>• Compliance documentation needed |
| **OC-2: Team Structure** | Small development team **(AI-Generated Placeholder: 2-4 developers)** | • Limited resources for maintenance<br>• Architecture must be simple and maintainable<br>• Comprehensive documentation required |
| **OC-3: Release Process** | Changes require approval through WorkON system **(Project-Sourced)** | • All configuration changes go through approval workflow<br>• Audit trail requirement<br>• Cannot deploy directly to production |
| **OC-4: Database Access** | Shared database server with other applications **(Project-Sourced from DB URL)** | • Cannot modify database server configuration<br>• Must coordinate schema changes<br>• Performance impact from shared resources |
| **OC-5: Deployment Schedule** | Deployments during maintenance windows only **(AI-Generated Placeholder)** | • Limited deployment flexibility<br>• Must plan releases in advance<br>• Rollback procedures required |
| **OC-6: Support Model** | Business hours support (CET timezone) **(AI-Generated Placeholder)** | • System availability expectations align with support hours<br>• Critical issues handled during business hours |
| **OC-7: Training Resources** | Limited budget for user training **(AI-Generated Placeholder)** | • System must be intuitive and self-documenting<br>• Swagger UI serves as interactive documentation<br>• Online help and tooltips required |

### Budget and Timeline Constraints

- **Development Budget:** **[Number required – please provide]**
- **Maintenance Budget:** **[Number required – please provide]**
- **Project Timeline:** **[Number required – please provide]**
- **Team Availability:** **[Number required – please provide]**

## 2.3 Conventions

**General Purpose:** Coding standards, naming conventions, and design patterns to be followed.

### Code Conventions

#### Package Structure
```
com.bosch.epc
├── config         # Spring configuration classes
├── constant       # Constants and enums
├── controller     # REST API controllers
├── dao            # Data access layer (repositories)
├── datamodel      # JPA entities
├── dto            # Data transfer objects
├── exception      # Custom exceptions
├── jobs           # Scheduled jobs
├── model          # Business models and request/response objects
└── service        # Business logic layer

com.bosch.rtc.utils
└── templateexchange  # XML processing utilities
```

#### Naming Conventions

| Component | Convention | Example |
|-----------|-----------|---------|
| **Controllers** | `<Entity>Controller` | `ELMRoleController`, `RequestController` |
| **Services** | `<Entity>Service` or `<Entity>ServiceImpl` | `ELMRoleService`, `ProjectAreaServiceImpl` |
| **Repositories** | `<Entity>Repository` | `ELMRoleRepository`, `RequestRepository` |
| **DAOs** | `<Entity>DaoImpl` | `AttributeDaoImpl`, `StagesDaoImpl` |
| **Entities** | Singular noun | `ELMRole`, `Request`, `ProjectArea` |
| **DTOs** | `<Entity>DTO` | `PermissionWithMappingDTO` |
| **Constants** | UPPER_SNAKE_CASE | `DEFAULT_TIMEOUT`, `MAX_RETRY_COUNT` |
| **Enums** | `<Purpose>Enum` | `RoleEnum`, `StateGroupEnum`, `WITypeEnum` |

#### API Endpoint Conventions

```
/api/<resource>/<operation>

Examples:
- GET    /api/roles/getAllRoles
- POST   /api/roles/createRole
- PUT    /api/roles/updateRole/{id}
- DELETE /api/roles/deleteRole/{id}
- GET    /api/attrperm/getAttrPermConditions
```

### Design Patterns

| Pattern | Usage | Location |
|---------|-------|----------|
| **Repository Pattern** | Data access abstraction | DAO layer - all `*Repository` interfaces |
| **Service Layer Pattern** | Business logic encapsulation | Service layer - all `*Service` classes |
| **DTO Pattern** | Data transfer between layers | DTO package - request/response objects |
| **Factory Pattern** | XML bean creation | `XMLGenerator` class |
| **Template Method** | XML processing workflow | `XMLMerger` class |
| **Dependency Injection** | Loose coupling | Spring `@Autowired`, `@Service`, `@Repository` |
| **Scheduled Job Pattern** | Background tasks | Jobs package with `@Scheduled` annotation |

### Architecture Layers

```
┌─────────────────────────────────────┐
│   Presentation Layer (Controllers)   │  REST APIs, Swagger UI
├─────────────────────────────────────┤
│   Business Logic Layer (Services)    │  Business rules, validations
├─────────────────────────────────────┤
│   Data Access Layer (DAO/Repos)      │  Database operations
├─────────────────────────────────────┤
│   Persistence Layer (JPA Entities)   │  ORM mappings
└─────────────────────────────────────┘
```

**Layering Rules:**
- Controllers must NOT directly access repositories
- Services contain all business logic
- Repositories handle only data access
- No business logic in entities (anemic domain model)

### Error Handling Convention

```java
// Custom exceptions in exception package
throw new ResourceNotFoundException("Entity not found: " + id);
throw new EpcException("Configuration validation failed");
throw new ProcessConfigException("XML merge failed");

// Global exception handler
@ControllerAdvice
public class GlobalExceptionHandler { ... }
```

### Logging Convention

```java
// Logger declaration
private static final Logger logger = LoggerFactory.getLogger(ClassName.class);

// Log levels
logger.error("Critical error: {}", message);    // System errors
logger.warn("Warning: {}", message);            // Potential issues
logger.info("Operation completed: {}", message); // Important events
logger.debug("Debug info: {}", message);        // Development info
```

### Transaction Management

- Use `@Transactional` annotation for methods modifying data
- Read-only transactions: `@Transactional(readOnly = true)`
- Transaction boundaries at service layer

### Testing Conventions

```
src/test/java/com/bosch/epc/unittest/
├── <Component>Test.java       # Unit tests
└── <Component>IntegrationTest.java  # Integration tests
```

- Test class naming: `<ClassUnderTest>Test`
- Test method naming: `should<ExpectedBehavior>_when<Condition>()`
- Use JUnit for testing framework
- Mock external dependencies

### Documentation Standards

- Javadoc for all public classes and methods
- Swagger annotations for all REST endpoints
- README.md for setup and deployment
- Architecture documentation in `tech_docs/`

### Code Quality Standards

- **Checkstyle:** **[Configure if required – please provide rules]**
- **FindBugs/SpotBugs:** **[Configure if required]**
- **SonarQube:** **[Configure if required]**
- **Test Coverage Target:** >70% **(AI-Generated Placeholder)**

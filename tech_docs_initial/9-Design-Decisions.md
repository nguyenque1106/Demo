# 9. Design Decisions

**General Purpose:** Document key architectural decisions and rationale.

## Overview

This chapter records the most significant architectural decisions made during the design and development of the EPC system. Each decision is documented using the Architecture Decision Record (ADR) format.

## ADR Template

Each ADR follows this structure:
- **ID:** Unique identifier
- **Date:** When the decision was made
- **Status:** Proposed, Accepted, Deprecated, Superseded
- **Context:** The issue motivating this decision
- **Decision:** The change we're proposing or have agreed to
- **Consequences:** The results of applying this decision (positive and negative)
- **Alternatives Considered:** Other options that were evaluated

---

## ADR-001: Use Spring Boot Framework

**Date:** **[Provide project start date]**  
**Status:** Accepted  
**Deciders:** Development Team, Architecture Team

### Context

The EPC system requires a robust, enterprise-grade framework for building a RESTful web application with:
- Database integration
- Security features (LDAP, CSRF)
- Job scheduling
- External service integration
- Easy deployment

### Decision

Adopt **Spring Boot 2.2.8.RELEASE** **(Project-Sourced from pom.xml)** as the primary application framework.

### Rationale

1. **Bosch Standard:** Spring Boot is an approved framework within Bosch IT standards
2. **Rapid Development:** Auto-configuration reduces boilerplate code by 60-70% **(AI-Generated Placeholder)**
3. **Comprehensive Ecosystem:** 
   - Spring Security for authentication/authorization
   - Spring Data JPA for database access
   - Spring MVC for REST APIs
   - Spring Scheduling for background jobs
4. **Production-Ready:** Actuator provides health checks, metrics, monitoring
5. **Strong Community:** Large developer community, extensive documentation, active support
6. **Team Expertise:** Development team has prior Spring Boot experience

### Consequences

**Positive:**
- Faster development cycle
- Reduced code complexity
- Built-in best practices
- Easy to test with Spring Boot Test
- Excellent IDE support (Eclipse, IntelliJ)

**Negative:**
- Framework dependency - tight coupling to Spring ecosystem
- Learning curve for team members unfamiliar with Spring
- Spring Boot version upgrades may require code changes
- Larger deployment package size (~50MB WAR file)

### Alternatives Considered

1. **Java EE (Jakarta EE)**
   - Pros: Standard specification, vendor independence
   - Cons: More boilerplate, slower development, less modern features
   - Reason for rejection: Spring Boot offers better productivity

2. **Micronaut**
   - Pros: Faster startup, lower memory footprint
   - Cons: Smaller community, less mature, not Bosch-approved
   - Reason for rejection: Not aligned with Bosch standards

3. **Plain Servlets + Manual Configuration**
   - Pros: Full control, minimal dependencies
   - Cons: Significant development effort, reinventing the wheel
   - Reason for rejection: Not cost-effective

---

## ADR-002: Store Configurations in Database, Not Files

**Date:** **[Provide date]**  
**Status:** Accepted  
**Deciders:** Development Team, System Architect

### Context

ELM permission configurations ultimately need to be in XML format for deployment. We need to decide where to store configuration data before XML generation:

- **Option A:** Store directly as XML files in file system
- **Option B:** Store as structured data in database, generate XML on-demand

### Decision

Store all configuration data in **MySQL database** as structured relational data. Generate XML files only when needed for deployment.

### Rationale

1. **Query Capabilities:** SQL queries allow complex searches and reporting
   ```sql
   SELECT * FROM elm_role WHERE project_area_id = 1 AND is_built_in = FALSE;
   ```

2. **Data Integrity:** Database constraints enforce referential integrity
   - Foreign keys prevent orphaned records
   - Unique constraints prevent duplicates
   - NOT NULL constraints ensure required fields

3. **Version Control:** Each record has timestamps; audit trail possible
   - Created date
   - Modified date
   - Modified by user

4. **Validation:** Validate data before persisting to database
   - Catch errors early
   - Prevent invalid XML generation

5. **Rollback:** Easy to revert to previous state
   - Database transactions for atomicity
   - Backup and restore procedures

6. **Concurrent Access:** Database handles concurrent modifications
   - Transaction isolation
   - Locking mechanisms

### Consequences

**Positive:**
- Powerful query and reporting capabilities
- Strong data integrity guarantees
- Easy rollback and recovery
- Support for complex relationships
- Concurrent access handled by database
- Can generate XML in different formats if needed

**Negative:**
- More complex data model (20+ tables vs. simple XML files)
- XML generation adds processing time (1-3 seconds per configuration **(AI-Generated Placeholder)**)
- Requires database maintenance and backups
- Cannot directly edit configurations in text editor
- Need object-relational mapping (JPA/Hibernate)

### Alternatives Considered

1. **Store as XML Files**
   - Pros: Direct deployment format, human-readable, version control friendly (Git)
   - Cons: Hard to query, no referential integrity, concurrent editing issues, merging conflicts
   - Reason for rejection: Poor query capabilities, data integrity risks

2. **Store as JSON in NoSQL Database**
   - Pros: Flexible schema, document-oriented
   - Cons: Limited query capabilities, no foreign keys, not Bosch-approved infrastructure
   - Reason for rejection: Relational model fits better, existing MySQL infrastructure

---

## ADR-003: Integrate with WorkON for Approval Workflow

**Date:** **[Provide date]**  
**Status:** Accepted  
**Deciders:** Product Owner, Development Team

### Context

Configuration changes to ELM permissions are critical and can affect entire development teams. Changes need approval before deployment to prevent:
- Unauthorized modifications
- Accidental permission escalation
- Compliance violations

We need an approval workflow mechanism.

### Decision

Integrate with existing **WorkON system** **(Project-Sourced)** for approval workflow instead of building custom approval functionality.

### Rationale

1. **Existing Infrastructure:** WorkON is already used across Bosch for approvals
2. **Organizational Policy:** Bosch IT mandates using WorkON for critical change approvals
3. **Compliance:** WorkON provides audit trail required for compliance
4. **No Reinvention:** Avoid building and maintaining custom approval system (estimated 3-4 weeks development **(AI-Generated Placeholder)**)
5. **Familiar Interface:** Users already know WorkON interface
6. **Notification System:** WorkON handles email notifications to approvers

### Consequences

**Positive:**
- Compliance with organizational policy
- Reuse existing, proven system
- No development/maintenance of approval UI
- Automatic notification system
- Audit trail built-in
- Faster time to market (no approval UI to build)

**Negative:**
- External dependency on WorkON availability
  - If WorkON is down, cannot submit new requests
  - Mitigation: Queue requests locally, submit when available
- Network latency for WorkON API calls (100-300ms **(AI-Generated Placeholder)**)
- Must handle WorkON API changes/versioning
- Additional integration complexity
- Cannot customize approval workflow within EPC

### Alternatives Considered

1. **Build Custom Approval Workflow**
   - Pros: Full control, customizable, no external dependency
   - Cons: 3-4 weeks development **(AI-Generated Placeholder)**, ongoing maintenance, duplicate functionality
   - Reason for rejection: Not aligned with organizational policy, wasteful duplication

2. **Email-Based Approval**
   - Pros: Simple, no system integration
   - Cons: No audit trail, manual tracking, error-prone, non-standard
   - Reason for rejection: Insufficient audit trail, not compliant

3. **No Approval (Direct Deployment)**
   - Pros: Fastest workflow, no integration needed
   - Cons: Security risk, compliance violation, no oversight
   - Reason for rejection: Unacceptable security and compliance risk

---

## ADR-004: Use LDAP for Authentication (No Local Users)

**Date:** **[Provide date]**  
**Status:** Accepted  
**Deciders:** Security Team, Development Team

### Context

The EPC system needs user authentication. Options include:
- Local user database with passwords
- LDAP integration with Bosch Active Directory
- SSO/SAML integration
- OAuth 2.0 / OpenID Connect

### Decision

Implement **LDAP authentication** **(Project-Sourced from SecurityConfig)** using Spring Security LDAP module. No local user accounts or passwords stored in EPC database.

### Rationale

1. **Bosch Security Policy:** Mandatory use of centralized authentication (LDAP/Active Directory)
2. **Single Sign-On:** Users use same credentials as other Bosch systems
3. **Centralized Management:** User provisioning/deprovisioning managed by IT
4. **Password Policy:** Enforced by LDAP, not application
5. **Group Membership:** LDAP groups can be mapped to application roles
6. **Security:** No password storage in EPC database
7. **Compliance:** Meets Bosch IT security requirements

### Consequences

**Positive:**
- Compliant with Bosch security policy
- No password management in application
- Centralized user management
- Automatic user deactivation when leaving company
- Group-based role assignment
- No password reset functionality needed

**Negative:**
- Dependency on LDAP server availability
  - If LDAP is down, users cannot log in
  - Mitigation: LDAP has high availability (99.9% uptime **(AI-Generated Placeholder)**)
- Network latency for authentication (50-150ms **(AI-Generated Placeholder)**)
- Cannot easily test locally without VPN/LDAP access
  - Mitigation: Use embedded LDAP for local development
- LDAP configuration complexity
- Cannot have external users without LDAP accounts

### Alternatives Considered

1. **Local User Database**
   - Pros: No external dependency, works offline, full control
   - Cons: Violates security policy, password management burden, compliance issues
   - Reason for rejection: Policy violation, security risks

2. **SAML SSO**
   - Pros: Modern standard, better for web SSO
   - Cons: More complex, not required for internal tool, overkill
   - Reason for rejection: LDAP is sufficient and simpler

3. **OAuth 2.0 / OpenID Connect**
   - Pros: Modern, token-based, better for APIs
   - Cons: Additional infrastructure (authorization server), complex setup
   - Reason for rejection: Not required for internal tool

---

## ADR-005: Use RESTful API Over SOAP

**Date:** **[Provide date]**  
**Status:** Accepted  
**Deciders:** Development Team, API Consumers

### Context

EPC needs to expose APIs for:
- Web UI (Swagger UI)
- Potential future integrations (scripts, other systems)
- Scheduled jobs (internal)

API style options: REST vs. SOAP vs. GraphQL

### Decision

Implement **RESTful API** using Spring MVC with JSON payloads.

### Rationale

1. **Simplicity:** REST is simpler than SOAP (no WSDL, lighter payloads)
2. **JSON Format:** Easier to work with than XML for JavaScript clients
3. **HTTP Standards:** Uses standard HTTP methods (GET, POST, PUT, DELETE)
4. **Swagger Integration:** Easy to document with Swagger/OpenAPI
5. **Modern Practice:** REST is the current standard for web APIs
6. **Lightweight:** Smaller payloads compared to SOAP (30-50% smaller **(AI-Generated Placeholder)**)
7. **Caching:** HTTP caching mechanisms can be leveraged

### Consequences

**Positive:**
- Easy to consume from any HTTP client
- Self-documenting via Swagger UI
- Lightweight payloads
- Browser-testable (Swagger UI)
- Standard HTTP status codes
- Easy to implement with Spring MVC

**Negative:**
- No formal contract (no WSDL equivalent)
  - Mitigation: OpenAPI specification provides contract
- Less tooling for code generation compared to SOAP
- No built-in WS-* standards (security, transactions)
  - Not needed for this use case
- Must handle API versioning manually

### Alternatives Considered

1. **SOAP/XML**
   - Pros: Formal contract (WSDL), strong tooling, WS-* standards
   - Cons: Verbose XML, complex, heavyweight, outdated
   - Reason for rejection: Overkill, unnecessarily complex

2. **GraphQL**
   - Pros: Flexible queries, single endpoint, no over-fetching
   - Cons: Additional complexity, learning curve, caching challenges
   - Reason for rejection: Not needed for simple CRUD operations

3. **gRPC**
   - Pros: High performance, binary protocol, strong typing
   - Cons: Not browser-friendly, requires code generation, overkill
   - Reason for rejection: REST is sufficient for performance needs

---

## ADR-006: Use Ehcache for Caching (Not Distributed Cache)

**Date:** **[Provide date]**  
**Status:** Accepted  
**Deciders:** Development Team, Operations Team

### Context

Some data in EPC is frequently accessed but rarely changes:
- Project areas
- ELM roles (read-heavy)
- Work item types
- Workflow states

Caching can improve performance. Cache options:
- No caching
- Embedded cache (Ehcache)
- Distributed cache (Redis, Memcached)

### Decision

Use **Ehcache** **(Project-Sourced from ehcache.xml)** as embedded second-level cache for Hibernate.

### Rationale

1. **Simple Setup:** Embedded in application, no separate server
2. **Hibernate Integration:** Native support as Hibernate 2nd-level cache
3. **Sufficient for Scale:** Expected load: 100 concurrent users **(AI-Generated Placeholder)**, single app server
4. **Low Maintenance:** No additional infrastructure to manage
5. **Configuration-Based:** XML configuration, no code changes
6. **Mature Technology:** Proven, stable, well-documented

### Consequences

**Positive:**
- No additional infrastructure/servers required
- Simple configuration
- Reduced database load (50-70% for cached queries **(AI-Generated Placeholder)**)
- Faster response times (< 50ms for cached data **(AI-Generated Placeholder)**)
- Transparent to application code (Hibernate handles it)
- Works well for single-server deployment

**Negative:**
- Not suitable for multi-server deployment without synchronization
  - Cache inconsistency across servers
  - Mitigation: Currently single server; if scaling needed, can add Redis later
- Cache lives in application memory (increases heap usage)
  - Typical cache size: 500MB-1GB **(AI-Generated Placeholder)**
- No cache visibility/monitoring tools (unlike Redis)
- Cache cleared on application restart

### Alternatives Considered

1. **Redis (Distributed Cache)**
   - Pros: Distributed, shared across servers, better monitoring, persistent
   - Cons: Additional infrastructure, network latency, operational complexity
   - Reason for rejection: Overkill for current single-server deployment

2. **No Caching**
   - Pros: Simplest, no cache invalidation issues
   - Cons: Higher database load, slower response times, poor user experience
   - Reason for rejection: Unacceptable performance for frequently accessed data

3. **Memcached**
   - Pros: Simple, distributed, fast
   - Cons: Additional infrastructure, no native Hibernate support
   - Reason for rejection: Ehcache is sufficient and simpler

**Future Consideration:** If scaling to multiple application servers, migrate to Redis for distributed caching.

---

## ADR-007: Deploy as WAR (Not Standalone JAR)

**Date:** **[Provide date]**  
**Status:** Accepted  
**Deciders:** Operations Team, Development Team

### Context

Spring Boot applications can be packaged as:
- **Standalone JAR:** Embedded Tomcat, run with `java -jar`
- **WAR:** Deploy to external application server (Tomcat, WebSphere)

### Decision

Package EPC as **WAR file** **(Project-Sourced from pom.xml: `<packaging>war</packaging>`)** for deployment to existing Bosch application servers.

### Rationale

1. **Existing Infrastructure:** Bosch has standardized application servers (Tomcat/WebSphere)
2. **Shared Resources:** Multiple applications share same server, reducing costs
3. **Centralized Management:** Operations team manages server configuration
4. **Enterprise Monitoring:** Existing monitoring tools integrated with app servers
5. **SSL/TLS Offloading:** Handled by front-end load balancer/web server
6. **Organizational Standard:** Bosch policy for enterprise applications

### Consequences

**Positive:**
- Fits existing Bosch deployment model
- Shared server resources (cost-effective)
- Centralized configuration management
- Leverages existing monitoring/logging infrastructure
- Operations team familiar with deployment process

**Negative:**
- Cannot run standalone (requires app server)
- Deployment more complex than `java -jar`
- Shared server resources may cause performance issues
  - Mitigation: Resource monitoring, dedicated server if needed
- Server restart affects all deployed applications
- Less control over server configuration

### Alternatives Considered

1. **Standalone JAR (Embedded Tomcat)**
   - Pros: Simple deployment, full control, isolated resources
   - Cons: Not aligned with Bosch standards, additional server management
   - Reason for rejection: Policy preference for centralized app servers

2. **Docker Container**
   - Pros: Consistent environments, easy scaling, modern
   - Cons: Bosch container infrastructure not yet mature **(AI-Generated Placeholder)**
   - Reason for rejection: Infrastructure not ready for production containers

---

## ADR-008: Use Maven for Build Management

**Date:** **[Provide date]**  
**Status:** Accepted  
**Deciders:** Development Team

### Context

Need a build tool for:
- Dependency management
- Compilation
- Testing
- Packaging
- Plugin execution

Options: Maven, Gradle, Ant

### Decision

Use **Apache Maven 3.x** **(Project-Sourced from pom.xml)** as build tool.

### Rationale

1. **Bosch Standard:** Maven is the approved build tool for Java projects
2. **Convention Over Configuration:** Standard directory structure
3. **Mature Ecosystem:** Extensive plugin repository
4. **IDE Integration:** Excellent support in Eclipse **(Project-Sourced from .project file)**
5. **Repository Management:** Bosch Nexus repository integration
6. **Team Familiarity:** Team has Maven experience

### Consequences

**Positive:**
- Standard build process
- Easy dependency management
- Good IDE integration
- Bosch Nexus integration for dependencies
- Well-documented
- Predictable build lifecycle

**Negative:**
- XML configuration verbose
- Slower than Gradle for large projects
- Less flexible than Gradle
- Transitive dependency conflicts can be complex

### Alternatives Considered

1. **Gradle**
   - Pros: Faster builds, more flexible, Groovy/Kotlin DSL
   - Cons: Not Bosch standard, learning curve, less mature
   - Reason for rejection: Policy prefers Maven

2. **Ant**
   - Pros: Maximum flexibility, simple XML
   - Cons: No dependency management, too low-level, outdated
   - Reason for rejection: Inferior dependency management

---

## ADR-009: Use JPA/Hibernate for Database Access

**Date:** **[Provide date]**  
**Status:** Accepted  
**Deciders:** Development Team

### Context

Need to access MySQL database. Options:
- Raw JDBC
- MyBatis (SQL mapping)
- JPA/Hibernate (ORM)
- jOOQ (type-safe SQL)

### Decision

Use **JPA (Java Persistence API)** with **Hibernate** **(Project-Sourced)** as ORM implementation.

### Rationale

1. **Spring Data JPA:** Eliminates repository boilerplate
   ```java
   // No implementation needed!
   public interface ELMRoleRepository extends JpaRepository<ELMRole, Long> {
       List<ELMRole> findByProjectAreaName(String name);
   }
   ```

2. **Object-Oriented:** Work with entities, not SQL
3. **Database Portability:** Easier to switch databases (MySQL, PostgreSQL, Oracle)
4. **Automatic Schema Generation:** For development/testing
5. **Caching Support:** Second-level cache (Ehcache) integration
6. **Lazy Loading:** Efficient loading of relationships
7. **Team Expertise:** Team familiar with JPA/Hibernate

### Consequences

**Positive:**
- 60-70% less boilerplate code compared to JDBC **(AI-Generated Placeholder)**
- Cleaner, more maintainable code
- Type-safe query methods
- Automatic CRUD operations
- Built-in pagination and sorting
- Protection against SQL injection

**Negative:**
- Learning curve for JPA concepts (entities, relationships, fetch strategies)
- Potential N+1 query issues if not careful
  - Mitigation: Use `@EntityGraph`, fetch joins
- Less control over SQL (can be worked around with native queries)
- Hibernate can generate inefficient SQL in complex scenarios
- Memory overhead for entity management

### Alternatives Considered

1. **Raw JDBC**
   - Pros: Full SQL control, maximum performance
   - Cons: Massive boilerplate, error-prone, manual mapping
   - Reason for rejection: Too much boilerplate

2. **MyBatis**
   - Pros: SQL control, simple mapping
   - Cons: More boilerplate than JPA, less Spring integration
   - Reason for rejection: JPA + Spring Data is more productive

3. **jOOQ**
   - Pros: Type-safe SQL, great for complex queries
   - Cons: Code generation required, less popular than JPA
   - Reason for rejection: JPA is sufficient

---

## Summary of Key Decisions

| ADR | Decision | Status | Impact |
|-----|----------|--------|--------|
| ADR-001 | Spring Boot Framework | Accepted | High - Foundation of architecture |
| ADR-002 | Database Storage (Not Files) | Accepted | High - Data model complexity |
| ADR-003 | WorkON Integration | Accepted | Medium - External dependency |
| ADR-004 | LDAP Authentication | Accepted | High - Security model |
| ADR-005 | REST API | Accepted | Medium - API design |
| ADR-006 | Ehcache (Not Distributed) | Accepted | Low - Performance optimization |
| ADR-007 | WAR Packaging | Accepted | Medium - Deployment model |
| ADR-008 | Maven Build Tool | Accepted | Low - Build process |
| ADR-009 | JPA/Hibernate ORM | Accepted | High - Data access approach |

## Decision Review Process

**Review Frequency:** Quarterly or when significant changes are needed  
**Review Participants:** Development Team, Architecture Team, Product Owner  
**Review Criteria:**
- Has context changed?
- Are consequences as expected?
- Are better alternatives now available?
- Should decision be superseded?

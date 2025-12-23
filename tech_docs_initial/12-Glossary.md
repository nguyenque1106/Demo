# 12. Glossary

**General Purpose:** Define technical or domain-specific terms used throughout the documentation.

## A

**Action**  
A specific operation that can be performed on a resource in ELM (e.g., Read, Write, Execute, Delete). Actions are components of permissions.

**Actuator**  
Spring Boot Actuator - provides production-ready features like health checks, metrics, and monitoring endpoints for the application.

**ADR (Architecture Decision Record)**  
A document that captures an important architectural decision made along with its context and consequences.

**ALM (Application Lifecycle Management)**  
The product lifecycle management (governance, development, and maintenance) of computer programs. IBM's ELM (Engineering Lifecycle Management) is a suite of ALM tools.

**API (Application Programming Interface)**  
A set of defined rules and specifications that software programs can follow to communicate with each other. EPC exposes a RESTful API.

**Approval Workflow**  
The process by which configuration change requests are reviewed and approved before deployment. Managed by WorkON system for EPC.

**Attribute**  
A property or field of a work item in ELM (e.g., Priority, Severity, Owner, Status). Attributes can have read/write/required permissions configured.

**Attribute Permission Condition**  
A rule that defines read/write/required access to a work item attribute based on roles and workflow states.

**Audit Log**  
A security-relevant chronological record of system activities, including user actions, configuration changes, and access attempts.

## B

**Batch Processing**  
Processing multiple items in a single operation for efficiency. EPC supports batch operations for creating multiple attribute permissions at once.

**Bean**  
In Spring framework, a Java object that is instantiated, assembled, and managed by the Spring IoC container. Also refers to JAXB-annotated classes for XML mapping in EPC.

**Boilerplate Code**  
Sections of code that have to be included in many places with little or no alteration. Spring Boot and JPA reduce boilerplate significantly.

**Built-in Role**  
A role that is predefined by ELM and cannot be deleted (e.g., "JazzAdmins", "JazzUsers"). Opposite of custom role.

## C

**Cache**  
A hardware or software component that stores data temporarily for faster access. EPC uses Ehcache to cache frequently accessed data.

**Cache Eviction**  
The process of removing entries from cache based on policies like LRU (Least Recently Used) or TTL (Time To Live).

**CCM (Change and Configuration Management)**  
A component of IBM ELM that manages change requests, work items, and configurations.

**CSRF (Cross-Site Request Forgery)**  
A type of web security vulnerability where malicious websites trick users' browsers into performing unwanted actions. EPC implements CSRF protection via Spring Security.

**Circuit Breaker**  
A design pattern that prevents cascading failures by detecting failures and encapsulating the logic of preventing a failure from constantly recurring.

**Configuration-as-Code**  
The practice of storing configuration in structured, version-controlled format rather than manual edits. EPC stores configurations in database.

**Controller**  
In MVC architecture, the component that handles HTTP requests and returns responses. EPC controllers handle REST API endpoints.

**CRUD (Create, Read, Update, Delete)**  
The four basic operations for persistent storage. EPC provides CRUD operations for roles, permissions, and configurations.

**Custom Role**  
A role created by users (not built-in to ELM) that can be customized and deleted.

## D

**DAO (Data Access Object)**  
A design pattern that provides an abstract interface to a database, allowing for separation of business logic from persistence logic.

**Dependency Injection (DI)**  
A design pattern where objects receive their dependencies from external sources rather than creating them. Spring framework provides DI via `@Autowired`.

**Deployment**  
The process of uploading and applying XML configurations to an ELM server.

**DTO (Data Transfer Object)**  
An object that carries data between processes, typically between the presentation and service layers.

**Dry Run**  
A test run of a process without actually executing changes, used to verify configuration before actual deployment.

## E

**Ehcache**  
An open-source, standards-based cache for Java. EPC uses Ehcache as Hibernate second-level cache.

**ELM (Engineering Lifecycle Management)**  
IBM's suite of tools for managing engineering lifecycle (formerly known as Jazz/RTC). EPC manages permissions for ELM.

**Entity**  
In JPA, a lightweight persistence domain object that represents a table in the database. EPC entities include `ELMRole`, `Request`, `ProjectArea`, etc.

**EPC (ELM Permission Compass)**  
The system described in this documentation - a web application for managing ELM permissions and roles.

## F

**Fetch Strategy**  
In JPA, defines when related entities are loaded: EAGER (immediately) or LAZY (when accessed). EPC uses LAZY fetching for performance.

**Foreign Key**  
A field in a database table that uniquely identifies a row in another table, establishing a relationship between tables.

## G

**Global Exception Handler**  
A centralized error handling mechanism that catches exceptions from all controllers and provides consistent error responses.

**Graceful Degradation**  
The ability of a system to continue operating (possibly with reduced functionality) when part of it fails.

## H

**Health Check**  
An automated test to determine whether a service or system is functioning properly. Spring Boot Actuator provides `/actuator/health` endpoint.

**Hibernate**  
An object-relational mapping (ORM) framework for Java. EPC uses Hibernate as the JPA implementation.

**HTTP Status Code**  
A three-digit code returned by a server indicating the result of an HTTP request (e.g., 200 OK, 404 Not Found, 500 Internal Server Error).

## I

**Idempotent**  
An operation that produces the same result regardless of how many times it's executed (e.g., PUT and DELETE are idempotent, POST typically is not).

**Integration Test**  
A test that verifies the correct interaction between different components or systems.

## J

**JAXB (Java Architecture for XML Binding)**  
A Java standard that defines mapping between Java objects and XML. EPC uses JAXB to generate XML configurations.

**JPA (Java Persistence API)**  
A Java specification for object-relational mapping. Hibernate is EPC's JPA implementation.

**JPQL (Java Persistence Query Language)**  
An object-oriented query language for JPA entities, similar to SQL but operates on entities rather than tables.

**JSON (JavaScript Object Notation)**  
A lightweight data interchange format. EPC's REST API uses JSON for requests and responses.

## L

**Layered Architecture**  
An architectural pattern where the application is divided into layers (presentation, business logic, data access), each with distinct responsibilities.

**LDAP (Lightweight Directory Access Protocol)**  
A protocol for accessing and maintaining distributed directory information services. EPC uses LDAP for user authentication via Bosch Active Directory.

**LDAPS**  
LDAP over SSL/TLS - secure version of LDAP protocol using encryption.

**Load Balancer**  
A device or software that distributes network traffic across multiple servers to ensure no single server is overwhelmed.

**Lazy Loading**  
In JPA, a strategy where related entities are not loaded from the database until explicitly accessed, improving performance.

## M

**Marshalling**  
The process of converting Java objects into XML or JSON format. Opposite of unmarshalling.

**Maven**  
A build automation and dependency management tool for Java projects. EPC uses Maven for building and packaging.

**Microservice**  
An architectural style where an application is composed of small, independent services. EPC is a monolithic application (not microservice).

**Mockito**  
A mocking framework for Java unit tests, allowing creation of mock objects. EPC uses Mockito in tests.

**MVC (Model-View-Controller)**  
An architectural pattern that separates an application into three interconnected components. Spring MVC implements this pattern.

## N

**N+1 Query Problem**  
A performance issue in ORMs where fetching a list of N entities results in N+1 database queries (1 for the list + N for related entities). EPC mitigates this with proper fetch strategies.

**Native Query**  
A raw SQL query executed directly against the database, bypassing JPA/Hibernate's query generation.

## O

**ORM (Object-Relational Mapping)**  
A technique for converting data between incompatible type systems (object-oriented programming and relational databases). Hibernate provides ORM for EPC.

**OAuth**  
An open standard for access delegation commonly used for token-based authentication. (Not currently used in EPC; LDAP is used instead.)

## P

**Pagination**  
Dividing large result sets into smaller "pages" for better performance and usability.

**Permission**  
An authorization to perform a specific operation in ELM (e.g., "Modify Work Items", "Save Process"). Permissions are mapped to roles.

**Persistence**  
The storage of data in a non-volatile medium (like a database) so it survives beyond the application's execution.

**POJO (Plain Old Java Object)**  
A Java object not bound by any special restriction, typically used for entity classes.

**Process Template**  
In ELM, a template that defines the structure, roles, permissions, and workflows for a project area. EPC generates configurations that merge with process templates.

**Project Area**  
A workspace in ELM that contains work items, source code, builds, and test results for a specific project. EPC manages permissions at the project area level.

**Project Operation**  
A high-level permission category in ELM related to project-wide actions (e.g., project administration, SCM access).

## Q

**Query Cache**  
A cache that stores the results of database queries to avoid repeated execution of the same query.

## R

**RBAC (Role-Based Access Control)**  
An approach to access control where permissions are assigned to roles, and roles are assigned to users. EPC uses RBAC.

**Read Replica**  
A copy of a database that serves read-only queries, reducing load on the primary database.

**Repository Pattern**  
A design pattern that encapsulates data access logic, providing a collection-like interface for accessing domain objects.

**Request**  
In EPC, a formal submission for a configuration change that requires approval before deployment.

**REST (Representational State Transfer)**  
An architectural style for web services using HTTP methods (GET, POST, PUT, DELETE). EPC exposes a RESTful API.

**Rollback**  
The process of reverting a transaction or deployment to a previous state, typically after a failure.

**Role**  
A named set of permissions that can be assigned to users. EPC manages role definitions for ELM projects.

**RTC (Rational Team Concert)**  
IBM's former name for what is now part of ELM/CCM. EPC code still references RTC in some package names.

## S

**SAML (Security Assertion Markup Language)**  
An XML-based standard for exchanging authentication and authorization data. (Not currently used in EPC.)

**Schema**  
The structure of a database (tables, columns, relationships) or XML document (element definitions, attributes).

**Second-Level Cache**  
In Hibernate, a cache shared across sessions that stores entities, collections, and query results. EPC uses Ehcache as second-level cache.

**Service Layer**  
The layer in a layered architecture that contains business logic and orchestrates operations between controllers and data access layer.

**Session**  
A temporary interactive information exchange between a user and the system. HTTP sessions track user authentication state.

**Soft Delete**  
Marking a record as inactive/deleted without physically removing it from the database, allowing for recovery and audit trail.

**Spring Boot**  
An opinionated framework built on top of Spring Framework that simplifies configuration and deployment of Spring applications. EPC is built with Spring Boot 2.2.8.

**Spring Data JPA**  
A Spring module that simplifies JPA-based data access by providing repository abstractions. EPC uses Spring Data JPA for database operations.

**Spring Security**  
A framework providing authentication and authorization for Java applications. EPC uses Spring Security with LDAP integration.

**SQL Injection**  
A security vulnerability where malicious SQL code is inserted into queries. EPC prevents this by using JPA parameterized queries.

**Swagger/OpenAPI**  
A specification and toolset for documenting and testing REST APIs. EPC provides Swagger UI for API exploration.

## T

**Team Operation**  
A permission category in ELM related to team-level actions (e.g., team area management, process role assignments).

**Template Exchange Utility (TEU)**  
A utility provided by IBM ELM for uploading and applying process template configurations. EPC uses TEU to deploy configurations.

**Transaction**  
A sequence of database operations that are treated as a single unit of work - either all succeed or all fail (rollback).

**Transitive Dependency**  
A dependency that is required by one of your direct dependencies. Maven manages transitive dependencies automatically.

**TTL (Time To Live)**  
The duration for which data remains valid in a cache before being refreshed or evicted.

## U

**Unit Test**  
A test that verifies the correctness of a single unit of code (typically a method or class) in isolation from dependencies.

**Unmarshalling**  
The process of converting XML or JSON data into Java objects. Opposite of marshalling.

**URI (Uniform Resource Identifier)**  
A string that identifies a resource, typically used in REST APIs (e.g., `/api/roles/123`).

## V

**Validation**  
The process of checking that data meets specified criteria before processing. EPC validates configurations before generating XML.

**VPN (Virtual Private Network)**  
An encrypted network connection. Required for accessing Bosch internal systems like LDAP from external networks.

## W

**WAR (Web Application Archive)**  
A JAR file used to distribute Java web applications. EPC is packaged as a WAR file for deployment to application servers.

**Work Item**  
A unit of work in ELM (e.g., Defect, Task, Story, Epic). Work items have attributes and follow workflows.

**Work Item Type**  
A category of work items with specific attributes and workflows (e.g., Defect, Task, Enhancement Request).

**WorkON**  
A Bosch internal system for managing approval workflows. EPC integrates with WorkON for configuration change approvals.

**Workflow**  
A sequence of states that a work item transitions through during its lifecycle (e.g., New → In Progress → Resolved → Closed).

**Workflow State**  
A specific status within a workflow (e.g., "In Progress", "Code Review", "Done"). Attribute permissions can vary by workflow state.

## X

**XML (eXtensible Markup Language)**  
A markup language for encoding documents. ELM configurations are in XML format; EPC generates these XML files.

**XML Marshalling**  
See Marshalling.

**XML Schema (XSD)**  
A definition of the structure and content rules for an XML document. EPC validates generated XML against ELM's schema.

**XSRF**  
Alternative term for CSRF (Cross-Site Request Forgery). See CSRF.

## Acronym Quick Reference

| Acronym | Full Term |
|---------|-----------|
| ADR | Architecture Decision Record |
| ALM | Application Lifecycle Management |
| API | Application Programming Interface |
| CCM | Change and Configuration Management |
| CRUD | Create, Read, Update, Delete |
| CSRF | Cross-Site Request Forgery |
| DAO | Data Access Object |
| DI | Dependency Injection |
| DTO | Data Transfer Object |
| ELM | Engineering Lifecycle Management |
| EPC | ELM Permission Compass |
| HTTPS | HTTP Secure |
| JAXB | Java Architecture for XML Binding |
| JDBC | Java Database Connectivity |
| JPA | Java Persistence API |
| JPQL | Java Persistence Query Language |
| JSON | JavaScript Object Notation |
| JVM | Java Virtual Machine |
| LDAP | Lightweight Directory Access Protocol |
| LDAPS | LDAP over SSL/TLS |
| LRU | Least Recently Used |
| MVC | Model-View-Controller |
| OAuth | Open Authorization |
| OOP | Object-Oriented Programming |
| ORM | Object-Relational Mapping |
| POJO | Plain Old Java Object |
| RBAC | Role-Based Access Control |
| REST | Representational State Transfer |
| RTC | Rational Team Concert |
| SAML | Security Assertion Markup Language |
| SCM | Source Code Management |
| SQL | Structured Query Language |
| SSL | Secure Sockets Layer |
| TEU | Template Exchange Utility |
| TLS | Transport Layer Security |
| TTL | Time To Live |
| URI | Uniform Resource Identifier |
| URL | Uniform Resource Locator |
| VPN | Virtual Private Network |
| WAR | Web Application Archive |
| XML | eXtensible Markup Language |
| XSD | XML Schema Definition |

## Domain-Specific Terms

**Stages** (Project Lifecycle Stages)  
The phases of a project lifecycle in EPC: Concept, Development, Testing, Production. Roles can be mapped to specific stages.

**PA (Project Area)**  
Abbreviation commonly used in EPC code for Project Area.

**Attr Perm (Attribute Permission)**  
Shorthand for Attribute Permission Condition.

**Role-Perm Mapping**  
The association between roles and permissions.

**Sync Job**  
Scheduled background task that synchronizes data between EPC and ELM servers.

**Request Status Values**  
- DRAFT: Created but not submitted
- PENDING: Awaiting approval
- APPROVED: Approved, ready for processing
- REJECTED: Rejected by approver
- IN_PROGRESS: Being processed
- COMPLETED: Successfully deployed
- FAILED: Deployment failed

**Permission Categories**  
- TEAM_OPERATION: Team-level permissions
- PROJECT_OPERATION: Project-level permissions
- PROCESS_OPERATION: Process configuration permissions **(AI-Generated Placeholder)**

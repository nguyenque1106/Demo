# 1. Introduction and Goals

**General Purpose:** Define the purpose and scope of the EPC (ELM Permission Compass) system.

## Overview

The ELM Permission Compass (EPC) is a web-based configuration management tool designed to streamline the process of managing roles, permissions, and attribute-level access controls for Engineering Lifecycle Management (ELM) systems at Bosch. The system automates the generation and deployment of XML-based permission configurations, reducing manual effort and minimizing configuration errors.

## 1.1 Requirements Overview

**General Purpose:** Summary of functional requirements.

### Core Functional Requirements

1. **Project Area Management**
   - Synchronize project areas from ELM servers **(Project-Sourced)**
   - Display available project areas with access control
   - Support multiple ELM server instances
   - Automated sync via scheduled jobs

2. **Role Management**
   - Create, update, and delete ELM roles
   - Map roles to permissions
   - Support built-in and custom roles
   - Role hierarchy and inheritance
   - Role definitions export to XML

3. **Permission Management**
   - Configure team operations (process roles, team areas)
   - Configure project operations (project administration, SCM, build)
   - Define action-based permissions (Read, Write, Execute, Delete)
   - Map permissions to roles with granular control
   - Support workflow-state-based permissions

4. **Attribute Permission Configuration**
   - Configure read/write/required permissions for work item attributes
   - Role-based attribute access control
   - Workflow-state-dependent attribute behaviors
   - Support for built-in and custom attributes
   - Bulk configuration capabilities for efficiency
   - Attribute permission inheritance

5. **Stage-Based Role Mapping**
   - Map roles to project lifecycle stages (Concept, Development, Testing, Production)
   - Configure stage-specific role assignments
   - Support transition workflows between stages
   - Stage-based permission inheritance

6. **Request Management**
   - Create configuration change requests
   - Track request status (Draft, Pending, Approved, Rejected, Completed, Failed)
   - Integration with WorkON approval workflow **(Project-Sourced)**
   - Request history and audit trail
   - Support for bulk request operations

7. **Configuration Generation & Deployment**
   - Generate XML configuration files (roles, permissions, conditions)
   - Merge configurations with process templates using Template Exchange Utility
   - Validate XML structure and content against ELM schemas
   - Deploy configurations to ELM servers
   - Rollback support for failed deployments

8. **User Authentication & Authorization**
   - LDAP integration for user authentication **(Project-Sourced)**
   - Role-based access control (Admin, Power User, User) **(Project-Sourced)**
   - Session management with timeout
   - CSRF protection **(Project-Sourced)**

9. **Job Scheduling & Automation**
   - ELM data synchronization job
   - Request processing job
   - WorkON status synchronization job
   - Configurable cron schedules

### Non-Functional Requirements

| Category | Requirement | Target |
|----------|-------------|--------|
| **Performance** | Concurrent users | Up to 100 users **(AI-Generated Placeholder)** |
| **Performance** | API response time | < 2 seconds for 95% of requests **(AI-Generated Placeholder)** |
| **Performance** | Bulk operations | < 10 seconds for 100 items **(AI-Generated Placeholder)** |
| **Availability** | Uptime | 99% during business hours (8am-6pm CET) **(AI-Generated Placeholder)** |
| **Availability** | Planned maintenance window | Weekends only **(AI-Generated Placeholder)** |
| **Reliability** | XML validation success | 100% before deployment |
| **Reliability** | Data loss tolerance | Zero data loss for approved configurations |
| **Scalability** | Project areas supported | Up to 50 project areas **(AI-Generated Placeholder)** |
| **Scalability** | Roles per project | Up to 200 roles **(AI-Generated Placeholder)** |
| **Security** | Authentication | LDAP mandatory **(Project-Sourced)** |
| **Security** | Authorization | Role-based access control |
| **Security** | Audit logging | All configuration changes logged |
| **Usability** | Learning curve | New users productive within 1 day **(AI-Generated Placeholder)** |

## 1.2 Quality Goals

**General Purpose:** Critical non-functional objectives (e.g., Understandability, Efficiency, Testability).

| Priority | Quality Goal | Motivation | Success Criteria |
|----------|-------------|------------|------------------|
| **1** | **Reliability** | Configuration errors can disrupt entire development teams. System must generate accurate, validated XML configurations that work correctly on first deployment. | • XML validation success rate: 100%<br>• Zero configuration rollbacks due to system errors<br>• Automated validation before deployment<br>• Configuration integrity checks |
| **2** | **Maintainability** | The system will evolve with ELM versions, organizational changes, and new permission models. Code must be modular, well-documented, and testable. | • New permission types added within 2 days **(AI-Generated Placeholder)**<br>• Code quality: >80% test coverage **(AI-Generated Placeholder)**<br>• Clear separation of concerns (Controller-Service-DAO)<br>• Comprehensive API documentation via Swagger |
| **3** | **Usability** | Users are ELM administrators and project managers, not necessarily developers. Interface must be intuitive and self-explanatory. | • User can create role configuration in < 5 minutes **(AI-Generated Placeholder)**<br>• Swagger UI for API testing and exploration<br>• Clear error messages and validation feedback<br>• Comprehensive user documentation |
| **4** | **Security** | Manages sensitive access control data affecting project confidentiality and integrity. Must prevent unauthorized access and configuration tampering. | • LDAP authentication mandatory **(Project-Sourced)**<br>• Role-based authorization enforced<br>• Audit logging for all changes<br>• CSRF protection enabled **(Project-Sourced)**<br>• SQL injection prevention via JPA |
| **5** | **Performance** | Batch operations on large project areas with hundreds of roles and attributes must complete in reasonable time without blocking users. | • Bulk attribute save: < 10 seconds for 100 items **(AI-Generated Placeholder)**<br>• Project sync job: < 5 minutes **(AI-Generated Placeholder)**<br>• Ehcache for frequently accessed data **(Project-Sourced)**<br>• Scheduled jobs don't block user operations |
| **6** | **Testability** | Configuration changes must be verifiable before production deployment to prevent breaking active projects. | • Unit test coverage > 70% **(AI-Generated Placeholder)**<br>• Integration tests for critical workflows<br>• Test data management utilities<br>• Swagger UI for manual API testing |
| **7** | **Interoperability** | Must integrate seamlessly with multiple external systems (ELM servers, WorkON, LDAP) using standard protocols. | • REST API for all operations<br>• Standard XML format for ELM configurations<br>• LDAP protocol for authentication **(Project-Sourced)**<br>• WorkON integration for approvals **(Project-Sourced)** |

## 1.3 Stakeholders

**General Purpose:** Identify all parties interested in or affected by the architecture.

| Stakeholder | Role | Expectations / Concerns |
|-------------|------|------------------------|
| **ELM Administrators** | Primary Users | • Simple, intuitive interface for managing permissions<br>• Fast configuration deployment<br>• Reliable XML generation without errors<br>• Audit trail for compliance |
| **Project Managers** | Secondary Users | • Quick setup of new project areas<br>• Standardized permission models<br>• Visibility into role assignments<br>• Stage-based role management |
| **Development Teams** | End Beneficiaries | • Correct permissions applied to work items<br>• No disruption during configuration updates<br>• Consistent permission behavior across projects |
| **IT Security Team** | Compliance Auditors | • LDAP integration for centralized authentication<br>• Role-based access control<br>• Complete audit logs<br>• Security vulnerability management |
| **System Administrators** | Operations Team | • Easy deployment and maintenance<br>• Monitoring and logging capabilities<br>• Scheduled job management<br>• Database backup and recovery procedures |
| **Development Team** | Maintainers | • Clean, maintainable code architecture<br>• Comprehensive documentation<br>• Automated testing infrastructure<br>• Clear separation of concerns |
| **ELM Server Team** | External Dependency | • Standard XML format compliance<br>• Minimal performance impact on ELM servers<br>• Proper error handling for server failures<br>• API version compatibility |
| **WorkON System** | Integration Partner | • Reliable request approval workflow integration<br>• Status synchronization<br>• Error notification on failures |
| **Bosch IT Management** | Sponsors | • Cost-effective solution<br>• Reduced manual effort for permission management<br>• Compliance with Bosch IT standards<br>• Scalability for future growth |

## System Context

EPC operates as a middleware system between:
- **Users** (ELM Administrators, Project Managers)
- **ELM Servers** (source of project data, target for configurations)
- **WorkON** (approval workflow system)
- **LDAP** (authentication provider)

The system's primary value proposition is **automation and validation** of complex XML-based permission configurations that would otherwise require manual editing and are prone to human error.

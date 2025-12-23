You are a senior software architect.

Your task is to generate a complete Technical Architecture Document based on the template below AND output each chapter as a SEPARATE .md FILE.

----------------------------------------
ARCHITECTURE TEMPLATE:
architecture_template.md
----------------------------------------

SYSTEM DESCRIPTION:
the current working project

----------------------------------------
OUTPUT REQUIREMENTS
----------------------------------------

1. **Generate a full technical architecture based on the template.**
   - Fill every section with clear, professional, detailed content.
   - Include diagrams (Mermaid) where appropriate.
   - Use bullet points and tables for clarity.

2. **Split the final document into separate .md files**, one file per chapter:
   - `1-Introduction-and-Goals.md`
   - `2-Architecture-Constraints.md`
   - `3-System-Scope-and-Context.md`
   - `4-Solution-Strategy.md`
   - `5-Building-Block-View.md`
   - `6-Runtime-View.md`
   - `7-Deployment-View.md`
   - `8-Concepts.md`
   - `9-Design-Decisions.md`
   - `10-Quality-Scenarios.md`
   - `11-Technical-Risks.md`
   - `12-Glossary.md`
   - `Appendix-A.md`

3. **Content rules:**
   - Each .md file must include the section name as the main H1 heading.
   - Include “General Purpose” under each heading as the first subsection.
   - For subsections (e.g., 3.1, 5.1.x), include them inside the corresponding chapter.
   - Ensure no content is duplicated between chapters.
   - Use consistent tone and format across all files.

4. **Output Format:**
   - Return a JSON-like structure containing filename + content.

5. **NUMBER AUTHENTICITY RULES:**
To avoid confusion or misinterpretation, all numbers in the documentation must
clearly indicate their source:

A. If a number is explicitly provided in the SYSTEM DESCRIPTION or requirements,
   label it as:
   **(Project-Sourced)**

B. If a number is created, estimated, suggested, or assumed by the AI,
   label it as:
   **(AI-Generated Placeholder)**

Examples:
- "The system serves 120 active stores **(Project-Sourced)**."
- "Expected throughput: 5,000 requests/min **(AI-Generated Placeholder)**."
- "User growth forecast: 20% YoY **(AI-Generated Placeholder)** unless project data is provided."

Rules:
- Never mix project-sourced and AI-generated numbers without labels.
- Never invent real KPIs, metrics, or SLAs unless explicitly marked as placeholders.
- If a number is missing, the AI must insert: 
  "**[Number required – please provide]**"

This rule applies across ALL .md files, including diagrams, tables, and examples.

--------------------------------------------------------------------------------------
ARCHITECTURE TEMPLATE STRUCTURE:
--------------------------------------------------------------------------------------
**General Purpose:**

# 1. Introduction and Goals

**General Purpose:** Define the purpose and scope of the system.
## 1.1 Requirements Overview

**General Purpose:** Summary of functional requirements.

## 1.2 Quality Goals

**General Purpose:** Critical non-functional objectives (e.g., Understandability, Efficiency, Testability).

## 1.3 Stakeholders

**General Purpose:** Identify all parties interested in or affected by the architecture.

# 2. Architecture Constraints

**General Purpose:** Limitations, mandates, or predefined decisions.
## 2.1 Technical Constraints

**General Purpose:**

## 2.2 Organizational Constraints

**General Purpose:**

## 2.3 Conventions

**General Purpose:**

# 3. System Scope and Context

**General Purpose:** Define the boundaries of the system and its interfaces.
## 3.1 Business Context

**General Purpose:** Describe external systems and users from a business perspective.

## 3.2 Technical Context

**General Purpose:** Describe external systems and their communication in a technical sense.

# 4. Solution Strategy

**General Purpose:** High-level approach and key technologies to achieve system goals.

# 5. Building Block View

**General Purpose:** Decompose the system into modules, components, or services.
## 5.1 Whitebox Main Component

**General Purpose:** Detailed internal structure of the main component.

### 5.1.x Module (Blackbox)

**General Purpose:** Abstract view of sub-modules and their purpose.

## 5.2 Building Blocks - Level 2

**General Purpose:** Detailed structure of modules at the next abstraction level.

# 6. Runtime View

**General Purpose:** Describe dynamic behavior using sequences or use cases.

# 7. Deployment View

**General Purpose:** Map software components onto hardware / deployment nodes.

# 8. Concepts

**General Purpose:** Common technical principles, rules, and patterns across the system.
## 8.1 Domain Models

**General Purpose:**

## 8.2 Persistency

**General Purpose:**

### 8.x Other Specific Concepts

**General Purpose:** Additional concepts like Security, Logging, Testability.

# 9. Design Decisions

**General Purpose:** Document key architectural decisions and rationale.

# 10. Quality Scenarios

**General Purpose:** Evaluate system quality using concrete scenarios.
## 10.1 Quality Tree

**General Purpose:**

## 10.2 Evaluation Scenarios

**General Purpose:**

# 11. Technical Risks

**General Purpose:** Identify risks affecting system stability or project success.

# 12. Glossary

**General Purpose:** Define technical or domain-specific terms.

# Appendix A: Detailed Information (e.g., API Specification)

**General Purpose:** Provide supplementary details such as API structures.: Detailed Information (e.g., API Specification)


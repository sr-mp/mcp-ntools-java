# MCP NTools Java: Architecture Overview

## Overview
The MCP NTools Java server is a Spring Boot application implementing the Model Context Protocol (MCP) for agent-driven network security automation. It exposes a robust, protocol-compliant API for tools like Nmap and Nuclei, designed for extensibility, reliability, and developer onboarding.

---

## Key Features
- **MCP Protocol Compliance:** Implements `/mcp`, `/mcp/tools`, `/mcp/tools/{toolName}/describe`, and `/mcp/command/{toolName}` endpoints with strict JSON schema adherence.
- **Tool Registry:** Central registry for tool metadata, schemas, and descriptions, supporting dynamic discovery and documentation.
- **Extensible Tool Integration:** Easily add new tools by registering DTOs, service logic, and metadata.
- **Consistent Error Handling:** All endpoints return structured JSON with `error` fields for robust agent integration.
- **Comprehensive Documentation:** Javadoc on all core classes, onboarding guides, and usage examples for new developers.

---

## Design Choices
- **Spring Boot:** Chosen for rapid REST API development, dependency injection, and testability.
- **DTO-Driven API:** All requests and responses use explicit DTOs for schema clarity and validation.
- **Centralized Tool Registry:** Enables dynamic tool listing, schema introspection, and future automation (e.g., OpenAPI generation).
- **Separation of Concerns:** Controllers handle HTTP, services encapsulate tool logic, DTOs define schemas, and registry manages metadata.
- **Test-First Development:** Integration and edge case tests ensure protocol compliance and robustness.

---

## Code Organization
- **Main Application:**
  - `src/main/java/com/example/mcp/McpNtoolsApplication.java` — Spring Boot entry point.
- **Controllers:**
  - `controller/` — REST endpoints for MCP protocol, tool commands, and metadata.
- **Services:**
  - `tool/` — Business logic for each tool (e.g., Nmap, Nuclei), including command execution and result parsing.
- **DTOs:**
  - `tool/dto/` — Request and response objects for all tool operations, fully documented.
- **Tool Registry/Metadata:**
  - `tool/ToolRegistry.java`, `tool/ToolMetadata.java` — Central registry and schema definitions for all tools.
- **Configuration:**
  - `resources/application.properties` — Spring Boot and tool configuration.

---

## Test Organization
- **Integration Tests:**
  - `src/test/java/com/example/mcp/controller/` — End-to-end tests for all MCP endpoints, protocol compliance, and error handling.
- **Service/Unit Tests:**
  - `src/test/java/com/example/mcp/tool/` — Tests for tool logic, edge cases, and schema validation.
- **Test Coverage:**
  - Enforced via JaCoCo; all endpoints and error cases are covered.

---

## Extending the Server
1. **Add DTOs:** Define request/response objects in `tool/dto/`.
2. **Implement Service:** Add business logic in `tool/`.
3. **Register Tool:** Add metadata and schemas in `ToolRegistry.java`.
4. **Expose Endpoint:** Add controller method if needed.
5. **Test:** Add integration and service tests.

---

## Error Handling & Robustness
- All errors are returned as JSON with an `error` field.
- Edge cases (invalid input, missing fields, large/Unicode input) are tested and handled.
- Consistent response structure for agent and developer reliability.

---

## Developer Onboarding
- All core classes, DTOs, and endpoints are documented with Javadoc.
- See `mcp/docs/USAGE.md` and `mcp/docs/quality.md` for onboarding and workflow.

---

## Future Improvements
- Automated OpenAPI/JSON Schema generation from DTOs.
- More advanced tool orchestration and agent integration.
- Enhanced error reporting and observability.

---

For more, see the [Usage Guide](USAGE.md), [Quality Guide](quality.md), and in-code Javadoc.

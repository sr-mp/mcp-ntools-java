# MCP NTools Java: Copilot Collaboration Instructions

## Summary of This Conversation

This session focused on refactoring, documenting, and hardening a Spring Boot MCP server ("McpNtools") for protocol compliance, agent integration, and developer onboarding. The following major steps and standards were implemented:

### 1. Protocol Compliance & Robustness
- Ensured all endpoints (`/mcp`, `/mcp/tools`, `/mcp/tools/{toolName}/describe`, `/mcp/command/{toolName}`) are MCP-compliant and return consistent JSON (including error fields).
- Added comprehensive integration and edge case tests for protocol compliance, error handling, schema consistency, and robustness (large input, Unicode, missing/invalid fields).

### 2. Code Organization & Refactoring
- Refactored the main application class and all references to use `McpNtoolsApplication`.
- Centralized tool metadata and schemas in a `ToolRegistry` and `ToolMetadata` system.
- Moved all hardcoded tool registry data to a JSON file in `resources` and refactored the registry to load from it.
- Separated concerns: controllers for HTTP, services for business logic, DTOs for schemas, registry for metadata.

### 3. Documentation & Onboarding
- Added Javadoc to all core classes, controllers, services, registry, and DTOs.
- Created and organized documentation:
  - `mcp/docs/USAGE.md`: API usage, onboarding, extension steps.
  - `mcp/docs/quality.md`: Quality workflow, best practices, and tool commands.
  - `mcp/docs/architecture.md`: Design, code/test organization, extensibility, and error handling.
  - Updated `README.md` to reference new docs and onboarding steps.
- Moved all MCP-specific docs under `mcp/docs/` for modularity.

### 4. Test Organization
- Integration tests for all endpoints and protocol compliance in `src/test/java/com/example/mcp/controller/`.
- Service/unit tests for tool logic and edge cases in `src/test/java/com/example/mcp/tool/`.
- Enforced coverage and quality gates (JaCoCo, Checkstyle, PMD, SpotBugs).

### 5. Standards & Best Practices
- DTO-driven API for schema clarity and validation.
- Consistent error handling (all errors as JSON with `error` field).
- Test-first and documentation-first development.
- Modular, extensible design for easy tool addition.
- All code and docs are discoverable and well-commented for new developers.

---

## Instructions for Future Copilot Collaboration

### General Principles
- **Always gather context first**: Use file and text search to understand the codebase before making changes.
- **Never assume file contents**: Read files before editing, especially if the user may have made manual changes.
- **Minimize code repetition**: Use `...existing code...` in file edits to avoid duplicating unchanged code.
- **Document everything**: Add Javadoc to all new/changed classes, methods, and DTOs.
- **Keep endpoints robust**: All controllers must return valid JSON, with an `error` field on failure.
- **Test everything**: Add/extend integration and edge case tests for all new features or protocol changes.
- **Organize docs by module**: Place module-specific docs under the relevant folder (e.g., `mcp/docs/`).
- **Update onboarding docs**: When adding features, update `USAGE.md`, `quality.md`, and `architecture.md` as needed.

### For Tool Registry/Metadata
- **No hardcoded tool metadata in Java**: Store all tool schemas and metadata in a JSON file under `resources` (e.g., `tool-registry.json`).
- **Registry class should only load/parse JSON**: Keep the registry simple and focused on reading/parsing the JSON file.
- **Document the JSON schema**: Add comments or a sample in the docs for future contributors.

### For Adding New Tools
1. Define request/response DTOs in `tool/dto/`.
2. Implement service logic in `tool/`.
3. Register the tool in the JSON registry file.
4. Add/extend controller endpoints if needed.
5. Add integration and service tests.
6. Update onboarding and architecture docs.

### For Documentation
- **Javadoc required**: All public classes, methods, and DTOs must have Javadoc.
- **Update docs on every major change**: Especially `USAGE.md`, `quality.md`, and `architecture.md`.
- **README should always point to latest docs**.

### For Testing
- **Integration tests for all endpoints**: Cover protocol compliance, error cases, and edge cases.
- **Service/unit tests for tool logic**.
- **Enforce coverage and quality gates**: Use JaCoCo, Checkstyle, PMD, SpotBugs.

### For Error Handling
- **All errors must be JSON**: Always include an `error` field in responses.
- **Test for invalid/missing fields, large/Unicode input, and schema mismatches**.

### For Refactoring
- **Read the file before editing**: Especially if the user may have made manual changes.
- **Minimize disruption**: Only change what is necessary for the task.
- **Summarize changes in commit messages and docs**.

---

## Enterprise Development & Debugging Enhancements

### Debugging & Fast Issue Resolution
- **Add logging at all key points**: Use structured logging (e.g., SLF4J) in controllers, services, and error handlers.
- **Expose actuator endpoints**: Enable Spring Boot Actuator for health, metrics, and environment info.
- **Centralized exception handling**: Use `@ControllerAdvice` for global error handling and logging.
- **Return stack traces in dev mode**: Optionally include stack traces in error responses for debugging (never in production).
- **Add trace IDs to logs and responses**: For distributed tracing and easier debugging.
- **Log all external tool invocations and results**: Capture command, arguments, and output for audit/debug.
- **Add debug/test endpoints**: (e.g., `/mcp/debug/ping`, `/mcp/debug/info`) for quick health checks and diagnostics.

### Fast Feature Development
- **Use feature flags/configs**: Allow toggling new features via config or environment variables.
- **Hot reload for dev**: Enable Spring DevTools for automatic reloads during development.
- **Provide DTO and schema generators**: Scripts or templates for quickly scaffolding new tool DTOs and registry entries.
- **Document code conventions and review checklist**: Add a checklist for PRs (tests, docs, error handling, logging, etc).
- **Add sample payloads and curl scripts**: In `mcp/docs/USAGE.md` for all endpoints and edge/error cases.
- **Keep a changelog**: Maintain `CHANGELOG.md` for tracking features, fixes, and breaking changes.

### Enterprise Quality Standards
- **Security reviews**: Run static analysis (e.g., Semgrep) for security issues.
- **Dependency management**: Use tools like OWASP Dependency-Check.
- **Automated CI/CD**: Ensure all tests, quality checks, and builds run in CI.
- **Code review required**: Enforce reviews for all merges to main branches.
- **API versioning**: Plan for `/v1/`, `/v2/` endpoints as the API evolves.
- **Rate limiting/throttling**: Add basic rate limiting for public endpoints.
- **Comprehensive monitoring**: Integrate with Prometheus/Grafana or similar for metrics and alerting.
- **Backup and disaster recovery**: Document and automate backup strategies for persistent data (if any).

---

## Example Workflow for Copilot
1. **User requests a feature or refactor**.
2. **Search for relevant files and read them**.
3. **Edit files using minimal, focused changes**.
4. **Add/extend tests and docs as needed**.
5. **Validate with tests and quality checks**.
6. **Update onboarding and architecture docs**.
7. **Summarize changes and next steps for the user**.

---

## Additional Tips
- If unsure, ask the user for clarification or to confirm before making large changes.
- Always check for and respect manual edits made by the user.
- Use concise, clear explanations in all file edits and documentation.
- Keep the codebase and docs discoverable and easy for new developers to onboard.

---

*Save this file as `.github/learned-instructions.md` for future Copilot sessions to ensure fast, high-quality collaboration!*

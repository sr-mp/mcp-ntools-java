# MCP NTools Java: Usage Guide

This guide provides practical usage examples and onboarding steps for new developers working with the MCP NTools Java project. It covers how to run the MCP server, interact with its endpoints, and extend its functionality.

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build and Run the MCP Server

```bash
cd mcp
mvn clean verify
mvn spring-boot:run
```
The server will start on `http://localhost:8080` by default.

---

## 🛠️ MCP API Usage Examples

### 1. List Available Tools
- **Endpoint:** `GET /mcp/tools`
- **Description:** Returns metadata for all registered tools (Nmap, Nuclei, etc).
- **Example:**
```bash
curl http://localhost:8080/mcp/tools
```

### 2. Describe a Tool
- **Endpoint:** `GET /mcp/tools/{toolName}/describe`
- **Description:** Returns input/output schema and description for a specific tool.
- **Example:**
```bash
curl http://localhost:8080/mcp/tools/nmap/describe
```

### 3. Run a Tool Command
- **Endpoint:** `POST /mcp/command/{toolName}`
- **Description:** Executes a tool with the provided JSON request body.
- **Example:**
```bash
curl -X POST http://localhost:8080/mcp/command/nmap-scan-ports \
  -H 'Content-Type: application/json' \
  -d '{"ipAddress": "8.8.8.8", "ports": "80,443"}'
```

### 4. Example: Scan Multiple Hosts
- **Endpoint:** `POST /mcp/command/nmap-scan-multiple-hosts`
- **Request Body:**
```json
{
  "ipList": ["8.8.8.8", "1.1.1.1"],
  "ports": "80,443"
}
```
- **Example:**
```bash
curl -X POST http://localhost:8080/mcp/command/nmap-scan-multiple-hosts \
  -H 'Content-Type: application/json' \
  -d '{"ipList": ["8.8.8.8", "1.1.1.1"], "ports": "80,443"}'
```

### 5. Example: Advanced Nmap Scan
- **Endpoint:** `POST /mcp/command/nmap-advanced-scan`
- **Request Body:** See `/mcp/tools/nmap-advanced-scan/describe` for full schema.

---

## 🧑‍💻 Developer Onboarding

- **Code Structure:**
  - Main: `mcp/src/main/java/com/example/mcp/`
  - Controllers: `controller/`
  - Services: `tool/`
  - DTOs: `tool/dto/`
  - Tool registry/metadata: `tool/ToolRegistry.java`, `tool/ToolMetadata.java`
- **Integration Tests:**
  - `mcp/src/test/java/com/example/mcp/controller/`
  - `mcp/src/test/java/com/example/mcp/tool/`

- **Adding a New Tool:**
  1. Define request/response DTOs in `tool/dto/`
  2. Implement service logic in `tool/`
  3. Register the tool in `ToolRegistry.java`
  4. Add controller endpoint if needed
  5. Add integration tests

---

## 📝 Error Handling
- All endpoints return JSON with an `error` field if an error occurs.
- Example error response:
```json
{
  "error": "Invalid input: missing required field 'ipAddress'"
}
```

---

## 📚 See Also
- [Main README](../README.md)
- [Quality Guide](quality.md)
- [API source code](../mcp/src/main/java/com/example/mcp/)

---

For further help, see the Javadoc comments in the codebase or contact the project maintainers.

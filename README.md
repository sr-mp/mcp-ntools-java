# MCP NTools Java

A Spring Boot application providing a Model Context Protocol (MCP) server for network security tools (Nmap, Nuclei, etc.) with robust protocol compliance, agent integration, and developer onboarding support.

---

## ✅ Project Status

**BUILD STATUS: ✅ FULLY FUNCTIONAL**

The project is now fully operational with all quality tools working correctly:

- ✅ **Maven Build**: `mvn clean verify` - All phases complete successfully
- ✅ **Checkstyle**: Google Java Style compliance - 0 violations
- ✅ **PMD**: Static analysis - Passes on default profile
- ✅ **SpotBugs**: Bug detection - 0 issues found
- ✅ **JaCoCo**: Code coverage - 60%+ threshold met
- ✅ **Lombok**: Annotation processing - Working correctly
- ✅ **Spring Boot**: Application starts and tests pass
- ✅ **Quality Profiles**: Both `quality` and `quick-quality` profiles functional

### Ready for Development

The project is ready for:
- ✅ Immediate development work
- ✅ CI/CD pipeline integration
- ✅ Team onboarding with [`mcp/docs/quality.md`](mcp/docs/quality.md)
- ✅ Production deployment preparation

---

## 🚀 Quick Start

1. **Clone the repository**
2. **Build and run the MCP server:**
   ```bash
   cd mcp
   mvn clean verify
   mvn spring-boot:run
   ```
   The server will start on `http://localhost:8080`.

3. **Explore the API:**
   - List tools: `GET /mcp/tools`
   - Tool schema: `GET /mcp/tools/{toolName}/describe`
   - Run command: `POST /mcp/command/{toolName}`

---

## Features

- **Spring Boot Web Application** - RESTful Hello World API
- **Comprehensive Testing** - Unit and integration tests with JUnit 5
- **Lombok Integration** - Reduced boilerplate code with automatic generation
- **Enterprise-Grade Quality Tools**:
  - **PMD** - Static code analysis
  - **Checkstyle** - Code style enforcement (Google Java Style)
  - **SpotBugs** - Bug pattern detection
  - **JaCoCo** - Code coverage analysis (60% minimum)
  - **Maven Integration** - All quality checks run automatically

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

## 📋 Quick Start

### For New Developers
👉 **Read the comprehensive quality guide**: [`mcp/docs/quality.md`](mcp/docs/quality.md)

This guide covers:
- Quality tools explanation
- IDE setup instructions
- Daily development workflow
- Common issues and solutions
- Best practices and standards

### Essential Commands
```bash
# Complete quality pipeline (run before committing)
mvn clean verify

# Quick style check during development
mvn clean compile -Pquick-quality

# Generate coverage report
mvn jacoco:report
# View at: target/site/jacoco/index.html
```

---

## 🛠️ Usage Examples

See [`mcp/docs/USAGE.md`](mcp/docs/USAGE.md) for detailed API usage and curl examples.

---

## API Endpoints

- `GET /hello` - Returns "Hello World!"
- `GET /hello/name?name=John` - Returns "Hello John!" (or "Hello World!" if no name provided)

---

## 📚 Documentation

- [MCP Usage Guide](mcp/docs/USAGE.md): API usage, onboarding, and extension steps
- [MCP Quality Guide](mcp/docs/quality.md): Code quality, workflow, and best practices
- [MCP Docs Index](mcp/docs/README.md): Documentation index for the MCP server

For the main project overview, see below.

---

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build with Quality Checks
```bash
mvn clean verify
```

This command will:
1. Compile the source code
2. Run unit tests
3. Generate code coverage reports
4. Run PMD static analysis
5. Run Checkstyle validation
6. Run SpotBugs analysis
7. Validate code coverage meets 80% threshold

### Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Run Tests Only
```bash
mvn test
```

---

## Quality Checks

### Maven-Based Quality Pipeline

All quality checks are integrated into Maven lifecycle phases and run automatically:

```bash
# Run all quality checks (recommended)
mvn clean verify

# Run with enhanced quality profile
mvn clean verify -Pquality

# Quick quality check (Checkstyle only)
mvn clean compile -Pquick-quality

# Generate comprehensive site reports
mvn clean verify site -Pquality
```

### Quality Tools Integration

| Tool | Maven Goal | Phase | Purpose |
|------|------------|-------|---------|
| **Checkstyle** | `checkstyle:check` | validate | Code style enforcement |
| **PMD** | `pmd:check` | verify | Static code analysis |
| **SpotBugs** | `spotbugs:check` | verify | Bug detection |
| **JaCoCo** | `jacoco:check` | verify | Code coverage verification |
| **Tests** | `test` | test | Unit & integration tests |

### Individual Quality Commands

```bash
# Code style check
mvn checkstyle:check

# Static analysis
mvn pmd:check

# Bug detection
mvn spotbugs:check

# Code coverage
mvn jacoco:check

# Generate coverage report
mvn jacoco:report
```

### Quality Reports

After running `mvn verify`, reports are generated in:
- **Checkstyle**: `target/checkstyle-result.xml`
- **PMD**: `target/pmd.xml` and `target/site/pmd.html`
- **SpotBugs**: `target/spotbugsXml.xml`
- **JaCoCo**: `target/site/jacoco/index.html`

### Maven Profiles

- **`quality`**: Enhanced quality checks with detailed reporting
- **`quick-quality`**: Fast style checks only

Example usage:
```bash
# Full quality pipeline with enhanced reporting
mvn clean verify -Pquality

# Quick style check during development
mvn compile -Pquick-quality
```

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

## 📋 Project Structure

```
src/
├── main/java/com/example/mcpntools/
│   ├── McpNtoolsJavaApplication.java      # Main Spring Boot application
│   └── controller/
│       └── HelloWorldController.java      # REST controller
├── main/resources/
│   └── application.properties             # Application configuration
└── test/java/com/example/mcpntools/
    ├── McpNtoolsJavaApplicationTests.java # Integration tests
    └── controller/
        └── HelloWorldControllerTest.java  # Controller unit tests

docs/
├── README.md                              # Documentation index
├── quality.md                             # Comprehensive quality guide
└── USAGE.md                               # API usage and onboarding

Configuration Files:
├── .gitignore                             # Git ignore patterns
├── checkstyle.xml                         # Checkstyle rules
├── pom.xml                               # Maven configuration
├── QUALITY-CHECKS.md                     # Quality tools summary
└── README.md                             # This file
```

---

## 🔧 Version Control

The project includes a comprehensive `.gitignore` file that excludes:

- **Generated artifacts**: `target/`, `*.class`, `*.jar`, etc.
- **IDE files**: `.idea/`, `.vscode/`, `.project`, `*.iml`
- **OS files**: `.DS_Store`, `Thumbs.db`, etc.
- **Quality tool outputs**: Coverage reports, analysis results
- **Temporary files**: Logs, cache files, backup files
- **Security files**: Certificates, keystores, local configs

### Git Workflow
```bash
# Initialize repository (if not done)
git init

# Add source files (generated artifacts automatically ignored)
git add .

# Commit changes
git commit -m "Initial commit with quality tools setup"
```

---

## 🤝 Contributing
- Follow code style and quality rules (`mvn clean verify` must pass)
- Add Javadoc to new classes and methods
- Add/extend integration tests for new features

---

## Code Quality Configuration

- **Checkstyle**: `checkstyle.xml` - Enforces Java coding standards
- **Semgrep**: `.semgrep.yml` - Custom security and best practice rules
- **JaCoCo**: Configured in `pom.xml` with 80% line coverage requirement
- **PMD**: Uses standard Java rulesets for best practices, design, and performance
- **SpotBugs**: Configured for maximum effort and low threshold bug detection
- **Lombok**: Reduces boilerplate code with automatic generation
- **Git**: `.gitignore` properly excludes all generated artifacts and IDE files

---

## Development Guidelines

1. **Code Style**: Follow the Checkstyle rules defined in `checkstyle.xml`
2. **Testing**: Maintain minimum 80% code coverage
3. **Security**: Address all Semgrep findings
4. **Quality**: Fix PMD and SpotBugs violations
5. **Documentation**: Use Javadoc for public methods and classes

For more, see the [MCP Quality Guide](mcp/docs/quality.md) and [MCP Usage Guide](mcp/docs/USAGE.md).

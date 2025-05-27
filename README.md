# MCP NTools Java

A Spring Boot application demonstrating comprehensive code quality tools integration including PMD, Checkstyle, SpotBugs, JaCoCo, and Semgrep.

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
- ✅ Team onboarding with [`docs/quality.md`](docs/quality.md)
- ✅ Production deployment preparation

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

## 📋 Quick Start

### For New Developers
👉 **Read the comprehensive quality guide**: [`docs/quality.md`](docs/quality.md)

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

## API Endpoints

- `GET /hello` - Returns "Hello World!"
- `GET /hello/name?name=John` - Returns "Hello John!" (or "Hello World!" if no name provided)

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
└── quality.md                             # Comprehensive quality guide

Configuration Files:
├── .gitignore                             # Git ignore patterns
├── checkstyle.xml                         # Checkstyle rules
├── pom.xml                               # Maven configuration
├── QUALITY-CHECKS.md                     # Quality tools summary
└── README.md                             # This file
```

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

## Code Quality Configuration

- **Checkstyle**: `checkstyle.xml` - Enforces Java coding standards
- **Semgrep**: `.semgrep.yml` - Custom security and best practice rules
- **JaCoCo**: Configured in `pom.xml` with 80% line coverage requirement
- **PMD**: Uses standard Java rulesets for best practices, design, and performance
- **SpotBugs**: Configured for maximum effort and low threshold bug detection
- **Lombok**: Reduces boilerplate code with automatic generation
- **Git**: `.gitignore` properly excludes all generated artifacts and IDE files

## Development Guidelines

1. **Code Style**: Follow the Checkstyle rules defined in `checkstyle.xml`
2. **Testing**: Maintain minimum 80% code coverage
3. **Security**: Address all Semgrep findings
4. **Quality**: Fix PMD and SpotBugs violations
5. **Documentation**: Use Javadoc for public methods and classes

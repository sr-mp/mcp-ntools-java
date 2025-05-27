<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# MCP NTools Java Project Instructions

This is a Spring Boot Java project with comprehensive code quality tools including:
- PMD for static code analysis
- Checkstyle for code style enforcement
- SpotBugs for bug detection
- JaCoCo for code coverage
- Semgrep for security analysis

## Code Quality Standards
- Follow Java coding conventions as defined in checkstyle.xml
- Maintain minimum 80% code coverage
- Write comprehensive unit tests for all controllers and services
- Use proper logging instead of System.out.println
- Handle exceptions appropriately - avoid empty catch blocks
- Use prepared statements for database queries to prevent SQL injection

## Project Structure
- Main application: `McpNtoolsJavaApplication.java`
- Controllers: `com.example.mcpntools.controller` package
- Tests: Mirror the main package structure in test directory
- Configuration: `application.properties` for Spring Boot settings

## Testing Guidelines
- Use `@WebMvcTest` for controller tests
- Use `@SpringBootTest` for integration tests
- Mock external dependencies appropriately
- Test both success and error scenarios

# Copilot Instructions for Java Project

## 🚨 Objective

Generate Java code that complies with this project's code quality and design standards. The project uses PMD (including design rules), SpotBugs, and Checkstyle. All generated code **must pass static analysis checks without modification**.

---

## 🔍 PMD Design Rules to Follow

- ✅ **Single Responsibility Principle**: Each class or method should do only one thing.
- ✅ **Avoid God Classes**: Classes should not exceed 25 methods or 1000 lines of code.
- ✅ **Avoid Long Methods**: Methods should not exceed 30 lines. Break logic into smaller, reusable methods.
- ✅ **Minimize Class Coupling**:
  - Use interfaces rather than concrete classes where possible.
  - Avoid high coupling between unrelated classes.
- ✅ **Law of Demeter**: Do not chain method calls from other objects (e.g., `a.getB().getC().doSomething()`).
- ✅ **Avoid Deep Inheritance Trees**: Prefer composition over inheritance; limit inheritance depth to 3 or fewer levels.
- ✅ **Avoid Large Constructors**: Constructors should not contain complex logic.
- ✅ **Do Not Use System.exit**: Instead, throw exceptions or return gracefully.
- ✅ **Avoid Empty Catch Blocks**: Always log or handle exceptions meaningfully.
- ✅ **Avoid Excessive Method Overloading**: Only overload when behavior is logically related and clear.
- ✅ **Avoid Public Static Fields**: Use private static fields with public accessors if needed.

---

## 💡 Code Style & Readability (Checkstyle Guidelines)

- Use **camelCase** for variables and methods, **PascalCase** for classes.
- Constants should be `UPPER_CASE_WITH_UNDERSCORES`.
- Include JavaDoc comments on all public classes and methods.
- Limit line length to 120 characters.
- Use 4 spaces for indentation, no tabs.
- Always use braces (`{}`) for `if`, `else`, `for`, `while`, and `do` blocks.
- Avoid magic numbers: define constants instead.

---

## 🕵️‍♂️ SpotBugs-Related Best Practices

- Do not ignore exceptions silently.
- Avoid creating and ignoring `InterruptedException`.
- Prefer `StringBuilder` over `+` for string concatenation inside loops.
- Close resources (`FileInputStream`, `BufferedReader`, etc.) in `finally` or use try-with-resources.
- Avoid returning references to mutable internal fields.

---

## 📊 Metrics-Based Thresholds (Maintainability)

| Metric                  | Limit               |
|-------------------------|---------------------|
| Class length            | ≤ 1000 lines        |
| Method length           | ≤ 30 lines          |
| Cyclomatic complexity   | ≤ 10                |
| Number of fields        | ≤ 20                |
| Number of methods       | ≤ 25                |

---

## 🧪 Testing & Coverage

- Every public method should have a corresponding unit test.
- Prefer using JUnit 5.
- Avoid hardcoded paths or dependencies that break test isolation.

---

## ✅ Summary for Copilot

Copilot must generate Java code that is:
- **Modular** (small classes and methods)
- **Well-encapsulated** (minimal field exposure)
- **Easy to test**
- **Consistent with style guides**
- **Free of code smells**
- **Passes PMD, SpotBugs, and Checkstyle checks**

Thank you, Copilot! 🙌
# Code Quality & Style Guide

## Overview

This project uses automated tools to maintain consistent code style and improve overall code quality.

We currently use:

* **Spotless** – automatic code formatting
* **Checkstyle** – static analysis and style validation

These tools help ensure the codebase remains readable, consistent, and maintainable across contributors.

---

## Checkstyle

### What It Does

Checkstyle analyzes Java source code and reports style and formatting issues, such as:

* Missing spaces in comments (e.g., `//comment` → `// comment`)
* Naming convention violations
* Unused imports
* Missing braces in control structures
* Excessively long lines

Checkstyle does not automatically modify code; it provides feedback that developers can address.

---

## Configuration

The project-wide Checkstyle rules are defined in:

```plaintext
checkstyle.xml
```

This file serves as the single source of truth for code style expectations.

---

## Developer Workflow

### During Development

Developers may configure their IDE to use the project's `checkstyle.xml` for real-time feedback while writing code.

This is optional but recommended for a smoother development experience.

### Before Committing

* Ensure the project builds successfully
* Address any obvious style issues where reasonable

---

## IDE Setup (Optional)

### VS Code

1. Install the **Checkstyle for Java** extension
2. Configure your workspace to use the project rules:

```json
{
  "java.checkstyle.configuration": "checkstyle.xml"
}
```

### Other IDEs

Most Java IDEs (e.g., IntelliJ, NetBeans) support Checkstyle plugins or built-in integrations.
Refer to your IDE’s documentation to configure it using the project’s `checkstyle.xml`.

---

## Enforcement Strategy

Currently:

* Checkstyle provides **developer feedback only**
* It is not enforced in the build or CI pipeline

Future improvements may include:

* Automated checks in GitHub Actions
* Build-time enforcement

---

## Notes

* Existing code may not fully comply with all rules
* New and modified code should follow these standards where possible
* If a rule is unclear or too restrictive, discuss it with the team before modifying it

---

## Goal

The goal of introducing Checkstyle is to:

* Improve code readability and consistency
* Reduce stylistic inconsistencies across contributors
* Support long-term maintainability of the project

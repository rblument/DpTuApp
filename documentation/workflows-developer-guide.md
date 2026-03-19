# DpTu Workflows Developer Guide

This document explains the automated workflows used in the **DpTu application** GitHub repository and how developers should interact with them.

It covers:

* What GitHub Actions workflows are and why we use them
* How each repository workflow functions
* How to respond when workflows fail
* How Dependabot manages dependency updates
* How these workflows support code quality, security, and consistency

This file is located at: `documentation/workflows-developer-guide.md`.

---

## Overview: Why We Use Automated Workflows

DpTu uses [**GitHub Actions**](https://docs.github.com/en/actions) to automatically enforce development standards, run tests, validate builds, format code, and monitor security risks.

These workflows ensure that:

* Code compiles before merging
* Tests run consistently
* Formatting remains standardized
* Logging rules are enforced
* Security vulnerabilities are detected early
* Dependencies remain up-to-date

This removes manual enforcement and keeps the repository stable and production-ready.

---

## What Are GitHub Workflows?

A **GitHub workflow** is an automated process defined in a YAML file stored under: `.github/workflows/`.

Each workflow contains:

| Component           | Meaning                                                      |
| ------------------- | ------------------------------------------------------------ |
| **Trigger (`on:`)** | When the workflow runs (push, pull request, manual, etc.)    |
| **Job**             | A unit of work executed on a runner                          |
| **Steps**           | Commands or actions executed sequentially                    |
| **Runner**          | The environment that executes the job (usually Ubuntu Linux) |

Workflows run automatically when triggered and appear in the repository’s **Actions** tab.

Many DpTu workflows can also be run manually using **workflow_dispatch**.

---

## How Workflows Fit Into the Development Process

Typical pull request lifecycle:

1. Developer opens PR
2. Formatting workflow may auto-format code
3. Logging enforcement runs
4. Project compilation is validated
5. Tests run
6. Security analysis runs (CodeQL)
7. Approver reviews PR
8. Merge allowed if checks pass

On push to main/development:

* Tests run again
* Security analysis runs
* Logging enforcement runs

Separately:

* Dependabot continuously monitors dependencies

---

*Below is a complete explanation of each workflow currently used by the repository.*

---

## 1. PR Build Validation (`pr-build.yml`)

### Purpose

Ensures the project **compiles successfully** before merging. This prevents broken code from entering the main branches.

### When It Runs

* Pull request targetting: `development` or `main` branches.
* Manual trigger

### What It Does

1. Checks out repository code
2. Sets up JDK 17
3. Caches Maven dependencies
4. Runs:

    ```shell
    mvn compile
    ```

### What Causes Failure

* Compilation errors
* Missing dependencies
* Broken imports
* Syntax errors

### How to Fix Failures

Compile locally before pushing:

```shell
mvn compile
```

Resolve all compiler errors, commit, and push again.

---

## 2. Run Tests (`test.yml`)

### Purpose

Executes all automated tests, generates coverage artifacts, compares PR coverage against `development`, and publishes test results. This ensures functional correctness and highlights coverage regressions quickly.

### When It Runs

* Push to any branch (badge-only pushes are ignored)
* Pull requests targeting `development` or `main`
* Manual trigger

### What It Does

1. Checks out code
2. Sets up JDK 17
3. Caches Maven dependencies
4. Runs:

    ```shell
    mvn -B verify
    ```

5. Generates a stored coverage summary snapshot from the current branch's JaCoCo report.
6. Compares PR JaCoCo line coverage to baseline coverage and posts/updates a sticky PR comment with separate percentages for:

    * all classes
    * non-Swing UI classes
    * baseline coverage (`development`)
    * PR coverage
    * increase/decrease delta

    On pull requests, the baseline values come from the coverage summary file already stored on `development`, rather than rerunning the full `development` test suite.

7. Writes a workflow run summary including both all-class and non-Swing UI coverage, plus coverage hot spots (top classes by missed lines).
8. Generates the coverage badge from JaCoCo output.
9. On `development` pushes, commits updated badge files (if changed).
10. Publishes JUnit test reports to GitHub UI.

### What Causes Failure

* Failing tests
* Runtime exceptions in tests
* Test configuration errors

### How to Fix Failures

Run locally:

```shell
mvn verify
```

Fix failing tests or underlying logic. If coverage decreases unexpectedly, review the PR coverage-delta comment and add or improve tests around changed code.

---

## 3. Code Formatting (`format-code.yml`)

### Purpose

Automatically formats code using [**Spotless**](https://github.com/diffplug/spotless) to enforce consistent style. This prevents style-related review noise.

### When It Runs

* Pull request opened or updated
* Manual trigger

### What It Does

1. Checks out PR branch
2. Sets up JDK 17
3. Caches Maven dependencies
4. Runs:

    ```shell
    mvn spotless:apply
    ```

5. If formatting changes were made:

   * Commits changes automatically
   * Pushes back to the PR branch

### Developer Impact

You may see new commits appear automatically in your PR and these commits bypass other workflows. This is expected.

### Best Practice

Format locally before pushing:

```shell
mvn spotless:apply
```

---

## 4. Logging Standards Enforcement (`logging-check.yml`)

### Purpose

Enforces repository logging rules. This workflow implements the policies defined in: `documentation/logging-developer-guide.md`. All developers should follow that guide.

### When It Runs

* Push to: `development` or `main` branches.
* Pull requests
* Manual trigger

### What It Enforces

Scope note: Logging checks are run against `*.java` source files only (non-test Java sources). Resource files such as `.properties`, `.xml`, images, and text files are not scanned by this workflow.

#### Hard Failures (Build Stops)

These patterns are forbidden:

* `System.out.*`
* `System.err.*`
* `printStackTrace()`
* `java.util.logging`
* Direct Log4j usage
* Logging framework bypasses
* `System.exit(...)`

#### Warnings (Non-blocking)

* Classes with no logging
* Logger declared but never used
* Empty catch blocks
* `log.error` calls without exception context

### Pull Request Delta Comment

On pull requests, the workflow compares current logging findings against `development` and posts/updates a sticky PR comment listing only **newly introduced violations**.

This keeps legacy findings visible but focuses reviewer attention on what the PR added.

### Test Code Exemptions

Test sources are excluded from enforcement.

### How to Fix Failures

Follow the logging developer guide:

* Use SLF4J only
* Log exceptions properly
* Replace console output
* Use parameterized logging

---

## 5. CodeQL Security Analysis (`codeql.yml`)

### Purpose

Performs **static security analysis** to detect vulnerabilities.

This identifies:

* Injection risks
* Unsafe code patterns
* Dependency misuse
* Security misconfigurations

### When It Runs

* Push to: `development` or `main` branches.
* Pull requests
* Manual trigger

### Languages Analyzed

* GitHub Actions workflows
* Java

### How Results Appear

Findings appear in the GitHub Browser UI at: `Security → Code scanning alerts`.

### Developer Responsibilities

If CodeQL reports an issue:

1. Read the alert description
2. Fix the vulnerable pattern
3. Commit and push
4. Confirm alert is resolved

Do not ignore alerts without review.

---

## 6. Dependabot (`dependabot.yml`)

### Purpose

Keeps project dependencies up to date and secure.

Dependabot automatically:

* Checks Maven dependencies
* Detects outdated versions
* Detects known vulnerabilities
* Opens pull requests with updates

### Update Schedule

Daily.

### Pull Request Behavior

Dependabot PRs:

* Update dependencies in `pom.xml`
* Are labeled:
  * `dependencies`
  * `security`
* Limited to 5 open PRs at a time

Security updates are grouped.

### Developer Responsibilities

When a Dependabot PR appears:

1. Review version changes
2. Ensure tests pass
3. Check for breaking changes
4. Merge if safe

Never merge dependency updates blindly without CI passing.

---

## Manual Workflow Execution

All workflows support manual execution.

To run manually:

1. Open repository
2. Click **Actions**
3. Select workflow
4. Click **Run workflow**

This is useful for:

* Verifying fixes
* Re-running failed checks
* Testing configuration changes

---

## Failure Handling Strategy

| Workflow                 | Fix Location               |
| ------------------------ | -------------------------- |
| Build fails              | Compilation errors         |
| Tests fail               | Application logic or tests |
| Formatting changed       | Accept auto-format commit  |
| Logging enforcement      | Update logging usage       |
| CodeQL alerts            | Fix security issue         |
| Dependabot PR tests fail | Dependency compatibility   |

Never merge failing checks.

---

## Local Developer Checklist

Before pushing code:

```shell
mvn compile
mvn test
mvn spotless:apply
```

Also verify:

* Logging follows SLF4J rules
* No console printing
* Exceptions logged properly

---

## Why This Automation Matters

These workflows ensure:

* Consistent code style
* Enforced logging architecture
* Verified builds
* Verified tests
* Continuous security scanning
* Controlled dependency updates

Together they create a stable, secure, production-ready development environment.

---

## Related Documentation

* `documentation/logging-developer-guide.md` — Logging standards and enforcement rules

---

## Maintenance Notes

When adding new workflows:

* Document them in this file
* Explain developer responsibilities
* Describe failure recovery

This guide should always reflect the current CI/CD configuration.

---

**Last reviewed:** 08 March 2026 by Harrison Sherwin

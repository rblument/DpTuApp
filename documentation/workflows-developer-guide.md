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
2. Consolidated PR validation workflow evaluates formatting first
3. Same-repository PRs may receive an auto-format commit if Spotless finds changes
4. Project compilation is validated
5. Tests run and coverage is reported
6. Logging enforcement runs
7. Security analysis runs (CodeQL)
8. Approver reviews PR
9. Merge allowed if checks pass

On push to main/development:

* Tests run again
* Security analysis runs
* Logging enforcement runs

Separately:

* Dependabot continuously monitors dependencies

---

*Below is a complete explanation of each workflow currently used by the repository.*

---

## 1. Format Build Test (`format-build-test.yml`)

### Purpose

Consolidates pull request and branch validation into a single ordered workflow with explicit stages: format, build, and test.

### When It Runs

* Push to `development` or `main`
* Pull requests targeting `development` or `main`
* Manual trigger

### What It Does

1. Runs a `format` job first.
2. On same-repository pull requests, runs:

    ```shell
    mvn -B spotless:apply
    ```

   If formatting changes are required, the workflow commits and pushes them. A new workflow run then validates the updated branch.

3. On push/manual runs, checks formatting with:

    ```shell
    mvn -B spotless:check
    ```

4. On fork pull requests, if Spotless would change files, the workflow fails and instructs the contributor to run Spotless locally.
5. Runs a `build` job after formatting succeeds:

    ```shell
    mvn -B compile
    ```

6. Runs a `test` job after build succeeds:

    ```shell
    mvn -B test jacoco:report
    ```

7. Generates a coverage summary snapshot and compares PR coverage against stored `development` baseline.
8. Updates coverage summary comments and run summaries.
9. Generates and, on `development` pushes, commits updated coverage badges.
10. Publishes JUnit test reports.

### What Causes Failure

* Formatting violations on push/manual runs
* Formatting violations on fork pull requests
* Compilation errors
* Missing dependencies
* Broken imports
* Syntax errors
* Failing tests
* Runtime exceptions in tests
* Coverage processing/report generation errors

### How to Fix Failures

Run locally before pushing:

```shell
mvn spotless:apply
mvn compile
mvn test jacoco:report
```

Resolve formatting, compile, or test failures, commit, and push again.

---

## 2. Logging Standards Enforcement (`logging-check.yml`)

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

## 3. CodeQL Security Analysis (`codeql.yml`)

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

## 4. Dependabot (`dependabot.yml`)

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
| Format/Build/Test fails  | Formatting, compile, tests |
| Build fails              | Compilation errors         |
| Logging enforcement      | Update logging usage       |
| CodeQL alerts            | Fix security issue         |
| Dependabot PR tests fail | Dependency compatibility   |

Never merge failing checks.

---

## Local Developer Checklist

Before pushing code:

```shell
mvn spotless:apply
mvn compile
mvn test jacoco:report
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

**Last reviewed:** 18 March 2026 by Harrison Sherwin

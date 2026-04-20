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
* Pull requests targeting `development` or `main` (including when labels are applied or removed)
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

   > **Note:** When the `ci:test-fail-format-build-test` label is applied the format job fails intentionally, but the build and test jobs still run so that unit test coverage metrics are always produced during PR testing.

6. Runs a `test` job after build succeeds:

    ```shell
    mvn -B test jacoco:report
    ```

7. Generates a coverage summary JSON snapshot and, on pull requests, posts or updates a sticky PR comment showing covered and missed line counts for:
   * **All classes** — the full project
   * **Non-Swing UI classes** — all packages except `edu.regis.dptu.view.*`
8. If JaCoCo output is unavailable the PR comment includes a specific diagnostic reason (for example: tests failed, test step was skipped, test run was cancelled) rather than a generic message.
9. Publishes a step summary with the coverage table.

### What Causes Failure

* Formatting violations on push/manual runs
* Formatting violations on fork pull requests
* Compilation errors
* Missing dependencies
* Broken imports
* Syntax errors
* Failing tests
* Runtime exceptions in tests

### How to Fix Failures

Run locally before pushing:

```shell
mvn spotless:apply
mvn compile
mvn test jacoco:report
```

Resolve formatting, compile, or test failures, commit, and push again.

### Intentional Failure Label

Apply the `ci:test-fail-format-build-test` label to a PR to force the format job to fail.
This is used to verify that the PR Workflow Failure Comments workflow correctly detects and reports the failure.
Remove the label to restore normal behavior.

---

## 2. Standards Check (`standards-check.yml`)

### Purpose

Enforces repository coding standards including:

* **Logging standards** — enforces the policies defined in: `documentation/logging-developer-guide.md`
* **Spelling standards** — enforces correct spelling via cspell in documentation, workflows, and configuration as defined in: `documentation/code-quality.md`

All developers should follow both guides.

### When It Runs

* Push to: `development` or `main` branches
* Pull requests (including when labels are applied or removed)
* Manual trigger

### What It Enforces

#### Job 1: `logging-standards`

**Scope:** Logging checks are run against `*.java` source files only (non-test Java sources).

**Hard Failures (Build Stops):**

These patterns are forbidden:

* `System.out.*` (console output)
* `System.err.*` (console error)
* `printStackTrace()` (stack trace printing)
* `java.util.logging` (direct JUL usage)
* Direct Log4j usage (must use SLF4J)
* Logging framework bypasses
* `System.exit(...)` (abrupt JVM termination)

**Warnings (Non-blocking):**

* Classes with no logging
* Logger declared but never used
* Empty catch blocks
* `log.error` calls without exception context

#### Job 2: `spelling-check` (NEW)

**Scope:** Spelling checks via cspell are run on:

* `README.md`
* `documentation/**/*.md` (markdown documentation)
* `.github/**/*.yml` and `.github/**/*.yaml` (workflow files)
* `src/main/resources/Msgs.properties` (UI message strings)

**Ignored files:** `target/`, `.git/`, image files, database scripts, and class names matching the domain qualifier pattern `edu.regis.dptu.*`.

**Hard Failures on PRs:**

Only *new* spelling violations introduced by the PR fail the build (delta-based check against `development`).

**Advisory (Non-blocking) on push/manual runs:**

Spelling issues are reported but do not fail the build.

**Project Dictionary:**

Project-specific terms (DpTu, SLF4J, JaCoCo, etc.) are stored in `.cspell/project-words.txt`.

To add accepted terms, edit this file and commit.

### Pull Request Delta Comments

On pull requests, the workflow compares findings against `development` and posts/updates sticky PR comments:

* **Logging violations comment** (`<!-- logging-violations-delta -->`): lists newly introduced logging issues
* **Spelling violations comment** (`<!-- cspell-violations-delta -->`): lists newly introduced spelling errors

If a later commit resolves all violations, the corresponding comment is automatically deleted.

### Test Code Exemptions

Test sources are excluded from logging enforcement.

### How to Fix Failures

Follow the logging developer guide:

* Use SLF4J only
* Log exceptions properly
* Replace console output
* Use parameterized logging

### Intentional Failure Label

Apply the `ci:test-fail-standards-check` label to a PR to force the logging-standards job to fail.
This is used to verify that the PR Workflow Failure Comments workflow correctly detects and reports the failure.
Remove the label to restore normal behavior.

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
* Pull requests (including when labels are applied or removed)
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

### Intentional Failure Label

Apply the `ci:test-fail-codeql-advanced` label to a PR to force the analyze job to fail.
This is used to verify that the PR Workflow Failure Comments workflow correctly detects and reports the failure.
Remove the label to restore normal behavior.

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

| Workflow                    | Fix Location                                            |
| --------------------------- | ------------------------------------------------------- |
| Format / Build / Test fails | Formatting, compile errors, failing tests               |
| Standards Check fails       | Logging violations, spelling errors                     |
| CodeQL alerts               | Fix security issue                                      |
| Dependabot PR tests fail    | Dependency compatibility                                |
| PR Workflow Failure comment | See comment on PR; link to the failing run is included  |
| Poller workflow fails       | Inspect poller run logs and workflow/run association API calls |

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
* `documentation/code-quality.md` — Code style, formatting, and spelling standards

---

## 5. PR Workflow Failure Comments (`pr-workflow-failure-comments.yml`)

### Purpose

Provides **centralized, sticky, and ephemeral failure comments** on pull requests.

When any watched workflow fails on a PR, this workflow automatically posts a comment on that PR summarizing what failed and how to investigate. When the same workflow later passes, the comment is automatically deleted.

This keeps PRs clean and informative without requiring developers to navigate to the Actions tab for every failure.

### Default-Branch Limitation and Backup Path

GitHub only evaluates `workflow_run.workflows` from the **default branch** (`development`) at dispatch time. This means a pull request that introduces or modifies watch-list entries cannot activate those changes until merged.

To keep PR failure comments available before merge, DpTu also uses a backup polling workflow (`pr-workflow-failure-comments-poller.yml`) that runs directly from pull request events and polls watched workflow runs for the PR head SHA.

### Backup Poller Workflow (`pr-workflow-failure-comments-poller.yml`)

The poller is a secondary safety net for the same comment markers used by the primary workflow. It keeps failure comments working on PR branches before `workflow_run` watch-list changes are active on `development`.

**When the poller runs:**

* Pull request events: `opened`, `synchronize`, `reopened`, `labeled`, `unlabeled`, `ready_for_review`
* Manual trigger

**How the poller behaves:**

1. Resolves the current PR number and head SHA.
2. Polls watched workflows for that head SHA until completion (bounded wait window).
3. For each watched workflow:
   * If failing (`failure`, `timed_out`, `cancelled`, etc.), posts or updates the same sticky marker comment used by the primary workflow.
   * If healthy, deletes any existing marker comment for that workflow.
4. Uses the same failure payload format (failed jobs, failing steps, first matching log lines) so PR comments stay consistent regardless of which path produced them.

**Important maintenance notes:**

* The poller includes `# pr-workflow-failure-comments: ignore` so drift validation does not require poller self-registration.
* Keep watched workflow names and marker formats aligned between:
  * `pr-workflow-failure-comments.yml`
  * `pr-workflow-failure-comments-poller.yml`
* If you add a new PR-triggered workflow, update both files so primary and backup behavior stay in sync.

### When It Runs

* After any watched workflow completes (via `workflow_run` event)
* When a workflow YAML file changes on push or pull request (for drift validation)
* Manual trigger

### What It Does

#### Job 1: `validate-watchlist-drift`

Runs whenever a `.github/workflows/*.yml` file changes. Verifies that the watch list in this workflow's `workflow_run.workflows` section covers every workflow that has a `pull_request:` trigger.

* **Missing entries** (new PR workflows not in the watch list) cause the job to fail.
* **Stale entries** (watch list names that no longer match any workflow) cause the job to fail.

To exclude a workflow from the watch requirement, add this comment anywhere in that workflow file:

```yaml
# pr-workflow-failure-comments: ignore
```

> **Note:** GitHub reads the `workflow_run.workflows` list from the **default branch** at dispatch time. Changes to the watch list on a PR branch only take full effect after that PR is merged to `development`. The drift validation job (which runs from the PR branch) catches problems early, but the watch behavior activates post-merge.

#### Job 2: `report-workflow-failures`

Fires when a watched workflow completes on a pull request run. For each associated PR:

* **Failure / timed out / cancelled / etc.:** Posts or updates a sticky comment containing:
  * Workflow name, conclusion, and a link to the run
  * Each failing job name and its first failing steps
  * Up to three first-matching error log lines from the job's raw logs
* **Success:** Deletes any existing failure comment for that workflow from the PR.

Each workflow has its own comment identified by a unique HTML marker (`<!-- pr-workflow-failure:{workflow-slug} -->`), so comments for different workflows never overwrite each other.

### Watched Workflows

| Workflow Name                 | Failure Label (testing)          |
| ----------------------------- | -------------------------------- |
| Format Build Test             | `ci:test-fail-format-build-test` |
| Standards Check               | `ci:test-fail-standards-check`   |
| CodeQL Advanced               | `ci:test-fail-codeql-advanced`   |
| PR Workflow Failure Comments  | _(self-watch; validates the watch list drift job)_ |

### Testing the Failure Comments

Each watched workflow has an intentional failure hook. To test the end-to-end comment flow:

1. Apply the corresponding `ci:test-fail-*` label to a PR.
2. Wait for the workflow run to complete (the label triggers a new run).
3. Verify that a failure summary comment appears on the PR.
4. Remove the label.
5. Wait for the workflow run to complete again (healthy).
6. Verify that the failure comment is deleted from the PR.

### Developer Responsibilities

When a failure comment appears on your PR:

1. Read the failing job and step names in the comment.
2. Click the run link to view full logs.
3. Push a fix — the comment will update automatically on the next run.
4. If all failures are resolved the comment disappears automatically.

---

## Maintenance Notes

When adding new workflows:

* Document them in this file
* Explain developer responsibilities
* Describe failure recovery

This guide should always reflect the current CI/CD configuration.

---

**Last reviewed:** 20 April 2026 by Harrison Sherwin

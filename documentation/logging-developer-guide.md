# DpTu Logging Developer Guide

This document explains how logging works in the DpTu application and how to use it correctly when developing new features.

It was last reviewed on 9 Feb 2026 by Harrison Sherwin.

## Overview

DpTu now uses:

| Layer                | Library                        | Purpose                                                             |
| -------------------- | ------------------------------ | ------------------------------------------------------------------- |
| **API used in code** | **SLF4J** (`org.slf4j.Logger`) | Unified logging interface used everywhere in application code       |
| **Logging backend**  | **Log4j2**                     | Controls formatting, output, file logging, rotation, and log levels |

### Key Takeaway

**All new logging should use SLF4J**, not JUL.

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

private static final Logger log = LoggerFactory.getLogger(MyClassName.class);
```

## Logger Initialization (Required Pattern)

Every class that logs should include:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleClass {
    private static final Logger log = LoggerFactory.getLogger(ExampleClass.class);

    public void doWork() {
        log.info("Starting work...");
    }
}
```

### Why `log` (not `LOG` or `LOGGER`)

* It follows standard Java & SLF4J convention.
* Avoids confusion with compile-time constants (which use ALL_CAPS).

## Logging Levels

| Level         | When to Use                                                |
| ------------- | ---------------------------------------------------------- |
| `log.trace()` | Extremely detailed information (rare)                      |
| `log.debug()` | Developer troubleshooting, internal details                |
| `log.info()`  | Normal operations, startup, shutdown, user actions         |
| `log.warn()`  | Something unexpected happened, but the system can continue |
| `log.error()` | Serious issue or failure; something has gone wrong         |

### Examples

```java
log.info("User {} logged in", userId);
log.warn("Could not load resource {}; using default instead", resourceName);
log.error("Failed to save record", exception);
```

## `isXEnabled()` Guards (When and When *Not* to Use Them)

SLF4J provides methods such as `log.isDebugEnabled()` and `log.isTraceEnabled()`.

**These methods do not control whether logs are printed** — logging configuration already does that.

Their purpose is to avoid *unnecessary work* performed to **prepare** log messages when a level is disabled.

### Important Rule

> **Only use `isXEnabled()` if it prevents non-trivial work.**

Do **not** use it mechanically around every debug statement.

---

### ✅ Good Usage (Guard Saves Real Work)

Use a guard when logging:

* Calls multiple getters
* Touches domain / DAO-backed objects
* Builds strings, collections, or formatted output
* Dumps tables, matrices, or internal state

```java
if (log.isDebugEnabled()) {
    log.debug(
        "Retrieved problem: id={}, type={}",
        problem.getId(),
        problem.getType()
    );
}
```

```java
if (log.isDebugEnabled()) {
    log.debug("Matrix contents:\n{}", matrix.prettyPrint());
}
```

Why this is good:

* `getId()`, `getType()`, or `prettyPrint()` may be expensive
* Work is skipped entirely when DEBUG is disabled

---

### ❌ Bad Usage (Guard Adds No Value)

Do **not** guard simple logging like this:

```java
if (log.isDebugEnabled()) {
    log.debug("Mode set to {}", mode);
}
```

```java
if (log.isDebugEnabled()) {
    log.debug("Backtracking enabled");
}
```

Why this is bad:

* Enums, primitives, and simple strings are cheap
* The guard adds noise and reduces readability
* SLF4J already skips output when DEBUG is disabled

Prefer:

```java
log.debug("Mode set to {}", mode);
```

---

### ⚠️ Common Misconception

```java
log.debug("Value: {}", expensiveCall());
```

This **still calls** `expensiveCall()` even if DEBUG is disabled.

Correct version:

```java
if (log.isDebugEnabled()) {
    log.debug("Value: {}", expensiveCall());
}
```

---

### Summary Guidance

| Situation                          | Use `isXEnabled()`? |
| ---------------------------------- | ------------------- |
| Logging primitives / enums         | ❌ No                |
| Logging simple strings             | ❌ No                |
| Logging domain objects             | ✅ Yes               |
| Logging via multiple getters       | ✅ Yes               |
| Logging inside loops               | ✅ Yes               |
| Logging heavy `toString()` / dumps | ✅ Yes               |

> **Rule of thumb:** If removing the guard would cause *real work* to happen, keep it. Otherwise, don’t.

## Log Configuration (Log4j2)

Configuration file is located at: `src/main/resources/log4j2.xml`

This file controls:

* Console formatting
* File logging
* Log rotation
* Logging levels per package

### Viewing Logs

During development, logs appear in the **console**.

Logs files are also written to: `~/.dptu/logs`

## CI Enforcement

This repository includes automated CI checks that enforce these logging standards (`.github/workflows/logging-check.yml`).

### Enforced as Errors

* Console output (`System.out.*`, `System.err.*`)
* `printStackTrace()` usage
* `java.util.logging` imports or logger creation
* Direct Log4j / Log4j2 API usage
* `System.exit(...)`

### Enforced as Warnings

* Classes with no logging at all
* Classes that import or declare an SLF4J `Logger` but never use it

These checks exist to keep logs consistent, searchable, and production-safe.

### Sanity Exemptions

Some Java file types are assumed not to require logging by default and are exempt from “missing logger” warnings:

* enum
* interface
* @interface (annotations)
* record

These are considered declarative constructs, not behavioral classes. You do not need to add a logger or suppression for these.

### Suppressing Logging Warnings

In rare cases, a Java class may not require logging. Examples include:

* DTOs / value objects
* Marker or configuration-only classes
* Pure UI layout containers with no behavior
* Generated code

To explicitly suppress CI logging warnings, annotate the class with:

```java
@SuppressWarnings("Logging")
public class ExampleDto {
    private final int id;
    private final String name;
}
```

## Common Mistakes to Avoid

| Mistake                                       | Correct Usage                                                 |
| --------------------------------------------- | ------------------------------------------------------------- |
| `System.out.print*`, `System.err.print*`      | `log.info(...)`, `log.warn(...)`, etc.                        |
| `e.printStackTrace()` / `t.printStackTrace()` | `log.error("message", e)`                                     |
| Importing or using `java.util.logging`        | Use SLF4J only                                                |
| Using Log4j / Log4j2 APIs directly            | Use SLF4J (`org.slf4j.Logger`) only                           |
| `System.exit(...)`                            | Log the error and allow normal shutdown                       |
| Hard-coded string concatenation               | Use `{}` parameter placeholders                               |

> Hard-coded string concatenation example to avoid:
>
> ```java
> log.info("Count is " + count); // inefficient + noisy
> ```
>
> Correct:
>
> ```java
> log.info("Count is {}", count);
> ```

## Quick Checklist for New Code

* [ ] Add `private static final Logger log = LoggerFactory.getLogger(ThisClass.class);`
* [ ] **Actually use the logger** (at least some meaningful `log.*(...)` calls)
* [ ] Use `log.info()`, `log.debug()`, etc. appropriately
* [ ] Include exception objects in `log.error("message", e)` calls where applicable
* [ ] Replace all `System.out.*`, `System.err.*`, and `printStackTrace()` usage
* [ ] Use `isXEnabled()` only when it saves work
* [ ] Do **not** import or use Log4j / Log4j2 APIs directly
* [ ] Do not suppress logging on behavioral classes

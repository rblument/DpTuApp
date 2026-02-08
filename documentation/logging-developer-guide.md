# DpTu Logging Developer Guide

This document explains how logging works in the DpTu application and how to use it correctly when developing new features.

It was last reviewed on 3 Feb 2026 by Harrison Sherwin.

## Overview

DpTu now uses:

| Layer | Library | Purpose |
|------|---------|---------|
| **API used in code** | **SLF4J** (`org.slf4j.Logger`) | Unified logging interface used everywhere in application code |
| **Logging backend** | **Log4j2** | Controls formatting, output, file logging, rotation, and log levels |

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

- It follows standard Java & SLF4J convention.
- Avoids confusion with compile-time constants (which use ALL_CAPS).

## Logging Levels

| Level | When to Use |
|------|-------------|
| `log.trace()` | Extremely detailed information (rare) |
| `log.debug()` | Developer troubleshooting, internal details |
| `log.info()` | Normal operations, startup, shutdown, user actions |
| `log.warn()` | Something unexpected happened, but the system can continue |
| `log.error()` | Serious issue or failure; something has gone wrong |

### Examples

```java
log.info("User {} logged in", userId);
log.warn("Could not load resource {}; using default instead", resourceName);
log.error("Failed to save record", exception);
```

## Log Configuration (Log4j2)

Configuration file is located at: `src/main/resources/log4j2.xml`

This file controls:

- Console formatting
- File logging
- Log rotation
- Logging levels per package

### Viewing Logs

During development, logs appear in the **console**.

Logs files are also written to: `~/.dptu/logs`

## CI Enforcement

This repository includes automated CI checks that enforce these logging standards (`.github/workflows/logging-check.yml`).

### Enforced as Errors

- Console output (`System.out.*`, `System.err.*`)
- `printStackTrace()` usage
- `java.util.logging` imports or logger creation
- Direct Log4j / Log4j2 API usage
- `System.exit(...)`

### Enforced as Warnings

- Classes with no logging at all
- Classes that import or declare an SLF4J `Logger` but never use it

These checks exist to keep logs consistent, searchable, and production-safe.

### Suppressing Logging Warnings

In rare cases, a Java class may not require logging. Examples include:

- DTOs / value objects
- Marker or configuration-only classes
- Pure UI layout containers with no behavior
- Generated code

To explicitly suppress CI logging warnings, annotate the class with:

```java
@SuppressWarnings("Logging")
public class ExampleDto {
    private final int id;
    private final String name;
}
```

## Legacy JUL Logging (Temporary Compatibility)

Some older classes still use `java.util.logging` (JUL).  
Until they are fully migrated, JUL loggers are named `julLogger` and use **fully-qualified types**:

```java
private static final java.util.logging.Logger julLogger =
    java.util.logging.Logger.getLogger(MyClass.class.getName());

julLogger.log(java.util.logging.Level.WARNING, "Old logging path still active");
```

### Rules for JUL Code During Migration

- **Do not** import `java.util.logging.Logger`
- **Do not** use `Logger.getLogger(...)`
- Always use fully-qualified names (`java.util.logging.Logger`, `java.util.logging.Level`)

Eventually, JUL will be removed.

A legacy configuration file for JUL is at: `src/main/resources/Logging.properties`

## Common Mistakes to Avoid

| Mistake | Correct Usage |
|--------|---------------|
| `System.out.print*`, `System.err.print*` | `log.info(...)`, `log.warn(...)`, etc. |
| `e.printStackTrace()` / `t.printStackTrace()` | `log.error("message", e)` |
| Importing or using `java.util.logging` | Use SLF4J, or fully-qualified JUL types *only in legacy code* |
| Using Log4j / Log4j2 APIs directly | Use SLF4J (`org.slf4j.Logger`) only |
| `System.exit(...)` | Log the error and allow normal shutdown |
| Hard-coded string concatenation | Use `{}` parameter placeholders |

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

- [ ] Add `private static final Logger log = LoggerFactory.getLogger(ThisClass.class);`
- [ ] **Actually use the logger** (at least some meaningful `log.*(...)` calls)
- [ ] Use `log.info()`, `log.debug()`, etc. appropriately
- [ ] Include exception objects in `log.error("message", e)` calls where applicable
- [ ] Replace all `System.out.*`, `System.err.*`, and `printStackTrace()` usage
- [ ] Do **not** import or use Log4j / Log4j2 APIs directly
- [ ] If you *must* use JUL temporarily, use the `julLogger` pattern (fully-qualified names only)

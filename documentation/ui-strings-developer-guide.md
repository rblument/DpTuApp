# DpTu UI Strings Developer Guide

This guide explains how DpTu handles user-facing UI text using message bundles, why this is a best practice, and how to add or update strings correctly.

It was created on 18 Mar 2026.

## Why This Is Best Practice

Storing user-facing text directly in Java classes creates long-term problems:

- Localization is expensive: every language update requires code changes.
- Inconsistent wording: similar screens often drift in tone and terminology.
- Risky maintenance: editing text can accidentally affect behavior when mixed with logic.
- Hard review process: content changes are buried inside code diffs.

Centralizing UI text in [src/main/resources/Msgs.properties](../src/main/resources/Msgs.properties) solves this:

- Enables localization by replacing or adding locale bundles.
- Keeps wording consistent and reviewable in one place.
- Lets engineers change behavior without touching copy, and vice versa.
- Makes future content QA and translation handoff straightforward.

## How It Works In DpTu

DpTu now uses [src/main/java/edu/regis/dptu/util/ResourceMgr.java](../src/main/java/edu/regis/dptu/util/ResourceMgr.java) as the single access point for localized strings.

### Message Lookup

Use:

- `ResourceMgr.instance().string("key")` for static text
- `ResourceMgr.instance().string("key", arg1, arg2, ...)` for formatted text

The manager:

- Loads `Msgs` bundle with configured locale from [src/main/resources/DpTu.properties](../src/main/resources/DpTu.properties)
- Applies locale-aware `MessageFormat` for parameterized strings
- Returns a visible fallback token like `!!missing.key!!` and logs a warning when a key is missing

This behavior prevents hard crashes from missing keys and makes key issues obvious during testing.

### Message Source

Primary bundle:

- [src/main/resources/Msgs.properties](../src/main/resources/Msgs.properties)

Keys are grouped by feature area, for example:

- `action.*`
- `dialog.*`
- `auth.*`
- `dashboard.*`
- `stepView.*`
- `newAccount.*`

## How This Supports Localization Later

The app is now prepared for language expansion without changing UI classes:

1. Keep existing keys in `Msgs.properties` as the base bundle.
2. Add locale-specific bundles, for example:
   - `Msgs_es_ES.properties`
   - `Msgs_fr_FR.properties`
3. Translate values only; do not rename keys.
4. Set language and country in [src/main/resources/DpTu.properties](../src/main/resources/DpTu.properties).

Because all UI code resolves text through `ResourceMgr`, switching locale automatically changes text sources for all migrated components.

## How To Add New UI Text

When implementing UI or dialog changes:

1. Add a descriptive key to [src/main/resources/Msgs.properties](../src/main/resources/Msgs.properties).
2. Use `ResourceMgr.instance().string(...)` in Java code.
3. Use parameterized messages for dynamic values.
4. Compile and run UI flows to check for missing-key fallback tokens.

### Example: Static Label

Properties:

```properties
profile.button.save=Save Profile
```

Java:

```java
saveButton = new JButton(ResourceMgr.instance().string("profile.button.save"));
```

### Example: Parameterized Message

Properties:

```properties
auth.error.attempt=Invalid password attempt {0} of {1}
```

Java:

```java
String msg = ResourceMgr.instance().string("auth.error.attempt", attempt, maxAttempts);
```

## Key Naming Guidelines

Use consistent, domain-based names:

- `feature.component.purpose`
- `dialog.title.*` for dialog titles
- `action.<name>.name` and `action.<name>.tooltip` for actions

Good:

- `dashboard.button.viewStats`
- `dialog.title.error`
- `newAccount.validation.invalidEmail`

Avoid:

- Generic keys like `title1` or `msgA`
- Duplicating near-identical keys when a parameterized key is better

## Developer Checklist

Before opening a PR:

- [ ] No new user-facing literals added in view/action classes
- [ ] New strings added to [src/main/resources/Msgs.properties](../src/main/resources/Msgs.properties)
- [ ] Dynamic text uses parameterized keys where appropriate
- [ ] UI tested for missing-key fallback tokens (`!!key!!`)
- [ ] Build passes (`mvn -q -DskipTests compile` at minimum)

## Scope Notes

This pattern is for user-facing text. Internal exception messages and technical logs can remain code-local unless they are displayed directly to end users.

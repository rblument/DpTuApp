# Session Saving and Resuming Developer Guide

## Purpose

This guide explains how tutoring session save/resume works in DpTu after the session persistence enhancements.

It covers:

- what state is saved
- where the state is stored
- how state is restored on sign-in and lesson launch
- how to test and extend the behavior

## High-Level Flow

1. Student signs in.
2. The server returns a `TutoringSession` payload via `SIGN_IN`.
3. The client stores this as the signed-in session in `SplashFrame`.
4. Student starts a mode (`SEE_ONE`, `DO_ONE`, `TEACH_ONE`).
5. If a saved session exists for that mode/problem kind with pending progress, the mode action resumes it.
6. If no matching saved progress exists, a fresh in-memory lesson session is created.
7. When the student triggers Save, `SaveSessionAction` writes session metadata and pending progress.

## Primary Classes

- `src/main/java/edu/regis/dptu/view/act/SaveSessionAction.java`
  - Handles user Save action.
  - Calls `SessionSvc.update(...)` with fallback to `create(...)` if the session is missing.
  - Shows user-visible success/error dialogs.

- `src/main/java/edu/regis/dptu/dao/SessionDAO.java`
  - Persists `TutoringSession` metadata (token, course, unit, mode, active flag).
  - Persists current pending task/step state.
  - Restores pending task/step state on retrieve.

- `src/main/java/edu/regis/dptu/view/act/SeeOneAction.java`
- `src/main/java/edu/regis/dptu/view/act/DoOneAction.java`
- `src/main/java/edu/regis/dptu/view/act/TeachOneAction.java`
  - Prefer resuming saved session state when it matches selected mode/problem type and includes pending task data.

- `src/main/java/edu/regis/dptu/view/act/SignInAction.java`
  - Deserializes full session JSON from server into `TutoringSession` and stores it in `SplashFrame`.

## Persistence Model

### TutoringSession table

`SessionDAO` create/update now persists mode in addition to existing session metadata.

Relevant columns:

- `SessionId`
- `SecurityToken`
- `UserId`
- `CourseId`
- `UnitId`
- `IsActive`
- `ProblemType`
- `ProblemId`
- `Mode` (new)

### PendingTask and PendingStep tables

These tables store resumable in-progress state for the active task/step:

- `PendingTask(SessionId, TaskId, PendingStepId)`
- `PendingStep(Id, SessionId, StepId, NotifyTutor, IsCompleted, CurrentHintIndex)`

On save/update:

1. Existing pending progress rows for the session are deleted.
2. Current pending step is inserted in `PendingStep`.
3. Current pending task is inserted in `PendingTask` linked to the inserted pending step.

On retrieve:

1. Session metadata is loaded from `TutoringSession`.
2. Problem is loaded via `ProblemSvc`.
3. Pending task/step row is joined and loaded.
4. Task and steps are loaded via `CourseSvc.retrieveTask(...)`.
5. Pending step flags and hint index are restored.

## Resume Decision in Mode Actions

Each mode action resumes only when all of these are true:

- saved session mode equals action mode
- saved session has a problem
- saved session problem kind equals selected problem kind
- saved session has pending task data

Otherwise, action falls back to creating a fresh session scaffold.

## Save Action Feedback Behavior

`SaveSessionAction` feedback outcomes:

- No active lesson session: informational dialog
- Save success (`update`): informational dialog
- Missing session (`ObjNotFoundException`): fallback to `create` then informational dialog
- Save/create failure: error dialog

## Database Setup Note

`database/setup_DpTuDB.sql` includes `Mode` on `TutoringSession`.

For existing databases created before this change, add a migration before running new save/resume logic. Example:

```sql
ALTER TABLE TutoringSession
ADD COLUMN Mode ENUM('SEE_ONE', 'DO_ONE', 'TEACH_ONE') DEFAULT 'SEE_ONE';
```

## Tests

### Save action integration tests

- `src/test/java/edu/regis/dptu/view/act/SaveSessionActionIntegrationTest.java`

Covers:

- successful save
- missing-session fallback create
- no active session feedback
- persistence failure feedback

### Session DAO tests

- `src/test/java/edu/regis/dptu/test/SessionDAOTest.java`

Covers:

- mode persistence in create/update/retrieve
- pending progress persistence during save/update
- pending progress rehydration during retrieve

## Current Scope and Limitations

Current save/resume persists and restores:

- session metadata
- mode
- active pending task ID
- active pending step ID
- pending step flags/hint index

Not yet persisted:

- full algorithm runtime state inside `Problem` (for example full DP table execution position history)
- multiple pending tasks per session beyond active task row

If full algorithm-level runtime replay is needed, extend `Problem` persistence separately.

## Extension Guidance

If extending save/resume:

1. Add schema fields/tables first.
2. Keep all session save operations transactional in DAO methods.
3. Extend `SessionDAO.persistPendingProgress(...)` and `loadPendingProgress(...)` together.
4. Add/update both DAO tests and action-level integration tests.
5. Keep user-facing feedback text in `Msgs.properties`.

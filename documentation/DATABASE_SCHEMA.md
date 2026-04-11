DpTu Database Schema Documentation

This document provides a comprehensive description of the database schema used by the Dynamic Programming Tutor (DpTu) application, derived from analysis of the Java DAO (Data Access Object) classes. For each table, it lists columns, inferred data types, keys/constraints, and a description of the table's purpose.

GENERAL NOTES

- Inference: Table structures, column names, data types, primary keys (PK), and foreign keys (FK) are primarily inferred from SQL query strings and JDBC method calls.
- Data Types: SQL data types listed are MySQL equivalents. VARCHAR lengths are suggested. BOOLEAN is TINYINT(1). TEXT is used for long strings.
- Constraints: PK and FK constraints are inferred. NOT NULL and UNIQUE are assumed based on typical usage.
- Enums: Enum values are stored as VARCHARs containing the enum constant's name.
- Auto Increment: Columns inferred to be AUTO_INCREMENT based on primary key usage and generated keys.

TABLE OF CONTENTS

- Account
- Course
- Unit
- Task
- Step
- Hint
- KnowledgeComponent
- LCSProblem
- MatrixChainProblem
- MatrixSizes
- ExercisingLocation
- Timeout
- InfoMsgStep
- TutoringSession
- StudentModel
- Assessment

ACCOUNT

Purpose: Stores user account information including credentials and personal details.

- UserId: VARCHAR(255) [PRIMARY KEY]
  Email used as user ID and login.
- Password: VARCHAR(64) [NOT NULL]
  SHA-256 hash of password.
- FirstName: VARCHAR(100) [NOT NULL]
- LastName: VARCHAR(100) [NOT NULL]
- Question: INT [NOT NULL]
  Security question index.
- Answer: VARCHAR(64) [NOT NULL]
  SHA-256 hash of security answer.
- IsStudent: BOOLEAN [NOT NULL]
  True if user is a student.

COURSE

Purpose: Defines the structure and properties of a course.

- CourseId: INT [PRIMARY KEY]
- Title: VARCHAR(255) [NOT NULL]
- PrimaryPedagogy: VARCHAR(50) [NOT NULL]
  Stores TaskSelectionKind enum name.
- Description: TEXT

UNIT

Purpose: Represents a logical module or section in a course.

- UnitId: INT [PRIMARY KEY]
- CourseId: INT [NOT NULL, FK to Course(CourseId)]
- Title: VARCHAR(255) [NOT NULL]
- Description: TEXT
- SequenceIndex: INT [NOT NULL]
- Pedagogy: VARCHAR(50) [NOT NULL]

TASK

Purpose: Represents an activity, problem, or exercise within a unit.

- TaskId: INT [PRIMARY KEY]
- CourseId: INT [NOT NULL, FK to Course(CourseId)]
- UnitId: INT [NOT NULL, FK to Unit(UnitId)]
- Title: VARCHAR(255) [NOT NULL]
- Description: TEXT
- Kind: VARCHAR(50) [NOT NULL]
  Stores TaskKind enum name.
- SequenceIndex: INT [NOT NULL]
- KindId: INT
  FK to problem data (e.g., LCSProblem.Id)

STEP

Purpose: Represents a single interaction required to complete a task.

- Id: INT [PRIMARY KEY]
- CourseId: INT [NOT NULL, FK to Course(CourseId)]
- TaskId: INT [NOT NULL, FK to Task(TaskId)]
- Title: VARCHAR(255)
- Description: TEXT
- SequenceIndex: INT [NOT NULL]
- StepSubType: VARCHAR(50) [NOT NULL]
  Stores StepSubType enum name.
- SubTypeId: INT
  FK to subtype-specific table (e.g., InfoMsgStep)
- TimeoutId: INT
  FK to Timeout(Id)

HINT

Purpose: Stores hint messages for steps.

- Id: INT [PRIMARY KEY, AUTO_INCREMENT]
- StepId: INT [NOT NULL, FK to Step(Id)]
- Text: TEXT [NOT NULL]
- SequenceIndex: INT [NOT NULL]

KNOWLEDGE COMPONENT

Purpose: Defines learning outcomes or concepts tracked in the course.

- Id: INT [PRIMARY KEY]
- CourseId: INT [NOT NULL, FK to Course(CourseId)]
- Title: VARCHAR(255) [NOT NULL]
- Description: TEXT
- BloomLevel: VARCHAR(50) [NOT NULL]
  Stores BloomLevel enum name.
- IsDomainFocus: BOOLEAN [NOT NULL]
- Pedagogy: VARCHAR(50) [NOT NULL]
  Stores TaskSelectionKind enum name.
- ExercisingLocations: TEXT
  Comma-separated list of ExercisingLocation.Id values.
- Granularity: VARCHAR(50) [NOT NULL]
  Stores OutcomeGranularity enum name.

LCSPROBLEM

Purpose: Stores input for Longest Common Subsequence problems.

- Id: INT [PRIMARY KEY]
- Title: VARCHAR(255)
- Description: TEXT
- Sequence1: TEXT [NOT NULL]
- Sequence2: TEXT [NOT NULL]

MATRIXCHAINPROBLEM

Purpose: Stores input for Matrix Chain Optimization problems.

- Id INT [PRIMARY KEY]

MATRIXSIZES

Purpose: Stores input on sizes of matrices for a specific Matrix Chain Optimization problem.

- SizeId: INT [PRIMARY KEY]
- ProblemId: INT [NOT NULL, FK to MatrixChainProblem(Id)]
- Width: INT
- Height: INT

EXERCISING LOCATION

Purpose: Maps Knowledge Components to locations where they are taught or practiced.

- Id: INT [PRIMARY KEY, AUTO_INCREMENT]
- CourseId: INT [NOT NULL, FK to Course(CourseId)]
- UnitId: INT [nullable, FK to Unit(UnitId)]
- TaskId: INT [nullable, FK to Task(TaskId)]
- StepId: INT [nullable, FK to Step(Id)]

TIMEOUT

Purpose: Defines timeout behavior linked to steps.

- Id: INT [PRIMARY KEY, AUTO_INCREMENT]
- TimeoutType: VARCHAR(100)
- Seconds: INT [NOT NULL]
- Event: VARCHAR(100)
- Msg: TEXT

INFOMSGSTEP

Purpose: Stores content for informational steps.

- SubStepId: INT [PRIMARY KEY]
- Text: TEXT [NOT NULL]

TUTORING SESSION

Purpose: Tracks active or past sessions linking students to problems.

- SessionId: INT [PRIMARY KEY, AUTO_INCREMENT]
- SecurityToken: VARCHAR(64) [NOT NULL]
- UserId: VARCHAR(255) [NOT NULL, FK to Account(UserId)]
- CourseId: INT [NOT NULL, FK to Course(CourseId)]
- UnitId: INT [NOT NULL, FK to Unit(UnitId)]
- IsActive: BOOLEAN [NOT NULL, DEFAULT TRUE]
- StartDate: TIMESTAMP [NOT NULL, DEFAULT CURRENT_TIMESTAMP]
- ProblemKind: VARCHAR(50) [NOT NULL]
  Stores TaskKind enum name.
- ProblemId: INT [NOT NULL]

STUDENT MODEL

Purpose: Stores overall adaptive preferences for students.

- UserId: VARCHAR(255) [PRIMARY KEY, FK to Account(UserId)]
- ScaffoldLevel: VARCHAR(50) [NOT NULL]

ASSESSMENT

Purpose: Tracks performance on individual Knowledge Components.

- Id: INT [PRIMARY KEY, AUTO_INCREMENT]
- UserId: VARCHAR(255) [NOT NULL, FK to StudentModel(UserId)]
- KnowledgeComponentId: INT [NOT NULL, FK to KnowledgeComponent(Id)]
- AssessmentLevel: VARCHAR(50) [NOT NULL]
  Stores AssessmentLevel enum name.
- Exposures: INT [NOT NULL, DEFAULT 0]
- Successes: INT [NOT NULL, DEFAULT 0]
- Hints: INT [NOT NULL, DEFAULT 0]

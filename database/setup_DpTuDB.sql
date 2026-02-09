/* 
 * DPTu: Dynamic Programming Tutor
 * 
 *  (C) Johanna & Richard Blumenthal, All rights reserved
 * 
 *  Unauthorized use, duplication or distribution without the authors'
 *  permission is strictly prohibited.
 * 
 *  Unless required by applicable law or agreed to in writing, this
 *  software is distributed on an "AS IS" basis without warranties
 *  or conditions of any kind, either expressed or implied.
 */
/**
 * Author:  rickb
 * Created: Jan 26, 2025
 */

# If the DpTuDB exists, drop it. In general, you will lose any existing data.
DROP DATABASE IF EXISTS DpTuDB;

#
# Create a database in MySql
CREATE DATABASE DpTuDB;

# Create user representing the tutor.
CREATE USER IF NOT EXISTS 'DpTuTs'@'localhost' IDENTIFIED BY 'DpTu2023';

# Give the DpTuTs tutor the following priveledges.
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP ON DpTuDB.* TO 'DpTuTs'@'localhost';

# Switch to the DpTuDB
USE DpTuDB;

# Create Tables

# Create the User Account Table
CREATE TABLE Account (
   UserId VARCHAR(256),
   Password VARCHAR(256) NOT NULL,
   FirstName VARCHAR(256),
   LastName VARCHAR(256),
   Question int,
   Answer VARCHAR(256),
   IsStudent tinyint DEFAULT 0,
   PRIMARY KEY (UserId)
);

CREATE TABLE StudentModel (
   UserId VARCHAR(255) NOT NULL PRIMARY KEY,
   ScaffoldLevel VARCHAR(16) NOT NULL
);

#
# Course related Tables

# Create the Course Table
CREATE TABLE Course(
   CourseId INT NOT NULL AUTO_INCREMENT,
   Title VARCHAR(256),
   PrimaryPedagogy ENUM(
      'STUDENT_CHOICE',
      'FIXED_SEQUENCE',
      'MASTERY_LEARNING',
      'MICROADAPTATION',
      'OTHER',
      'ERROR'
   ),
   Description VARCHAR(256),

   PRIMARY KEY (CourseId)
);

# Create the Unit Table
# All Units belong to a course (hence not null CourseId)
CREATE TABLE Unit (
   UnitId INT NOT NULL AUTO_INCREMENT,
   CourseId INT NOT NULL,
   Title VARCHAR(256),
   Description VARCHAR(256),
   SequenceIndex INT DEFAULT 0,
   Pedagogy ENUM(
      'STUDENT_CHOICE',
      'FIXED_SEQUENCE',
      'MASTERY_LEARNING',
      'MICROADAPTATION'
   ),
   PRIMARY KEY(UnitId),
   FOREIGN KEY (CourseId)
      REFERENCES Course(CourseId)
      ON UPDATE CASCADE ON DELETE CASCADE
);

# Create the TutoringSession Table
# SecurityToken is a 256bit hash, so it has constant chars
# Session is one-to-one with Account and Course
CREATE TABLE TutoringSession (
   SessionId INT NOT NULL AUTO_INCREMENT,
   SecurityToken CHAR(255) NOT NULL,
   UserId VARCHAR(256) NOT NULL,
   CourseId INT NOT NULL,
   UnitId INT NOT NULL,
   IsActive BOOLEAN DEFAULT false,
   StartDate TIMESTAMP NOT NULL,
   ProblemType ENUM (
    'LCS_PROBLEM',
    'MATRIX_CHAIN',
    'KNAPSACK_0_1'
   ),
   ProblemId INT NOT NULL,

   PRIMARY KEY (SessionId),
   FOREIGN KEY (UserId)
      REFERENCES Account(UserId)
      ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (CourseId)
      REFERENCES Course(CourseId)
      ON UPDATE CASCADE ON DELETE CASCADE
);

# KindId is an index into the appropriate table identified by Kind.
# For example, if the Kind is PROBLEM, then the KindId is the id of the
# problem in the Problem table.
CREATE TABLE Task (
  TaskId int NOT NULL DEFAULT 0,
  CourseId int NOT NULL DEFAULT 1,
  UnitId int DEFAULT NULL,
  SequenceIndex int,
  Title varchar(256) NOT NULL,
  Description varchar(256) NOT NULL,
  Kind ENUM(
      'PROBLEM',
      'INITIALIZE_FIRST_ROW',
      'INITIALIZE_FIRST_COL',
      'ASSIGN_CELL',
      'CREATE_TABLE',
      'SOLUTION_PATH',
      'SOLVE'
   ),
  KindId int,
  PRIMARY KEY (TaskId));

# SubTypeId is determined by the StepSubType. For example, for a PROBLEM_REVIEW,
# the SubTypeId is an id of a problem in the Problem table
CREATE TABLE Step (
  Id int NOT NULL,
  CourseId int NOT NULL,
  UnitId int NOT NULL,
  TaskId int NOT NULL,
  Title varchar(256) DEFAULT NULL,
  Description varchar(256) DEFAULT NULL,
  SequenceIndex int DEFAULT NULL,
  ExercisedComponentId int,
  StepSubType ENUM(
      'PROBLEM_REVIEW',
      'INFO_MESSAGE',
      'GUI_ACTION',
      'COMPLETE_CELL',
      'COMPLETE_FIRST_ROW',
      'COMPLETE_FIRST_COL'
   ),
  SubTypeId int,
  TimeoutId int,
  PRIMARY KEY (Id));

CREATE TABLE Hint (
  Id int NOT NULL DEFAULT '0',
  StepId int NOT NULL,
  Text varchar(256) DEFAULT NULL,
  SequenceIndex int DEFAULT NULL,
  PRIMARY KEY (Id));

CREATE TABLE Timeout (
  id int NOT NULL,
  TimeoutType varchar(256),
  Seconds int,
  Event varchar(256),
  Msg varchar(4096),
  PRIMARY KEY(id));

CREATE TABLE PendingTask (
  SessionId int NOT NULL,
  TaskId int NOT NULL,
  PendingStepId int NOT NULL,
  PRIMARY KEY (SessionId)
);

CREATE TABLE PendingStep (
  Id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  SessionId int NOT NULL,
  StepId int NOT NULL,
  NotifyTutor tinyint DEFAULT 0,
  IsCompleted tinyint DEFAULT 0,
  CurrentHintIndex int NOT NULL
);

CREATE TABLE KnowledgeComponent (
  Id int NOT NULL ,
  CourseId int NOT NULL,
  Title varchar(256) NOT NULL,
  Description varchar(255),
  BloomLevel varchar(256) NOT NULL,
  IsDomainFocus tinyint,
  Pedagogy varchar(256),
  ExercisingLocations varchar(256),
  Granularity varchar(256),
  PRIMARY KEY (Id));

CREATE TABLE ExercisingLocation (
  Id int NOT NULL,
  CourseId int,
  UnitId int,
  TaskId int,
  StepId int,
  PRIMARY KEY (Id));

CREATE TABLE Assessment (
   Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   UserId VARCHAR(256) NOT NULL,
   KnowledgeComponentId INT NOT NULL,
   AssessmentLevel VARCHAR(32) NOT NULL,
   Exposures INT,
   Successes INT,
   Hints INT
);

# If ProblemType is LCS_PROBLEM, then SubTypeId is the id of the problem in
# the LCSProblem table
CREATE TABLE Problem (
  Id INT NOT NULL,
  ProblemType ENUM (
    'LCS_PROBLEM',
    'MATRIX_CHAIN',
    'KNAPSACK_0_1'
  ),
  SubTypeId INT NOT NULL,
  Title VARCHAR(256),
  Description VARCHAR(256),
  PRIMARY KEY (Id)
); 

CREATE TABLE LCSProblem (
  Id int NOT NULL,
  Sequence1 VARCHAR(256),
  Sequence2 VARCHAR(256),
  PRIMARY KEY (Id)
);

#records when a student completes a Task, per student
# used to persist progress across sessions
CREATE TABLE CompletedTask (
  UserId VARCHAR(256) NOT NULL,
  TaskId INT NOT NULL,
  CompletedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (UserId, TaskId)
);

/*********************************************************************************
* Seeds
*/

INSERT INTO Course
  (CourseId, Title, PrimaryPedagogy, Description)
VALUES
  (1,'Dynamic Programming', 'FIXED_SEQUENCE', 
  'Familiarizes students with the Dynamic Programming Paradigm.');

INSERT INTO Unit
  (CourseId, Title, Description, SequenceIndex, Pedagogy)
VALUES
  (1, 'LCS: See One', 
  'In this unit, the student will see an example of a Dynamic Programming
   approach that solves a Longest Common Subsequence (LCS) problem for two
   string sequences.', 
    0, 'FIXED_SEQUENCE');

INSERT INTO Task
 (TaskId, CourseId, UnitId, SequenceIndex, Title,
  Description,
  Kind, KindId)
 VALUES
 (0, 1, 1, 0, 'Problem Overview', 
  'Review the presentation of the current Dynamic Programming problem', 
  'PROBLEM', 0);

INSERT INTO Step 
 (Id, CourseId, UnitId, TaskId, SequenceIndex,
  Title, 
  Description,
  ExercisedComponentId,
  StepSubType,
  SubTypeId, TimeoutId)
 VALUES
 (0, 1, 1, 0, 0,
  'Review Problem', 'Acknowledge understanding of the dynamic programming problem.',
  0, 'PROBLEM_REVIEW', 0, 0);


INSERT INTO Timeout (id, TimeoutType, Seconds, Event, Msg) VALUES
 (0, 'Info Message', 360, 'Reminder', 'Please acknowledge you understand the current problem.');

INSERT INTO Hint
(Id, StepId, Text, SequenceIndex)
VALUES
(0, 0, 'Acknowledge this problem review by pressing the ''Acknowledged'' button.', 0);

INSERT INTO KnowledgeComponent
(Id, CourseId, Title,
 Description,
 BloomLevel, IsDomainFocus, Pedagogy, ExercisingLocations, Granularity)
VALUES
(0, 1, 'Problem Review Acknowledgement', 
 'Student has appropriately demonstrated acknowleding they understand the current problem.',
 'Application', 0, 'Other', '0', 'Knowledge Component');

INSERT INTO Problem
 (Id, ProblemType, SubTypeId, Title, Description)
VALUES
 (0, 'LCS_PROBLEM', 0, 'Longest Common Subsequence Problem 1',
  'Determine the longests common subsequence for the given sequences/strings.');

INSERT INTO LCSProblem
 (Id, Sequence1, Sequence2)
 VALUES
 (0, 'skullandbones', 'lullabybabies');

/*********************************************************************************
* Foreign Indexes
*/

ALTER TABLE TutoringSession
ADD CONSTRAINT fk_tutoring_session_unit
FOREIGN KEY (UnitId) REFERENCES Unit(UnitId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Task
ADD CONSTRAINT fk_task_course
FOREIGN KEY (CourseId) REFERENCES Course(CourseId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Task
ADD CONSTRAINT fk_task_unit
FOREIGN KEY (UnitId) REFERENCES Unit(UnitId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Step
ADD CONSTRAINT fk_step_course
FOREIGN KEY (CourseId) REFERENCES Course(CourseId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Step
ADD CONSTRAINT fk_step_unit
FOREIGN KEY (UnitId) REFERENCES Unit(UnitId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Step
ADD CONSTRAINT fk_step_task
FOREIGN KEY (TaskId) REFERENCES Task(TaskId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE PendingTask
ADD CONSTRAINT fk_pending_task_session
FOREIGN KEY (SessionId) REFERENCES TutoringSession(SessionId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE PendingTask
ADD CONSTRAINT fk_pending_task_task
FOREIGN KEY (TaskId) REFERENCES Task(TaskId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE PendingStep
ADD CONSTRAINT fk_pending_step_session
FOREIGN KEY (SessionId) REFERENCES TutoringSession(SessionId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE PendingStep
ADD CONSTRAINT fk_pending_step_step
FOREIGN KEY (StepId) REFERENCES Step(Id)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Hint
ADD CONSTRAINT fk_hint_step
FOREIGN KEY (StepId) REFERENCES Step(Id)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Assessment
ADD CONSTRAINT fk_assessment_user
FOREIGN KEY (UserId) REFERENCES Account(UserId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Assessment
ADD CONSTRAINT fk_assessment_knowledge_component
FOREIGN KEY (KnowledgeComponentId) REFERENCES KnowledgeComponent(Id)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE KnowledgeComponent
ADD CONSTRAINT fk_knowledge_component_course
FOREIGN KEY (CourseId) REFERENCES Course(CourseId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ExercisingLocation
ADD CONSTRAINT fk_exercising_location_course
FOREIGN KEY (CourseId) REFERENCES Course(CourseId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ExercisingLocation
ADD CONSTRAINT fk_exercising_location_unit
FOREIGN KEY (UnitId) REFERENCES Unit(UnitId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ExercisingLocation
ADD CONSTRAINT fk_exercising_location_task
FOREIGN KEY (TaskId) REFERENCES Task(TaskId)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ExercisingLocation
ADD CONSTRAINT fk_exercising_location_step
FOREIGN KEY (StepId) REFERENCES Step(Id)
ON UPDATE CASCADE ON DELETE CASCADE;

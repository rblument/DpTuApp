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
CREATE USER 'DpTuTs'@'localhost' IDENTIFIED BY 'DpTu2023';

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

#
# Course related Tables

# Create the Course Table
CREATE TABLE Course(
   CourseId INT NOT NULL AUTO_INCREMENT,
   Title VARCHAR(256),
   PrimaryPedagogy ENUM(
      'Student Choice',
      'Fixed Sequence',
      'Mastery Learning',
      'Microadaptation',
      'Other',
      'Error'
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
      'Student Choice',
      'Fixed Sequence',
      'Mastery Learning',
      'Microadaptation',
      'Other',
      'Error'
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
   SecurityToken CHAR(256) NOT NULL,
   UserId VARCHAR(256) NOT NULL,
   CourseId INT NOT NULL,
   UnitId INT NOT NULL,
   IsActive BOOLEAN DEFAULT false,
   StartDate TIMESTAMP NOT NULL,

   PRIMARY KEY (SessionId),
   FOREIGN KEY (UserId)
      REFERENCES Account(UserId)
      ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (CourseId)
      REFERENCES Course(CourseId)
      ON UPDATE CASCADE ON DELETE CASCADE
);
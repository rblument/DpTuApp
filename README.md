
# DpTu

## Documentation
In the `documentation` directory, the `DatabaseDiagram.erdplus` file is an Entity Relationship Diagram formatted for the free online tool *ERDPlus* (found at this address: [https://erdplus.com/](https://erdplus.com)). By clicking the top-left `Menu`, one can import this file and make adjustments to the diagram before exporting and committing anew to this project. 

As of 3 May 2025 (end of the Spring 2025 semester), this is the state of the database

![The entity relation diagram of the ShaTu database](documentation/erd.png)

DpTu - Dynamic Programming Tutor

DpTu (Dynamic Programming Tutor) is an Intelligent Tutoring System (ITS) designed to help students learn and practice Dynamic Programming (DP) concepts and algorithms. It provides a step-by-step visual environment for specific DP problems, tracks student progress, and aims to adapt to individual learning needs.

This project was developed as part of the CS493_X01 Senior Capstone course.

FEATURES

- User Authentication: Secure account creation and sign-in for students.
- Dynamic Programming Tutoring: Currently focuses on the Longest Common Subsequence (LCS) problem.
- Algorithm Visualization:
  - Step-by-step execution of the DP algorithm.
  - Visual representation of the DP table (SubproblemTableView).
  - Display of algorithm pseudo-code with current line highlighting (CodeView).
  - Visualization of variable values during execution (VariablesView).
  - Visualization of subsequence comparison (SubSequenceView).
- Student Modeling: Tracks student progress on learning outcomes (Knowledge Components).
- Learning Modes: Basic structure for "Teach Me," "Practice," and "Quiz Me" modes (DashboardPanel).
- Client-Server Architecture: Java Swing GUI client communicates with a Java socket server over JSON.
- Database Persistence: Uses MySQL to store user accounts, course structure, tutoring sessions, and student models.

ARCHITECTURE OVERVIEW

- Client (GUI): Built using Java Swing (view package). Interacts with the server via svc.SvcFacade.
- Server: A simple Java socket server (svc.DpTuServer) listens for client connections. Each connection is handled by DpTuConnection.
- Tutor Logic: The core tutoring intelligence resides in svc.DpTuTutor, which processes client requests.
- Communication: Client and server exchange JSON messages (svc.ClientRequest, svc.TutorReply) using the Gson library.
- Service Layer: Abstracts business logic and data access (svc package, e.g., AccountSvc, CourseSvc). svc.ServiceFactory provides access to services.
- Data Access Objects (DAOs): Handle database interactions using JDBC (dao package, e.g., AccountDAO, CourseDAO). Extends dao.MySqlDAO for common DB connection logic.
- Model: Represents the application's data structures (model package, e.g., Account, Course, Problem, StudentModel).
- Database: MySQL database stores persistent data.

PREREQUISITES

- Java Development Kit (JDK): Version 11 or later recommended.
- MySQL Server: Version 5.7 or 8.x recommended.
- IDE: An IDE like NetBeans (which appears to have been used), Eclipse, or IntelliJ IDEA.
- MySQL JDBC Driver: The project uses com.mysql.cj.jdbc.Driver. Ensure this is included in the project's dependencies.

SETUP INSTRUCTIONS

1. Clone the Repository:
   git clone <your-repository-url>
   cd <repository-directory>

2. Database Setup:
   - Ensure your MySQL server is running.
   - Create a database (e.g., dptu_db).
   - Create a MySQL user (e.g., dptu_user) with privileges on the database.
   - Create Database Tables. Suggested tables:
     - Account (UserId, Password, FirstName, LastName, Question, Answer, IsStudent)
     - Course (CourseId, Title, PrimaryPedagogy, Description)
     - Unit (UnitId, CourseId, Title, Description, SequenceIndex, Pedagogy)
     - Task (TaskId, CourseId, UnitId, Title, Description, Kind, SequenceIndex, KindId)
     - Step (Id, CourseId, TaskId, Title, Description, SequenceIndex, StepSubType, SubTypeId, TimeoutId)
     - Hint (Id, StepId, Text, SequenceIndex)
     - LCSProblem (Id, Title, Description, Sequence1, Sequence2)
     - TutoringSession (SessionId, SecurityToken, UserId, CourseId, UnitId, IsActive, StartDate, ProblemKind, ProblemId)
     - StudentModel (UserId, ScaffoldLevel)
     - Assessment (Id, UserId, KnowledgeComponentId, AssessmentLevel, Exposures, Successes, Hints)
     - KnowledgeComponent (Id, CourseId, Title, Description, BloomLevel, IsDomainFocus, Pedagogy, ExercisingLocations, Granularity)
     - ExercisingLocation (Id, CourseId, UnitId, TaskId, StepId)
     - Timeout (Id, TimeoutType, Seconds, Event, Msg)
     - InfoMsgStep (SubStepId, Text)
   - Populate Initial Data: At minimum, insert an entry in Course table with CourseId = 1. Populate related tables as needed.

3. Configure Application:
   - Edit DpTu.properties file with:
     edu.regis.dptu.DB_HOST=your_mysql_host
     edu.regis.dptu.DB_NAME=your_database_name
     edu.regis.dptu.DB_USER=your_mysql_user
     edu.regis.dptu.DB_PASS=your_mysql_password

4. Build the Project:
   - Open in your IDE.
   - Use "Build" or "Clean and Build" to compile the code.

RUNNING THE APPLICATION

1. Start the Application:
   - Run edu.regis.dptu.DpTuApp
   - This starts the server and GUI, showing the SplashFrame.

2. Using the GUI:
   - Splash Screen: Choose "Sign In" or "New User".
   - New User: Fill in form on NewAccountPanel and create account.
   - Sign In: Enter User ID and password to access DashboardPanel.
   - Tutoring Session: Select a mode to begin interacting with DP visualizations.

USAGE EXAMPLES

- Creating a New Account:
  Run DpTuApp -> Click "New User" -> Fill and submit form.
- Signing In:
  Run DpTuApp -> Enter credentials -> Click "Sign In".
- Running LCS:
  After sign-in, select a mode and use algorithm controls to step through LCS problem.

KNOWN ISSUES / FUTURE WORK

- Incomplete Problem Types: Matrix Chain and 0/1 Knapsack views may be placeholders.
- Limited Tutoring Logic: Adaptation strategies in DpTuTutor may be basic.
- Error Handling: Needs more robust reporting for database/network issues.
- Save Session: Menu option unimplemented.
- Step Feedback: Answer checking and feedback mechanisms may be incomplete.
- Database Schema: A SQL script should be created for future setup.
- Hardcoded Values: Consider moving defaults to configuration files.

LICENSE

(C) 2019-2025 Johanna and Richard Blumenthal. All Rights Reserved.
Unauthorized use, duplication or distribution without the authors' permission is strictly prohibited.
This software is distributed on an "AS IS" basis without warranties or conditions of any kind, either expressed or implied.


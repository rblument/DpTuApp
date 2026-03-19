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
package edu.regis.dptu.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import edu.regis.dptu.dao.StudentModelDAO;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.Course;
import edu.regis.dptu.model.KnowledgeComponent;
import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.ScaffoldLevel;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.StudentModelFieldKind;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.model.aol.Assessment;
import edu.regis.dptu.model.aol.AssessmentLevel;
import edu.regis.dptu.model.aol.StudentModel;
import edu.regis.dptu.svc.CourseSvc;
import edu.regis.dptu.svc.ServiceFactory;
import edu.regis.dptu.svc.SessionSvc;

@SuppressWarnings("Logging")
public class StudentModelDAOTest {
    private StudentModelDAO dao;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private MockedStatic<DriverManager> mockedDriverManager;

    @BeforeEach
    public void setUp() throws SQLException {
        dao = new StudentModelDAO();
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        mockedDriverManager = mockStatic(DriverManager.class);
        mockedDriverManager
                .when(() -> DriverManager.getConnection(anyString()))
                .thenReturn(mockConnection);
    }

    @AfterEach
    public void tearDown() {
        if (mockedDriverManager != null) {
            mockedDriverManager.close();
        }
    }

    @Test
    public void testCreateWithNullStudentThrows() {
        assertThrows(NonRecoverableException.class, () -> dao.create(null));
    }

    @Test
    public void testCreateSuccessAssignsGeneratedAssessmentIds() throws Exception {
        Student student = buildStudentWithAssessment("student@regis.edu");

        PreparedStatement stmt1 = mock(PreparedStatement.class);
        PreparedStatement stmt2 = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("INSERT INTO StudentModel")))
                .thenReturn(stmt1);
        when(mockConnection.prepareStatement(startsWith("INSERT INTO Assessment"), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(stmt2);
        when(stmt2.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(700);

        assertDoesNotThrow(() -> dao.create(student));

        Assessment assessment = student.getStudentModel().getAssessments().values().iterator().next();
        assertEquals(700, assessment.getId());
        verify(mockConnection).setAutoCommit(false);
        verify(mockConnection).commit();
    }

    @Test
    public void testCreateSQLExceptionRollsBack() throws Exception {
        Student student = buildStudentWithAssessment("student@regis.edu");

        SQLException sqlEx = mock(SQLException.class);
        when(mockConnection.prepareStatement(startsWith("INSERT INTO StudentModel")))
            .thenThrow(sqlEx);

        assertThrows(NonRecoverableException.class, () -> dao.create(student));
        verify(mockConnection).rollback();
    }

    @Test
    public void testRetrieveSuccessIncludesAssessmentsAndSession() throws Exception {
        PreparedStatement scaffoldStmt = mock(PreparedStatement.class);
        PreparedStatement assessmentStmt = mock(PreparedStatement.class);
        ResultSet scaffoldRs = mock(ResultSet.class);
        ResultSet assessmentRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("SELECT ScaffoldLevel")))
                .thenReturn(scaffoldStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT Id,KnowledgeComponentId")))
                .thenReturn(assessmentStmt);

        when(scaffoldStmt.executeQuery()).thenReturn(scaffoldRs);
        when(scaffoldRs.next()).thenReturn(true);
        when(scaffoldRs.getString(1)).thenReturn("Extreme");

        when(assessmentStmt.executeQuery()).thenReturn(assessmentRs);
        when(assessmentRs.next()).thenReturn(true).thenReturn(false);
        when(assessmentRs.getInt(1)).thenReturn(55);
        when(assessmentRs.getInt(2)).thenReturn(22);
        when(assessmentRs.getString(3)).thenReturn("Very High");
        when(assessmentRs.getInt(4)).thenReturn(2);
        when(assessmentRs.getInt(5)).thenReturn(1);
        when(assessmentRs.getInt(6)).thenReturn(0);

        CourseSvc courseSvc = mock(CourseSvc.class);
        Course course = new Course(1);
        KnowledgeComponent outcome = new KnowledgeComponent(22);
        course.addOutcome(outcome);
        when(courseSvc.retrieve(1)).thenReturn(course);

        SessionSvc sessionSvc = mock(SessionSvc.class);
        TutoringSession session = new TutoringSession("student@regis.edu");
        session.setId(999);
        when(sessionSvc.retrieve(any(Student.class))).thenReturn(session);

        try (MockedStatic<ServiceFactory> mockedServiceFactory = mockStatic(ServiceFactory.class)) {
            mockedServiceFactory.when(ServiceFactory::findCourseSvc).thenReturn(courseSvc);
            mockedServiceFactory.when(ServiceFactory::findSessionSvc).thenReturn(sessionSvc);

            StudentModel model = dao.retrieve("student@regis.edu");

            assertEquals(ScaffoldLevel.EXTREME, model.getScaffoldLevel());
            assertEquals(1, model.getAssessments().size());
            assertEquals(1, model.getSessions().size());
            Assessment loaded = model.getAssessments().get(22);
            assertNotNull(loaded);
            assertEquals(AssessmentLevel.VERY_HIGH, loaded.getAssessment());
            assertEquals(2, loaded.getExposures());
            assertEquals(1, loaded.getSuccessess());
        }
    }

    @Test
    public void testRetrieveWithoutSessionStillSucceeds() throws Exception {
        PreparedStatement scaffoldStmt = mock(PreparedStatement.class);
        PreparedStatement assessmentStmt = mock(PreparedStatement.class);
        ResultSet scaffoldRs = mock(ResultSet.class);
        ResultSet assessmentRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("SELECT ScaffoldLevel")))
                .thenReturn(scaffoldStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT Id,KnowledgeComponentId")))
                .thenReturn(assessmentStmt);

        when(scaffoldStmt.executeQuery()).thenReturn(scaffoldRs);
        when(scaffoldRs.next()).thenReturn(true);
        when(scaffoldRs.getString(1)).thenReturn("Extreme");

        when(assessmentStmt.executeQuery()).thenReturn(assessmentRs);
        when(assessmentRs.next()).thenReturn(false);

        CourseSvc courseSvc = mock(CourseSvc.class);
        when(courseSvc.retrieve(1)).thenReturn(new Course(1));

        SessionSvc sessionSvc = mock(SessionSvc.class);
        when(sessionSvc.retrieve(any(Student.class))).thenThrow(new ObjNotFoundException("none"));

        try (MockedStatic<ServiceFactory> mockedServiceFactory = mockStatic(ServiceFactory.class)) {
            mockedServiceFactory.when(ServiceFactory::findCourseSvc).thenReturn(courseSvc);
            mockedServiceFactory.when(ServiceFactory::findSessionSvc).thenReturn(sessionSvc);

            StudentModel model = dao.retrieve("student@regis.edu");
            assertNotNull(model);
            assertEquals(0, model.getSessions().size());
        }
    }

    @Test
    public void testRetrieveNotFoundThrows() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertThrows(ObjNotFoundException.class, () -> dao.retrieve("student@regis.edu"));
    }

    @Test
    public void testRetrieveSQLExceptionThrows() throws Exception {
        SQLException sqlEx = mock(SQLException.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        assertThrows(NonRecoverableException.class, () -> dao.retrieve("student@regis.edu"));
    }

    @Test
    public void testUpdateAssessmentForAllSupportedFields() throws Exception {
        Assessment assessment = new Assessment(new KnowledgeComponent(10), AssessmentLevel.LOW);
        assessment.setId(77);
        assessment.setExposures(3);
        assessment.setSuccessess(2);
        assessment.setHints(1);

        StudentModel model = new StudentModel("student@regis.edu");

        PreparedStatement assessmentLevelStmt = mock(PreparedStatement.class);
        PreparedStatement attemptsStmt = mock(PreparedStatement.class);
        PreparedStatement successesStmt = mock(PreparedStatement.class);
        PreparedStatement hintsStmt = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(startsWith("UPDATE Assessment SET AssessmentLevel")))
                .thenReturn(assessmentLevelStmt);
        when(mockConnection.prepareStatement(startsWith("UPDATE Assessment SET Exposures")))
                .thenReturn(attemptsStmt);
        when(mockConnection.prepareStatement(startsWith("UPDATE Assessment SET Successes")))
                .thenReturn(successesStmt);
        when(mockConnection.prepareStatement(startsWith("UPDATE Assessment SET Hints")))
                .thenReturn(hintsStmt);

        assertDoesNotThrow(() -> dao.updateAssessment(model, assessment, StudentModelFieldKind.ASSESSMENT_LEVEL));
        assertDoesNotThrow(() -> dao.updateAssessment(model, assessment, StudentModelFieldKind.ATTEMPTS));
        assertDoesNotThrow(() -> dao.updateAssessment(model, assessment, StudentModelFieldKind.SUCCESSES));
        assertDoesNotThrow(() -> dao.updateAssessment(model, assessment, StudentModelFieldKind.HINTS));

        verify(assessmentLevelStmt).execute();
        verify(attemptsStmt).execute();
        verify(successesStmt).execute();
        verify(hintsStmt).execute();
    }

    @Test
    public void testUpdateAssessmentInvalidFieldThrows() {
        Assessment assessment = new Assessment(new KnowledgeComponent(10), AssessmentLevel.LOW);
        assessment.setId(77);

        StudentModel model = new StudentModel("student@regis.edu");

        assertThrows(
                NonRecoverableException.class,
            () -> dao.updateAssessment(model, assessment, StudentModelFieldKind.ALL));
    }

    @Test
    public void testExistsTrueAndFalse() throws Exception {
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("SELECT UserId FROM StudentModel"))).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);

        assertTrue(dao.exists("student@regis.edu"));
        assertFalse(dao.exists("student@regis.edu"));
    }

    @Test
    public void testExistsSQLExceptionThrows() throws Exception {
        SQLException sqlEx = mock(SQLException.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        assertThrows(NonRecoverableException.class, () -> dao.exists("student@regis.edu"));
    }

    @Test
    public void testRetrieveIncompleteLessonsByMode() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("Title")).thenReturn("Lesson 1", "Lesson 2", "Lesson 3");
        when(mockResultSet.getString("AssessmentLevel"))
                .thenReturn("Not Started", "In Progress", "Completed");

        List<String> teachOne = dao.retrieveIncompleteLessons("student@regis.edu", Mode.TEACH_ONE);

        assertEquals(3, teachOne.size());
        assertTrue(teachOne.stream().anyMatch(s -> s.contains("Lesson 1")));
        assertTrue(teachOne.stream().anyMatch(s -> s.contains("Lesson 2")));
        assertTrue(teachOne.stream().anyMatch(s -> s.contains("Lesson 3")));
    }

    @Test
    public void testRetrieveIncompleteLessonsReturnsCompletionMessage() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        List<String> lessons = dao.retrieveIncompleteLessons("student@regis.edu", Mode.SEE_ONE);

        assertEquals(1, lessons.size());
        assertEquals("All lessons completed!", lessons.get(0));
    }

    @Test
    public void testRetrieveIncompleteLessonsSQLExceptionThrows() throws Exception {
        SQLException sqlEx = mock(SQLException.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        assertThrows(
                NonRecoverableException.class,
                () -> dao.retrieveIncompleteLessons("student@regis.edu", Mode.SEE_ONE));
    }

    @Test
    public void testUpdateAndDeleteUnsupported() {
        StudentModel model = new StudentModel("student@regis.edu");

        assertThrows(UnsupportedOperationException.class, () -> dao.update(model));
        assertThrows(UnsupportedOperationException.class, () -> dao.delete("student@regis.edu"));
    }

    private Student buildStudentWithAssessment(String userId) {
        Account account = new Account();
        account.setUserId(userId);

        Student student = new Student(account);
        Assessment assessment = new Assessment(new KnowledgeComponent(22), AssessmentLevel.NOT_STARTED);
        student.getStudentModel().addAssessment(assessment);

        return student;
    }
}

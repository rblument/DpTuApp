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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import edu.regis.dptu.dao.SessionDAO;
import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.CourseDigest;
import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.PendingStep;
import edu.regis.dptu.model.PendingTask;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.model.UnitDigest;
import edu.regis.dptu.svc.CourseSvc;
import edu.regis.dptu.svc.ProblemSvc;
import edu.regis.dptu.svc.ServiceFactory;

@SuppressWarnings("Logging")
public class SessionDAOTest {
    private SessionDAO dao;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private MockedStatic<DriverManager> mockedDriverManager;

    @BeforeEach
    public void setUp() throws SQLException {
        dao = new SessionDAO();
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
    public void testCreateSuccess() throws Exception {
        TutoringSession session = buildSession(100, "student@regis.edu");
        session.setMode(Mode.DO_ONE);

        PendingTask pendingTask = new PendingTask(new Task(13));
        PendingStep pendingStep = new PendingStep(new Step(21, 0, StepSubType.INFO_MESSAGE));
        pendingStep.setCurrentHintIndex(2);
        pendingStep.setNotifyTutor(true);
        pendingStep.setIsCompleted(true);
        pendingTask.setCurrentStep(pendingStep);
        session.addTask(pendingTask);

        PreparedStatement existsStmt = mock(PreparedStatement.class);
        ResultSet existsRs = mock(ResultSet.class);
        PreparedStatement insertStmt = mock(PreparedStatement.class);
        PreparedStatement clearPendingTaskStmt = mock(PreparedStatement.class);
        PreparedStatement clearPendingStepStmt = mock(PreparedStatement.class);
        PreparedStatement insertPendingStepStmt = mock(PreparedStatement.class);
        PreparedStatement insertPendingTaskStmt = mock(PreparedStatement.class);
        ResultSet sessionKeyRs = mock(ResultSet.class);
        ResultSet keyRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(contains("SELECT SessionId"))).thenReturn(existsStmt);
        when(mockConnection.prepareStatement(startsWith("INSERT INTO TutoringSession"), anyInt()))
                .thenReturn(insertStmt);
        when(mockConnection.prepareStatement(startsWith("DELETE FROM PendingTask")))
                .thenReturn(clearPendingTaskStmt);
        when(mockConnection.prepareStatement(startsWith("DELETE FROM PendingStep")))
                .thenReturn(clearPendingStepStmt);
        when(mockConnection.prepareStatement(startsWith("INSERT INTO PendingStep"), anyInt()))
                .thenReturn(insertPendingStepStmt);
        when(mockConnection.prepareStatement(startsWith("INSERT INTO PendingTask")))
                .thenReturn(insertPendingTaskStmt);
        when(existsStmt.executeQuery()).thenReturn(existsRs);
        when(existsRs.next()).thenReturn(false);
        when(insertStmt.executeUpdate()).thenReturn(1);
        when(insertStmt.getGeneratedKeys()).thenReturn(sessionKeyRs);
        when(sessionKeyRs.next()).thenReturn(true);
        when(sessionKeyRs.getInt(1)).thenReturn(501);
        when(insertPendingStepStmt.executeUpdate()).thenReturn(1);
        when(insertPendingTaskStmt.executeUpdate()).thenReturn(1);
        when(insertPendingStepStmt.getGeneratedKeys()).thenReturn(keyRs);
        when(keyRs.next()).thenReturn(true);
        when(keyRs.getInt(1)).thenReturn(44);

        assertDoesNotThrow(() -> dao.create(session));

        verify(insertStmt).setString(1, session.getSecurityToken());
        verify(insertStmt).setString(2, session.getUserId());
        verify(insertStmt).setInt(3, session.getCourse().getId());
        verify(insertStmt).setInt(4, session.getUnit().getId());
        verify(insertStmt).setBoolean(5, session.isIsActive());
        verify(insertStmt).setString(6, session.getProblem().getType().toString());
        verify(insertStmt).setInt(7, session.getProblem().getId());
        verify(insertStmt).setString(8, Mode.DO_ONE.name());
        assertEquals(501, session.getId());
        verify(insertPendingStepStmt).setInt(1, 501);
        verify(insertPendingStepStmt).setInt(2, 21);
        verify(insertPendingTaskStmt).setInt(1, 501);
        verify(insertPendingTaskStmt).setInt(2, 13);
        verify(insertPendingTaskStmt).setInt(3, 44);
    }

    @Test
    public void testCreateDuplicateThrowsIllegalArg() throws Exception {
        TutoringSession session = buildSession(100, "student@regis.edu");

        PreparedStatement existsStmt = mock(PreparedStatement.class);
        ResultSet existsRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(contains("SELECT SessionId"))).thenReturn(existsStmt);
        when(existsStmt.executeQuery()).thenReturn(existsRs);
        when(existsRs.next()).thenReturn(true);

        assertThrows(IllegalArgException.class, () -> dao.create(session));
    }

    @Test
    public void testCreateSQLException() throws Exception {
        TutoringSession session = buildSession(100, "student@regis.edu");
        SQLException sqlEx = mock(SQLException.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        assertThrows(NonRecoverableException.class, () -> dao.create(session));
    }

    @Test
    public void testRetrieveSuccess() throws Exception {
        when(mockConnection.prepareStatement(startsWith("SELECT SessionId")))
                .thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(100);
        when(mockResultSet.getString(2)).thenReturn("token-100");
        when(mockResultSet.getDate(3)).thenReturn(Date.valueOf("2026-03-18"));
        when(mockResultSet.getBoolean(4)).thenReturn(true);
        when(mockResultSet.getInt(5)).thenReturn(1);
        when(mockResultSet.getInt(6)).thenReturn(1);
        when(mockResultSet.getInt(8)).thenReturn(55);
        when(mockResultSet.getString(9)).thenReturn("TEACH_ONE");

        PreparedStatement pendingStmt = mock(PreparedStatement.class);
        ResultSet pendingRs = mock(ResultSet.class);
        when(mockConnection.prepareStatement(startsWith("SELECT pt.TaskId")))
                .thenReturn(pendingStmt);
        when(pendingStmt.executeQuery()).thenReturn(pendingRs);
        when(pendingRs.next()).thenReturn(false);

        Problem problem = new LCSProblem(55, "ABC", "ABD");
        CourseSvc mockCourseSvc = mock(CourseSvc.class);
        ProblemSvc mockProblemSvc = mock(ProblemSvc.class);
        when(mockProblemSvc.retrieve(55)).thenReturn(problem);

        try (MockedStatic<ServiceFactory> mockedServiceFactory = mockStatic(ServiceFactory.class)) {
            mockedServiceFactory.when(ServiceFactory::findProblemSvc).thenReturn(mockProblemSvc);
            mockedServiceFactory.when(ServiceFactory::findCourseSvc).thenReturn(mockCourseSvc);

            Account account = new Account();
            account.setUserId("student@regis.edu");
            Student student = new Student(account);

            TutoringSession result = dao.retrieve(student);

            assertEquals(100, result.getId());
            assertEquals("token-100", result.getSecurityToken());
            assertTrue(result.isIsActive());
            assertNotNull(result.getProblem());
            assertEquals(55, result.getProblem().getId());
            assertEquals(Mode.TEACH_ONE, result.getMode());
        }
    }

    @Test
    public void testRetrieveLoadsPendingProgress() throws Exception {
        PreparedStatement sessionStmt = mock(PreparedStatement.class);
        ResultSet sessionRs = mock(ResultSet.class);
        PreparedStatement pendingStmt = mock(PreparedStatement.class);
        ResultSet pendingRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("SELECT SessionId")))
                .thenReturn(sessionStmt);
        when(sessionStmt.executeQuery()).thenReturn(sessionRs);
        when(sessionRs.next()).thenReturn(true);
        when(sessionRs.getInt(1)).thenReturn(222);
        when(sessionRs.getString(2)).thenReturn("token-222");
        when(sessionRs.getDate(3)).thenReturn(Date.valueOf("2026-03-20"));
        when(sessionRs.getBoolean(4)).thenReturn(true);
        when(sessionRs.getInt(5)).thenReturn(1);
        when(sessionRs.getInt(6)).thenReturn(1);
        when(sessionRs.getInt(8)).thenReturn(55);
        when(sessionRs.getString(9)).thenReturn("SEE_ONE");

        when(mockConnection.prepareStatement(startsWith("SELECT pt.TaskId")))
                .thenReturn(pendingStmt);
        when(pendingStmt.executeQuery()).thenReturn(pendingRs);
        when(pendingRs.next()).thenReturn(true).thenReturn(false);
        when(pendingRs.getInt(1)).thenReturn(13);
        when(pendingRs.getInt(2)).thenReturn(400);
        when(pendingRs.getInt(3)).thenReturn(21);
        when(pendingRs.getBoolean(4)).thenReturn(true);
        when(pendingRs.getBoolean(5)).thenReturn(false);
        when(pendingRs.getInt(6)).thenReturn(3);

        Problem problem = new LCSProblem(55, "ABC", "ABD");
        Task restoredTask = new Task(13);
        restoredTask.addStep(new Step(21, 0, StepSubType.INFO_MESSAGE));

        CourseSvc mockCourseSvc = mock(CourseSvc.class);
        ProblemSvc mockProblemSvc = mock(ProblemSvc.class);
        when(mockProblemSvc.retrieve(55)).thenReturn(problem);
        when(mockCourseSvc.retrieveTask(eq(1), eq(13), any(Connection.class)))
                .thenReturn(restoredTask);

        try (MockedStatic<ServiceFactory> mockedServiceFactory = mockStatic(ServiceFactory.class)) {
            mockedServiceFactory.when(ServiceFactory::findProblemSvc).thenReturn(mockProblemSvc);
            mockedServiceFactory.when(ServiceFactory::findCourseSvc).thenReturn(mockCourseSvc);

            Account account = new Account();
            account.setUserId("student@regis.edu");
            Student student = new Student(account);

            TutoringSession session = dao.retrieve(student);

            assertEquals(Mode.SEE_ONE, session.getMode());
            assertEquals(1, session.getTasks().size());
            PendingTask pendingTask = session.getCurrentTask();
            assertEquals(13, pendingTask.getTask().getId());
            assertNotNull(pendingTask.getCurrentStep());
            assertEquals(21, pendingTask.getCurrentStep().getStep().getId());
            assertEquals(3, pendingTask.getCurrentStep().getCurrentHintIndex());
            assertTrue(pendingTask.getCurrentStep().isNotifyTutor());
            assertFalse(pendingTask.getCurrentStep().isCompleted());
        }
    }

    @Test
    public void testRetrieveNotFoundThrows() throws Exception {
        when(mockConnection.prepareStatement(startsWith("SELECT SessionId")))
                .thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Account account = new Account();
        account.setUserId("student@regis.edu");

        assertThrows(ObjNotFoundException.class, () -> dao.retrieve(new Student(account)));
    }

    @Test
    public void testRetrieveSecurityTokenSuccess() throws Exception {
        when(mockConnection.prepareStatement(startsWith("SELECT SecurityToken")))
                .thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(1)).thenReturn("token-123");

        assertEquals("token-123", dao.retrieveSecurityToken("student@regis.edu"));
    }

    @Test
    public void testRetrieveSecurityTokenNotFound() throws Exception {
        when(mockConnection.prepareStatement(startsWith("SELECT SecurityToken")))
                .thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertThrows(
                ObjNotFoundException.class, () -> dao.retrieveSecurityToken("student@regis.edu"));
    }

    @Test
    public void testUpdateSuccess() throws Exception {
        TutoringSession session = buildSession(88, "student@regis.edu");
        session.setMode(Mode.SEE_ONE);
        PendingTask pendingTask = new PendingTask(new Task(7));
        pendingTask.setCurrentStep(new PendingStep(new Step(11, 0, StepSubType.INFO_MESSAGE)));
        session.addTask(pendingTask);

        PreparedStatement clearPendingTaskStmt = mock(PreparedStatement.class);
        PreparedStatement clearPendingStepStmt = mock(PreparedStatement.class);
        PreparedStatement insertPendingStepStmt = mock(PreparedStatement.class);
        PreparedStatement insertPendingTaskStmt = mock(PreparedStatement.class);
        ResultSet keyRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("UPDATE TutoringSession")))
                .thenReturn(mockStatement);
        when(mockConnection.prepareStatement(startsWith("DELETE FROM PendingTask")))
                .thenReturn(clearPendingTaskStmt);
        when(mockConnection.prepareStatement(startsWith("DELETE FROM PendingStep")))
                .thenReturn(clearPendingStepStmt);
        when(mockConnection.prepareStatement(startsWith("INSERT INTO PendingStep"), anyInt()))
                .thenReturn(insertPendingStepStmt);
        when(mockConnection.prepareStatement(startsWith("INSERT INTO PendingTask")))
                .thenReturn(insertPendingTaskStmt);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(insertPendingStepStmt.executeUpdate()).thenReturn(1);
        when(insertPendingTaskStmt.executeUpdate()).thenReturn(1);
        when(insertPendingStepStmt.getGeneratedKeys()).thenReturn(keyRs);
        when(keyRs.next()).thenReturn(true);
        when(keyRs.getInt(1)).thenReturn(15);

        assertDoesNotThrow(() -> dao.update(session));

        verify(mockConnection).setAutoCommit(false);
        verify(mockConnection).commit();
        verify(mockStatement).setString(5, Mode.SEE_ONE.name());
        verify(mockStatement).setInt(6, session.getId());
    }

    @Test
    public void testUpdateBadRowCountRollsBack() throws Exception {
        TutoringSession session = buildSession(88, "student@regis.edu");
        when(mockConnection.prepareStatement(startsWith("UPDATE TutoringSession")))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);

        assertThrows(NonRecoverableException.class, () -> dao.update(session));
        verify(mockConnection).rollback();
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        PreparedStatement lookupStmt = mock(PreparedStatement.class);
        PreparedStatement clearPendingTaskStmt = mock(PreparedStatement.class);
        PreparedStatement clearPendingStepStmt = mock(PreparedStatement.class);
        ResultSet lookupRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("SELECT SessionId FROM TutoringSession")))
                .thenReturn(lookupStmt);
        when(lookupStmt.executeQuery()).thenReturn(lookupRs);
        when(lookupRs.next()).thenReturn(true);
        when(lookupRs.getInt(1)).thenReturn(88);
        when(mockConnection.prepareStatement(startsWith("DELETE FROM PendingTask")))
                .thenReturn(clearPendingTaskStmt);
        when(mockConnection.prepareStatement(startsWith("DELETE FROM PendingStep")))
                .thenReturn(clearPendingStepStmt);
        when(mockConnection.prepareStatement(startsWith("DELETE FROM TutoringSession")))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> dao.delete("student@regis.edu"));

        verify(mockConnection).setAutoCommit(false);
        verify(mockConnection).commit();
    }

    @Test
    public void testDeleteBadRowCountRollsBack() throws Exception {
        PreparedStatement lookupStmt = mock(PreparedStatement.class);
        PreparedStatement clearPendingTaskStmt = mock(PreparedStatement.class);
        PreparedStatement clearPendingStepStmt = mock(PreparedStatement.class);
        ResultSet lookupRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("SELECT SessionId FROM TutoringSession")))
                .thenReturn(lookupStmt);
        when(lookupStmt.executeQuery()).thenReturn(lookupRs);
        when(lookupRs.next()).thenReturn(true);
        when(lookupRs.getInt(1)).thenReturn(88);
        when(mockConnection.prepareStatement(startsWith("DELETE FROM PendingTask")))
                .thenReturn(clearPendingTaskStmt);
        when(mockConnection.prepareStatement(startsWith("DELETE FROM PendingStep")))
                .thenReturn(clearPendingStepStmt);
        when(mockConnection.prepareStatement(startsWith("DELETE FROM TutoringSession")))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);

        assertThrows(NonRecoverableException.class, () -> dao.delete("student@regis.edu"));
        verify(mockConnection).rollback();
    }

    private TutoringSession buildSession(int id, String userId) {
        TutoringSession session = new TutoringSession(userId);
        session.setId(id);
        session.setSecurityToken("token-" + id);
        session.setIsActive(true);

        CourseDigest course = new CourseDigest(5, "Course");
        session.setCourse(course);

        UnitDigest unit = new UnitDigest(9);
        session.setUnit(unit);

        Problem problem = new LCSProblem(55, "ABC", "ABD");
        session.setProblem(problem);

        return session;
    }
}

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
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.model.UnitDigest;
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

        PreparedStatement existsStmt = mock(PreparedStatement.class);
        ResultSet existsRs = mock(ResultSet.class);
        PreparedStatement insertStmt = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(contains("SELECT SessionId"))).thenReturn(existsStmt);
        when(mockConnection.prepareStatement(
                        startsWith("INSERT INTO TutoringSession"), any(String[].class)))
                .thenReturn(insertStmt);
        when(existsStmt.executeQuery()).thenReturn(existsRs);
        when(existsRs.next()).thenReturn(false);
        when(insertStmt.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> dao.create(session));

        verify(insertStmt).setString(1, session.getSecurityToken());
        verify(insertStmt).setString(2, session.getUserId());
        verify(insertStmt).setInt(3, session.getCourse().getId());
        verify(insertStmt).setInt(4, session.getUnit().getId());
        verify(insertStmt).setBoolean(5, session.isIsActive());
        verify(insertStmt).setString(6, session.getProblem().getType().toString());
        verify(insertStmt).setInt(7, session.getProblem().getId());
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
        when(mockResultSet.getInt(6)).thenReturn(55);

        Problem problem = new LCSProblem(55, "ABC", "ABD");
        ProblemSvc mockProblemSvc = mock(ProblemSvc.class);
        when(mockProblemSvc.retrieve(55)).thenReturn(problem);

        try (MockedStatic<ServiceFactory> mockedServiceFactory = mockStatic(ServiceFactory.class)) {
            mockedServiceFactory.when(ServiceFactory::findProblemSvc).thenReturn(mockProblemSvc);

            Account account = new Account();
            account.setUserId("student@regis.edu");
            Student student = new Student(account);

            TutoringSession result = dao.retrieve(student);

            assertEquals(100, result.getId());
            assertEquals("token-100", result.getSecurityToken());
            assertTrue(result.isIsActive());
            assertNotNull(result.getProblem());
            assertEquals(55, result.getProblem().getId());
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
        when(mockConnection.prepareStatement(startsWith("UPDATE TutoringSession")))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> dao.update(session));

        verify(mockConnection).setAutoCommit(false);
        verify(mockConnection).commit();
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
        when(mockConnection.prepareStatement(startsWith("DELETE FROM TutoringSession")))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> dao.delete("student@regis.edu"));

        verify(mockConnection).setAutoCommit(false);
        verify(mockConnection).commit();
    }

    @Test
    public void testDeleteBadRowCountRollsBack() throws Exception {
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

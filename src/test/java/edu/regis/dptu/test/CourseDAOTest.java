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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import edu.regis.dptu.dao.CourseDAO;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Course;
import edu.regis.dptu.model.CourseDigest;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TaskKind;
import edu.regis.dptu.model.UnitDigest;

/**
 * Unit test class for CourseDAO using mocked database connections
 *
 * @author benm
 */
public class CourseDAOTest {
    private CourseDAO dao;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private MockedStatic<DriverManager> mockedDriverManager;

    private static final int TEST_COURSE_ID = 1;
    private static final int TEST_UNIT_ID = 1;
    private static final int TEST_TASK_ID = 1;
    private static final String TEST_TITLE = "Test Course";
    private static final String TEST_DESCRIPTION = "Test Description";

    @BeforeEach
    public void setUp() throws SQLException {
        dao = new CourseDAO();
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

    /** Test retrieving a course */
    @Test
    public void testRetrieve() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(true)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false);

        when(mockResultSet.getString(1)).thenReturn(TEST_TITLE);
        when(mockResultSet.getString(2)).thenReturn("FIXED_SEQUENCE");
        when(mockResultSet.getString(3)).thenReturn(TEST_DESCRIPTION);

        Course result = dao.retrieve(TEST_COURSE_ID);

        assertNotNull(result);
        assertEquals(TEST_COURSE_ID, result.getId());
        assertEquals(TEST_TITLE, result.getTitle());
        assertEquals(TEST_DESCRIPTION, result.getDescription());
        assertNotNull(result.getPrimaryPedagogy());

        verify(mockStatement, atLeastOnce()).setInt(1, TEST_COURSE_ID);
    }

    /** Test retrieving a non-existent course */
    @Test
    public void testRetrieveNonExistent() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertThrows(ObjNotFoundException.class, () -> dao.retrieve(TEST_COURSE_ID));
    }

    /** Test retrieving a course digest */
    @Test
    public void testRetrieveDigest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(1)).thenReturn(TEST_TITLE);
        when(mockResultSet.getString(2)).thenReturn(TEST_DESCRIPTION);

        CourseDigest result = dao.retrieveDigest(TEST_COURSE_ID, mockConnection);

        assertNotNull(result);
        assertEquals(TEST_COURSE_ID, result.getId());
        assertEquals(TEST_TITLE, result.getTitle());
        assertEquals(TEST_DESCRIPTION, result.getDescription());

        verify(mockStatement).setInt(1, TEST_COURSE_ID);
    }

    /** Test retrieving a non-existent course digest */
    @Test
    public void testRetrieveDigestNonExistent() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertThrows(
                ObjNotFoundException.class,
                () -> dao.retrieveDigest(TEST_COURSE_ID, mockConnection));
    }

    /** Test retrieving a unit digest */
    @Test
    public void testRetrieveUnitDigest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(1)).thenReturn(TEST_TITLE);
        when(mockResultSet.getString(2)).thenReturn(TEST_DESCRIPTION);

        UnitDigest result = dao.retrieveUnitDigest(TEST_COURSE_ID, TEST_UNIT_ID, mockConnection);

        assertNotNull(result);
        assertEquals(TEST_UNIT_ID, result.getId());
        assertEquals(TEST_TITLE, result.getTitle());
        assertEquals(TEST_DESCRIPTION, result.getDescription());

        verify(mockStatement).setInt(1, TEST_COURSE_ID);
        verify(mockStatement).setInt(2, TEST_UNIT_ID);
    }

    /** Test retrieving a non-existent unit digest */
    @Test
    public void testRetrieveUnitDigestNonExistent() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertThrows(
                ObjNotFoundException.class,
                () -> dao.retrieveUnitDigest(TEST_COURSE_ID, TEST_UNIT_ID, mockConnection));
    }

    /** Test retrieving a task */
    @Test
    public void testRetrieveTask() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        when(mockResultSet.getString(1)).thenReturn(TEST_TITLE);
        when(mockResultSet.getString(2)).thenReturn(TEST_DESCRIPTION);
        when(mockResultSet.getString(3)).thenReturn("PROBLEM");
        when(mockResultSet.getInt(4)).thenReturn(1);

        Task result = dao.retrieveTask(TEST_COURSE_ID, TEST_TASK_ID, mockConnection);

        assertNotNull(result);
        assertEquals(TEST_TASK_ID, result.getId());
        assertEquals(TEST_TITLE, result.getTitle());
        assertEquals(TEST_DESCRIPTION, result.getDescription());
        assertEquals(TaskKind.PROBLEM, result.getKind());
        assertNotNull(result.getSteps());

        verify(mockStatement, atLeastOnce()).setInt(1, TEST_COURSE_ID);
        verify(mockStatement, atLeastOnce()).setInt(2, TEST_TASK_ID);
    }

    /** Test retrieving a non-existent task */
    @Test
    public void testRetrieveTaskNonExistent() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertThrows(
                ObjNotFoundException.class,
                () -> dao.retrieveTask(TEST_COURSE_ID, TEST_TASK_ID, mockConnection));
    }

    /** Test that SQLException in retrieve is wrapped in NonRecoverableException */
    @Test
    public void testRetrieveWithSQLException() throws Exception {
        SQLException sqlEx = new SQLException("Test error");
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        assertThrows(NonRecoverableException.class, () -> dao.retrieve(TEST_COURSE_ID));
    }

    /** Test that SQLException in retrieveDigest is wrapped in NonRecoverableException */
    @Test
    public void testRetrieveDigestWithSQLException() throws Exception {
        SQLException sqlEx = new SQLException("Test error");
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        assertThrows(
                NonRecoverableException.class,
                () -> dao.retrieveDigest(TEST_COURSE_ID, mockConnection));
    }

    /** Test that SQLException in retrieveUnitDigest is wrapped in NonRecoverableException */
    @Test
    public void testRetrieveUnitDigestWithSQLException() throws Exception {
        SQLException sqlEx = new SQLException("Test error");
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        assertThrows(
                NonRecoverableException.class,
                () -> dao.retrieveUnitDigest(TEST_COURSE_ID, TEST_UNIT_ID, mockConnection));
    }

    /** Test that SQLException in retrieveTask is wrapped in NonRecoverableException */
    @Test
    public void testRetrieveTaskWithSQLException() throws Exception {
        SQLException sqlEx = new SQLException("Test error");
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        assertThrows(
                NonRecoverableException.class,
                () -> dao.retrieveTask(TEST_COURSE_ID, TEST_TASK_ID, mockConnection));
    }
}

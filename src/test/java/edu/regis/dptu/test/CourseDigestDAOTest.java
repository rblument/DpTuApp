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

import edu.regis.dptu.dao.CourseDigestDAO;
import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.CourseDigest;
import edu.regis.dptu.model.TaskSelectionKind;

@SuppressWarnings("Logging")
public class CourseDigestDAOTest {
    private CourseDigestDAO dao;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private MockedStatic<DriverManager> mockedDriverManager;

    @BeforeEach
    public void setUp() throws SQLException {
        dao = new CourseDigestDAO();
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
    public void testCreate() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        CourseDigest digest = new CourseDigest(1, "DP Course");
        digest.setPrimaryPedagogy(TaskSelectionKind.FIXED_SEQUENCE);
        digest.setDescription("A test course");

        assertDoesNotThrow(() -> dao.create(digest));

        verify(mockStatement, atLeastOnce()).setString(anyInt(), anyString());
        verify(mockStatement).execute();
    }

    @Test
    public void testCreateDuplicateThrows() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        CourseDigest digest = new CourseDigest(1, "DP Course");
        digest.setPrimaryPedagogy(TaskSelectionKind.FIXED_SEQUENCE);
        digest.setDescription("A test course");

        assertThrows(IllegalArgException.class, () -> dao.create(digest));
    }

    @Test
    public void testRetrieve() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(1)).thenReturn("Course A");
        when(mockResultSet.getString(2)).thenReturn("MASTERY_LEARNING");
        when(mockResultSet.getString(3)).thenReturn("Course description");

        CourseDigest digest = dao.retrieve(10);

        assertEquals(10, digest.getId());
        assertEquals("Course A", digest.getTitle());
        assertEquals(TaskSelectionKind.MASTERY_LEARNING, digest.getPrimaryPedagogy());
        assertEquals("Course description", digest.getDescription());
    }

    @Test
    public void testRetrieveNotFoundThrows() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertThrows(ObjNotFoundException.class, () -> dao.retrieve(10));
    }

    @Test
    public void testCreateWithSQLExceptionThrowsNonRecoverable() throws Exception {
        SQLException sqlEx = mock(SQLException.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        CourseDigest digest = new CourseDigest(1, "DP Course");
        digest.setPrimaryPedagogy(TaskSelectionKind.FIXED_SEQUENCE);
        digest.setDescription("A test course");

        assertThrows(NonRecoverableException.class, () -> dao.create(digest));
    }

    @Test
    public void testRetrieveWithSQLExceptionThrowsNonRecoverable() throws Exception {
        SQLException sqlEx = mock(SQLException.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        assertThrows(NonRecoverableException.class, () -> dao.retrieve(10));
    }
}

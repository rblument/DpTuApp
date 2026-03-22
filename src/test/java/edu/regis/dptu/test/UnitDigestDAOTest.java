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

import edu.regis.dptu.dao.UnitDigestDAO;
import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.TaskSelectionKind;
import edu.regis.dptu.model.UnitDigest;

/**
 * Unit test class for UnitDigestDAO using mocked database connections
 *
 * @author benm
 */
@SuppressWarnings("Logging")
public class UnitDigestDAOTest {
    private UnitDigestDAO dao;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private MockedStatic<DriverManager> mockedDriverManager;

    private static final int TEST_UNIT_ID = 1;
    private static final int TEST_COURSE_ID = 1;
    private static final String TEST_TITLE = "Test Unit";
    private static final String TEST_DESCRIPTION = "Test Description";
    private static final int TEST_SEQUENCE_INDEX = 1;

    @BeforeEach
    public void setUp() throws SQLException {
        dao = new UnitDigestDAO();
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

    /** Test creating a new unit digest */
    @Test
    public void testCreate() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        UnitDigest unit = new UnitDigest(TEST_UNIT_ID);
        unit.setCourseId(TEST_COURSE_ID);
        unit.setTitle(TEST_TITLE);
        unit.setDescription(TEST_DESCRIPTION);
        unit.setSequenceIndex(TEST_SEQUENCE_INDEX);
        unit.setPedagogy(TaskSelectionKind.FIXED_SEQUENCE);

        assertDoesNotThrow(() -> dao.create(unit));

        verify(mockStatement, atLeastOnce()).setInt(anyInt(), anyInt());
        verify(mockStatement, atLeastOnce()).setString(anyInt(), anyString());
        verify(mockStatement).execute();
    }

    /** Test creating a duplicate unit digest should throw exception */
    @Test
    public void testCreateDuplicate() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        UnitDigest unit = new UnitDigest(TEST_UNIT_ID);
        unit.setCourseId(TEST_COURSE_ID);
        unit.setTitle(TEST_TITLE);
        unit.setDescription(TEST_DESCRIPTION);
        unit.setSequenceIndex(TEST_SEQUENCE_INDEX);
        unit.setPedagogy(TaskSelectionKind.FIXED_SEQUENCE);

        assertThrows(IllegalArgException.class, () -> dao.create(unit));
    }

    /** Test retrieving a unit digest */
    @Test
    public void testRetrieve() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(TEST_COURSE_ID);
        when(mockResultSet.getString(2)).thenReturn(TEST_TITLE);
        when(mockResultSet.getString(3)).thenReturn(TEST_DESCRIPTION);
        when(mockResultSet.getInt(4)).thenReturn(TEST_SEQUENCE_INDEX);
        when(mockResultSet.getString(5)).thenReturn("FIXED_SEQUENCE");

        UnitDigest result = dao.retrieve(TEST_UNIT_ID);

        assertNotNull(result);
        assertEquals(TEST_UNIT_ID, result.getId());
        assertEquals(TEST_COURSE_ID, result.getCourseId());
        assertEquals(TEST_TITLE, result.getTitle());
        assertEquals(TEST_DESCRIPTION, result.getDescription());
        assertEquals(TEST_SEQUENCE_INDEX, result.getSequenceIndex());
        assertEquals(TaskSelectionKind.FIXED_SEQUENCE, result.getPedagogy());

        verify(mockStatement).setInt(1, TEST_UNIT_ID);
        verify(mockStatement).executeQuery();
    }

    /** Test retrieving a non-existent unit digest should throw exception */
    @Test
    public void testRetrieveNonExistent() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertThrows(ObjNotFoundException.class, () -> dao.retrieve(TEST_UNIT_ID));
    }

    /** Test retrieving with different pedagogy types */
    @Test
    public void testRetrieveWithMasteryLearning() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(TEST_COURSE_ID);
        when(mockResultSet.getString(2)).thenReturn(TEST_TITLE);
        when(mockResultSet.getString(3)).thenReturn(TEST_DESCRIPTION);
        when(mockResultSet.getInt(4)).thenReturn(TEST_SEQUENCE_INDEX);
        when(mockResultSet.getString(5)).thenReturn("MASTERY_LEARNING");

        UnitDigest result = dao.retrieve(TEST_UNIT_ID);

        assertEquals(TaskSelectionKind.MASTERY_LEARNING, result.getPedagogy());
    }

    /** Test that SQLException in create is wrapped in NonRecoverableException */
    @Test
    public void testCreateWithSQLException() throws Exception {
        SQLException sqlEx = new SQLException("Test error");
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        UnitDigest unit = new UnitDigest(TEST_UNIT_ID);
        unit.setCourseId(TEST_COURSE_ID);
        unit.setTitle(TEST_TITLE);
        unit.setDescription(TEST_DESCRIPTION);
        unit.setSequenceIndex(TEST_SEQUENCE_INDEX);
        unit.setPedagogy(TaskSelectionKind.FIXED_SEQUENCE);

        assertThrows(NonRecoverableException.class, () -> dao.create(unit));
    }

    /** Test that SQLException in retrieve is wrapped in NonRecoverableException */
    @Test
    public void testRetrieveWithSQLException() throws Exception {
        SQLException sqlEx = new SQLException("Test error");
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        assertThrows(NonRecoverableException.class, () -> dao.retrieve(TEST_UNIT_ID));
    }
}

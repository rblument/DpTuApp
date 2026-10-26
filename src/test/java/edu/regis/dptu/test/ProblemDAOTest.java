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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.regis.dptu.dao.ProblemDAO;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.KnapsackProblem;
import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;

@SuppressWarnings("Logging")
public class ProblemDAOTest {
    private ProblemDAO dao;
    private Connection mockConnection;
    private MockedStatic<DriverManager> mockedDriverManager;

    @BeforeEach
    public void setUp() throws SQLException {
        dao = new ProblemDAO();
        mockConnection = mock(Connection.class);

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
    public void retrieveReturnsLcsProblemWithMetadata() throws Exception {
        PreparedStatement mainStmt = mock(PreparedStatement.class);
        PreparedStatement subTypeStmt = mock(PreparedStatement.class);
        PreparedStatement taskStmt = mock(PreparedStatement.class);
        ResultSet mainRs = mock(ResultSet.class);
        ResultSet subTypeRs = mock(ResultSet.class);
        ResultSet taskRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("SELECT ProblemType")))
                .thenReturn(mainStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT Sequence1")))
                .thenReturn(subTypeStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT TaskId"))).thenReturn(taskStmt);

        when(mainStmt.executeQuery()).thenReturn(mainRs);
        when(subTypeStmt.executeQuery()).thenReturn(subTypeRs);
        when(taskStmt.executeQuery()).thenReturn(taskRs);
        when(taskRs.next()).thenReturn(false);

        when(mainRs.next()).thenReturn(true);
        when(mainRs.getString(1)).thenReturn(ProblemKind.LCS_PROBLEM.name());
        when(mainRs.getInt(2)).thenReturn(11);
        when(mainRs.getString(3)).thenReturn("Top title");
        when(mainRs.getString(4)).thenReturn("Top description");

        when(subTypeRs.next()).thenReturn(true);
        when(subTypeRs.getString(1)).thenReturn("ABC");
        when(subTypeRs.getString(2)).thenReturn("AFC");

        Problem result = dao.retrieve(5);

        assertTrue(result instanceof LCSProblem);
        assertEquals(5, result.getId());
        assertEquals(11, result.getSubTypeId());
        assertEquals("Top title", result.getTitle());
        assertEquals("Top description", result.getDescription());
        verify(mainStmt).setInt(1, 5);
        verify(subTypeStmt).setInt(1, 11);
    }

    @Test
    public void retrieveByKindReturnsTaskIdWhenPresent() throws Exception {
        PreparedStatement mainStmt = mock(PreparedStatement.class);
        PreparedStatement subTypeStmt = mock(PreparedStatement.class);
        PreparedStatement taskStmt = mock(PreparedStatement.class);
        ResultSet mainRs = mock(ResultSet.class);
        ResultSet subTypeRs = mock(ResultSet.class);
        ResultSet taskRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("SELECT Id, SubTypeId")))
                .thenReturn(mainStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT Sequence1")))
                .thenReturn(subTypeStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT TaskId"))).thenReturn(taskStmt);

        when(mainStmt.executeQuery()).thenReturn(mainRs);
        when(subTypeStmt.executeQuery()).thenReturn(subTypeRs);
        when(taskStmt.executeQuery()).thenReturn(taskRs);

        when(mainRs.next()).thenReturn(true);
        when(mainRs.getInt(1)).thenReturn(7);
        when(mainRs.getInt(2)).thenReturn(3);
        when(mainRs.getString(3)).thenReturn("Kind title");
        when(mainRs.getString(4)).thenReturn("Kind description");

        when(subTypeRs.next()).thenReturn(true);
        when(subTypeRs.getString(1)).thenReturn("XMJYAUZ");
        when(subTypeRs.getString(2)).thenReturn("MZJAWXU");

        when(taskRs.next()).thenReturn(true);
        when(taskRs.getInt(1)).thenReturn(42);

        Problem result = dao.retrieveByKind(ProblemKind.LCS_PROBLEM);

        assertEquals(7, result.getId());
        assertEquals(42, result.getTaskId());
        assertEquals("Kind title", result.getTitle());
    }

    @Test
    public void retrieveByKindReturnsMinusOneWhenTaskIsMissing() throws Exception {
        PreparedStatement mainStmt = mock(PreparedStatement.class);
        PreparedStatement subTypeStmt = mock(PreparedStatement.class);
        PreparedStatement taskStmt = mock(PreparedStatement.class);
        ResultSet mainRs = mock(ResultSet.class);
        ResultSet subTypeRs = mock(ResultSet.class);
        ResultSet taskRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("SELECT Id, SubTypeId")))
                .thenReturn(mainStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT Sequence1")))
                .thenReturn(subTypeStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT TaskId"))).thenReturn(taskStmt);

        when(mainStmt.executeQuery()).thenReturn(mainRs);
        when(subTypeStmt.executeQuery()).thenReturn(subTypeRs);
        when(taskStmt.executeQuery()).thenReturn(taskRs);

        when(mainRs.next()).thenReturn(true);
        when(mainRs.getInt(1)).thenReturn(9);
        when(mainRs.getInt(2)).thenReturn(2);
        when(mainRs.getString(3)).thenReturn("Title");
        when(mainRs.getString(4)).thenReturn("Description");

        when(subTypeRs.next()).thenReturn(true);
        when(subTypeRs.getString(1)).thenReturn("AB");
        when(subTypeRs.getString(2)).thenReturn("AC");

        when(taskRs.next()).thenReturn(false);

        Problem result = dao.retrieveByKind(ProblemKind.LCS_PROBLEM);

        assertEquals(-1, result.getTaskId());
    }

    @Test
    public void retrieveByKindReturnsKnapsackProblem() throws Exception {
        PreparedStatement mainStmt = mock(PreparedStatement.class);
        PreparedStatement capacityStmt = mock(PreparedStatement.class);
        PreparedStatement itemsStmt = mock(PreparedStatement.class);
        PreparedStatement taskStmt = mock(PreparedStatement.class);
        ResultSet mainRs = mock(ResultSet.class);
        ResultSet capacityRs = mock(ResultSet.class);
        ResultSet itemsRs = mock(ResultSet.class);
        ResultSet taskRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("SELECT Id, SubTypeId")))
                .thenReturn(mainStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT Capacity")))
                .thenReturn(capacityStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT ItemId"), anyInt(), anyInt()))
                .thenReturn(itemsStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT TaskId"))).thenReturn(taskStmt);

        when(mainStmt.executeQuery()).thenReturn(mainRs);
        when(capacityStmt.executeQuery()).thenReturn(capacityRs);
        when(itemsStmt.executeQuery()).thenReturn(itemsRs);
        when(taskStmt.executeQuery()).thenReturn(taskRs);

        when(mainRs.next()).thenReturn(true);
        when(mainRs.getInt(1)).thenReturn(2); // problem id
        when(mainRs.getInt(2)).thenReturn(0); // subTypeId
        when(mainRs.getString(3)).thenReturn("Knapsack title");
        when(mainRs.getString(4)).thenReturn("Knapsack description");

        when(capacityRs.next()).thenReturn(true);
        when(capacityRs.getInt(1)).thenReturn(10);

        when(itemsRs.last()).thenReturn(true);
        when(itemsRs.getRow()).thenReturn(2);
        when(itemsRs.next()).thenReturn(true, true, false);
        when(itemsRs.getString(2)).thenReturn("Guitar", "Stereo");
        when(itemsRs.getInt(3)).thenReturn(1, 4);
        when(itemsRs.getInt(4)).thenReturn(15, 30);

        when(taskRs.next()).thenReturn(true);
        when(taskRs.getInt(1)).thenReturn(50);

        Problem result = dao.retrieveByKind(ProblemKind.KNAPSACK_0_1);

        assertTrue(result instanceof KnapsackProblem);
        KnapsackProblem knap = (KnapsackProblem) result;
        assertEquals(2, result.getId());
        assertEquals(0, result.getSubTypeId());
        assertEquals("Knapsack title", result.getTitle());
        assertEquals(50, result.getTaskId());
        assertEquals(10, knap.getCapacity());
        assertEquals(2, knap.getN());
        assertArrayEquals(new int[] {1, 4}, knap.getWeights());
        assertArrayEquals(new int[] {15, 30}, knap.getValues());
    }

    @Test
    public void retrieveByKindThrowsWhenKnapsackHasNoItems() throws Exception {
        PreparedStatement mainStmt = mock(PreparedStatement.class);
        PreparedStatement capacityStmt = mock(PreparedStatement.class);
        PreparedStatement itemsStmt = mock(PreparedStatement.class);
        ResultSet mainRs = mock(ResultSet.class);
        ResultSet capacityRs = mock(ResultSet.class);
        ResultSet itemsRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("SELECT Id, SubTypeId")))
                .thenReturn(mainStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT Capacity")))
                .thenReturn(capacityStmt);
        when(mockConnection.prepareStatement(startsWith("SELECT ItemId"), anyInt(), anyInt()))
                .thenReturn(itemsStmt);

        when(mainStmt.executeQuery()).thenReturn(mainRs);
        when(capacityStmt.executeQuery()).thenReturn(capacityRs);
        when(itemsStmt.executeQuery()).thenReturn(itemsRs);

        when(mainRs.next()).thenReturn(true);
        when(mainRs.getInt(1)).thenReturn(2);
        when(mainRs.getInt(2)).thenReturn(0);
        when(mainRs.getString(3)).thenReturn("Knapsack title");
        when(mainRs.getString(4)).thenReturn("Knapsack description");

        when(capacityRs.next()).thenReturn(true);
        when(capacityRs.getInt(1)).thenReturn(10);

        when(itemsRs.last()).thenReturn(false);

        assertThrows(
                NonRecoverableException.class, () -> dao.retrieveByKind(ProblemKind.KNAPSACK_0_1));
    }

    @Test
    public void retrieveThrowsObjNotFoundWhenMissing() throws Exception {
        PreparedStatement mainStmt = mock(PreparedStatement.class);
        ResultSet mainRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("SELECT ProblemType")))
                .thenReturn(mainStmt);
        when(mainStmt.executeQuery()).thenReturn(mainRs);
        when(mainRs.next()).thenReturn(false);

        assertThrows(ObjNotFoundException.class, () -> dao.retrieve(1001));
    }

    @Test
    public void retrieveByKindWrapsSqlExceptions() throws Exception {
        SQLException sqlEx = mock(SQLException.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        NonRecoverableException ex =
                assertThrows(
                        NonRecoverableException.class,
                        () -> dao.retrieveByKind(ProblemKind.LCS_PROBLEM));

        assertTrue(ex.getMessage().contains("ProblemDAO-ERR-2"));
    }
}

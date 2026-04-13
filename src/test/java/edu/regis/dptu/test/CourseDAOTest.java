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
import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TaskKind;
import edu.regis.dptu.model.UnitDigest;
import edu.regis.dptu.svc.ProblemSvc;
import edu.regis.dptu.svc.ServiceFactory;

/**
 * Unit test class for CourseDAO using mocked database connections
 *
 * @author benm
 */
@SuppressWarnings("Logging")
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

    /** Test full retrieve path that traverses units, tasks, steps, hints, and outcomes. */
    @Test
    public void testRetrieveFullCourseStructure() throws Exception {
        PreparedStatement courseStmt = mock(PreparedStatement.class);
        PreparedStatement locationsStmt = mock(PreparedStatement.class);
        PreparedStatement unitsStmt = mock(PreparedStatement.class);
        PreparedStatement tasksStmt = mock(PreparedStatement.class);
        PreparedStatement stepsStmt = mock(PreparedStatement.class);
        PreparedStatement timeoutStmt = mock(PreparedStatement.class);
        PreparedStatement hintsStmt = mock(PreparedStatement.class);
        PreparedStatement outcomesStmt = mock(PreparedStatement.class);

        ResultSet courseRs = mock(ResultSet.class);
        ResultSet locationsRs = mock(ResultSet.class);
        ResultSet unitsRs = mock(ResultSet.class);
        ResultSet tasksRs = mock(ResultSet.class);
        ResultSet stepsRs = mock(ResultSet.class);
        ResultSet timeoutRs = mock(ResultSet.class);
        ResultSet hintsRs = mock(ResultSet.class);
        ResultSet outcomesRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(courseStmt)
                .thenReturn(locationsStmt)
                .thenReturn(unitsStmt)
                .thenReturn(tasksStmt)
                .thenReturn(stepsStmt)
                .thenReturn(timeoutStmt)
                .thenReturn(hintsStmt)
                .thenReturn(outcomesStmt);

        when(courseStmt.executeQuery()).thenReturn(courseRs);
        when(locationsStmt.executeQuery()).thenReturn(locationsRs);
        when(unitsStmt.executeQuery()).thenReturn(unitsRs);
        when(tasksStmt.executeQuery()).thenReturn(tasksRs);
        when(stepsStmt.executeQuery()).thenReturn(stepsRs);
        when(timeoutStmt.executeQuery()).thenReturn(timeoutRs);
        when(hintsStmt.executeQuery()).thenReturn(hintsRs);
        when(outcomesStmt.executeQuery()).thenReturn(outcomesRs);

        when(courseRs.next()).thenReturn(true);
        when(courseRs.getString(1)).thenReturn("Dynamic Programming");
        when(courseRs.getString(2)).thenReturn("FIXED_SEQUENCE");
        when(courseRs.getString(3)).thenReturn("Course description");

        when(locationsRs.next()).thenReturn(true).thenReturn(false);
        when(locationsRs.getInt(1)).thenReturn(0);
        when(locationsRs.getInt(2)).thenReturn(1);
        when(locationsRs.getInt(3)).thenReturn(5);
        when(locationsRs.getInt(4)).thenReturn(9);

        when(unitsRs.next()).thenReturn(true).thenReturn(false);
        when(unitsRs.getInt(1)).thenReturn(1);
        when(unitsRs.getString(2)).thenReturn("Unit 1");
        when(unitsRs.getString(3)).thenReturn("Unit description");
        when(unitsRs.getInt(4)).thenReturn(0);
        when(unitsRs.getString(5)).thenReturn("Fixed Sequence");

        when(tasksRs.next()).thenReturn(true).thenReturn(false);
        when(tasksRs.getInt(1)).thenReturn(5);
        when(tasksRs.getString(2)).thenReturn("Task 1");
        when(tasksRs.getString(3)).thenReturn("Task description");
        when(tasksRs.getString(4)).thenReturn("PROBLEM");
        when(tasksRs.getInt(5)).thenReturn(0);
        when(tasksRs.getInt(6)).thenReturn(10);

        when(stepsRs.next()).thenReturn(true).thenReturn(false);
        when(stepsRs.getInt(1)).thenReturn(9);
        when(stepsRs.getString(2)).thenReturn("Step title");
        when(stepsRs.getString(3)).thenReturn("Step description");
        when(stepsRs.getInt(4)).thenReturn(0);
        when(stepsRs.getString(5)).thenReturn("INFO_MESSAGE");
        when(stepsRs.getInt(7)).thenReturn(2);

        when(timeoutRs.next()).thenReturn(true);
        when(timeoutRs.getString(1)).thenReturn("SOFT");
        when(timeoutRs.getInt(2)).thenReturn(15);
        when(timeoutRs.getString(3)).thenReturn("WARN");
        when(timeoutRs.getString(4)).thenReturn("Keep going");

        when(hintsRs.next()).thenReturn(true).thenReturn(false);
        when(hintsRs.getInt(1)).thenReturn(1);
        when(hintsRs.getString(2)).thenReturn("Hint text");
        when(hintsRs.getInt(3)).thenReturn(0);

        when(outcomesRs.next()).thenReturn(true).thenReturn(false);
        when(outcomesRs.getInt(1)).thenReturn(111);
        when(outcomesRs.getString(2)).thenReturn("Outcome 1");
        when(outcomesRs.getString(3)).thenReturn("Outcome description");
        when(outcomesRs.getString(4)).thenReturn("Knowledge");
        when(outcomesRs.getBoolean(5)).thenReturn(true);
        when(outcomesRs.getString(6)).thenReturn("Fixed Sequence");
        when(outcomesRs.getString(7)).thenReturn("0");
        when(outcomesRs.getString(8)).thenReturn("Course");

        ProblemSvc problemSvc = mock(ProblemSvc.class);
        when(problemSvc.retrieve(10)).thenReturn(new LCSProblem(10, "AB", "AC"));

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findProblemSvc).thenReturn(problemSvc);

            Course course = dao.retrieve(TEST_COURSE_ID);

            assertNotNull(course);
            assertEquals(TEST_COURSE_ID, course.getId());
            assertEquals("Dynamic Programming", course.getTitle());
            assertEquals(1, course.getUnits().size());
            assertEquals(1, course.getOutcomes().size());
            assertEquals(1, course.currentUnit().getTasks().size());
            assertNotNull(course.currentUnit().getTasks().get(0).getProblem());
        }
    }

    /** Test inconsistent DB branch when a PROBLEM task references a missing problem row. */
    @Test
    public void testRetrieveWithMissingProblemReferenceThrowsNonRecoverable() throws Exception {
        PreparedStatement courseStmt = mock(PreparedStatement.class);
        PreparedStatement locationsStmt = mock(PreparedStatement.class);
        PreparedStatement unitsStmt = mock(PreparedStatement.class);
        PreparedStatement tasksStmt = mock(PreparedStatement.class);

        ResultSet courseRs = mock(ResultSet.class);
        ResultSet locationsRs = mock(ResultSet.class);
        ResultSet unitsRs = mock(ResultSet.class);
        ResultSet tasksRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(courseStmt)
                .thenReturn(locationsStmt)
                .thenReturn(unitsStmt)
                .thenReturn(tasksStmt);

        when(courseStmt.executeQuery()).thenReturn(courseRs);
        when(locationsStmt.executeQuery()).thenReturn(locationsRs);
        when(unitsStmt.executeQuery()).thenReturn(unitsRs);
        when(tasksStmt.executeQuery()).thenReturn(tasksRs);

        when(courseRs.next()).thenReturn(true);
        when(courseRs.getString(1)).thenReturn("Course");
        when(courseRs.getString(2)).thenReturn("FIXED_SEQUENCE");
        when(courseRs.getString(3)).thenReturn("Course description");

        when(locationsRs.next()).thenReturn(false);

        when(unitsRs.next()).thenReturn(true).thenReturn(false);
        when(unitsRs.getInt(1)).thenReturn(1);
        when(unitsRs.getString(2)).thenReturn("Unit 1");
        when(unitsRs.getString(3)).thenReturn("Unit description");
        when(unitsRs.getInt(4)).thenReturn(0);
        when(unitsRs.getString(5)).thenReturn("Fixed Sequence");

        when(tasksRs.next()).thenReturn(true);
        when(tasksRs.getInt(1)).thenReturn(5);
        when(tasksRs.getString(2)).thenReturn("Task 1");
        when(tasksRs.getString(3)).thenReturn("Task description");
        when(tasksRs.getString(4)).thenReturn("PROBLEM");
        when(tasksRs.getInt(5)).thenReturn(0);
        when(tasksRs.getInt(6)).thenReturn(222);

        ProblemSvc problemSvc = mock(ProblemSvc.class);
        when(problemSvc.retrieve(222)).thenThrow(new ObjNotFoundException("missing problem"));

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findProblemSvc).thenReturn(problemSvc);

            assertThrows(NonRecoverableException.class, () -> dao.retrieve(TEST_COURSE_ID));
        }
    }
}

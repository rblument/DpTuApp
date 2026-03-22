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

import edu.regis.dptu.dao.AccountDAO;
import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Account;

/**
 * Unit test class for AccountDAO using mocked database connections
 *
 * @author benm
 */
@SuppressWarnings("Logging")
public class AccountDAOTest {
    private AccountDAO dao;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private MockedStatic<DriverManager> mockedDriverManager;

    private static final String TEST_USER_ID = "test@regis.edu";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_FIRST_NAME = "Test";
    private static final String TEST_LAST_NAME = "User";
    private static final int TEST_SECURITY_QUESTION = 1;
    private static final String TEST_SECURITY_ANSWER = "answer";

    @BeforeEach
    public void setUp() throws SQLException {
        dao = new AccountDAO();
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

    /** Test creating a student account */
    @Test
    public void testCreateStudentAccount() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        when(mockConnection.prepareStatement(anyString(), any(String[].class)))
                .thenReturn(mockStatement);

        Account account = new Account(TEST_USER_ID, TEST_PASSWORD);
        account.setFirstName(TEST_FIRST_NAME);
        account.setLastName(TEST_LAST_NAME);
        account.setSecurityQuestion(TEST_SECURITY_QUESTION);
        account.setSecurityAnswer(TEST_SECURITY_ANSWER);
        account.setIsStudent(true);

        assertDoesNotThrow(() -> dao.create(account));

        verify(mockStatement, atLeastOnce()).setString(1, TEST_USER_ID);
        verify(mockStatement, atLeastOnce()).setString(2, TEST_PASSWORD);
        verify(mockStatement).executeUpdate();
    }

    /** Test creating a non-student account should throw exception */
    @Test
    public void testCreateNonStudentAccount() {
        Account account = new Account(TEST_USER_ID, TEST_PASSWORD);
        account.setIsStudent(false);

        assertThrows(IllegalArgException.class, () -> dao.create(account));
    }

    /** Test creating a duplicate account */
    @Test
    public void testCreateDuplicateAccount() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        Account account = new Account(TEST_USER_ID, TEST_PASSWORD);
        account.setFirstName(TEST_FIRST_NAME);
        account.setLastName(TEST_LAST_NAME);
        account.setSecurityQuestion(TEST_SECURITY_QUESTION);
        account.setSecurityAnswer(TEST_SECURITY_ANSWER);
        account.setIsStudent(true);

        assertThrows(IllegalArgException.class, () -> dao.create(account));
    }

    /** Test retrieving an account */
    @Test
    public void testRetrieve() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(1)).thenReturn(TEST_PASSWORD);
        when(mockResultSet.getString(2)).thenReturn(TEST_FIRST_NAME);
        when(mockResultSet.getString(3)).thenReturn(TEST_LAST_NAME);
        when(mockResultSet.getInt(4)).thenReturn(TEST_SECURITY_QUESTION);
        when(mockResultSet.getString(5)).thenReturn(TEST_SECURITY_ANSWER);
        when(mockResultSet.getBoolean(6)).thenReturn(true);

        Account result = dao.retrieve(TEST_USER_ID);

        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());
        assertEquals(TEST_PASSWORD, result.getPassword());
        assertEquals(TEST_FIRST_NAME, result.getFirstName());
        assertEquals(TEST_LAST_NAME, result.getLastName());
        assertEquals(TEST_SECURITY_QUESTION, result.getSecurityQuestion());
        assertEquals(TEST_SECURITY_ANSWER, result.getSecurityAnswer());
        assertTrue(result.isStudent());

        verify(mockStatement).setString(1, TEST_USER_ID);
        verify(mockStatement).executeQuery();
    }

    /** Test retrieving a non-existent account */
    @Test
    public void testRetrieveNonExistent() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // User doesn't exist

        assertThrows(ObjNotFoundException.class, () -> dao.retrieve(TEST_USER_ID));
    }

    /** Test updating an account */
    @Test
    public void testUpdate() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(1)).thenReturn(TEST_PASSWORD);
        when(mockResultSet.getString(2)).thenReturn(TEST_FIRST_NAME);
        when(mockResultSet.getString(3)).thenReturn(TEST_LAST_NAME);
        when(mockResultSet.getInt(4)).thenReturn(TEST_SECURITY_QUESTION);
        when(mockResultSet.getString(5)).thenReturn(TEST_SECURITY_ANSWER);
        when(mockResultSet.getBoolean(6)).thenReturn(true);

        when(mockStatement.executeUpdate()).thenReturn(1);

        Account account = new Account(TEST_USER_ID, "newpassword");
        account.setFirstName("NewFirst");
        account.setLastName("NewLast");
        account.setSecurityQuestion(2);
        account.setSecurityAnswer("newanswer");
        account.setIsStudent(true);

        assertDoesNotThrow(() -> dao.update(account));

        verify(mockStatement).executeUpdate();
    }

    /** Test updating a non-student account should throw exception */
    @Test
    public void testUpdateNonStudentAccount() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getBoolean(6)).thenReturn(false);

        Account account = new Account(TEST_USER_ID, TEST_PASSWORD);
        account.setIsStudent(true);

        assertThrows(IllegalArgException.class, () -> dao.update(account));
    }

    /** Test deleting an account */
    @Test
    public void testDelete() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        assertDoesNotThrow(() -> dao.delete(TEST_USER_ID));

        verify(mockStatement).setString(1, TEST_USER_ID);
        verify(mockStatement).executeUpdate();
    }

    /** Test checking if account exists */
    @Test
    public void testExists() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        boolean result = dao.exists(TEST_USER_ID);

        assertTrue(result);
        verify(mockStatement).setString(1, TEST_USER_ID);
        verify(mockStatement).executeQuery();
    }

    /** Test checking if non-existent account exists */
    @Test
    public void testExistsNonExistent() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean result = dao.exists(TEST_USER_ID);

        assertFalse(result);
    }

    /** Test that SQLException in create is wrapped in NonRecoverableException */
    @Test
    public void testCreateWithSQLException() throws Exception {
        SQLException sqlEx = new SQLException("Test error");
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        Account account = new Account(TEST_USER_ID, TEST_PASSWORD);
        account.setFirstName(TEST_FIRST_NAME);
        account.setLastName(TEST_LAST_NAME);
        account.setSecurityQuestion(TEST_SECURITY_QUESTION);
        account.setSecurityAnswer(TEST_SECURITY_ANSWER);
        account.setIsStudent(true);

        assertThrows(NonRecoverableException.class, () -> dao.create(account));
    }

    /** Test that SQLException in retrieve is wrapped in NonRecoverableException */
    @Test
    public void testRetrieveWithSQLException() throws Exception {
        SQLException sqlEx = new SQLException("Test error");
        when(mockConnection.prepareStatement(anyString())).thenThrow(sqlEx);

        assertThrows(NonRecoverableException.class, () -> dao.retrieve(TEST_USER_ID));
    }
}

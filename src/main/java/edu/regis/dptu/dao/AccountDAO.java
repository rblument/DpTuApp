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
package edu.regis.dptu.dao;

import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Account;
import edu.regis.dptu.svc.AccountSvc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A Data Access Object implementing {@link AccountSvc} behaviors.
 *
 * @author rickb
 */
public class AccountDAO extends MySqlDAO implements AccountSvc {

    /**
     * Initialize this DAO via the parent constructor.
     */
    public AccountDAO() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Account acct) throws IllegalArgException, NonRecoverableException {
        final String sql = "INSERT INTO Account (UserId, Password, FirstName, LastName, Question, Answer, IsStudent) VALUES (?,?,?,?,?,?,?)";

        if (acct.isStudent()) { // Can only create students, not admins.
            Connection conn = null;
            PreparedStatement stmt = null;

            String userId = acct.getUserId();

            try {
                conn = DriverManager.getConnection(URL);

                if (exists(userId, conn)) {
                    throw new IllegalArgException("User exists " + userId);
                }

                String[] keyCol = {"Id"};
                stmt = conn.prepareStatement(sql, keyCol);

                stmt.setString(1, userId);
                stmt.setString(2, acct.getPassword());
                stmt.setString(3, acct.getFirstName());
                stmt.setString(4, acct.getLastName());
                stmt.setInt(5, acct.getSecurityQuestion());
                stmt.setString(6, acct.getSecurityAnswer());
                stmt.setBoolean(7, acct.isStudent());

                stmt.executeUpdate();

            } catch (SQLException e) {
                throw new NonRecoverableException("AccountDAO-ERR-1", e);

            } finally {
                close(conn, stmt);
            }
        } else {
            throw new IllegalArgException("AccountDAO-ERR-2 New accounts must be students.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String userId) throws NonRecoverableException {
        final String sql = "DELETE FROM Account WHERE UserId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);

            stmt = conn.prepareStatement(sql);

            stmt.setString(1, userId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new NonRecoverableException("AccountDAO-ERR-3" + e.toString(), e);
        } finally {
            close(conn, stmt);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(String userId) throws NonRecoverableException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(URL);
            return exists(userId, conn);

        } catch (SQLException e) {
            throw new NonRecoverableException("AccountDAO-ERR-4" + e.toString(), e);
        } finally {
            close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account retrieve(String userId) throws ObjNotFoundException, NonRecoverableException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(URL);

            return retrieve(userId, conn);

        } catch (SQLException e) {
            throw new NonRecoverableException("AccountDAO-ERR-5" + e.toString(), e);
        } finally {
            close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Account account) throws ObjNotFoundException, IllegalArgException, NonRecoverableException {
        final String sql = "UPDATE Account SET Password = ?, FirstName = ?, LastName = ?, SecurityQuestion = ?, SecurityAnswer = ? WHERE UserId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        String userId = account.getUserId();

        try {
            conn = DriverManager.getConnection(URL);

            Account dbAcct = retrieve(userId, conn);

            if (dbAcct.isStudent()) {
                stmt = conn.prepareStatement(sql);

                stmt.setString(1, account.getPassword());
                stmt.setString(2, account.getFirstName());
                stmt.setString(3, account.getLastName());
                stmt.setInt(4, account.getSecurityQuestion());
                stmt.setString(5, account.getSecurityAnswer());
                stmt.setString(6, userId);

                int rows = stmt.executeUpdate();

                if (rows != 1) {
                    throw new NonRecoverableException("AccountDAO-ERR-6 Account update failed");
                }

            } else {
                throw new IllegalArgException("AccountDAO-ERR-7 Can only update a student account");
            }

        } catch (SQLException e) {
            throw new NonRecoverableException("AccountDAO-ERR-8" + e.toString(), e);
        } finally {
            close(conn, stmt);
        }
    }

    /**
     * Utility to retrieve the account with the given user id that uses an
     * established connection to the DB, which it does not close.
     *
     * @param userId
     * @param conn
     * @return
     * @throws ObjNotFoundException
     * @throws NonRecoverableException
     */
    private Account retrieve(String userId, Connection conn) throws ObjNotFoundException, NonRecoverableException {
        final String sql = "SELECT Password, FirstName, LastName, Question, Answer, IsStudent FROM Account WHERE UserId = ?";

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Account account = new Account(userId);

                account.setPassword(rs.getString(1));
                account.setFirstName(rs.getString(2));
                account.setLastName(rs.getString(3));
                account.setSecurityQuestion(rs.getInt(4));
                account.setSecurityAnswer(rs.getString(5));
                account.setIsStudent(rs.getBoolean(6));

                return account;

            } else {
                throw new ObjNotFoundException("Student Id:" + userId);
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("AccountDAO-ERR-9" + e.toString(), e);
        } finally {
            close(stmt);
        }
    }

    /**
     * Utility that returns whether the given user (id) exists in the database.
     *
     * @param userId the account user id format name@university.edu
     * @param conn an existing connection to the database, which is not closed
     * by this method.
     * @return true, if the user id exists in the database, otherwise false
     * @throws NonRecoverableException (see ex.getCause().getErrorCode())
     */
    private boolean exists(String userId, Connection conn) throws NonRecoverableException {
        final String sql = "SELECT UserId FROM Account WHERE UserId = ?";

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException ex) {
            throw new NonRecoverableException("AccountDAO-ERR-10" + ex.toString(), ex);
        } finally {
            close(stmt);
        }
    }
}


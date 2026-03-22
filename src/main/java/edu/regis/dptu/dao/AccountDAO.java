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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Account;
import edu.regis.dptu.svc.AccountSvc;

/**
 * A Data Access Object implementing {@link AccountSvc} behaviors.
 *
 * @author rickb
 */
public class AccountDAO extends MySqlDAO implements AccountSvc {
    private static final Logger log = LoggerFactory.getLogger(AccountDAO.class);

    /** Initialize this DAO via the parent constructor. */
    public AccountDAO() {
        super();
        log.debug("AccountDAO initialized");
    }

    /** {@inheritDoc} */
    @Override
    public void create(Account acct) throws IllegalArgException, NonRecoverableException {
        log.debug("Creating account for userId={}", acct.getUserId());

        final String sql =
                "INSERT INTO Account (UserId, Password, FirstName, LastName, Question, Answer, IsStudent) VALUES (?,?,?,?,?,?,?)";

        if (acct.isStudent()) { // Can only create students, not admins.
            Connection conn = null;
            PreparedStatement stmt = null;
            String userId = acct.getUserId();

            try {
                conn = DriverManager.getConnection(URL);
                log.debug("Database connection established for create()");

                if (exists(userId, conn)) {
                    log.warn("Account creation failed: user {} already exists", userId);
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

                int rows = stmt.executeUpdate();
                log.info("Account created for userId={}, rows affected={}", userId, rows);

            } catch (SQLException e) {
                log.error("SQLException creating account for userId={}", userId, e);
                throw new NonRecoverableException("AccountDAO-ERR-1", e);
            } finally {
                close(conn, stmt);
            }
        } else {
            log.warn("Attempted to create non-student account userId={}", acct.getUserId());
            throw new IllegalArgException("AccountDAO-ERR-2 New accounts must be students.");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void delete(String userId) throws NonRecoverableException {
        log.debug("Deleting account userId={}", userId);
        final String sql = "DELETE FROM Account WHERE UserId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            int rows = stmt.executeUpdate();
            log.info("Deleted account userId={}, rows affected={}", userId, rows);

        } catch (SQLException e) {
            log.error("SQLException deleting account userId={}", userId, e);
            throw new NonRecoverableException("AccountDAO-ERR-3" + e.toString(), e);
        } finally {
            close(conn, stmt);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean exists(String userId) throws NonRecoverableException {
        log.debug("Checking if account exists userId={}", userId);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            boolean exists = exists(userId, conn);
            log.debug("Account exists check for userId={} returned {}", userId, exists);
            return exists;
        } catch (SQLException e) {
            log.error("SQLException checking existence for userId={}", userId, e);
            throw new NonRecoverableException("AccountDAO-ERR-4" + e.toString(), e);
        } finally {
            close(conn);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Account retrieve(String userId) throws ObjNotFoundException, NonRecoverableException {
        log.debug("Retrieving account userId={}", userId);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            Account account = retrieve(userId, conn);
            log.debug("Retrieved account userId={}", userId);
            return account;
        } catch (SQLException e) {
            log.error("SQLException retrieving account userId={}", userId, e);
            throw new NonRecoverableException("AccountDAO-ERR-5" + e.toString(), e);
        } finally {
            close(conn);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void update(Account account)
            throws ObjNotFoundException, IllegalArgException, NonRecoverableException {
        log.debug("Updating account userId={}", account.getUserId());
        final String sql =
                "UPDATE Account SET Password = ?, FirstName = ?, LastName = ?, SecurityQuestion = ?, SecurityAnswer = ? WHERE UserId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            Account dbAcct = retrieve(account.getUserId(), conn);

            if (dbAcct.isStudent()) {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, account.getPassword());
                stmt.setString(2, account.getFirstName());
                stmt.setString(3, account.getLastName());
                stmt.setInt(4, account.getSecurityQuestion());
                stmt.setString(5, account.getSecurityAnswer());
                stmt.setString(6, account.getUserId());

                int rows = stmt.executeUpdate();
                log.info("Updated account userId={}, rows affected={}", account.getUserId(), rows);

                if (rows != 1) {
                    log.error("Account update failed for userId={}", account.getUserId());
                    throw new NonRecoverableException("AccountDAO-ERR-6 Account update failed");
                }
            } else {
                log.warn("Attempted to update non-student account userId={}", account.getUserId());
                throw new IllegalArgException("AccountDAO-ERR-7 Can only update a student account");
            }
        } catch (SQLException e) {
            log.error("SQLException updating account userId={}", account.getUserId(), e);
            throw new NonRecoverableException("AccountDAO-ERR-8" + e.toString(), e);
        } finally {
            close(conn, stmt);
        }
    }

    /**
     * Utility to retrieve the account with the given user id that uses an established connection to
     * the DB, which it does not close.
     *
     * @param userId the account user id to retrieve.
     * @param conn an existing JDBC connection (not closed by this method).
     * @return the fully populated {@link Account}.
     * @throws ObjNotFoundException if no account exists for the given user id.
     * @throws NonRecoverableException if a SQL error occurs.
     */
    private Account retrieve(String userId, Connection conn)
            throws ObjNotFoundException, NonRecoverableException {
        log.debug("Retrieving account with existing connection userId={}", userId);
        final String sql =
                "SELECT Password, FirstName, LastName, Question, Answer, IsStudent FROM Account WHERE UserId = ?";

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

                log.debug("Account retrieved successfully userId={}", userId);
                return account;
            } else {
                log.warn("Account not found userId={}", userId);
                throw new ObjNotFoundException("Student Id:" + userId);
            }
        } catch (SQLException e) {
            log.error("SQLException retrieving account userId={}", userId, e);
            throw new NonRecoverableException("AccountDAO-ERR-9" + e.toString(), e);
        } finally {
            close(stmt);
        }
    }

    /**
     * Utility that returns whether the given user (id) exists in the database.
     *
     * @param userId the account user id format name@university.edu
     * @param conn an existing connection to the database, which is not closed by this method.
     * @return true, if the user id exists in the database, otherwise false
     * @throws NonRecoverableException (see ex.getCause().getErrorCode())
     */
    private boolean exists(String userId, Connection conn) throws NonRecoverableException {
        log.debug("Checking existence of account with connection userId={}", userId);
        final String sql = "SELECT UserId FROM Account WHERE UserId = ?";

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            boolean exists = stmt.executeQuery().next();
            log.debug("Exists check returned {} for userId={}", exists, userId);
            return exists;
        } catch (SQLException ex) {
            log.error("SQLException checking existence userId={}", userId, ex);
            throw new NonRecoverableException("AccountDAO-ERR-10" + ex.toString(), ex);
        } finally {
            close(stmt);
        }
    }
}

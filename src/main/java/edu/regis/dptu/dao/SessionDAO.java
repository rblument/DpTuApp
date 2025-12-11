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
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.svc.ProblemSvc;
import edu.regis.dptu.svc.ServiceFactory;
import edu.regis.dptu.svc.SessionSvc;

/**
 * A Data Access Object implementing {@link SessionSvc} behaviors.
 *
 * @author rickb
 */
public class SessionDAO extends MySqlDAO implements SessionSvc {
    private static final Logger log = LoggerFactory.getLogger(SessionDAO.class);

    /** Initialize this DAO via the parent constructor. */
    public SessionDAO() {
        super();
        log.debug("SessionDAO initialized");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Autoincrements the id and sets startDate to CURRENT_TIMESTAMP() so session need not
     * specify those attributes
     */
    @Override
    public void create(TutoringSession session)
            throws IllegalArgException, NonRecoverableException {
        log.debug("Creating session id={}", session.getId());
        final String sql =
                "INSERT INTO TutoringSession(SecurityToken, UserId, CourseId, UnitId, IsActive, StartDate, ProblemType, ProblemId) VALUES (?,?,?,?,?,CURRENT_TIMESTAMP(),?,?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        int sessionId = session.getId();

        try {
            conn = DriverManager.getConnection(URL);
            log.debug("Database connection established for creating session id={}", sessionId);

            if (exists(sessionId, conn)) {
                log.warn("Session already exists with id={}", sessionId);
                throw new IllegalArgException("Session already exists with id " + sessionId);
            }

            String[] keyCol = {"SessionId"};
            stmt = conn.prepareStatement(sql, keyCol);
            stmt.setString(1, session.getSecurityToken());
            stmt.setString(2, session.getUserId());
            stmt.setInt(3, session.getCourse().getId());
            stmt.setInt(4, session.getUnit().getId());
            stmt.setBoolean(5, session.isIsActive());

            Problem prob = session.getProblem();
            stmt.setString(6, prob.getType().toString());
            stmt.setInt(7, prob.getId());

            int rows = stmt.executeUpdate();
            log.debug("Session created successfully id={}, rows affected={}", sessionId, rows);
        } catch (SQLException e) {
            log.error("SQLException creating session id={}", sessionId, e);
            throw new NonRecoverableException("Create Session Error", e);
        } finally {
            close(conn, stmt);
        }
    }

    /** {@inheritDoc} */
    @Override
    public TutoringSession retrieve(Student student)
            throws ObjNotFoundException, NonRecoverableException {
        String userId = student.getAccount().getUserId();
        log.debug("Retrieving session for userId={}", userId);
        final String sql =
                "SELECT SessionId, SecurityToken, StartDate, IsActive, ProblemType, ProblemId FROM TutoringSession WHERE UserId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TutoringSession session = new TutoringSession(null);
                GregorianCalendar date = new GregorianCalendar();

                session.setId(rs.getInt(1));
                session.setSecurityToken(rs.getString(2));
                date.setTime(rs.getDate(3));
                session.setStartDate(date);
                session.setIsActive(rs.getBoolean(4));

                ProblemSvc problemSvc = ServiceFactory.findProblemSvc();
                session.setProblem(problemSvc.retrieve(rs.getInt(6)));

                log.debug(
                        "Session retrieved successfully for userId={}, sessionId={}",
                        userId,
                        session.getId());
                return session;
            } else {
                log.warn("Session not found for userId={}", userId);
                throw new ObjNotFoundException("User id:" + userId);
            }
        } catch (SQLException e) {
            log.error("SQLException retrieving session for userId={}", userId, e);
            throw new NonRecoverableException("Retrieve Session Error", e);
        } finally {
            close(conn, stmt);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String retrieveSecurityToken(String userId)
            throws ObjNotFoundException, NonRecoverableException {
        log.debug("Retrieving security token for userId={}", userId);
        final String sql = "SELECT SecurityToken FROM TutoringSession WHERE UserId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String token = rs.getString(1);
                log.debug("Security token retrieved for userId={}", userId);
                return token;
            } else {
                log.warn("Security token not found for userId={}", userId);
                throw new ObjNotFoundException("User Id:" + userId);
            }
        } catch (SQLException e) {
            log.error("SQLException retrieving security token for userId={}", userId, e);
            throw new NonRecoverableException("SessionDAO-ERR-5" + e.toString(), e);
        } finally {
            close(conn, stmt);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void update(TutoringSession session)
            throws ObjNotFoundException, NonRecoverableException {
        log.debug("Updating session id={}", session.getId());
        final String sql =
                "UPDATE TutoringSession SET SecurityToken = ?, CourseId = ?, UnitId = ?, IsActive = ? WHERE SessionId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, session.getSecurityToken());
            stmt.setInt(2, session.getCourse().getId());
            stmt.setInt(3, session.getUnit().getId());
            stmt.setBoolean(4, session.isIsActive());
            stmt.setInt(5, session.getId());

            int rows = stmt.executeUpdate();

            if (rows != 1) {
                conn.rollback();
                log.warn(
                        "Session update affected {} rows, rolling back sessionId={}",
                        rows,
                        session.getId());
                throw new NonRecoverableException("Session update updated too many rows: " + rows);
            }

            conn.commit();
            log.debug("Session updated successfully sessionId={}", session.getId());
        } catch (SQLException e) {
            log.error("SQLException updating session id={}", session.getId(), e);
            throw new NonRecoverableException("Update Session Error", e);
        } finally {
            close(conn, stmt);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void delete(String userId) throws NonRecoverableException {
        log.debug("Deleting session for userId={}", userId);
        final String sql = "DELETE FROM TutoringSession WHERE UserId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);

            int rows = stmt.executeUpdate();

            if (rows != 1) {
                conn.rollback();
                log.warn(
                        "Session delete affected {} rows for userId={}, rolling back",
                        rows,
                        userId);
                throw new NonRecoverableException("Session delete deleted too many rows: " + rows);
            }

            conn.commit();
            log.debug("Session deleted successfully for userId={}", userId);
        } catch (SQLException e) {
            log.error("SQLException deleting session for userId={}", userId, e);
            throw new NonRecoverableException("Delete Session Error", e);
        } finally {
            close(conn, stmt);
        }
    }

    /**
     * @param sessionId the session id
     * @param conn an existing connection to the database
     * @return true, if the session exists, otherwise false
     * @throws NonRecoverableException (see ex.getCause().getErrorCode())
     */
    private boolean exists(int sessionId, Connection conn) throws NonRecoverableException {
        log.debug("Checking existence of session id={}", sessionId);
        final String sql = "SELECT SessionId FROM TutoringSession WHERE SessionId = ?;";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sessionId);

            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next();
            log.debug("Session existence check id={} result={}", sessionId, exists);
            return exists;
        } catch (SQLException e) {
            log.error("SQLException checking existence for session id={}", sessionId, e);
            throw new NonRecoverableException("Exists Session Error", e);
        } finally {
            close(stmt);
        }
    }
}

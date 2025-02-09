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
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.svc.SessionSvc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

/**
 *
 * A Data Access Object implementing {@link SessionSvc} behaviors.
 * 
 * @author rickb
 */
public class SessionDAO extends MySqlDAO implements SessionSvc {
    /**
     * Initialize this DAO via the parent constructor.
     */
    public SessionDAO() {
        super();
    }
    
    /**
     * {@inheritDoc}
     * 
     * Autoincrements the id and sets startDate to CURRENT_TIMESTAMP() so session need not specify those attributes
     */
    @Override
    public void create(TutoringSession session) throws IllegalArgException, NonRecoverableException {
        final String sql = "INSERT INTO TutoringSession(SecurityToken, UserId, CourseId, UnitId, IsActive, StartDate) VALUES (?,?,?,?,?,CURRENT_TIMESTAMP())";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        int sessionId = session.getId();

        try {
            conn = DriverManager.getConnection(URL);

            if (exists(sessionId, conn)) {
                throw new IllegalArgException("Session already exists with id " + sessionId);
            }

            String[] keyCol = {"SessionId"};
            stmt = conn.prepareStatement(sql, keyCol);

            stmt.setString(1, session.getSecurityToken());
            stmt.setString(2, session.getStudent().getStudentModel().getUserId());
            stmt.setInt(3, session.getCourse().getId());
            stmt.setInt(4, session.getUnit().getId());
            stmt.setBoolean(5, session.isIsActive());

            stmt.execute();
        } catch(SQLException e) {
            throw new NonRecoverableException("Create Session Error", e);
        } finally {
            close(conn, stmt);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TutoringSession retrieve(String userId) throws ObjNotFoundException, NonRecoverableException {
        final String sql = "SELECT SessionId, SecurityToken, StartDate, IsActive FROM TutoringSession WHERE UserId = ?";

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

                return session;
            } else {
                throw new ObjNotFoundException("User id:" + userId);
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("Retrieve Session Error", e);
        } finally {
            close(stmt);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void update(TutoringSession session) throws ObjNotFoundException, NonRecoverableException {
        final String sql = "UPDATE TutoringSession SET SecurityToken = ?, CourseId = ?, UnitId = ?, IsActive = ? WHERE SessionId = ?";

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
                throw new NonRecoverableException("Session update updated too many rows: " + rows);
            }

            conn.commit();
        } catch(SQLException e) {
            throw new NonRecoverableException("Update Session Error", e);
        } finally {
            close(conn, stmt);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String userId) throws NonRecoverableException {
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
                throw new NonRecoverableException("Session delete deleted too many rows: " + rows);
            }

            conn.commit();
        } catch(SQLException e) {
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
        final String sql = "SELECT SessionId FROM TutoringSession WHERE SessionId = ?;";
        
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sessionId);

            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new NonRecoverableException("Exists Session Error", e);
        } finally {
            close(stmt);
        }
    }
}



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
import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TaskKind;
import static edu.regis.dptu.model.TaskKind.LCS_PROBLEM;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.svc.SessionSvc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        final String sql = "INSERT INTO TutoringSession(SecurityToken, UserId, CourseId, UnitId, IsActive, StartDate, ProblemKind, ProblemId) VALUES (?,?,?,?,?,CURRENT_TIMESTAMP(),?,?)";
        
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
            
            Problem prob = session.getProblem();
            stmt.setString(6, prob.getKind().toString());
            stmt.setInt(7, prob.getId());

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
    public TutoringSession retrieve(Student student) throws ObjNotFoundException, NonRecoverableException {
        final String sql = "SELECT SessionId, SecurityToken, StartDate, IsActive, ProblemKind, ProblemId FROM TutoringSession WHERE UserId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            stmt = conn.prepareStatement(sql);
            
            String userId = student.getAccount().getUserId();
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
                
                TaskKind kind = TaskKind.valueOf(rs.getString(5));
                
                session.setProblem(retrieveProblem(kind, rs.getInt(6), conn));

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
    public String retrieveSecurityToken(String userId) throws ObjNotFoundException, NonRecoverableException {
         final String sql = "SELECT SecurityToken FROM TutoringSession WHERE UserId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString(1);

            } else {
                throw new ObjNotFoundException("User Id:" + userId);
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("SessionDAO-ERR-5" + e.toString(), e);
        } finally {
            close(conn, stmt);
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
    
    private Problem retrieveProblem(TaskKind kind, int kindId, Connection conn) 
        throws NonRecoverableException {
        switch (kind) {
            case LCS_PROBLEM:
                return retrieveLCSProblem(kindId, conn);
                
            default:
                return null;
        }
    }
    
    private LCSProblem retrieveLCSProblem(int kindId, Connection conn) throws NonRecoverableException {
         final String sql = "SELECT Title,Description,Sequence1,Sequence2 FROM LCSProblem WHERE Id = ?";

        ArrayList<Task> tasks = new ArrayList<>();
    
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, kindId);
  

            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                LCSProblem prob = new LCSProblem(rs.getString(3), rs.getString(4));
                prob.setId(kindId);
                prob.setTitle(rs.getString(1));
                prob.setDescription(rs.getString(2));
                
                return prob;
                
            } else {
                throw new NonRecoverableException("Inconsisted DB LCSProb: " + kindId);
            }
            
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-10" + e.toString(), e);
        } finally {
            close(stmt); // Don't close the connection, retrieve(courseId) will
        }
    }
}



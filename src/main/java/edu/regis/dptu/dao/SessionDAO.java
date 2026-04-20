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
import java.sql.Statement;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.CourseDigest;
import edu.regis.dptu.model.Model;
import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.PendingStep;
import edu.regis.dptu.model.PendingTask;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.model.UnitDigest;
import edu.regis.dptu.svc.CourseSvc;
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
            "INSERT INTO TutoringSession(SecurityToken, UserId, CourseId, UnitId, IsActive, StartDate, ProblemType, ProblemId, Mode) VALUES (?,?,?,?,?,CURRENT_TIMESTAMP(),?,?,?)";

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
            stmt.setString(8, (session.getMode() == null ? Mode.SEE_ONE : session.getMode()).name());

            int rows = stmt.executeUpdate();
            log.debug("Session created successfully id={}, rows affected={}", sessionId, rows);

            persistPendingProgress(session, conn);
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
            "SELECT SessionId, SecurityToken, StartDate, IsActive, CourseId, UnitId, ProblemType, ProblemId, Mode FROM TutoringSession WHERE UserId = ?";

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
                session.setCourse(new CourseDigest(rs.getInt(5)));
                session.setUnit(new UnitDigest(rs.getInt(6)));
                String modeName = rs.getString(9);
                session.setMode(modeName == null ? Mode.SEE_ONE : Mode.valueOf(modeName));

                ProblemSvc problemSvc = ServiceFactory.findProblemSvc();
                session.setProblem(problemSvc.retrieve(rs.getInt(8)));
                loadPendingProgress(session, rs.getInt(5), conn);

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
            "UPDATE TutoringSession SET SecurityToken = ?, CourseId = ?, UnitId = ?, IsActive = ?, Mode = ? WHERE SessionId = ?";

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
            stmt.setString(5, (session.getMode() == null ? Mode.SEE_ONE : session.getMode()).name());
            stmt.setInt(6, session.getId());

            int rows = stmt.executeUpdate();

            if (rows != 1) {
                conn.rollback();
                log.warn(
                        "Session update affected {} rows, rolling back sessionId={}",
                        rows,
                        session.getId());
                throw new NonRecoverableException("Session update updated too many rows: " + rows);
            }

            persistPendingProgress(session, conn);

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
        final String lookupSql = "SELECT SessionId FROM TutoringSession WHERE UserId = ?";
        final String sql = "DELETE FROM TutoringSession WHERE UserId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement lookupStmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            conn.setAutoCommit(false);

            lookupStmt = conn.prepareStatement(lookupSql);
            lookupStmt.setString(1, userId);
            ResultSet rs = lookupStmt.executeQuery();
            if (rs.next()) {
                clearPendingProgress(rs.getInt(1), conn);
            }

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
            close(lookupStmt);
            close(conn, stmt);
        }
    }

    private void persistPendingProgress(TutoringSession session, Connection conn)
            throws SQLException {
        int sessionId = session.getId();
        clearPendingProgress(sessionId, conn);

        if (session.getTasks() == null || session.getTasks().isEmpty()) {
            return;
        }

        PendingTask pendingTask = session.getCurrentTask();
        if (pendingTask == null || pendingTask.getTask() == null) {
            return;
        }

        PendingStep pendingStep = pendingTask.getCurrentStep();
        if (pendingStep == null || pendingStep.getStep() == null) {
            return;
        }

        final String pendingStepSql =
                "INSERT INTO PendingStep(SessionId, StepId, NotifyTutor, IsCompleted, CurrentHintIndex) VALUES (?,?,?,?,?)";
        final String pendingTaskSql =
                "INSERT INTO PendingTask(SessionId, TaskId, PendingStepId) VALUES (?,?,?)";

        try (PreparedStatement insertStepStmt =
                conn.prepareStatement(pendingStepSql, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement insertTaskStmt = conn.prepareStatement(pendingTaskSql)) {
            insertStepStmt.setInt(1, sessionId);
            insertStepStmt.setInt(2, pendingStep.getStep().getId());
            insertStepStmt.setBoolean(3, pendingStep.isNotifyTutor());
            insertStepStmt.setBoolean(4, pendingStep.isCompleted());
            insertStepStmt.setInt(5, pendingStep.getCurrentHintIndex());
            insertStepStmt.executeUpdate();

            int pendingStepId = Model.DEFAULT_ID;
            ResultSet keys = insertStepStmt.getGeneratedKeys();
            if (keys.next()) {
                pendingStepId = keys.getInt(1);
            }

            if (pendingStepId == Model.DEFAULT_ID) {
                throw new SQLException("Unable to persist PendingStep for session " + sessionId);
            }

            insertTaskStmt.setInt(1, sessionId);
            insertTaskStmt.setInt(2, pendingTask.getTask().getId());
            insertTaskStmt.setInt(3, pendingStepId);
            insertTaskStmt.executeUpdate();
        }
    }

    private void loadPendingProgress(TutoringSession session, int courseId, Connection conn)
            throws NonRecoverableException {
        final String sql =
                "SELECT pt.TaskId, ps.Id, ps.StepId, ps.NotifyTutor, ps.IsCompleted, ps.CurrentHintIndex "
                        + "FROM PendingTask pt JOIN PendingStep ps ON pt.PendingStepId = ps.Id "
                        + "WHERE pt.SessionId = ?";

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, session.getId());
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return;
            }

            int taskId = rs.getInt(1);
            int pendingStepId = rs.getInt(2);
            int stepId = rs.getInt(3);
            boolean notifyTutor = rs.getBoolean(4);
            boolean isCompleted = rs.getBoolean(5);
            int currentHintIndex = rs.getInt(6);

            Task task = loadTask(courseId, taskId, conn);
            task.setProblem(session.getProblem());

            Step step = task.findStepById(stepId);
            if (step == null) {
                step = new Step(stepId, 0, StepSubType.INFO_MESSAGE);
            }

            PendingStep pendingStep = new PendingStep(pendingStepId, step);
            pendingStep.setNotifyTutor(notifyTutor);
            pendingStep.setIsCompleted(isCompleted);
            pendingStep.setCurrentHintIndex(currentHintIndex);

            PendingTask pendingTask = new PendingTask(task);
            pendingTask.setCurrentStep(pendingStep);
            session.addTask(pendingTask);
        } catch (SQLException e) {
            throw new NonRecoverableException("Load Pending Progress Error", e);
        } finally {
            close(stmt);
        }
    }

    private Task loadTask(int courseId, int taskId, Connection conn) {
        try {
            CourseSvc courseSvc = ServiceFactory.findCourseSvc();
            return courseSvc.retrieveTask(courseId, taskId, conn);
        } catch (ObjNotFoundException | NonRecoverableException ex) {
            log.warn(
                    "Unable to load Task {} for course {} while restoring session; using lightweight fallback",
                    taskId,
                    courseId,
                    ex);
            return new Task(taskId);
        }
    }

    private void clearPendingProgress(int sessionId, Connection conn) throws SQLException {
        final String deletePendingTaskSql = "DELETE FROM PendingTask WHERE SessionId = ?";
        final String deletePendingStepSql = "DELETE FROM PendingStep WHERE SessionId = ?";

        try (PreparedStatement deletePendingTaskStmt = conn.prepareStatement(deletePendingTaskSql);
                PreparedStatement deletePendingStepStmt = conn.prepareStatement(deletePendingStepSql)) {
            deletePendingTaskStmt.setInt(1, sessionId);
            deletePendingTaskStmt.executeUpdate();

            deletePendingStepStmt.setInt(1, sessionId);
            deletePendingStepStmt.executeUpdate();
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

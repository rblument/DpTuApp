/**
 * MySQL-backed DAO implementation of {@link CompletedTaskSvc}
 * 
 * <p> persists per-student task completion records and provides query methods for
 * computing student progress (for the dashboard progress bars)
 * 
 * @author Lindsey Cox 
 * @date 2/4/2026
 */



package edu.regis.dptu.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.svc.CompletedTaskSvc;

public class CompletedTaskDAO extends MySqlDAO implements CompletedTaskSvc {
    private static final Logger log = LoggerFactory.getLogger(CompletedTaskDAO.class);

    /**
     * {@inheritDoc}
     * 
     * <p> Inserts a (UserId, TaskId) completion record if one does not exist
     * if the record already exists, the completion timestamp is updated
     * 
     * <p> This method is idempotent, calling it multiple times for the same
     * user, task pair will not create duplicate records
     */
    @Override
    public void markCompleted(String userId, int taskId) throws NonRecoverableException {
        final String sql =
            "INSERT INTO CompletedTask (UserId, TaskId) VALUES (?, ?) " +
            "ON DUPLICATE KEY UPDATE CompletedAt = CURRENT_TIMESTAMP";
        
            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                    //bind params to prevent SQL injections and ensure type safety
                    stmt.setString(1, userId);
                    stmt.setInt(2, taskId);

                    stmt.executeUpdate();
                 } catch (SQLException e) {
                    //log contextual info to help diagnose failures
                    log.error("SQLException marking completed task userId={}, taskId={}", userId, taskId);
                    //esc alate as non-recov erable since progress persistence is critical state
                    throw new NonRecoverableException("CompletedTaskDAO-ERR-1 " + e.toString(), e);
                 }
    }

    /**
     * {@inheritDoc}
     * 
     * <p> returns the total number of tasks completed by the specified user
     * 
     * <p> value used to compute progress percentages to UI components (progress bars)
     */
    @Override
    public int countCompleted(String userId) throws NonRecoverableException {
        final String sql = "SELECT COUNT(*) FROM CompletedTask WHERE UserId = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                //bind the user identifier to the query
                stmt.setString(1, userId);

                ResultSet rs = stmt.executeQuery();

                /** explanation of this line
                 * rs.next() moves cursor to first row of the result set, returning true 
                 * if row exists, and false if No rows
                 * rs.getInt() reads the first col of curr row, (COUNT(*) val)
                 * then the ? operator, where if rs.next() is true, get rs.getInt(1),
                 * otherwise get 0
                 * basically if this query ever stops guaranteeing a row, 
                 * this method still behaves safely*/ 
                return rs.next () ? rs.getInt(1) : 0;

             } catch (SQLException e) {
                //log user id for easier debugging
                log.error("SQLException counting completed tasks userId={}", userId, e);
                throw new NonRecoverableException("CompletedTaskDAO-ERR-2 " + e.toString(), e);
             }
    }
}

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
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.MatrixChainProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;
import edu.regis.dptu.svc.ProblemSvc;

public class ProblemDAO extends MySqlDAO implements ProblemSvc {
    private static final Logger log = LoggerFactory.getLogger(ProblemDAO.class);

    /** Instantiate this Course DAO with default values. */
    public ProblemDAO() {
        log.debug("ProblemDAO initialized");
    }

    /** {@inheritDoc} */
    @Override
    public Problem retrieve(int problemId) throws ObjNotFoundException, NonRecoverableException {
        log.debug("Retrieving problem id={}", problemId);
        final String sql =
                "SELECT ProblemType, SubTypeId, Title, Description FROM Problem WHERE Id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            log.debug("Database connection established for retrieve({})", problemId);

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, problemId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Problem problem =
                        retrieveProblemSubType(
                                problemId,
                                ProblemKind.valueOf(rs.getString(1)),
                                rs.getInt(2),
                                conn);

                problem.setTitle(rs.getString(3));
                problem.setDescription(rs.getString(4));
                problemId = retrieveTaskIdForProblem(problemId, conn);

                log.debug("Problem retrieved successfully id={}", problemId);
                return problem;
            } else {
                log.warn("Problem not found id={}", problemId);
                throw new ObjNotFoundException("Problem Id:" + problemId);
            }
        } catch (SQLException e) {
            log.error("SQLException retrieving problem id={}", problemId, e);
            throw new NonRecoverableException("ProblemDAO-ERR-1 " + e.toString(), e);
        } finally {
            close(conn, stmt);
        }
    }

    @Override
    public Problem retrieveByKind(ProblemKind kind)
            throws ObjNotFoundException, NonRecoverableException {
        log.debug("Retrieving problem by kind={}", kind);
        final String sql =
                "SELECT Id, SubTypeId, Title, Description FROM Problem WHERE ProblemType = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            log.debug("Database connection established for retrieveByKind({})", kind);

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, kind.toString());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Problem problem = retrieveProblemSubType(rs.getInt(1), kind, rs.getInt(2), conn);
                problem.setTitle(rs.getString(3));
                problem.setDescription(rs.getString(4));

                int taskId = retrieveTaskIdForProblem(problem.getId(), conn);
                problem.setTaskId(taskId);

                log.debug("Problem retrieved successfully for kind={}, taskId={}", kind, taskId);
                return problem;
            } else {
                log.warn("No problem found for kind={}", kind);
                throw new ObjNotFoundException("Problem Kind:" + kind.toString());
            }
        } catch (SQLException e) {
            log.error("SQLException retrieving problem by kind={}", kind, e);
            throw new NonRecoverableException("ProblemDAO-ERR-2 " + e.toString(), e);
        } finally {
            close(conn, stmt);
        }
    }

    private Problem retrieveProblemSubType(int id, ProblemKind type, int subTypeId, Connection conn)
            throws NonRecoverableException {
        log.debug("Retrieving problem subtype id={}, type={}, subTypeId={}", id, type, subTypeId);

        switch (type) {
            case LCS_PROBLEM:
                return retrieveLCSProblem(id, subTypeId, conn);

            case MATRIX_CHAIN:
                return retrieveMatrixChainProblem(id, subTypeId, conn);

            case KNAPSACK_0_1:
                log.warn("KNAPSACK_0_1 retrieval not implemented for id={}", id);
                return null;

            default:
                log.warn("Unknown problem type={} for id={}", type, id);
                return null;
        }
    }

    /**
     * Load and return a LCSProblrem from LCSProblem table in the DB using the given subTypeId.
     *
     * @param id the id of the returned LCS problem
     * @param subTypeId the id of the specific LCS problem in the LCSProblem table
     * @param conn an open connection to the DB, which isn't closed.
     * @return an LCSProblem with the given id and subTypeId
     * @throws NonRecoverableException possible see getCause()
     */
    private LCSProblem retrieveLCSProblem(int id, int subTypeId, Connection conn)
            throws NonRecoverableException {
        log.debug("Retrieving LCSProblem id={}, subTypeId={}", id, subTypeId);
        final String sql = "SELECT Sequence1, Sequence2 FROM LCSProblem WHERE Id = ?";

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, subTypeId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LCSProblem prob = new LCSProblem(id, rs.getString(1), rs.getString(2));
                prob.setSubTypeId(subTypeId);
                prob.setTitle(rs.getString(1));
                prob.setDescription(rs.getString(2));

                log.debug("LCSProblem retrieved successfully id={}, subTypeId={}", id, subTypeId);
                return prob;
            } else {
                log.warn("LCSProblem not found id={}, subTypeId={}", id, subTypeId);
                throw new NonRecoverableException("Inconsistent DB LCSProblem: " + id);
            }
        } catch (SQLException e) {
            log.error("SQLException retrieving LCSProblem id={}, subTypeId={}", id, subTypeId, e);
            throw new NonRecoverableException("ProblemDAO-ERR-2 " + e.toString(), e);
        } finally {
            close(stmt); // Don't close the connection, retrieve(courseId) will
        }
    }
    
    /**
     * Load and return a MatrixChainProblem from MatrixChainProblem table in the DB using the given subTypeId.
     * 
     * @param id the id of the returned Matrix Chain problem
     * @param subTypeId the id of the specific Matrix Chain problem in the MatrixChainProblem table
     * @param conn an open connection to the DB, which isn't closed.
     * @return a MatrixChainProblem with the given id and subTypeId
     * @throws NonRecoverableException possible see getCause()
     */
    private MatrixChainProblem retrieveMatrixChainProblem(int id, int subTypeId, Connection conn) throws NonRecoverableException {
        log.debug("Retrieving MatrixChainProblem id={}, subTypeId={}", id, subTypeId);
        final String sql = "SELECT SizeId, Width, Height from MatrixSizes where ProblemId = ?";
        
        PreparedStatement stmt = null;
        
        try {
            stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1, subTypeId);
            
            ResultSet rs = stmt.executeQuery();
            
            // Get the number of rows returned, then add each width and height to a numRows by 2 array for MatrixChainProblem
            if (rs.last()){
                int numRows = rs.getRow();
                log.debug("Number of Matrices={}", numRows);
                
                int[][] sizes = new int[numRows][2];
                rs.beforeFirst();
                int i = 0;
                while (rs.next()) {
                    sizes[i][0] = rs.getInt(2);
                    sizes[i][1] = rs.getInt(3);
                    i++;
                }
                log.debug("Matrix Sizes Array={}", Arrays.toString(sizes));
                for (int[] size : sizes) log.debug("Size={}", Arrays.toString(size));
                
                MatrixChainProblem prob = new MatrixChainProblem(id, sizes);
                prob.setSubTypeId(subTypeId);
                
                log.debug("MatrixChainProblem retrieved successfully id={}, subTypeId={}", id, subTypeId);
                return prob;
            }
            else {
                log.warn("MatrixChainProblem not found id={}, subTypeId={}", id, subTypeId);
                throw new NonRecoverableException("Inconsistent DB MatrixChainProblem: " + id);
            }
        } catch (SQLException e) {
            log.error("SQLException retrieving MatrixChainProblem id={}, subTypeId={}", id, subTypeId, e);
            throw new NonRecoverableException("ProblemDAO-ERR-2 " + e.toString(), e);
        } finally {
            close(stmt); // Don't close the connection, retrieve(courseId) will
        }
    }

    /**
     * Retrieve the TaskId for the Task that represents the given Problem.
     *
     * @param problemId the id of the problem
     * @param conn open database connection
     * @return the task id, or -1 if no matching task exists
     * @throws NonRecoverableException if the query fails
     */
    private int retrieveTaskIdForProblem(int problemId, Connection conn)
            throws NonRecoverableException {
        final String sql = "SELECT TaskId FROM Task WHERE Kind = ? AND KindId = ?";

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "PROBLEM");
            stmt.setInt(2, problemId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int taskId = rs.getInt(1);
                log.debug("Found taskId={} for problemId={}", taskId, problemId);
                return taskId;
            }

            log.warn("No Task found for problemId={}", problemId);
            return -1;

        } catch (SQLException e) {
            log.error("SQLException retrieving taskId for problemId={}", problemId, e);
            throw new NonRecoverableException("ProblemDAO-ERR-taskLookup " + e.toString(), e);
        } finally {
            close(stmt);
        }
    }
}

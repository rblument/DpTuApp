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

import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;
import edu.regis.dptu.svc.ProblemSvc;

public class ProblemDAO extends MySqlDAO implements ProblemSvc {

    /** Instantiate this Course DAO with default values. */
    public ProblemDAO() {}

    /** {@inheritDoc} */
    @Override
    public Problem retrieve(int problemId) throws ObjNotFoundException, NonRecoverableException {
        final String sql =
                "SELECT ProblemType, SubTypeId, Title, Description FROM Problem WHERE Id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
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

                return problem;

            } else {
                throw new ObjNotFoundException("Course Id:" + problemId);
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("ProblemDAO-ERR-1" + e.toString(), e);
        } finally {
            close(conn, stmt);
        }
    }

    private Problem retrieveProblemSubType(int id, ProblemKind type, int subTypeId, Connection conn)
            throws NonRecoverableException {
        switch (type) {
            case LCS_PROBLEM:
                return retrieveLCSProblem(id, subTypeId, conn);

            case MATRIX_CHAIN:
                return null;

            case KNAPSACK_0_1:
                return null;

            default:
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
        final String sql = "SELECT Sequence1,Sequence2 FROM LCSProblem WHERE Id = ?";

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

                return prob;

            } else {
                throw new NonRecoverableException("Inconsisted DB LCSProb: " + id);
            }

        } catch (SQLException e) {
            throw new NonRecoverableException("ProblemDAO-ERR-2" + e.toString(), e);
        } finally {
            close(stmt); // Don't close the connection, retrieve(courseId) will
        }
    }
}

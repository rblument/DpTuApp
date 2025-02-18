package edu.regis.dptu.dao;

import edu.regis.dptu.err.NonRecoverableException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An Transaction Data Access Object to extend for when multiple
 * database operations should complete or fail as a logical unit
 * 
 * @author benm
 */
public abstract class Transactionable extends MySqlDAO {
    /**
     * Start a transaction on the connection
     * 
     * @param conn the database connection
     */
    protected void startTransaction(Connection conn) {
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
        }
    }

    /**
     * Commit any statements made in the current transaction associated
     * with the given connection.
     * 
     * @param conn the database connection
     */
    protected void commit(Connection conn) throws NonRecoverableException {
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new NonRecoverableException("Transaction Error: Not able to commit transaction");
        }
    }

    /**
     * Rollback any statements made in the current transaction associated
     * with the given connection.
     * 
     * @param conn the database connection
     */
    protected void rollback(Connection conn) throws NonRecoverableException {
        try {
            conn.rollback();
        } catch (SQLException e) {
            throw new NonRecoverableException("Transaction Error: Not able to rollback transaction");
        }
    }
}

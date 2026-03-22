package edu.regis.dptu.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.err.NonRecoverableException;

/**
 * A Transaction Data Access Object to extend for when multiple database operations should complete
 * or fail as a logical unit.
 *
 * @author benm
 */
public abstract class Transactionable extends MySqlDAO {
    private static final Logger log = LoggerFactory.getLogger(Transactionable.class);

    /**
     * Start a transaction on the connection
     *
     * @param conn the database connection
     */
    protected void startTransaction(Connection conn) {
        try {
            log.debug("Starting transaction on connection {}", conn);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            log.error("Failed to start transaction on connection {}", conn, e);
        }
    }

    /**
     * Commit any statements made in the current transaction associated with the given connection.
     *
     * @param conn the database connection
     */
    protected void commit(Connection conn) throws NonRecoverableException {
        try {
            log.debug("Committing transaction on connection {}", conn);
            conn.commit();
            log.debug("Transaction committed successfully on connection {}", conn);
        } catch (SQLException e) {
            log.error("Failed to commit transaction on connection {}", conn, e);
            throw new NonRecoverableException(
                    "Transaction Error: Not able to commit transaction", e);
        }
    }

    /**
     * Rollback any statements made in the current transaction associated with the given connection.
     *
     * @param conn the database connection
     */
    protected void rollback(Connection conn) throws NonRecoverableException {
        try {
            log.debug("Rolling back transaction on connection {}", conn);
            conn.rollback();
            log.debug("Transaction rolled back successfully on connection {}", conn);
        } catch (SQLException e) {
            log.error("Failed to rollback transaction on connection {}", conn, e);
            throw new NonRecoverableException(
                    "Transaction Error: Not able to rollback transaction", e);
        }
    }
}

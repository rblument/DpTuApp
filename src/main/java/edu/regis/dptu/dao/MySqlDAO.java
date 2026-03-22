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
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.err.MissingPropertyException;
import edu.regis.dptu.util.ResourceMgr;

/**
 * A root Data Access Object that provides some utility operations and ensures the MySQL JDBC driver
 * is loaded.
 *
 * @author Rickb
 */
public abstract class MySqlDAO {
    private static final Logger log = LoggerFactory.getLogger(MySqlDAO.class);

    /** The host where MySQL resides (see /resources/DpTu.Properties). */
    public static final String DB_HOST_PROP = "edu.regis.dptu.DB_HOST";

    /** The name of the DpTu database (see /resources/DpTu.Properties). */
    public static final String DB_NAME_PROP = "edu.regis.dptu.DB_NAME";

    /** The DB user used by DpTu to login (see /resources/DpTu.Properties). */
    public static final String DB_USER_PROP = "edu.regis.dptu.DB_USER";

    /** The password used by DpTu to login into the DB. */
    public static final String DB_PASS_PROP = "edu.regis.dptu.DB_PASS";

    /** Fully qualified name of the MySql JDBC driver class. */
    public static String DRIVER = "com.mysql.cj.jdbc.Driver";

    /*
     * The URL used to obtain a JDBC connection.
     */
    public static String URL;

    /**
     * Utility indicating whether the DriverClass was explictly loaded (in order to overcome errors
     * in certain JVMs).
     */
    public static boolean IS_LOADED = false;

    /**
     * If it hasn't already been loaded, explicitly load the MySql driver. Suppress the warning for
     * the old school reflection method
     */
    @SuppressWarnings("deprecation")
    public MySqlDAO() {
        if (!IS_LOADED) {
            try {
                ResourceMgr rscr = ResourceMgr.instance();

                String dbHost = rscr.getProp(DB_HOST_PROP);
                String dbName = rscr.getProp(DB_NAME_PROP);
                String dbUser = rscr.getProp(DB_USER_PROP);
                String dbPass = rscr.getProp(DB_PASS_PROP);

                URL =
                        "jdbc:mysql://"
                                + dbHost
                                + "/"
                                + dbName
                                + "?user="
                                + dbUser
                                + "&password="
                                + dbPass;

                Class.forName(DRIVER).newInstance(); // Old School

                IS_LOADED = true;

            } catch (MissingPropertyException e) {
                // Property initialization is required to establish a DB connection.
                // Log at WARN to aid troubleshooting but allow the application to decide how to
                // proceed (some execution paths may not require a DB).
                log.warn("Missing DB property while initializing JDBC URL: {}", e.getMessage(), e);
            } catch (ClassNotFoundException e) {
                log.error("MySqlDAO-ERR-1: JDBC driver class not found: {}", DRIVER, e);
            } catch (InstantiationException e) {
                log.error("MySqlDAO-ERR-2: Unable to instantiate JDBC driver: {}", DRIVER, e);
            } catch (IllegalAccessException e) {
                log.error(
                        "MySqlDAO-ERR-3: Illegal access while instantiating JDBC driver: {}",
                        DRIVER,
                        e);
            }
        }
    }

    /**
     * If the given connection or statement is open, close it, but log any errors that might be
     * thrown during the closing operations.
     *
     * @param conn an JDBC Connection that will be closed.
     * @param stmt an JDBC Statement that will be closed.
     */
    protected void close(Connection conn, Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                log.debug("MySqlDAO-ERR-4: stmt.close() failed", e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                log.debug("MySqlDAO-ERR-5: conn.close() failed", e);
            }
        }
    }

    /**
     * If the given connection is open, close it, but log any errors that occur in attempting to
     * close the connection.
     *
     * @param conn an existing JDBC connection (may be null)
     */
    protected void close(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true); // Convenience
                conn.close();
            } catch (Exception e) {
                log.debug("MySqlDAO-ERR-6: conn.close() failed", e);
            }
        }
    }

    /**
     * If the given statement is open, close it, but log any errors that occur in attempting to
     * close the connection.
     *
     * @param stmt an existing JDBC statement (may be null)
     */
    protected void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                log.debug("MySqlDAO-ERR-7: stmt.close() failed", e);
            }
        }
    }
}

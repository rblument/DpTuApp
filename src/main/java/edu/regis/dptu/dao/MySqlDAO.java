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

import edu.regis.dptu.err.MissingPropertyException;
import edu.regis.dptu.util.ResourceMgr;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A root Data Access Object that provides some utility operations and ensures
 * the MySQL JDBC driver is loaded.
 * 
 * @author Rickb
 */
public abstract class MySqlDAO {
    /**
     * The host where MySQL resides (see /resources/DpTu.Properties).
     */
    public static final String DB_HOST_PROP = "edu.regis.dptu.DB_HOST";
    
    /**
     * The name of the DpTu database (see /resources/DpTu.Properties).
     */
    public static final String DB_NAME_PROP = "edu.regis.dptu.DB_NAME";
    
    /**
     * The DB user used by DpTu to login (see /resources/DpTu.Properties).
     */
    public static final String DB_USER_PROP = "edu.regis.dptu.DB_USER";
    
    /**
     * The password used by DpTu to login into the DB.
     */
    public static final String DB_PASS_PROP = "edu.regis.dptu.DB_PASS";
    
    /**
     * Fully qualified name of the MySql JDBC driver class.
     */
    public static String DRIVER = "com.mysql.cj.jdbc.Driver";

    /*
     * The URL used to obtain a JDBC connection.
     */
    public static String URL;

    private static final Logger LOGGER = Logger.getLogger(MySqlDAO.class.getName());

    /**
     * Utility indicating whether the DriverClass was explictly loaded (in
     * order to overcome errors in certain JVMs).
     */
    public static boolean IS_LOADED = false;

    /**
     * If it hasn't already been loaded, explicitly load the MySql driver.
     */
    public MySqlDAO() {
	if (!IS_LOADED) {
	    try {
                ResourceMgr rscr = ResourceMgr.instance();
                
                String dbHost = rscr.getProp(DB_HOST_PROP);
                String dbName = rscr.getProp(DB_NAME_PROP);
                String dbUser = rscr.getProp(DB_USER_PROP);
                String dbPass = rscr.getProp(DB_PASS_PROP);
                
                URL = "jdbc:mysql://" + dbHost + "/" + dbName + "?user=" +
                        dbUser + "&password=" + dbPass;

		Class.forName(DRIVER).newInstance(); // Old School

		IS_LOADED = true;

	    } catch (MissingPropertyException e) {
                LOGGER.log(Level.INFO, "Missing DB property: {0}", e.toString());
            } catch (ClassNotFoundException e) {
		LOGGER.log(Level.SEVERE, "MySqlDao-ERR-1: Illegal driver class name {0}", e.toString());
	    } catch (InstantiationException e) {
		LOGGER.log(Level.SEVERE, "MySqlDao-ERR-2: Illegal instance {0}", e.toString());
	    } catch (IllegalAccessException e) {
		LOGGER.log(Level.SEVERE, "MySqlDao-ERR-3: No create driver permission {0}", e.toString());
	    }
	}
    }

    /**
     * If the given connection or statement is open, close it, but log any
     * errors that might be thrown during the closing operations.
     *
     * @param conn An JDBC Connection that will to be closed.
     * @param stmt An JDBC Statement that will be closed.
     */
    protected void close(Connection conn, Statement stmt) {
	if (stmt != null) {
	    try {
		stmt.close();
	    } catch (Exception e){
		LOGGER.log(Level.INFO, "MySqlDao-ERR-4: stmt.close() {0}", e.toString());
	    }
	}

	if (conn != null) {
	    try {
		conn.close();
	    } catch (Exception e){
		LOGGER.log(Level.INFO, "MySqlDao-ERR-5: close() {0}", e.toString());
	    }
	}
    }

    /**
     * If the given connection is open, close it, but log any errors that occur
     * in attempting to close the connection.
     * @param conn
     */
    protected void close(Connection conn) {
	if (conn != null) {
	    try {
		conn.setAutoCommit(true);    // Convenience
		conn.close();
	    } catch (Exception e){
		LOGGER.log(Level.INFO, "MySqlDao-ERR-6: close() {0}", e.toString());
	    }
	}
    }

    /**
     * If the given statement is open, close it, but log any errors that occur
     * in attempting to close the connection.
     * @param stmt
     */
    protected void close(Statement stmt) {
	if (stmt != null) {
	    try {
		stmt.close();
	    } catch (Exception e){
		LOGGER.log(Level.INFO, "MySqlDao-ERR-7: close() {0}", e.toString());
	    }
	}
    }

    /**
     * Rollback any statements made in the current transaction associated
     * with the given connection.
     * @param conn
     */
    protected void rollback(Connection conn) {
	try {
	    conn.rollback();
	} catch (SQLException e) {
	    LOGGER.log(Level.SEVERE, "MySqlDao-ERR-8: rollback {0}", e.toString());
	}
    }
}


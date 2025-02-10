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
import edu.regis.dptu.model.TaskSelectionKind;
import edu.regis.dptu.model.UnitDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An MySQL Data Access Object for the UnitDigest
 * 
 * @author benm
 */
public class UnitDigestDAO extends MySqlDAO {
    /**
     * Initialize this DAO via the parent constructor.
     */
    public UnitDigestDAO() {
        super();
    }

    /**
     * Insert a unit into the database using the unit digest model.
     * 
     * @param unit the unit to create.
     * @throws IllegalArgException a unit already exists with that id.
     * @throws NonRecoverableException perhaps see getCause().getErrorCode().
     */
    public void create(UnitDigest unit) throws IllegalArgException, NonRecoverableException {
        final String sql = "INSERT INTO Unit(CourseId, Title, Description, SequenceIndex, Pedagogy) VALUES (?,?,?,?,?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);

            if (exists(unit.getId(), conn)) {
                throw new IllegalArgException("Unit Digest already exists with id " + unit.getId());
            }

            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, unit.getCourseId());
            stmt.setString(2, unit.getTitle());
            stmt.setString(3, unit.getDescription());
            stmt.setInt(4, unit.getSequenceIndex());
            stmt.setString(5, unit.getPedagogy().toString());

            stmt.execute();
        } catch(SQLException e) {
            throw new NonRecoverableException("Create Unit Digest Error", e);
        } finally {
            close(conn, stmt);
        }
    }

    /**
     * Find the UnitDigest for the respective unitId
     * eliminates the need to return an entire unit to the GUI.
     *
     * @param unitId integer key of the unit to load.
     * @return The unit digest of the given id.
     * @exception ObjNotFoundException No unit digest with the given id exists.
     * @throws NonRecoverableException see the documentation for this exception.
     */
    public UnitDigest retrieve(int unitId) throws ObjNotFoundException, NonRecoverableException {
        final String sql = "SELECT CourseId, Title, Description, SequenceIndex, Pedagogy FROM Unit WHERE UnitId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, unitId);

            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                UnitDigest unit = new UnitDigest(unitId);

                unit.setCourseId(rs.getInt(1));
                unit.setTitle(rs.getString(2));
                unit.setDescription(rs.getString(3));
                unit.setSequenceIndex(rs.getInt(4));
                unit.setPedagogy(TaskSelectionKind.valueOf(rs.getString(5)));

                return unit;
            } else {
                throw new ObjNotFoundException("Unit id: " + unitId);
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("Retrieve Unit Error", e);
        } finally {
            close(stmt);
        }
    }

    /**
     * @param unitId the unit id
     * @param conn an existing connection to the database
     * @return true, if the unit exists, otherwise false
     * @throws NonRecoverableException (see ex.getCause().getErrorCode())
     */
    private boolean exists(int unitId, Connection conn) throws NonRecoverableException {
        final String sql = "SELECT unitId FROM Unit WHERE UnitId = ?;";
        
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, unitId);

            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new NonRecoverableException("Exists Unit Error", e);
        } finally {
            close(stmt);
        }
    }
}

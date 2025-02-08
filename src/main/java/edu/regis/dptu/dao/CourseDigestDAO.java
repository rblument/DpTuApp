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
import edu.regis.dptu.model.CourseDigest;
import edu.regis.dptu.model.TaskSelectionKind;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An MySQL Data Access Object for the CoruseDigest
 * 
 * @author benm
 */
public class CourseDigestDAO extends MySqlDAO {
    /**
     * Initialize this DAO via the parent constructor.
     */
    public CourseDigestDAO() {
        super();
    }

    /**
     * Insert a course into the database using the course digest model.
     * 
     * @param course the Course to create.
     * @throws IllegalArgException a course already exists with that id.
     * @throws NonRecoverableException perhaps see getCause().getErrorCode().
     */
    public void create(CourseDigest course) throws IllegalArgException, NonRecoverableException {
        final String sql = "INSERT INTO Course(Title, PrimaryPedagogy, Description) VALUES (?,?,?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);

            if (exists(course.getId(), conn)) {
                throw new IllegalArgException("Course Digest already exists with id " + course.getId());
            }

            stmt = conn.prepareStatement(sql);

            stmt.setString(1, course.getTitle());
            stmt.setString(2, course.getPrimaryPedagogy().toString());
            stmt.setString(3, course.getDescription());

            stmt.execute();
        } catch(SQLException e) {
            throw new NonRecoverableException("Create Course Digest Error", e);
        } finally {
            close(conn, stmt);
        }
    }

    /**
     * Find the CourseDigest for the respective CourseId
     * eliminates the need to return an entire course to the GUI.
     *
     * @param courseId integer key of the course to load.
     * @return The course digest of the given id.
     * @exception ObjNotFoundException No course digest with the given id exists.
     * @throws NonRecoverableException see the documentation for this exception.
     */
    public CourseDigest retrieve(int courseId) throws ObjNotFoundException, NonRecoverableException {
        final String sql = "SELECT Title, PrimaryPedagogy, Description FROM Course WHERE CourseId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);

            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                CourseDigest course = new CourseDigest();

                course.setId(courseId);
                course.setTitle(rs.getString(1));
                course.setPrimaryPedagogy(TaskSelectionKind.valueOf(rs.getString(2)));
                course.setDescription(rs.getString(3));

                return course;
            } else {
                throw new ObjNotFoundException("Course id: " + courseId);
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("Retrieve Course Error", e);
        } finally {
            close(stmt);
        }
    }

    /**
     * @param courseId the course id
     * @param conn an existing connection to the database
     * @return true, if the course exists, otherwise false
     * @throws NonRecoverableException (see ex.getCause().getErrorCode())
     */
    private boolean exists(int courseId, Connection conn) throws NonRecoverableException {
        final String sql = "SELECT CourseId FROM Course WHERE CourseId = ?;";
        
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);

            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new NonRecoverableException("Exists Course Error", e);
        } finally {
            close(stmt);
        }
    }
}

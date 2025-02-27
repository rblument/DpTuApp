package edu.regis.dptu.dao;

import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Course;
import edu.regis.dptu.model.KnowledgeComponent;
import edu.regis.dptu.model.ScaffoldLevel;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.StudentModelFieldKind;
import edu.regis.dptu.model.aol.Assessment;
import edu.regis.dptu.model.aol.AssessmentLevel;
import edu.regis.dptu.model.aol.StudentModel;
import edu.regis.dptu.svc.CourseSvc;
import edu.regis.dptu.svc.ServiceFactory;
import edu.regis.dptu.svc.StudentModelSvc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * A Data Access Object implementing {@link StudentModelSvc } behaviors.
 * 
 * @todo temporary
 * Shows how to use the {@link Transactionable} class
 *
 * @author rickb
 * @author benm
 */
/**
 * A Data Access Object implementing {@link StudentModelSvc } behaviors.
 *
 * @author rickb
 */
public class StudentModelDAO extends MySqlDAO implements StudentModelSvc {

    /**
     * Initialize this DAO via the parent constructor.
     */
    public StudentModelDAO() {
        super();
    }

    @Override
    public void create(Student student) throws NonRecoverableException {
        final String sql1 = "INSERT INTO StudentModel (UserId, ScaffoldLevel) VALUES (?,?)";

        final String sql2 = "INSERT INTO Assessment (UserId, KnowledgeComponentId, AssessmentLevel, Exposures, Successes, Hints) VALUES (?,?,?,?,?,?)";

        String userId = student.getAccount().getUserId();
        StudentModel studentModel = student.getStudentModel();

        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;

        try {
            conn = DriverManager.getConnection(URL);
            stmt1 = conn.prepareStatement(sql1);
            
            stmt1.setString(1, userId);
            stmt1.setString(2, ScaffoldLevel.EXTREME.toString());
            stmt1.executeUpdate();
            
            stmt2 = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);

            for (Assessment assessment : studentModel.getAssessments().values()) {

                stmt2.setString(1, userId);
                stmt2.setInt(2, assessment.getOutcome().getId());
                stmt2.setString(3, "Not Started");
                stmt2.setInt(4, assessment.getExposures());
                stmt2.setInt(5, assessment.getSuccessess());
                stmt2.setInt(6, assessment.getHints());

                stmt2.executeUpdate();

                // Get the id/key autogenerated by the DB
                ResultSet rs = stmt2.getGeneratedKeys();
                if (rs.next()) {
                    assessment.setId(rs.getInt(1));
                }
            }
             
        } catch (SQLException e) {
            throw new NonRecoverableException("UserDAO-ERR-5" + e.toString(), e);
        } finally {
            close(stmt2);
            close(conn, stmt1);
        }
    }

    @Override
    public StudentModel retrieve(String userId) throws ObjNotFoundException, NonRecoverableException {
        final String sql = "SELECT ScaffoldLevel FROM StudentModel WHERE UserId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                StudentModel studentModel = new StudentModel(userId);

                studentModel.setScaffoldLevel(ScaffoldLevel.fromString(rs.getString(1)));

                for (Assessment assessment : retrieveAssessments(userId, conn)) {
                    studentModel.addAssessment(assessment);
                }

                return studentModel;

            } else {
                throw new ObjNotFoundException("Student Id:" + userId);
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("UserDAO-ERR-5" + e.toString(), e);
        } finally {
            close(conn, stmt);
        }
    }

    @Override
    public void update(StudentModel model) throws ObjNotFoundException, NonRecoverableException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void updateAssessment(StudentModel model, Assessment assessment, StudentModelFieldKind field)
            throws NonRecoverableException {

        String sql = "";

        int assessmentId = assessment.getId();
        String userId = model.getUserId();
        int knowledgeComponentId = assessment.getOutcome().getId();

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);

            switch (field) {
                case ASSESSMENT_LEVEL:
                    sql = "UPDATE Assessment SET AssessmentLevel = ? WHERE Id = ? ";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, assessment.getAssessment().title());

                    break;
                case ATTEMPTS:
                    sql = "UPDATE Assessment SET Exposures = ? WHERE Id = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, assessment.getExposures());
                    break;

                case SUCCESSES:
                    sql = "UPDATE Assessment SET Successes = ? WHERE Id = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, assessment.getSuccessess());

                    break;

                case HINTS:
                    sql = "UPDATE Assessment SET Hints = ? WHERE Id = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, assessment.getHints());
                    break;
            }

            stmt.setInt(2, assessmentId);

            System.out.println("STMT: **" + stmt.toString() + "**");

            stmt.execute();

        } catch (SQLException e) {
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("SQL Error Code: " + e.getErrorCode());
            throw new NonRecoverableException("UserDAO-ERR-5" + e.toString(), e);
        } finally {
            close(conn, stmt);
        }
    }

    @Override
    public void delete(String userId) throws NonRecoverableException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean exists(String userId) throws NonRecoverableException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(URL);
            return exists(userId, conn);

        } catch (SQLException e) {
            throw new NonRecoverableException("UserDAO-ERR-3" + e.toString(), e);
        } finally {
            close(conn);
        }
    }

    /**
     * Utility that returns whether the student model exists for the given user
     * id in the database.
     *
     * As all students are created with an associated student model, this should
     * always return true.
     *
     * @param userId the user id of the student whose student model is being
     * checked.
     * @param conn an existing connection to the database, which is not closed
     * by this method.
     * @return true, if the student model for the given user id exists in the
     * database, which should always be the case, otherwise false
     * @throws NonRecoverableException (see ex.getCause().getErrorCode())
     */
    private boolean exists(String userId, Connection conn) throws NonRecoverableException {
        final String sql = "SELECT UserId FROM StudentModel WHERE UserId = ?";

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException ex) {
            throw new NonRecoverableException("StudentModelDAO-ERR-3" + ex.toString(), ex);
        } finally {
            close(stmt);
        }
    }
    
   /**
    * Retrieves a list of unfinished lessons for a student in a specific learning category.
    * 
    * The category is inferred from `AssessmentLevel`:
    * - "Not Started" → Teach Me
    * - "In Progress" → Practice
    * - "Completed", "Very Low", "Low", "Medium", "High", "Very High" → Quiz Me
    *
    * If a lesson is not yet completed in a **previous category**, it will indicate that.
    *
    * @param userId The unique identifier of the student.
    * @param learningCategory The learning category ("Teach Me", "Practice", "Quiz Me").
    * @return A list of unfinished lesson names, formatted accordingly.
    * @throws ObjNotFoundException If the student record is not found.
    * @throws NonRecoverableException If a database error occurs.
    */
   @Override
   public List<String> retrieveIncompleteLessons(String userId, String learningCategory) 
           throws ObjNotFoundException, NonRecoverableException {

       List<String> lessons = new ArrayList<>();

       // SQL Query: Retrieve all lessons and their assessment levels
       final String sql = """
           SELECT kc.Title, a.AssessmentLevel
           FROM Assessment a
           JOIN KnowledgeComponent kc ON a.KnowledgeComponentId = kc.Id
           WHERE a.UserId = ?
       """;

       try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

           stmt.setString(1, userId);
           try (ResultSet rs = stmt.executeQuery()) {
               while (rs.next()) {
                   String lessonTitle = rs.getString("Title");
                   String assessmentLevel = rs.getString("AssessmentLevel");

                   // Determine category based on AssessmentLevel
                   switch (AssessmentLevel.fromString(assessmentLevel)) {
                       case NOT_STARTED:
                           if (learningCategory.equalsIgnoreCase("Teach Me")) {
                               lessons.add(lessonTitle);
                           } else {
                               // If user is in "Practice" or "Quiz Me" but hasn't done Teach Me
                               lessons.add(lessonTitle + " (Complete in Teach Me first)");
                           }
                           break;
                       case IN_PROGRESS:
                           if (learningCategory.equalsIgnoreCase("Practice")) {
                               lessons.add(lessonTitle);
                           } else {
                               // If user is in "Quiz Me" but hasn't completed Practice
                               lessons.add(lessonTitle + " (Complete in Practice first)");
                           }
                           break;
                       case COMPLETED:
                       case VERY_LOW:
                       case LOW:
                       case MEDIUM:
                       case HIGH:
                       case VERY_HIGH:
                           if (learningCategory.equalsIgnoreCase("Quiz Me")) {
                               lessons.add(lessonTitle);
                           }
                           break;
                       default:
                           break;
                   }
               }
           }
       } catch (SQLException e) {
           throw new NonRecoverableException("Error retrieving incomplete lessons: " + e, e);
       }

       // If no lessons are found, return a single message to indicate completion
       if (lessons.isEmpty()) {
           lessons.add("All lessons completed!");
       }

       return lessons;
   }




    /**
     * Retrive
     * @param userId
     * @param conn
     * @return
     * @throws ObjNotFoundException
     * @throws SQLException
     * @throws NonRecoverableException 
     */
    private ArrayList<Assessment> retrieveAssessments(String userId, Connection conn)
            throws ObjNotFoundException, SQLException, NonRecoverableException {

        final String sql = "SELECT Id,KnowledgeComponentId,AssessmentLevel,Exposures,Successes,Hints FROM Assessment WHERE UserId = ?";

        CourseSvc courseSvc = ServiceFactory.findCourseSvc();
        Course course = courseSvc.retrieve(1); // Note only one course possible.

        ArrayList<Assessment> assessments = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, userId);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int assessmentId = rs.getInt(1);

            int knowledgeComponentId = rs.getInt(2);
            KnowledgeComponent outcome = course.findKnowledgeComponent(knowledgeComponentId);

            AssessmentLevel level = AssessmentLevel.fromString(rs.getString(3));

            Assessment assessment = new Assessment(outcome, level);

            assessment.setId(knowledgeComponentId);
            assessment.setExposures(rs.getInt(4));
            assessment.setSuccessess(rs.getInt(5));
            assessment.setHints(rs.getInt(6));

            assessments.add(assessment);
        }

        return assessments;
    }
}

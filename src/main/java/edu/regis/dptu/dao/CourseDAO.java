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

import edu.regis.dptu.err.InconsistentDBException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.BloomLevel;
import edu.regis.dptu.model.Course;
import edu.regis.dptu.model.CourseDigest;
import edu.regis.dptu.model.ExercisingLocation;
import edu.regis.dptu.model.Hint;
import edu.regis.dptu.model.KnowledgeComponent;
import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TaskKind;
import edu.regis.dptu.model.TaskSelectionKind;
import edu.regis.dptu.model.Timeout;
import edu.regis.dptu.model.Unit;
import edu.regis.dptu.model.UnitDigest;
import edu.regis.dptu.model.aol.OutcomeGranularity;
import edu.regis.dptu.svc.CourseSvc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A MySql Data Access Object implementing CourseSvc behaviors.
 *
 * @author rickb
 */
public class CourseDAO extends MySqlDAO implements CourseSvc {

    /**
     * Instantiate this Course DAO with default values.
     */
    public CourseDAO() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course retrieve(int courseId) throws ObjNotFoundException, NonRecoverableException {
        final String sql = "SELECT Title,PrimaryPedagogy,Description FROM Course WHERE CourseId = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL);
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, courseId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Course course = new Course(courseId);

                course.setTitle(rs.getString(1));
                course.setPrimaryPedagogy(TaskSelectionKind.valueOf(rs.getString(2)));
                course.setDescription(rs.getString(3));
                course.setExercisingLocations(retrieveExercisingLocations(courseId, conn));
                course.setUnits(retrieveUnits(course, conn));
                course.setOutcomes(retrieveKnowledgeComponents(course, conn));
                
                return course;

            } else {
                throw new ObjNotFoundException("Course Id:" + courseId);
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-1" + e.toString(), e);
        } finally {
            close(conn, stmt);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CourseDigest retrieveDigest(int courseId, Connection conn)
            throws ObjNotFoundException, NonRecoverableException {
        
        final String sql = "SELECT Title,Description FROM Course WHERE Id = ?";

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, courseId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CourseDigest digest = new CourseDigest(courseId);

                digest.setTitle(rs.getString(1));
                digest.setDescription(rs.getString(2));
                
                return digest;

            } else {
                throw new ObjNotFoundException("Course Id:" + courseId);
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-2" + e.toString(), e);
        } finally {
            close(stmt);
        }
    }
    
     /**
     * {@inheritDoc}
     */
    @Override
    public UnitDigest retrieveUnitDigest(int courseId, int unitId, Connection conn)
            throws ObjNotFoundException, NonRecoverableException {
        
        final String sql = "SELECT Title,Description FROM Unit WHERE CourseId = ? AND UnitId = ?";

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, courseId);
            stmt.setInt(2, unitId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UnitDigest digest = new UnitDigest(unitId);

                digest.setTitle(rs.getString(1));
                digest.setDescription(rs.getString(2));
                
                return digest;

            } else {
                throw new ObjNotFoundException("Unit Id:" + courseId);
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-3" + e.toString(), e);
        } finally {
            close(stmt);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Task retrieveTask(int courseId, int taskId, Connection conn) throws ObjNotFoundException, NonRecoverableException {
        final String sql = "SELECT Title,Description,Kind,SequenceIndex,ExampleType,ProblemId FROM Task WHERE CourseId = ? AND TaskId = ?";

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, courseId);
            stmt.setInt(2, taskId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Task task = new Task(taskId);
                task.setTitle(rs.getString(1));
                task.setDescription(rs.getString(2));
                task.setKind(TaskKind.valueOf(rs.getString(3)));
                task.setSequenceIndex(rs.getInt(4));
                
          
                task.setSteps(retrieveSteps(courseId, task.getId(), conn));
                
                // retrieve LCS Problem

                return task;

            } else {
                throw new ObjNotFoundException("Course Id:" + courseId);
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-4" + e.toString(), e);
        } finally {
            close(stmt);
        }
    }

    /**
     * Extract child &lt;Outcome> elements from given XML DOM parent element
     * adding each as a Outcome to the given Course.
     *
     * @param parent an XML DOM Element containing one or more child
     * &lt;Outcome> node elements.
     * @throws NonRecoverableExeption a nonrecoverable exception also see getCause()
     * @return a List of KnowledgeComponent outcomes
     */
    private ArrayList<KnowledgeComponent> retrieveKnowledgeComponents(Course course, Connection conn) 
            throws NonRecoverableException {
        
        final String sql = "SELECT Id, Title, Description, BloomLevel, IsDomainFocus, Pedagogy, ExercisingLocations, Granularity FROM KnowledgeComponent WHERE CourseId = ?";
    
        ArrayList<KnowledgeComponent> outcomes = new ArrayList<>();

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
        
            stmt.setInt(1, course.getId());

            ResultSet rs = stmt.executeQuery();
        
            while (rs.next()) {
                KnowledgeComponent comp = new KnowledgeComponent(rs.getInt(1));
                comp.setTitle(rs.getString(2));
                comp.setDescription(rs.getString(3));
                // ToDo: Convert table to Enum, then use valueOf
                comp.setBloomLevel(BloomLevel.findValue(rs.getString(4)));
                comp.setIsDomainFocus(rs.getBoolean(5));
                comp.setPedagogy(TaskSelectionKind.findValue(rs.getString(6)));
                comp.setGranularity(OutcomeGranularity.findValue(rs.getString(8)));
                         
                // Link the exercising locations in this outcome to those in the course.
                ArrayList<ExercisingLocation> locations = new ArrayList<>();
                String[] ids = rs.getString(7).split(",");
                for (int i = 0; i < ids.length; i++)
                    locations.add(course.findLocation(i));
         
                comp.setExercisingLocations(locations);
                
                outcomes.add(comp);
            }
       
             return outcomes;
        
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-5" + e.toString(), e);
        } finally {
            close(stmt);
        }
    }

    /**
     * Return the units in this course from the &lt;Unit> elements.
     * 
     * @param parent a &lt;Course> element
     * @return a List of Units
     * @throws NonRecoverableException 
     */
    private ArrayList<Unit> retrieveUnits(Course course, Connection conn) throws NonRecoverableException {
        final String sql = "SELECT UnitId,Title,Description,SequenceIndex,Pedagogy FROM Unit WHERE CourseId = ?";

        ArrayList<Unit> units = new ArrayList<>();
        
         PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, course.getId());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Unit unit = new Unit(rs.getInt(1));
                unit.setTitle(rs.getString(2));
                unit.setDescription(rs.getString(3));
                unit.setSequenceId(rs.getInt(4));
                unit.setPedagogy(TaskSelectionKind.findValue(rs.getString(5)));
                unit.setTasks(retrieveTasks(course, unit, conn));
                
                units.add(unit);
            }
            
            return units;
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-6" + e.toString(), e);
        } finally {
            close(stmt); // Don't close the connection, retrieve(courseId) will
        }
    }

    /**
     * Extract and return the tasks in the given &lt;Unit> element
     * 
     * @param parent &lt;Unit>   // ToDo is this always true?
     * @return a Task list.
     */
    private ArrayList<Task> retrieveTasks(Course course, Unit unit, Connection conn)
        throws NonRecoverableException {
        final String sql = "SELECT TaskId,Title,Description,Kind,SequenceIndex,KindId FROM Task WHERE CourseId = ? AND UnitId = ?";

        ArrayList<Task> tasks = new ArrayList<>();
    
        PreparedStatement stmt = null;
        
        int courseId = course.getId();

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, courseId);
            stmt.setInt(2, unit.getId());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Task task = new Task(rs.getInt(1));
                task.setTitle(rs.getString(2));
                task.setDescription(rs.getString(3));
                task.setKind(TaskKind.valueOf(rs.getString(4)));
                task.setSequenceIndex(rs.getInt(5));
                
                task.setProblem(retrieveProblem(task.getKind(), rs.getInt(6), conn));
                
                tasks.add(task);
                
                task.setSteps(retrieveSteps(courseId, task.getId(), conn));

                // ToDo: retrieve exercising locations
            }
            
            return tasks;
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-7" + e.toString(), e);
        } finally {
            close(stmt); // Don't close the connection, retrieve(courseId) will
        }

    }
    
    /**
     * Extract and return a list of child steps from the given parent element.
     * 
     * @param courseId
     * @param taskId
     * @param conn an open connection to the DB, which isn't closed by this method.
     * @return a List of Step elements, may be empty.
     */
    private ArrayList<Step> retrieveSteps(int courseId, int taskId, Connection conn) 
        throws NonRecoverableException {            
        final String sql = "SELECT Id,Title,Description,SequenceIndex,StepSubType,SubTypeId,TimeoutId FROM Step WHERE CourseId = ? AND TaskId = ?";

        ArrayList<Step> steps = new ArrayList<>();
    
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, courseId);
            stmt.setInt(2, taskId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                StepSubType subType = StepSubType.valueOf(rs.getString(5));
 
                Step step = new Step(rs.getInt(1), rs.getInt(4), subType);
                
                step.setTitle(rs.getString(1));
                step.setDescription(rs.getString(2));
                step.setTimeout(retrieveTimeout(rs.getInt(7), conn));
   
               // extractStepSubTypeData(subType, rs.getInt(6),conn);
        
                 // ToDo retrieve exercising locations
                
                steps.add(step);
                
                step.setHints(retrieveHints(step.getId(), conn));
            }
            
            return steps;
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-8" + e.toString(), e);
        } finally {
            close(stmt); // Don't close the connection, retrieve(courseId) will
        }
    }
    
    /**
     * Extract and return a list of hints from the given parent element.
     * 
     *
     * @param conn an open connection to the DB, which isn't closed by this method.
     * @return an ArrayList of Hint
     */
    private ArrayList<Hint> retrieveHints(int stepId, Connection conn)
        throws NonRecoverableException {            
        
        final String sql = "SELECT Id,Text,SequenceIndex FROM Hint WHERE StepId = ?";

        ArrayList<Hint> hints = new ArrayList<>();
    
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, stepId);       

            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Hint hint = new Hint(rs.getInt(1));
                hint.setText(rs.getString(2));
                hint.setSequenceIndex(rs.getInt(3));
            
                hints.add(hint);
            }
      
            return hints;
            
         } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-9" + e.toString(), e);
        } finally {
            close(stmt); // Don't close the connection, retrieve(courseId) will
        }
    }
    
    /**
     * 
     * @param subType
     * @param subTypeId index into the appropriate table determined by subType
     * @return 
     */
    private String extractStepSubTypeData(StepSubType subType, int subTypeId, Connection conn)
        throws NonRecoverableException {
        switch (subType) {
            case INFO_MESSAGE:
                return extractInfoMsgData(subTypeId, conn);
            case GUI_ACTION:
                return ""; // TBD
            case STEP_COMPLETION_REPLY:
                return ""; // TBD
            case REQUEST_HINT:
                return ""; // TBD
            default:
                return "";
        }
    }
    
    /**
     * 
     * The data is a POJO String object
     */
    private String extractInfoMsgData(int subTypeId, Connection conn) throws NonRecoverableException {
        final String sql = "SELECT Text FROM InfoMsgStep WHERE SubStepId = ?";
    
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1,subTypeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString(1);
            } else {
                String errMsg = "ERROR: ToDo: Throw database inconsistency InfoMsgStep table: " + subTypeId;
                throw new NonRecoverableException(errMsg, new InconsistentDBException(errMsg));
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-10" + e.toString(), e);
        } finally {
            close(stmt); // Don't close the connection, retrieve(courseId) will
        }
    }
    
    private Problem retrieveProblem(TaskKind kind, int kindId, Connection conn) 
        throws NonRecoverableException {
        switch (kind) {
            case LCS_PROBLEM:
                return retrieveLCSProblem(kindId, conn);
                
            default:
                return null;
        }
    }
    
    private LCSProblem retrieveLCSProblem(int kindId, Connection conn) throws NonRecoverableException {
         final String sql = "SELECT Title,Description,Sequence1,Sequence2 FROM LCSProblem WHERE Id = ?";

        ArrayList<Task> tasks = new ArrayList<>();
    
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, kindId);
  

            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                LCSProblem prob = new LCSProblem(rs.getString(3), rs.getString(4));
                prob.setId(kindId);
                prob.setTitle(rs.getString(1));
                prob.setDescription(rs.getString(2));
                
                return prob;
                
            } else {
                throw new NonRecoverableException("Inconsisted DB LCSProb: " + kindId);
            }
            
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-10" + e.toString(), e);
        } finally {
            close(stmt); // Don't close the connection, retrieve(courseId) will
        }
    }
    

    
    private Timeout retrieveTimeout(int timeoutId, Connection conn) 
        throws NonRecoverableException {
        final String sql = "SELECT TimeoutType,Seconds,Event,Msg FROM Timeout WHERE Id = ?";
    
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, timeoutId);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Timeout timeout = new Timeout(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4));
                return timeout;
            } else {
                // ToDo: throw a dabase inconsistency error
                String errMsg = "Timeout not found, id: " + timeoutId;
                throw new NonRecoverableException(errMsg, new InconsistentDBException(errMsg));
            }
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-11" + e.toString(), e);
        } finally {
            close(stmt); // Don't close the connection, retrieve(courseId) will
        }
    }
    
    /**
     * Extract child &lt;ExercisingLocation> elements from given XML DOM parent 
     * element and return the list of these locations.
     *
     * @param parent an XML DOM Element containing one or more child
     * &lt;ExercisingLocation> node elements.
     * @throws NonRecoverableException a nonrecoverable exception also see getCause()
     * @return a List of ExercisingElements
     */
    private ArrayList<ExercisingLocation> retrieveExercisingLocations(int courseId, Connection conn) 
            throws NonRecoverableException {
        
        final String sql = "SELECT Id, UnitId, TaskId, StepId FROM ExercisingLocation WHERE CourseId = ?";
  
        ArrayList<ExercisingLocation> locations = new ArrayList<>();
        
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, courseId);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
               ExercisingLocation location = new ExercisingLocation(rs.getInt(1));
               location.setCourseId(courseId);
               location.setUnitId(rs.getInt(2));
               location.setTaskId(rs.getInt(3));
               location.setStepId(rs.getInt(4));
                
               locations.add(location);            
            }
            
            return locations;
            
        } catch (SQLException e) {
            throw new NonRecoverableException("CourseDAO-ERR-12" + e.toString(), e);
        } finally {
            close(stmt); // Don't close the connection, retrieve(courseId) will
        }
    }
}

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
package edu.regis.dptu.model;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * A Dynamic Programming tutoring session, which is displayed in the tutor view.
 * 
 * @author rickb
 */
public class TutoringSession {
    /**
     * The id of this session in the database.
     */
    private int id;
    
    /**
     * An SHA-256 encrypted security token that must be communicated to the
     * tutor/server in all subsequent requests after signing in.
     */
    private String securityToken = "";
    
    /**
     * The student being tutored in this session.
     */
    private Student student;
    
    /**
     * A summary of the course currently being taught in this session.
     */
    private CourseDigest course;
    
    /**
     * A summary of the unit currently being taught in this session.
     */
    private UnitDigest unit;
    
     /**
     * True, if the session is currently active (though the student may not
     * be currently signed-in).
     */
    private boolean isActive = true;
    
    /**
     * The date and time when this session was initially created.
     */
    private GregorianCalendar startDate;
 
     /**
     * The current task list.
     * 
     * If there are multiple tasks, the first one is the current task and the
     * remaining tasks are pending. Multiple tasks occur when a student 
     * overrides the task proposed by the tutor.
     */
    private ArrayList<Task> tasks; // ToDo: Change to PendingTask

    /**
     * Initialize this session with default information.
     * 
     * @param student the Student being tutored in this session.
     */
    public TutoringSession(Student student) {
        this.student = student;
        tasks = new ArrayList<>();
    }
    
     public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    /**
     * Return the student being tutored in this tutoring session.
     * 
     * @return a Student
     */
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public CourseDigest getCourse() {
        return course;
    }

    public void setCourse(CourseDigest course) {
        this.course = course;
    }

    public UnitDigest getUnit() {
        return unit;
    }

    public void setUnit(UnitDigest unit) {
        this.unit = unit;
    }
    
    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public GregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = startDate;
    }
    
    public Task currentTask() {
        return tasks.get(0);
    }
    
    //ToDo: change to PendingTask
    public void addTask(Task task) {
        tasks.add(task);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
    
    public void removeTask(Task task) {
        tasks.remove(task);
    }
    
    public void removeTask(int taskId) {
        for (Task task : tasks) 
            if (task.getId() == taskId)
                removeTask(task);
    }
}

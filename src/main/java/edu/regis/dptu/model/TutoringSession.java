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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Dynamic Programming tutoring session, which is displayed in the tutor view.
 *
 * @author rickb
 */
public class TutoringSession {
    private static final Logger log = LoggerFactory.getLogger(TutoringSession.class);

    /** The id of this session in the database. */
    private int id;

    /** Encrypted security token for authenticated requests. */
    private String securityToken = "";

    /** The email address of the student being tutored in this session. */
    private String userId;

    /** A summary of the course currently being taught in this session. */
    private CourseDigest course;

    /** A summary of the unit currently being taught in this session. */
    private UnitDigest unit;

    /** True if the session is currently active. */
    private boolean isActive = true;

    /** The date and time when this session was initially created. */
    private GregorianCalendar startDate;

    /** The overall problem being solved in this session. */
    private Problem problem;

    /** The Mode the user is in—See One, Do One, or Teach One. */
    private Mode mode;

    /** Current and pending tasks for this session. */
    private ArrayList<PendingTask> tasks;

    /**
     * Initialize this session with default information.
     *
     * @param userId The email address of the student associated with this session.
     */
    public TutoringSession(String userId) {
        this.userId = userId;
        this.tasks = new ArrayList<>();
        this.startDate = new GregorianCalendar();

        log.debug("TutoringSession created for userId={}", userId);
    }

    /**
     * Initialize this session with an Account and a Problem.
     *
     * @param account the Account used to create the Student.
     * @param problem the Problem to be solved in this session.
     */
    public TutoringSession(Account account, Problem problem) {
        this.userId = account.getUserId();
        this.problem = problem;
        this.tasks = new ArrayList<>();
        this.startDate = new GregorianCalendar();

        log.debug(
                "TutoringSession created: userId={}, problemType={}",
                userId,
                problem != null ? problem.getType() : "null");
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CourseDigest getCourse() {
        return course;
    }

    public void setCourse(CourseDigest course) {
        this.course = course;
        log.debug("Session {} course set to {}", userId, course);
    }

    public UnitDigest getUnit() {
        return unit;
    }

    public void setUnit(UnitDigest unit) {
        this.unit = unit;
        log.debug("Session {} unit set to {}", userId, unit);
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
        log.debug("Session {} active state changed to {}", userId, isActive);
    }

    public GregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
        log.debug(
                "Session {} problem set to {}",
                userId,
                problem != null ? problem.getType() : "null");
    }

    public PendingTask getCurrentTask() {
        if (tasks == null || tasks.isEmpty()) {
            log.debug("Session {} has no current tasks", userId);
            throw new IndexOutOfBoundsException(
                    "Error: Session " + userId + " has no current tasks.");
        }
        return tasks.get(0);
    }

    public void addTask(PendingTask task) {
        tasks.add(task);
        log.debug("Session {} added task {}", userId, task);
    }

    public ArrayList<PendingTask> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<PendingTask> tasks) {
        this.tasks = tasks;
        log.debug("Session {} task list replaced (count={})", userId, tasks.size());
    }

    public void removeTask(PendingTask task) {
        tasks.remove(task);
        log.debug("Session {} removed task {}", userId, task);
    }

    public void removeTask(int taskId) {
        tasks.removeIf(task -> task.getTask().getId() == taskId);
        log.debug("Session {} removed task with id={}", userId, taskId);
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        log.debug("Session {} mode set to {}", userId, mode);
    }
}

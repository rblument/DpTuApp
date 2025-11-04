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
package edu.regis.dptu.model.aol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.regis.dptu.model.ScaffoldLevel;
import edu.regis.dptu.model.TutoringSession;

/**
 * Captures the current assessment for each learning outcome in a course, as well as all tutoring
 * sessions in which the student participated.
 *
 * @author rickb
 */
public class StudentModel {
    private static final Logger log = LoggerFactory.getLogger(StudentModel.class);

    /**
     * Convenience reference to the user id (email) of the student associated with this student
     * model.
     */
    private String userId;

    /**
     * The assessments of outcomes for the student associated with this model. The key is the id of
     * the knowledge component in the associated assessment.
     */
    private HashMap<Integer, Assessment> assessments;

    /** The tutoring sessions in which the student has participated. */
    private List<TutoringSession> sessions = new ArrayList<>();

    /** The current scaffolding being used to support the student. */
    private ScaffoldLevel scaffoldLevel = ScaffoldLevel.EXTREME;

    /**
     * Instantiate this student model with default information.
     *
     * <p>Create a student model for the given user id and with default information.
     *
     * @param userId the user id of the student whose model is being created.
     */
    public StudentModel(String userId) {
        this.userId = userId;
        assessments = new HashMap<>();
    }

    /**
     * @return the user id of the student
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the user id to set for this student
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Add an assessment for the given knowledge component id.
     *
     * @param knowledgeComponentId the id of the knowledge component
     * @param assessment the assessment to add
     */
    public void addAssessment(int knowledgeComponentId, Assessment assessment) {
        System.out.println("***** StuMod.addAssess: id: " + knowledgeComponentId);
        assessments.put(knowledgeComponentId, assessment);
    }

    /**
     * Add an assessment keyed by the outcome’s id.
     *
     * @param assessment the assessment to add
     */
    public void addAssessment(Assessment assessment) {
        addAssessment(assessment.getOutcome().getId(), assessment);
    }

    /**
     * Return whether this student has an assessment for the given outcome.
     *
     * @param knowledgeComponentId the id of the knowledge component
     * @return true if the student has an assessment for the given outcome
     */
    public boolean containsAssessment(int knowledgeComponentId) {
        return assessments.containsKey(knowledgeComponentId);
    }

    /**
     * Return the student assessment, if any, for the given outcome.
     *
     * @param knowledgeComponentId the id of the knowledge component
     * @return the Assessment for the given outcome, or null if none
     */
    public Assessment findAssessment(int knowledgeComponentId) {
        return assessments.get(knowledgeComponentId);
    }

    /**
     * @return the assessments for this student
     */
    public HashMap<Integer, Assessment> getAssessments() {
        return assessments;
    }

    /**
     * @return the tutoring sessions for this student
     */
    public List<TutoringSession> getSessions() {
        return sessions;
    }

    /**
     * @param sessions the tutoring sessions to set for this student
     */
    public void setSessions(List<TutoringSession> sessions) {
        this.sessions = sessions;
    }

    /**
     * Add a tutoring session for this student.
     *
     * @param session the session to add
     */
    public void addSession(TutoringSession session) {
        this.sessions.add(session);
    }

    /**
     * @return the current scaffolding level being used to support the student
     */
    public ScaffoldLevel getScaffoldLevel() {
        return scaffoldLevel;
    }

    /**
     * @param scaffoldLevel the scaffolding level to set for this student
     */
    public void setScaffoldLevel(ScaffoldLevel scaffoldLevel) {
        this.scaffoldLevel = scaffoldLevel;
    }
}

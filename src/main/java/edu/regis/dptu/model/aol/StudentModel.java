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

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import edu.regis.dptu.model.ScaffoldLevel;

/**
 * Captures the current assessment for each learning outcome in a course, as well as, all tutoring
 * sessions in which the student participated.
 *
 * @author rickb
 */
public class StudentModel {
    /** Convenience reference to the user id (email) of the student for this model. */
    private String userId;

    /**
     * Assessments of outcomes for the student. Key is the knowledge component id.
     */
    private HashMap<Integer, Assessment> assessments;

    /** Tutoring sessions this student has participated in. */
    private List<TutoringSession> sessions = new ArrayList<>();

    /** The current scaffolding being used to support the student. */
    private ScaffoldLevel scaffoldLevel = ScaffoldLevel.EXTREME;

    /**
     * Create a student model for the given user id with default information.
     * @param userId the user id of the student whose model is being created.
     */
    public StudentModel(String userId) {
        this.userId = userId;
        assessments = new HashMap<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void addAssessment(int knowledgeComponentId, Assessment assessment) {
        System.out.println("***** StuMod.addAssess: id: " + knowledgeComponentId);
        assessments.put(knowledgeComponentId, assessment);
    }

    public void addAssessment(Assessment assessment) {
        addAssessment(assessment.getOutcome().getId(), assessment);
    }

    /** @return true if the student has an assessment for the given outcome id. */
    public boolean containsAssessment(int knowledgeComponentId) {
        return assessments.containsKey(knowledgeComponentId);
    }

    /** @return the assessment for the given outcome id, or null if none. */
    public Assessment findAssessment(int knowledgeComponentId) {
        return assessments.get(knowledgeComponentId);
    }

    public HashMap<Integer, Assessment> getAssessments() {
        return assessments;
    }

    /** Sessions API */
    public List<TutoringSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<TutoringSession> sessions) {
        this.sessions = sessions;
    }

    public void addSession(TutoringSession session) {
        this.sessions.add(session);
    }

    /** Scaffolding API */
    public ScaffoldLevel getScaffoldLevel() {
        return scaffoldLevel;
    }

    public void setScaffoldLevel(ScaffoldLevel scaffoldLevel) {
        this.scaffoldLevel = scaffoldLevel;
    }
}

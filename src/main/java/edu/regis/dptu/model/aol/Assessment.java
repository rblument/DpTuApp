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

import edu.regis.dptu.model.KnowledgeComponent;

/**
 * An assessment by the tutor of a knowledge component outcome.
 * 
 * @author rickb
 */
public class Assessment {
    /**
     * The  knowledge component assessed in this assessment
     */
    private KnowledgeComponent outcome;
    
    /**
     * The student assessment of the outcome being assessed.
     */
    private AssessmentLevel assessment;
    
    /**
     * The number of times within a task step the student was exposed to the
     * knowledge component in this assessment.
     */
    private int exposures;
    
    /**
     * The number of times the student successfully demonstrated knowledge or
     * use of the knowledge component in this assessment.
     */
    private int successess;
    
    public Assessment(KnowledgeComponent outcome, AssessmentLevel assessment) {
        this.outcome = outcome;
        this.assessment = assessment;
    }

    public KnowledgeComponent getOutcome() {
        return outcome;
    }

    public void setOutcome(KnowledgeComponent outcome) {
        this.outcome = outcome;
    }

    /**
     * Return the student assessment for this outcome.
     * 
     * @return an AssessmentLevel
     */
    public AssessmentLevel getAssessment() {
        return assessment;
    }

    public void setAssessment(AssessmentLevel assessment) {
        this.assessment = assessment;
    }

    public int getExposures() {
        return exposures;
    }

    public void setExposures(int exposures) {
        this.exposures = exposures;
    }

    public int getSuccessess() {
        return successess;
    }

    public void setSuccessess(int successess) {
        this.successess = successess;
    }
}